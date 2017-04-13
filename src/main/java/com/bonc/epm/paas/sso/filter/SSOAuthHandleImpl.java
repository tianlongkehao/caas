
package com.bonc.epm.paas.sso.filter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.epm.paas.SpringApplicationContext;
import com.bonc.epm.paas.constant.UserConstant;
import com.bonc.epm.paas.controller.CephController;
import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.dao.UserResourceDao;
import com.bonc.epm.paas.dao.UserVisitingLogDao;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.entity.UserResource;
import com.bonc.epm.paas.entity.UserVisitingLog;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.model.Namespace;
import com.bonc.epm.paas.kubernetes.model.ResourceQuota;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
import com.bonc.epm.paas.sso.casclient.CasClientConfigurationProperties;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.ServiceException;
import com.bonc.epm.paas.util.WebClientUtil;

/**
 *  单点登录后的模拟登录
 * @author song
 */
@Component
public class SSOAuthHandleImpl implements com.bonc.sso.client.IAuthHandle{
    /**
     * 输出日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(SSOAuthHandleImpl.class);
    /**
     * CONST_CAS_ASSERTION
     */
    private static final String CONST_CAS_ASSERTION = "_const_cas_assertion_";

    /**
     * configProps
     */
    @Autowired
    private CasClientConfigurationProperties configProps;

    /**
     * kubernetesClientService
     */
    @Autowired
    private KubernetesClientService kubernetesClientService;

    /**
     * userDao
     */
    @Autowired
    private UserDao userDao;

    /**
     * userDao
     */
    @Autowired
    private UserResourceDao userResourceDao;

    /**
     * userVisitingLogDao
     */
    @Autowired
    private UserVisitingLogDao userVisitingLogDao;
    /**
     * resManUrl
     */
    @Value("${resourceMmanage.address}")
    private String resManUrl;
    /**
     * 内存和cpu的比例大小
     */
    @Value("${ratio.memtocpu}")
    private String RATIO_MEMTOCPU = "4";

    @Override
	public boolean onSuccess(HttpServletRequest request, HttpServletResponse response, String loginId) {
		// 添加访问日志
		String hostIp = request.getRemoteAddr();
		String headerData = request.getHeader("User-agent");
		UserVisitingLog userVisitingLog = new UserVisitingLog();
		boolean isLegal = false;

		SpringApplicationContext.CONTEXT.getAutowireCapableBeanFactory().autowireBean(this);
		if (configProps.getEnable()) {
			if ((null != request) && (null != loginId) && (loginId.trim().length() > 0)) {
				User currPaasUser = CurrentUserUtils.getInstance().getUser();
				if (currPaasUser != null && loginId.equals(currPaasUser.getUserName())) { // 如果已经登录和并且是一个用户
					return true;
				}

				// HttpServletRequest httpRequest = (HttpServletRequest)
				// request;
				// cas当前认证用户
				Assertion assertion = (Assertion) request.getSession().getAttribute(CONST_CAS_ASSERTION);
				Map<String, Object> attributes = assertion.getPrincipal().getAttributes();
				LOG.info("能力平台USER:" + attributes.toString());
				LOG.info("cas登陆Id" + assertion.getPrincipal().getName() + "||" + loginId);

				String tenantId = "";
				String namespace = "";
				//获取所在租户的租户名（namespace）
//				if (null == attributes.get("tenantId")) {
//					attributes.put("tenantId", assertion.getPrincipal().getName());
//				}
				if (null != attributes.get("tenantId")) {//tenantId:租户信息唯一标识
					tenantId = attributes.get("tenantId").toString();
					namespace = tenantId;
					// 把下划线替换为--
					if (namespace.contains("_")) {
						namespace = namespace.replace("_", "--");
					}
				}
				User user = new User();
				UserResource userResource = null;
				try {
					// 同步统一平台租户用户到本地
					user = fillUserInfo(assertion, namespace);
					user = userDao.save(user);
					CurrentUserUtils.getInstance().setUser(user);

					userResource = userResourceDao.findByUserId(user.getId());
					if (null == userResource) {
						userResource = new UserResource();
					}

					// 统一平台的userId
					if (user.getUser_autority().equals(UserConstant.AUTORITY_TENANT)) {
						if (createNamespace(namespace)) { // 创建命名空间
							createQuota(userResource, tenantId, namespace); // 创建资源
							createCeph(namespace); // 创建ceph
						}
					}
					userResource.setImage_count(2000);
					userResource.setUserId(user.getId());
					userResourceDao.save(userResource);
					CurrentUserUtils.getInstance().setCasEnable(configProps.getEnable());
					isLegal = true;
				} catch (Exception e) {
					LOG.error(e.getMessage());
					throw new ServiceException();
				}
				userVisitingLog = userVisitingLog.addUserVisitingLog(user, hostIp, headerData, isLegal);
				userVisitingLogDao.save(userVisitingLog);
				return true;
			}
			return false;
		}
		return true;
	}

