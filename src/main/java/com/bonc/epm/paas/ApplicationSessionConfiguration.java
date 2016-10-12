/*
 * 文件名：ApplicationSessionConfiguration.java
 * 版权：Copyright by www.huawei.com
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年9月30日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas;

import javax.servlet.http.HttpSessionListener;

import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ApplicationSessionConfiguration
 * springboot配置应用sesionListener
 * @author ke_wang
 * @version 2016年9月30日
 * @see ApplicationSessionConfiguration
 * @since
 */
@Configuration
public class ApplicationSessionConfiguration {
    /**
     * 
     * Description:
     * @return  ServletListenerRegistrationBean 
     * @see
     */
    @Bean
    public ServletListenerRegistrationBean<HttpSessionListener> sessionListener() {
        return new ServletListenerRegistrationBean<HttpSessionListener>(new SingleSignOutHttpSessionListener());
    }
}
