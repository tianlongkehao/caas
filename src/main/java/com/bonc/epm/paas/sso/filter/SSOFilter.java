package com.bonc.epm.paas.sso.filter;

import java.io.IOException;
import java.net.ConnectException;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.epm.paas.constant.UserConstant;
import com.bonc.epm.paas.controller.CiController;
import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.model.Namespace;
import com.bonc.epm.paas.kubernetes.model.ResourceQuota;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
import com.bonc.epm.paas.sso.casclient.CasClientConfigurationProperties;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.WebClientUtil;

@Component
public class SSOFilter implements Filter {
    /**
     * 输出日志
     */
   private static final Logger LOG = LoggerFactory.getLogger(SSOFilter.class);
    
	private static final String CONST_CAS_ASSERTION = "_const_cas_assertion_";
	private static final String AUTH_FAILURE_MSG_NO_PRIVILEGE = "您没有权限登录本系统！";
	private static final String AUTH_FAILURE_MSG_NO_RESOURCE = "您尚未分配部署资源，请联系管理员！";

	@Autowired
	CasClientConfigurationProperties configProps;

	@Autowired
	private KubernetesClientService kubernetesClientService;

	@Autowired
	public UserDao userDao;

	@Value("${resourceMmanage.address}")
	private String resManUrl;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (configProps.getEnable()) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			// cas当前认证用户
			Assertion assertion = (Assertion) httpRequest.getSession().getAttribute(CONST_CAS_ASSERTION);
			Map<String, Object> attributes = assertion.getPrincipal().getAttributes();
			LOG.debug("能力平台USER:" + attributes.toString());
			if (assertion == null || assertion.getPrincipal() == null) {
				authFailure(response, AUTH_FAILURE_MSG_NO_PRIVILEGE);
			}
			LOG.debug("cas登陆Id" + assertion.getPrincipal().getName());
			
			attributes.put("tenantId", "unicom");
			attributes.put("tenantAdmin", "1");
			
