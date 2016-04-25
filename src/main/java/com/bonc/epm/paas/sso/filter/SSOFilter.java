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

import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bonc.epm.paas.constant.UserConstant;
import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;
import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.model.Namespace;
import com.bonc.epm.paas.kubernetes.model.ResourceQuota;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
import com.bonc.epm.paas.sso.casclient.CasClientConfigurationProperties;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.RESTResourceClient;

@Component
public class SSOFilter implements Filter {
	private static final String CONST_CAS_ASSERTION = "_const_cas_assertion_";
	private static final String AUTH_FAILURE_MSG_NO_PRIVILEGE = "您没有权限登录本系统！";
	private static final String AUTH_FAILURE_MSG_NO_RESOURCE = "您尚未分配部署资源，请联系管理员！";

	@Autowired
	CasClientConfigurationProperties configProps;

	@Autowired
	private KubernetesClientService kubernetesClientService;

	@Autowired
	public UserDao userDao;

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
			if (assertion == null || assertion.getPrincipal() == null) {
				authFailure(response, AUTH_FAILURE_MSG_NO_PRIVILEGE);
			}
			String casLoginUserName = assertion.getPrincipal().getName();
			String tenantId = null;
			String tenantAdmin = null;
			if (attributes.get("tenantId") != null) {
				tenantId = attributes.get("tenantId").toString();
			}
			if (attributes.get("tenantAdmin") != null) {
				tenantAdmin = attributes.get("tenantAdmin").toString();
			}
			// paas当前登录用户
			User currPaasUser = CurrentUserUtils.getInstance().getUser();
			boolean isLoadPaasUser = false;
			if (currPaasUser == null) {
				isLoadPaasUser = true;
			} else if (currPaasUser != null && !casLoginUserName.equals(currPaasUser.getUserName())) {
				// cas当前认证用户发生变化
				isLoadPaasUser = true;
			}
			if (isLoadPaasUser) {
				// 更新租户
				User user = saveUser(assertion);
				// 当前登陆为租户
				if ("1".equals(tenantAdmin)) {
					// 创建命名空间
					createNamespace(tenantId);
					// 创建资源
					createQuota(tenantId);
				}
				httpRequest.getSession().setAttribute("cur_user", user);
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
	 * @return User
	 */
	private User saveUser(Assertion assertion) {
		String userName = assertion.getPrincipal().getName();
		User user = userDao.findByUserName(userName);
		if (user == null) {
			user = new User();
			user.setPassword(UserConstant.INIT_PASSWORD);
		}
		Map<String, Object> attributes = assertion.getPrincipal().getAttributes();
		user.setUserName(userName);
		user.setUser_realname(userName);
		user.setOpen_user_key(attributes.get("userId").toString());
		if (attributes.get("email") != null) {
			user.setEmail(attributes.get("email").toString());
		}
		if (attributes.get("tenantAdmin") != null) {
			String tenantAdmin = attributes.get("tenantAdmin").toString();
			// 租户
			if ("1".equals(tenantAdmin)) {
				user.setUser_autority(UserConstant.AUTORITY_TENANT);
				user.setParent_id(0);
			} else if ("0".equals(tenantAdmin)){
				user.setUser_autority(UserConstant.AUTORITY_USER);
				user.setParent_id(Long.valueOf(attributes.get("tenantId").toString()));
			}
		}
		// 创建租户用户
		userDao.save(user);
		return user;
	}

	/**
	 * 创建nameSpace
	 * 
	 * @param userName
	 */
	private void createNamespace(String tenantId) {
		// 查找租户名称
		User tenantUser = userDao.findById(Long.valueOf(tenantId));
		String tenantName = tenantUser.getUserName();
		// 以用户名(登陆帐号)为name，创建client
		KubernetesAPIClientInterface client = kubernetesClientService.getClient("");
		// 是否创建nameSpace
		boolean createFlg = false;
		try {
			client.getNamespace(tenantName);
		} catch (KubernetesClientException e) {
			createFlg = true;
		} catch (RuntimeException e) {
			System.out.println("连接kubernetesAPI超时！" + e);
		}
		if (createFlg) {
			// 以用户名(登陆帐号)为name，为client创建nameSpace
			Namespace namespace = kubernetesClientService.generateSimpleNamespace(tenantName);
			namespace = client.createNamespace(namespace);
		}
	}

	/**
	 * 给租户创建资源
	 * 
	 * @param userName
	 */
	@SuppressWarnings("unchecked")
	private void createQuota(String tenantId) {
		// 查找租户名称
		User tenantUser = userDao.findById(Long.valueOf(tenantId));
		String tenantName = tenantUser.getUserName();
		String open_cpu = "0";
		String open_mem = "0";
		try {
			// 调用能力平台用户资源查询接口，查询为此用户分配的资源
			RESTResourceClient resourceClient = new RESTResourceClient();
			// 根据名字查询能力平台resource
			Map<String, String> open_res = (Map<String, String>) resourceClient.getResById(tenantId);
			open_cpu = open_res.get("cpu");
			open_mem = open_res.get("memory");
			System.out.println("能力平台租户资源:" + open_res.toString());
		} catch (Exception e) {
			System.out.println("获取能力平台租户资源出错！" + e);
		}
		// 为client创建资源配额
		Map<String, String> open_map = new HashMap<String, String>();
		// 内存
		open_map.put("memory", open_mem + "G");
		// CPU数量(个)
		open_map.put("cpu", open_cpu + "");
		// 以用户名(登陆帐号)为name，创建client
		KubernetesAPIClientInterface client = kubernetesClientService.getClient(tenantName);
		// 生成quota
		ResourceQuota open_quota = kubernetesClientService.generateSimpleResourceQuota(tenantName, open_map);
		// 是否创建resourceQuota
		boolean createFlg = false;
		// kubernetes是否可以连接
		boolean disConnectFlg = false;
		ResourceQuota current_quota = null;
		try {
			// 获得resourceQuota
			current_quota = client.getResourceQuota(tenantName);
			System.out.println("k8s租户资源:" + current_quota.toString());
		} catch (KubernetesClientException e) {
			createFlg = true;
		} catch (RuntimeException e) {
			System.out.println("连接kubernetesAPI超时！" + e);
			disConnectFlg = true;
		}
		// kubernetes不能连接
		if (!disConnectFlg) {
			if (createFlg) {
				// 创建quota
				client.createResourceQuota(open_quota);
			} else {
				Map<String, String> map = current_quota.getSpec().getHard();
				Integer int_cur_cpu = Integer.parseInt(map.get("cpu"));
				Integer int_cur_mem = Integer.parseInt(map.get("memory").replace("Gi", "").replace("G", ""));
				Integer int_open_cpu = Integer.parseInt(open_cpu);
				Integer int_open_mem = Integer.parseInt(open_mem);

				// 判断是否增加了资源
				if (int_open_cpu != 0 && int_open_cpu >= int_cur_cpu && int_open_mem >= int_cur_mem) {
					// 更新quota
					client.updateResourceQuota(tenantName, open_quota);
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
