/*
 * 文件名：ApplicationFilterConfiguration.java
 * 版权：Copyright by www.huawei.com
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年9月30日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas;

import javax.servlet.Filter;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bonc.sso.client.SSOFilter;

/**
 * ApplicationFilterConfiguration
 * @author ke_wang
 * @version 2016年9月30日
 * @see ApplicationFilterConfiguration
 * @since
 */
@Configuration
public class ApplicationFilterConfiguration {
    /**
     * 
     * Description:
     * filterRegistration
     * @return FilterRegistrationBean 
     * @see
     */
    @Bean
    public FilterRegistrationBean filterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(ssoFilter());
        registration.setName("SSO Filter");
        registration.addInitParameter("serverName", "localhost"); // CAS客户端地址
        registration.addInitParameter("casServerUrlPrefix", "http://132.35.227.90/cas"); //CAS服务器地址全路径
        registration.addInitParameter("casServerLoginUrl", "http://132.35.227.90/cas/login"); //CAS服务器登录地址 全路径
        registration.addInitParameter("singleSignOut", "true"); //是否启用单点登出
        registration.addInitParameter("skipUrls", "/login.jsp"); // 单点登录忽略校验URL
        //登录成功后的的用户信息准备 须实现com.bonc.pure.sso.client.ILoginUserHand 接口
        registration.addInitParameter("loginUserHandle", "com.bonc.epm.paas.sso.filter.SSOAuthHandleImpl"); 
        // 可选参数，客户端应用使用的字符集，如果已经有其他的地方设置过了，则会忽略这个配置。默认将使用UTF-8作为默认字符集
        registration.addInitParameter("characterEncoding", "UTF-8");
        // 解决读取CAS server端返用户扩展信息中文乱码问题
        registration.addInitParameter("encoding", "UTF-8");
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    } 

    /**
     * 
     * Description:
     * 返回SSOFilter
     * @return com.bonc.sso.client.SSOFilter
     * @see
     */
    @Bean(name = "SSO Filter")
    public Filter ssoFilter() {
        return new SSOFilter();
    }
}