			String tenantId = "";
			String namespace = "";
			String tenantAdmin = "";
			if (attributes.get("tenantId") != null) {
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
			if (currPaasUser != null) {
				LOG.debug("paas当前登陆用户" + currPaasUser.getUserName());
			}
			boolean isLoadPaasUser = false;
			if (currPaasUser == null) {
				isLoadPaasUser = true;
			} else if (currPaasUser != null && !assertion.getPrincipal().getName().trim().equals(currPaasUser.getUserName())) {
				// cas当前认证用户发生变化
				isLoadPaasUser = true;
			}
			
			// 是否更新登陆用户
			if (isLoadPaasUser) {
				// 更新租户
				User user = saveUser(assertion, namespace);
				// 统一平台的userId
				if (null != attributes.get("userId")) {
	                 //是租户而且不是管理员
	                if ("1".equals(tenantAdmin) && !"1".equals(attributes.get("userId").toString().trim())) {
	                         // 创建命名空间
	                    createNamespace(namespace);
	                         // 创建资源
	                    createQuota(tenantId, namespace);
	                    }
				}
				
				httpRequest.getSession().setAttribute("cur_user", user);
				httpRequest.getSession().setAttribute("cas_enable", configProps.getEnable()); 
				httpRequest.getSession().setAttribute("ssoConfig", configProps);
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	/**
	 * 保存用户
	 * 
	 * @param assertion
	 * @return namespace
	 */
	private User saveUser(Assertion assertion, String namespace) {
		// 登陆Id
		String loginId = assertion.getPrincipal().getName();
		User user = userDao.findByUserName(loginId);
		if (user == null) {
			user = new User();
			user.setPassword(UserConstant.INIT_PASSWORD);
		}
		//NAMESPACE
		user.setNamespace(namespace);
		user.setUserName(loginId);
		Map<String, Object> attributes = assertion.getPrincipal().getAttributes();
		// 用户的名称
		if (null != attributes.get("userName")) {
		    user.setUser_realname(attributes.get("userName").toString());
		}
		// 统一平台的userId
		if (null != attributes.get("userId")) {
		    user.setOpen_user_id(attributes.get("userId").toString());
		      //判断是否为超级管理员
	       if("1".equals(attributes.get("userId").toString())){
	            user.setUser_autority(UserConstant.AUTORITY_MANAGER);
	        }
		}

		// email
		if (null != attributes.get("email")) {
			user.setEmail(attributes.get("email").toString());
		}
		// 手机
		if (null != attributes.get("mobile")) {
			user.setUser_cellphone(attributes.get("mobile").toString());
		}
		// 电话
		if (null != attributes.get("telephone")) {
			user.setUser_phone(attributes.get("telephone").toString());
		}
		if (null != attributes.get("tenantAdmin")) {
			// 是否为租户
			String tenantAdmin = attributes.get("tenantAdmin").toString();
			if ("1".equals(tenantAdmin)) {
				user.setUser_autority(UserConstant.AUTORITY_TENANT);
			} else if ("0".equals(tenantAdmin)) {
				user.setUser_autority(UserConstant.AUTORITY_USER);
			}
		}
		// 创建租户用户
		userDao.save(user);
		return user;
	}

	/**
	 * 创建nameSpace
	 * 
	 * @param tenantId
	 */
	private void createNamespace(String tenantId) {
		LOG.debug("创建nameSpace");
		// 以用户名(登陆帐号)为name，创建client
		KubernetesAPIClientInterface client = kubernetesClientService.getClient("");
		// 是否创建nameSpace
		try {
			client.getNamespace(tenantId);
		} catch (KubernetesClientException e) {
            // 以用户名(登陆帐号)为name，为client创建nameSpace
          Namespace namespace = kubernetesClientService.generateSimpleNamespace(tenantId);
          namespace = client.createNamespace(namespace);
		} catch (RuntimeException e) {
			LOG.error("连接kubernetesAPI超时！" + e);
		}
	}

	/**
	 * 给租户创建资源
	 * 
	 * @param tenantId
	 * @param namespace
	 */
	private void createQuota(String tenantId, String namespace) {
	   LOG.debug("给租户创建资源");
		String open_cpu = "0";
		String open_mem = "0";
		boolean isExistResMan = false;
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("tenant_id", "\"" + tenantId + "\"");
			data.put("resource_type_code", "\"container\"");
			params.put("param", data);
			String rtnStr = WebClientUtil.doGet(resManUrl, params);
			// 解析返回资源数据
			if (StringUtils.isNotBlank(rtnStr)) {
	            JSONObject jsStr = JSONObject.parseObject(rtnStr);
	            JSONArray jsArrData = (JSONArray) jsStr.get("data");
	            JSONObject jsObj = (JSONObject) jsArrData.get(0);
	            JSONArray jsArrData2 = (JSONArray) jsObj.get("data");
	            JSONObject jsObj2 = (JSONObject) jsArrData2.get(0);
	            JSONArray jsPro = (JSONArray) jsObj2.get("property");
	            JSONObject cpuObject = (JSONObject) jsPro.get(0);
	            JSONObject memObject = (JSONObject) jsPro.get(1);
	               // 获取CPU和MEM的值
	            open_cpu = (String) cpuObject.get("prop_value");
	            open_mem = (String) memObject.get("prop_value");
	            LOG.info("能力平台租户已分配资源:{" + "cpu:" + open_cpu + ",mem:" + open_mem + "}");
	            isExistResMan = true;
			}
		} catch (Exception e) {
			LOG.error("获取能力平台租户资源出错！");
			e.printStackTrace();
		}
		// 能力平台租户资源存在,则创建
		if (isExistResMan) {
		    createResourceQuota(namespace, open_cpu, open_mem);
		}
	}

	/**
	 * 
	 * Description: 
	 *  创建能力平台租户资源
	 * @param namespace 
	 * @param open_cpu 
	 * @param open_mem  
	 */
    private void createResourceQuota(String namespace, String open_cpu, String open_mem) {
        // 是否创建resourceQuota
        boolean isCreate = false;
        // KUBERNETES是否可以连接
        boolean isConnect = true;
        ResourceQuota current_quota = null;
        
        // 以用户名(登陆帐号)为name，创建client
        KubernetesAPIClientInterface client = kubernetesClientService.getClient(namespace);
        try {
        	// 获得resourceQuota
        	current_quota = client.getResourceQuota(namespace);
        	LOG.info("k8s租户资源:" + current_quota.toString());
        } catch (KubernetesClientException e) {
        	LOG.error("k8s环境中没有此租户的资源.错误信息：-" + e.getMessage());
        	isCreate = true;
        } catch (RuntimeException e) {
        	LOG.error("连接kubernetesAPI超时！错误信息：-" + e.getMessage());
        	isConnect = false;
        }
        // KUBERNETES不能连接
        if (isConnect) {
        	// 为client创建资源配额
        	Map<String, String> open_map = new HashMap<String, String>();
        	open_map.put("cpu", open_cpu + "");// CPU数量(核)
        	open_map.put("memory", open_mem + "G");// 内存（G）
        	// 生成quota
        	ResourceQuota open_quota = kubernetesClientService.generateSimpleResourceQuota(namespace, open_map);
        	// 是否新建quota
        	if (isCreate) {
        		// 创建quota
        		client.createResourceQuota(open_quota);
        	} else {
        		Map<String, String> map = current_quota.getSpec().getHard();
        		Integer int_cur_cpu = Integer.parseInt(map.get("cpu"));
        		Integer int_cur_mem = Integer.parseInt(map.get("memory").replace("Gi", "").replace("G", ""));
        		Integer int_open_cpu = Integer.parseInt(open_cpu);
        		Integer int_open_mem = Integer.parseInt(open_mem);

        		// 判断资源是否变化
        		if ((int_open_cpu != int_cur_cpu) || (int_open_mem != int_cur_mem)) {
        			// 更新quota
        			client.updateResourceQuota(namespace, open_quota);
        		}
        	}
        }
    }

	/**
	 * 失败
	 * 
	 * @param response
	 * @param msg
	 * @throws IOException
	 */
	private void authFailure(ServletResponse response, String msg) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.getWriter().print("<html><head></head><body>" + msg + "</body></html>");
	}
}