    /**
     *
     * Description:
     * 创建ceph卷组空间
     * @param namespace
     * @see
     */
    private void createCeph(String namespace) {
        try {
            CephController ceph = new CephController();
            ceph.connectCephFS();
            ceph.createNamespaceCephFS(namespace);
        }
        catch (Exception e) {
            LOG.error("create ceph error" +e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     *
     * Description:
     * 保存用户
     * @param assertion Assertion
     * @param namespace String
     * @exception Exception
     * @return user User
     * @see Assertion
     */
    private User fillUserInfo(Assertion assertion, String namespace) {
		/*
		 * 用户初始化
		 * Map中根据以下键值可获取登录用户如下信息
		 * userId:用户信息唯一标识
		 * loginId:登陆用户名
		 * userName:登陆用户姓名
		 * state:用户状态（1启用；0停用；）
		 * sex:性别（1男；0女；）
		 * email:电子邮箱
		 * mobile:移动电话号码
		 * tenantId:租户信息唯一标识
		 * tenantAdmin:是否租户管理员（1是；0否；）
		 * isSuperAdmin:是否平台管理员（1是；0否；）
		 */
		String loginId = assertion.getPrincipal().getName();
		User user = userDao.findByUserName(loginId);
		if (null == user) {
			user = new User();
			user.setPassword(UserConstant.INIT_PASSWORD);
		}
		user.setNamespace(namespace);
		user.setUserName(loginId);
		Map<String, Object> attributes = assertion.getPrincipal().getAttributes();
		if (null != attributes.get("userId")) { // 统一平台的userId
			user.setOpen_user_id(attributes.get("userId").toString());
		}
		if (null != attributes.get("userName")) { // 用户的名称
			user.setUser_realname(attributes.get("userName").toString());
		}
		if (null != attributes.get("email")) { // email
			user.setEmail(attributes.get("email").toString());
		}
		if (null != attributes.get("telephone")) { // 电话
			user.setUser_phone(attributes.get("telephone").toString());
		}
		if (null != attributes.get("mobile")) { // 手机
			user.setUser_cellphone(attributes.get("mobile").toString());
		}
		if (null != attributes.get("isSuperAdmin")) {
			String isSuperAdmin = attributes.get("isSuperAdmin").toString();
			if ("1".equals(isSuperAdmin)) {
				user.setUser_autority(UserConstant.AUTORITY_MANAGER);
			}
		} else if(user.getUserName().equals("admin")) {
			//兼容旧版能力平台
			user.setUser_autority(UserConstant.AUTORITY_MANAGER);
		}
			else {
			if (null != attributes.get("tenantAdmin")) {
				String tenantAdmin = attributes.get("tenantAdmin").toString();
				if ("1".equals(tenantAdmin)) { // 是租户
					user.setUser_autority(UserConstant.AUTORITY_TENANT);
				} else if ("0".equals(tenantAdmin)) {
					// 获取租户管理员id
					try {
						List<User> tenant = userDao.findTenant(namespace);
						if (!CollectionUtils.isEmpty(tenant)) {
							user.setParent_id(tenant.get(0).getId());
						}
					} catch (Exception e) {
						LOG.error("get parent error:" + e.getMessage());
					}
					user.setUser_autority(UserConstant.AUTORITY_USER);
				}
			}
		}
		return user;
	}

    /**
     *
     * Description:
     * 创建nameSpace
     * @param namespace
     * @return boolean
     * @throws ServiceException
     */
    private boolean createNamespace(String namespace) throws ServiceException{
        // 以用户名(登陆帐号)为name，创建client ??
        KubernetesAPIClientInterface client = kubernetesClientService.getClient("");
        // 是否创建nameSpace
        try {
            client.getNamespace(namespace);
        }
        catch (KubernetesClientException e) {
            // 以用户名(登陆帐号)为name，为client创建nameSpace
            Namespace nSpace = kubernetesClientService.generateSimpleNamespace(namespace);
            nSpace = client.createNamespace(nSpace);
            if (nSpace == null) {
                //client.deleteNamespace(namespace);
                LOG.error("Create a new Namespace:namespace["+nSpace+"]");
                return false;
            }
            else {
                LOG.info("create namespace:" + JSON.toJSONString(nSpace));
            }
        }
        catch (RuntimeException e) {
            LOG.error("连接kubernetesAPI超时！" + e);
            throw new ServiceException();
        }
        return true;
    }

    /**
     *
     * Description:
     * 给租户创建资源
     * @param tenantId
     * @param namespace
     * @exception Exception
     */
    private void createQuota(UserResource userResource,String tenantId, String namespace) throws Exception{
        double openCpu = 0;
        long openMem = 0;
        long volSize = 0;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("tenant_id","\""+tenantId+"\"");
            data.put("resource_type_code","\"Docker\"");
            params.put("param", data);
            String rtnStr = WebClientUtil.doGet(resManUrl, params);
            // 解析返回资源数据
//            rtnStr = "{\"data\":[{\"total\":\"4\",\"image_name\":\"7579\",\"resource_type_desc\":\"支持应用发布的Docker容器\",\"data\":[{\"memo\":null,\"resource_code\":\"RES-Docker-14933\",\"tenant_id\":\"unicom_zb\",\"property\":[{\"unit\":\"核\",\"ext1\":null,\"ext2\":\"1,2\",\"property_name\":\"CPU\",\"prop_value\":\"10\",\"ext3\":\"0\",\"type_name\":\"输入框\",\"value_type_id\":2,\"prop_id\":7678,\"ord\":1,\"property_code\":\"CPU\"},{\"unit\":\"G\",\"ext1\":null,\"ext2\":\"1,2\",\"property_name\":\"内存\",\"prop_value\":\"40\",\"ext3\":\"0\",\"type_name\":\"输入框\",\"value_type_id\":2,\"prop_id\":7850,\"ord\":2,\"property_code\":\"Memory\"},{\"unit\":\"GB\",\"ext1\":null,\"ext2\":\"2\",\"property_name\":\"存储\",\"prop_value\":\"20\",\"ext3\":\"0\",\"type_name\":\"输入框\",\"value_type_id\":2,\"prop_id\":14664,\"ord\":3,\"property_code\":\"Storage\"}],\"feedback\":null,\"resource_id\":14933,\"resource_name\":\"容器服务-14933\",\"create_time\":\"2016-10-1118:25:48\",\"value_type_id\":[{\"unit\":\"核\",\"ext1\":null,\"ext2\":\"1,2\",\"property_name\":\"CPU\",\"prop_value\":\"10\",\"ext3\":\"0\",\"type_name\":\"输入框\",\"value_type_id\":2,\"prop_id\":7678,\"ord\":1,\"property_code\":\"CPU\"},{\"unit\":\"G\",\"ext1\":null,\"ext2\":\"1,2\",\"property_name\":\"内存\",\"prop_value\":\"40\",\"ext3\":\"0\",\"type_name\":\"输入框\",\"value_type_id\":2,\"prop_id\":7850,\"ord\":2,\"property_code\":\"Memory\"},{\"unit\":\"GB\",\"ext1\":null,\"ext2\":\"2\",\"property_name\":\"存储\",\"prop_value\":\"20\",\"ext3\":\"0\",\"type_name\":\"输入框\",\"value_type_id\":2,\"prop_id\":14664,\"ord\":3,\"property_code\":\"Storage\"}],\"resource_type_id\":7536,\"resource_uri\":null,\"user_id\":\"admin\",\"off_lease\":false,\"property_code\":\"CPU\"},{\"memo\":null,\"resource_code\":\"RES-Docker-14905\",\"tenant_id\":\"unicom_zb\",\"property\":[{\"unit\":\"核\",\"ext1\":null,\"ext2\":\"1,2\",\"property_name\":\"CPU\",\"prop_value\":\"10\",\"ext3\":\"0\",\"type_name\":\"输入框\",\"value_type_id\":2,\"prop_id\":7678,\"ord\":1,\"property_code\":\"CPU\"},{\"unit\":\"G\",\"ext1\":null,\"ext2\":\"1,2\",\"property_name\":\"内存\",\"prop_value\":\"40\",\"ext3\":\"0\",\"type_name\":\"输入框\",\"value_type_id\":2,\"prop_id\":7850,\"ord\":2,\"property_code\":\"Memory\"},{\"unit\":\"GB\",\"ext1\":null,\"ext2\":\"2\",\"property_name\":\"存储\",\"prop_value\":\"10\",\"ext3\":\"0\",\"type_name\":\"输入框\",\"value_type_id\":2,\"prop_id\":14664,\"ord\":3,\"property_code\":\"Storage\"}],\"feedback\":null,\"resource_id\":14905,\"resource_name\":\"容器服务-14905\",\"create_time\":\"2016-10-1118:08:54\",\"value_type_id\":[{\"unit\":\"核\",\"ext1\":null,\"ext2\":\"1,2\",\"property_name\":\"CPU\",\"prop_value\":\"10\",\"ext3\":\"0\",\"type_name\":\"输入框\",\"value_type_id\":2,\"prop_id\":7678,\"ord\":1,\"property_code\":\"CPU\"},{\"unit\":\"G\",\"ext1\":null,\"ext2\":\"1,2\",\"property_name\":\"内存\",\"prop_value\":\"40\",\"ext3\":\"0\",\"type_name\":\"输入框\",\"value_type_id\":2,\"prop_id\":7850,\"ord\":2,\"property_code\":\"Memory\"},{\"unit\":\"GB\",\"ext1\":null,\"ext2\":\"2\",\"property_name\":\"存储\",\"prop_value\":\"10\",\"ext3\":\"0\",\"type_name\":\"输入框\",\"value_type_id\":2,\"prop_id\":14664,\"ord\":3,\"property_code\":\"Storage\"}],\"resource_type_id\":7536,\"resource_uri\":null,\"user_id\":\"uni000\",\"off_lease\":false,\"property_code\":\"CPU\"},{\"memo\":\"总部租户申请docker\",\"resource_code\":\"RES-Docker-7974\",\"tenant_id\":\"unicom_zb\",\"property\":[{\"unit\":\"核\",\"ext1\":null,\"ext2\":\"1,2\",\"property_name\":\"CPU\",\"prop_value\":\"10\",\"ext3\":\"0\",\"type_name\":\"输入框\",\"value_type_id\":2,\"prop_id\":7678,\"ord\":1,\"property_code\":\"CPU\"},{\"unit\":\"G\",\"ext1\":null,\"ext2\":\"1,2\",\"property_name\":\"内存\",\"prop_value\":\"100\",\"ext3\":\"0\",\"type_name\":\"输入框\",\"value_type_id\":2,\"prop_id\":7850,\"ord\":2,\"property_code\":\"Memory\"},{\"unit\":\"GB\",\"ext1\":null,\"ext2\":\"2\",\"property_name\":\"存储\",\"ext3\":\"0\",\"type_name\":\"输入框\",\"value_type_id\":2,\"ord\":3,\"prop_id\":14664}],\"feedback\":null,\"resource_id\":7974,\"resource_name\":\"容器服务-7974\",\"create_time\":\"2016-05-2319:01:05\",\"value_type_id\":[{\"unit\":\"核\",\"ext1\":null,\"ext2\":\"1,2\",\"property_name\":\"CPU\",\"prop_value\":\"10\",\"ext3\":\"0\",\"type_name\":\"输入框\",\"value_type_id\":2,\"prop_id\":7678,\"ord\":1,\"property_code\":\"CPU\"},{\"unit\":\"G\",\"ext1\":null,\"ext2\":\"1,2\",\"property_name\":\"内存\",\"prop_value\":\"100\",\"ext3\":\"0\",\"type_name\":\"输入框\",\"value_type_id\":2,\"prop_id\":7850,\"ord\":2,\"property_code\":\"Memory\"},{\"unit\":\"GB\",\"ext1\":null,\"ext2\":\"2\",\"property_name\":\"存储\",\"ext3\":\"0\",\"type_name\":\"输入框\",\"value_type_id\":2,\"ord\":3,\"prop_id\":14664}],\"resource_type_id\":7536,\"resource_uri\":null,\"user_id\":\"uni000\",\"off_lease\":false,\"property_code\":\"CPU\"},{\"memo\":null,\"resource_code\":\"RES-Docker-7938\",\"tenant_id\":\"unicom_zb\",\"property\":[{\"unit\":\"核\",\"ext1\":null,\"ext2\":\"1,2\",\"property_name\":\"CPU\",\"prop_value\":\"2\",\"ext3\":\"0\",\"type_name\":\"输入框\",\"value_type_id\":2,\"prop_id\":7678,\"ord\":1,\"property_code\":\"CPU\"},{\"unit\":\"G\",\"ext1\":null,\"ext2\":\"1,2\",\"property_name\":\"内存\",\"prop_value\":\"2\",\"ext3\":\"0\",\"type_name\":\"输入框\",\"value_type_id\":2,\"prop_id\":7850,\"ord\":2,\"property_code\":\"Memory\"},{\"unit\":\"GB\",\"ext1\":null,\"ext2\":\"2\",\"property_name\":\"存储\",\"ext3\":\"0\",\"type_name\":\"输入框\",\"value_type_id\":2,\"ord\":3,\"prop_id\":14664}],\"feedback\":null,\"resource_id\":7938,\"resource_name\":\"容器服务-7938\",\"create_time\":\"2016-05-2313:42:31\",\"value_type_id\":[{\"unit\":\"核\",\"ext1\":null,\"ext2\":\"1,2\",\"property_name\":\"CPU\",\"prop_value\":\"2\",\"ext3\":\"0\",\"type_name\":\"输入框\",\"value_type_id\":2,\"prop_id\":7678,\"ord\":1,\"property_code\":\"CPU\"},{\"unit\":\"G\",\"ext1\":null,\"ext2\":\"1,2\",\"property_name\":\"内存\",\"prop_value\":\"2\",\"ext3\":\"0\",\"type_name\":\"输入框\",\"value_type_id\":2,\"prop_id\":7850,\"ord\":2,\"property_code\":\"Memory\"},{\"unit\":\"GB\",\"ext1\":null,\"ext2\":\"2\",\"property_name\":\"存储\",\"ext3\":\"0\",\"type_name\":\"输入框\",\"value_type_id\":2,\"ord\":3,\"prop_id\":14664}],\"resource_type_id\":7536,\"resource_uri\":null,\"user_id\":\"uni000\",\"off_lease\":false,\"property_code\":\"CPU\"}],\"resource_type_id\":\"7536\",\"ord\":8,\"resource_multi_assigned\":null,\"resource_type_info\":\"容器是PaaS平台的典型应用，它通过Docker引擎实现的基于应用程序的快速部署，整合了负载均衡SLB，让开发者可以打包他们的应用以及依赖包到一个可移植的容器中，然后发布到任何流行的Linux机器上，也可以实现虚拟化。\",\"is_app\":true,\"resource_type_name\":\"容器服务\",\"type_code\":\"Docker\"}],\"message\":\"\",\"messageDetail\":\"\",\"success\":true,\"systemTime\":1487908772505,\"totalSize\":0}";
            if (StringUtils.isNotBlank(rtnStr)) {
                JSONObject jsStr = JSONObject.parseObject(rtnStr);
                if ((boolean)jsStr.get("success")) {
                	if (jsStr.get("data") != null) {
                		JSONArray jsArrData = (JSONArray) jsStr.get("data");
                		JSONObject jsObj = (JSONObject) jsArrData.get(0);
                		JSONArray jsArrData2 = (JSONArray) jsObj.get("data");
                		for (Object dataRow : jsArrData2){
                			JSONObject jsObj2 = (JSONObject) dataRow;
                			JSONArray jsPro = (JSONArray) jsObj2.get("property");
                			if (jsPro != null && jsPro.size() > 0) { // 获取CPU、MEM和Volume的值
                				for (Object oneRow : jsPro) {
                					JSONObject tmp = (JSONObject) oneRow;
                					if (((String)tmp.get("property_code")).trim().equals("CPU")) {
                						openCpu += Double.parseDouble((String)tmp.get("prop_value"));
                					}
                					else if (((String)tmp.get("property_code")).trim().equals("Memory")) {
                						openMem += Long.parseLong((String)tmp.get("prop_value"));
                					}
                					else if (((String)tmp.get("property_code")).trim().equals("Storage")){
                						volSize += Long.parseLong((String)tmp.get("prop_value"));
                					}
                				}
                			}
                		}
                		userResource.setVol_size(volSize);
                		if (userResource.getUserId() <= 0) {
                			userResource.setVol_surplus_size(userResource.getVol_size());
                		}
                		LOG.info("能力平台租户已分配资源:{" + "cpu:" + openCpu + ",mem:" + openMem +",volume:"+userResource.getVol_size()+"}");
                		createResourceQuota(namespace, openCpu, openMem,userResource);
                	}
				}
            }
        }
        catch (Exception e) {
            LOG.error("获取能力平台租户资源出错！******************************" + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     *
     * Description:
     *  创建能力平台租户资源
     * @param namespace
     * @param openCpu
     * @param openMem
     */
    private void createResourceQuota(String namespace, double openCpu, long openMem,UserResource userResource) {
        // 是否创建resourceQuota
        boolean isCreate = false;
        // KUBERNETES是否可以连接
        boolean isConnect = true;
        ResourceQuota currentQuota = null;

        // 以用户名(登陆帐号)为name，创建client
        KubernetesAPIClientInterface client = kubernetesClientService.getClient(namespace);
        try {
            currentQuota = client.getResourceQuota(namespace); // 获得resourceQuota
            LOG.info("k8s租户资源:" + currentQuota.toString());
        }
        catch (KubernetesClientException e) {
            LOG.error("k8s环境中没有此租户的资源.错误信息：-" + e.getMessage());
            isCreate = true;
        }
        catch (RuntimeException e) {
            LOG.error("连接kubernetesAPI超时！错误信息：-" + e.getMessage());
            isConnect = false;
        }
        // KUBERNETES不能连接
        if (isConnect) {
            // 为client创建资源配额
            Map<String, String> openMap = new HashMap<String, String>();
            openMap.put("memory", openMem + "G");// 内存（G）
            openMap.put("cpu", Double.valueOf(openCpu)/Double.valueOf(RATIO_MEMTOCPU) + "");// CPU数量(个)
            openMap.put("persistentvolumeclaims", userResource.getVol_size() + "");// 卷组数量

            userResource.setCpu(openCpu);
            userResource.setMemory(openMem);
            ResourceQuota openQuota = kubernetesClientService.generateSimpleResourceQuota(namespace, openMap);
            if (isCreate) { // 是否新建quota
                openQuota = client.createResourceQuota(openQuota); // 创建quota
                userResource.setCreateDate(new Date());
                if (openQuota != null) {
                    LOG.info("for namespace:-"+namespace+" create quota:" + JSON.toJSONString(openQuota));
                }
                else {
                    LOG.info("for namespace:-"+namespace+" create quota failed: namespace=" + namespace + "hard=" + openMap.toString());
                }
            }
            else { // 直接更新租户的quota 不用判断资源是否更新过
                currentQuota = client.updateResourceQuota(namespace, openQuota);
                userResource.setUpdateDate(new Date());
                if (null != currentQuota) {
                    LOG.info("for namespace:-"+namespace+" update quota:"+JSON.toJSONString(currentQuota));
                }
            }
        }
    }
}
