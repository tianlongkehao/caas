package com.bonc.epm.paas.sso.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.epm.paas.constant.UserConstant;
import com.bonc.epm.paas.controller.CephController;
import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.entity.User;
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
 * SSOFilter 过滤器
 * 实现cas单点登录
 * @author ke_wang
 * @version 2016年9月10日
 * @see SSOFilter
 */
@Component
public class SSOFilter implements Filter {
    /**
     * 输出日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(SSOFilter.class);
    /**
     * CONST_CAS_ASSERTION
     */
    private static final String CONST_CAS_ASSERTION = "_const_cas_assertion_";
    /**
     * AUTH_FAILURE_MSG_NO_PRIVILEGE
     */
    private static final String AUTH_FAILURE_NO_PRIVILEGE = "您没有权限登录本系统！";
    /**
     * AUTH_FAILURE_MSG_NO_RESOURCE
     */
	//private static final String AUTH_FAILURE_NO_RESOURCE = "您尚未分配部署资源，请联系管理员！";

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
     * resManUrl
     */
    @Value("${resourceMmanage.address}")
	private String resManUrl;

    @Override
	public void destroy() {
        
    }

    /* (non-Javadoc)
     * ${see_to_overridden}
     */
    @Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
        if (configProps.getEnable()) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
			// cas当前认证用户
            Assertion assertion = (Assertion) httpRequest.getSession().getAttribute(CONST_CAS_ASSERTION);
            Map<String, Object> attributes = assertion.getPrincipal().getAttributes();
            LOG.debug("能力平台USER:" + attributes.toString());
            if (null == assertion || null == assertion.getPrincipal()) {
                authFailure(response, AUTH_FAILURE_NO_PRIVILEGE);
            }
            LOG.debug("cas登陆Id" + assertion.getPrincipal().getName());
			
            String tenantId = "";
            String namespace = "";
            String tenantAdmin = "";
            if (null == attributes.get("tenantId")) {
                attributes.put("tenantId", assertion.getPrincipal().getName());
            }
            if (null != attributes.get("tenantId")) {
                tenantId = attributes.get("tenantId").toString();
                namespace = tenantId;
                //把下划线替换为--
                if (namespace.contains("_")){
                    namespace = namespace.replace("_", "--");
                }
            }
            if (attributes.get("tenantAdmin") != null) {
                tenantAdmin = attributes.get("tenantAdmin").toString();
            }
			
			// 与PAAS当前存在登录用户对比
            User currPaasUser = CurrentUserUtils.getInstance().getUser();
            if (null != currPaasUser) {
                LOG.debug("paas当前登陆用户" + currPaasUser.getUserName());
            }
            boolean isLoadPaasUser = false;
            if (null == currPaasUser) {
                isLoadPaasUser = true;
            }
            else if (null != currPaasUser && 
                            !assertion.getPrincipal().getName().trim().equals(currPaasUser.getUserName())) {
				// cas当前认证用户发生变化
                isLoadPaasUser = true;
            }
			
