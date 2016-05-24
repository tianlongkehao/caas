package com.bonc.epm.paas.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bonc.epm.paas.entity.User;

/**
 * 管理当前当前登录对象
 * @author fengtao
 * 2015-12-18
 */
public class CurrentUserUtils {
	private final String CUR_USER = "cur_user";
	private final String CAS_ENABLE = "cas_enable";
	public static CurrentUserUtils INSTANCE = null;
	
	private CurrentUserUtils(){
		
	}
	
	/**
	 * 获取实例
	 * @return
	 */
	public static CurrentUserUtils getInstance(){
		if(INSTANCE == null){
			synchronized (CurrentUserUtils.class) {
				if(INSTANCE == null) {
					INSTANCE = new CurrentUserUtils();
				}
			}
		}
		
		return INSTANCE;
	}
	/**
	 * 获取当前Request
	 * @return
	 */
	private HttpServletRequest getRequest() {  
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();  
        return requestAttributes.getRequest();  
    } 
	
	/**
	 * 获取当前Session
	 * @return
	 */
	private HttpSession getSession() {  
        return getRequest().getSession(true);  
    }
	
	/**
	 * 获取当前session里面放置的User对象
	 * @return
	 */
	public User getUser(){
		return (User)getSession().getAttribute(CUR_USER);
	}
	
	/**
	 * 把当前User对象放置到session里面
	 * @param user
	 */
	public void setUser(User user){
		getSession().setAttribute(CUR_USER, user);
	}

	public Boolean getCasEnable(){
		return (Boolean) getSession().getAttribute(CAS_ENABLE);
	}
	
	public void setCasEnable(Boolean cas_enable){
		getSession().setAttribute(CAS_ENABLE, cas_enable);
	}
	
	/**退出当前用户
	 * 
	 * 
	 */
	public void loginoutUser(User user){
		Enumeration<String> em = getSession().getAttributeNames();
		while (em.hasMoreElements()) {
			getSession().removeAttribute(em.nextElement().toString());
		}
		getSession().removeAttribute(CUR_USER);
		getSession().invalidate();
	}
}
