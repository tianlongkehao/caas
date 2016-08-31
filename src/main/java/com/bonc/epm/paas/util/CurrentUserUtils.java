
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
 * @version 2015年12月18日
 * @see CurrentUserUtils
 * @since 2015年12月18日
 */
public final class CurrentUserUtils {
    /**
     * CUR_USER
     */
    private static final String CUR_USER = "cur_user";
    /**
     * CAS_ENABLE
     */
    private static final String CAS_ENABLE = "cas_enable";
    /**
     * INSTANCE
     */
    private static CurrentUserUtils INSTANCE = null;
	
    /**
     * 
     * Description:
     * 获取实例
     * @return INSTANCE CurrentUserUtils
     * @see this
     */
    public static CurrentUserUtils getInstance(){
        if(INSTANCE == null) {
            synchronized (CurrentUserUtils.class) {
                if(INSTANCE == null) {
                    INSTANCE = new CurrentUserUtils();
                }
            }
        }
        return INSTANCE;
    }
    
    /**
     * 
     * Description:
     * 获取当前Request
     * @return request HttpServletRequest
     * @see HttpServletRequest
     */
    private HttpServletRequest getRequest() {  
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();  
        return requestAttributes.getRequest();  
    } 
	
    /**
     * 
     * Description:
     * 获取当前Session
     * @return session HttpSession
     * @see HttpSession
     */
    private HttpSession getSession() {  
        return getRequest().getSession(true);  
    }
	
    /**
     * 
     * Description:
     * 获取当前session里面放置的User对象
     * @return user User
     * @see User
     */
    public User getUser(){
        return (User)getSession().getAttribute(CUR_USER);
    }
	
    /**
     * 
     * Description:
     * 把当前User对象放置到session里面
     * @param user User
     * @see
     */
    public void setUser(User user){
        getSession().setAttribute(CUR_USER, user);
    }

    public Boolean getCasEnable(){
        return (Boolean) getSession().getAttribute(CAS_ENABLE);
    }
	
    /**
     * 
     * Description: 
     * setCasEnable
     * @param cas_enable 
     * @see
     */
    public void setCasEnable(Boolean cas_enable){
        getSession().setAttribute(CAS_ENABLE, cas_enable);
    }
	
    /**
     * 
     * Description:
     * 退出当前用户
     * @param user 
     * @see
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