			// 是否更新登陆用户
            if (isLoadPaasUser) {
                try {
                    // 更新租户
                    User user = saveUser(assertion, namespace);
                    // 统一平台的userId
                    if (null != attributes.get("userId")) {
                         //是租户而且不是管理员
                        if ("1".equals(tenantAdmin) && 
                                    !"1".equals(attributes.get("userId").toString().trim())) {
                            createNamespace(namespace); // 创建命名空间
                            createQuota(tenantId, namespace); // 创建资源
                            createCeph(namespace); // 创建ceph
                            
                        }
                    }
                    
                    httpRequest.getSession().setAttribute("cur_user", user);
                    httpRequest.getSession().setAttribute("cas_enable", configProps.getEnable()); 
                    httpRequest.getSession().setAttribute("ssoConfig", configProps);                    
                }
                catch (Exception e) {
                    LOG.error(e.getMessage());
                    throw new ServiceException();
                }
            }
        }
        chain.doFilter(request, response);
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
     * init
     * ${see_to_overridden}
     */
    @Override
	public void init(FilterConfig arg0) throws ServletException {
        
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
    private User saveUser(Assertion assertion, String namespace) throws Exception{
        String loginId = assertion.getPrincipal().getName();
        User user = userDao.findByUserName(loginId);
        if (null == user) {
            user = new User();
            user.setPassword(UserConstant.INIT_PASSWORD);
        }
        user.setNamespace(namespace);
        user.setUserName(loginId);
        Map<String, Object> attributes = assertion.getPrincipal().getAttributes();
        if (null != attributes.get("userName")) { // 用户的名称
            user.setUser_realname(attributes.get("userName").toString());
        }
        if (null != attributes.get("userId")) { // 统一平台的userId
            user.setOpen_user_id(attributes.get("userId").toString());
            if("1".equals(attributes.get("userId").toString())){ //判断是否为超级管理员
                user.setUser_autority(UserConstant.AUTORITY_MANAGER);
            }
        }
        if (null != attributes.get("email")) { // email
            user.setEmail(attributes.get("email").toString());
        }
        if (null != attributes.get("mobile")) { // 手机
            user.setUser_cellphone(attributes.get("mobile").toString());
        }
        if (null != attributes.get("telephone")) { // 电话
            user.setUser_phone(attributes.get("telephone").toString());
        }
        if (null != attributes.get("tenantAdmin")) {
            String tenantAdmin = attributes.get("tenantAdmin").toString();
            if ("1".equals(tenantAdmin)) { // 是租户
                user.setUser_autority(UserConstant.AUTORITY_TENANT);
            } 
            else if ("0".equals(tenantAdmin)) {
                user.setUser_autority(UserConstant.AUTORITY_USER);
            }
        }
        userDao.save(user);
        return user;
    }

    /**
     * 
     * Description:
     * 创建nameSpace
     * @param tenantId 
     * @throws ServiceException  
     */
    private void createNamespace(String tenantId) throws ServiceException{
		// 以用户名(登陆帐号)为name，创建client ??
        KubernetesAPIClientInterface client = kubernetesClientService.getClient("");
		// 是否创建nameSpace
        try {
            client.getNamespace(tenantId);
        } 
        catch (KubernetesClientException e) {
            // 以用户名(登陆帐号)为name，为client创建nameSpace
            Namespace namespace = kubernetesClientService.generateSimpleNamespace(tenantId);
            namespace = client.createNamespace(namespace);
            if (namespace == null) {
				LOG.error("Create a new Namespace:namespace["+namespace+"]");
			}else {
				LOG.info("create namespace:" + JSON.toJSONString(namespace));
			}
        }
        catch (RuntimeException e) {
            LOG.error("连接kubernetesAPI超时！" + e);
            throw new ServiceException();
        }
    }

    /**
     * 
     * Description:
     * 给租户创建资源
     * @param tenantId 
     * @param namespace  
     * @exception Exception 
     */
    private void createQuota(String tenantId, String namespace) throws Exception{
        String openCpu = "0";
        String openMem = "0";
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("tenant_id","\""+tenantId+"\"");
            data.put("resource_type_code","\"Docker\"");
            params.put("param", data);
            String rtnStr = WebClientUtil.doGet(resManUrl, params);
			// 解析返回资源数据
            if (StringUtils.isNotBlank(rtnStr)) {
                JSONObject jsStr = JSONObject.parseObject(rtnStr);
                if ((boolean)jsStr.get("success")) {
                    JSONArray jsArrData = (JSONArray) jsStr.get("data");
                    JSONObject jsObj = (JSONObject) jsArrData.get(0);
                    JSONArray jsArrData2 = (JSONArray) jsObj.get("data");
                    JSONObject jsObj2 = (JSONObject) jsArrData2.get(0);
                    JSONArray jsPro = (JSONArray) jsObj2.get("property");
                    JSONObject cpuObject = (JSONObject) jsPro.get(0);
                    JSONObject memObject = (JSONObject) jsPro.get(1);
                    // 获取CPU和MEM的值
                    openCpu = (String) cpuObject.get("prop_value");
                    openMem = (String) memObject.get("prop_value");
                    LOG.info("能力平台租户已分配资源:{" + "cpu:" + openCpu + ",mem:" + openMem + "}");
                    createResourceQuota(namespace, openCpu, openMem);
                }
            }
        }
        catch (Exception e) {
            LOG.error("获取能力平台租户资源出错！" + e.getMessage());
            e.printStackTrace();
            throw new Exception();
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
    private void createResourceQuota(String namespace, String openCpu, String openMem) {
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
            openMap.put("cpu", openCpu + "");// CPU数量(核)
            openMap.put("memory", openMem + "G");// 内存（G）
            ResourceQuota openQuota = kubernetesClientService.generateSimpleResourceQuota(namespace, openMap);
            if (isCreate) { // 是否新建quota
            	openQuota = client.createResourceQuota(openQuota); // 创建quota
                if (openQuota != null) {
                	LOG.info("create quota:" + JSON.toJSONString(openQuota));
				} else {
					LOG.info("create quota failed: namespace=" + namespace + "hard=" + openMap.toString());
				}
            } 
            else {
                Map<String, String> map = currentQuota.getSpec().getHard();
                Integer intCurCpu = Integer.parseInt(map.get("cpu"));
                Integer intCurMem = Integer.parseInt(map.get("memory").replace("Gi", "").replace("G", ""));
                Integer intOpenCpu = Integer.parseInt(openCpu);
                Integer intOpenMem = Integer.parseInt(openMem);
        		// 判断资源是否变化
                if ((intOpenCpu != intCurCpu) || (intOpenMem != intCurMem)) {
                    client.updateResourceQuota(namespace, openQuota);
                }
            }
        }
    }

    /**
     * 
     * Description:
     * cas认证失败
     * @param response 
     * @param msg 
     * @throws IOException  
     * @see
     */
    private void authFailure(ServletResponse response, String msg) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        response.getWriter().print("<html><head></head><body>" + msg + "</body></html>");
    }
}
