package com.bonc.epm.paas.sso.filter;

import java.io.IOException;

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

import com.bonc.epm.paas.dao.UserDao;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.sso.conf.CasClientConfigProperties;
import com.bonc.epm.paas.util.CurrentUserUtils;


 //@Component
public class SSOFilter implements Filter{
	private static final String CONST_CAS_ASSERTION = "_const_cas_assertion_";
	private static final String AUTH_FAILURE_MSG_NO_PRIVILEGE = "您没有权限登录本系统！";
	private static final String AUTH_FAILURE_MSG_NO_RESOURCE = "您尚未分配部署资源，请联系管理员！";
	
	@Autowired
    private CasClientConfigProperties casClientConfigProperties;
	
	@Autowired
    public UserDao userDao;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		//cas当前认证用户
		Assertion assertion = (Assertion) httpRequest.getSession().getAttribute(CONST_CAS_ASSERTION);
		
		if (assertion == null || assertion.getPrincipal() == null) {
			authFailure(response, AUTH_FAILURE_MSG_NO_PRIVILEGE);
		}
		
		String casLoginUserName = assertion.getPrincipal().getName();
		
		//paas当前登录用户
		User currPaasUser = CurrentUserUtils.getInstance().getUser();
		boolean isLoadPaasUser = false;
		
		if (currPaasUser == null) {
			isLoadPaasUser = true;
		} else if (currPaasUser != null && !casLoginUserName.equals(currPaasUser.getUserName())) {
			//cas当前认证用户发生变化
			isLoadPaasUser = true;
		}
		
		if (isLoadPaasUser) {
			User user = userDao.findByUserName(casLoginUserName);
			if (user == null) {
				authFailure(response, AUTH_FAILURE_MSG_NO_RESOURCE);
			}
			httpRequest.getSession().setAttribute("cur_user", user);
			httpRequest.getSession().setAttribute("ssoConfig", casClientConfigProperties);
		}
		
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
	
	private void authFailure(ServletResponse response, String msg) throws IOException{
		response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
		response.getWriter().print("<html><head></head><body>" + msg + "</body></html>");
	}
}
