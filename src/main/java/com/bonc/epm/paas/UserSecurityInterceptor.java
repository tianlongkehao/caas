package com.bonc.epm.paas;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.bonc.epm.paas.controller.ServiceController;
import com.bonc.epm.paas.dao.PortConfigDao;
import com.bonc.epm.paas.entity.User;
/**
 * 拦截未登录的用户信息
 * @author fengtao
 * 2015-12-21
 */
public class UserSecurityInterceptor implements HandlerInterceptor {

   @Autowired
   public PortConfigDao portConfigDao;
    
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		 //验证用户是否登陆
        Object obj = request.getSession().getAttribute("cur_user");
        if (obj == null || !(obj instanceof User)) {
            response.sendRedirect(request.getContextPath() + "/login");
            
            //ServiceController.smalSet.clear();
            if (portConfigDao == null) {//解决service为null无法注入问题 
               System.out.println("portConfigDao is null!!!"); 
               BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext()); 
               portConfigDao = (PortConfigDao) factory.getBean("portConfigDao"); 
               }
            ServiceController.smalSet.addAll(portConfigDao.findPortSets());
            ServiceController.smalSet.remove(null);
            return false;
        }
        
        return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
