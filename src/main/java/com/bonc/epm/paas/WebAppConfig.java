package com.bonc.epm.paas;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.bonc.epm.paas.constant.TemplateConf;
import com.bonc.epm.paas.constant.esConf;

import net.unicon.cas.client.configuration.EnableCasClient;

@EnableConfigurationProperties({TemplateConf.class,esConf.class})
@SpringBootApplication
//单点登录，打开下面两个注解。Todo：通过配置项启动单点
//@ServletComponentScan
//@EnableCasClient(validationType = EnableCasClient.ValidationType.CAS)
public class WebAppConfig extends WebMvcConfigurerAdapter{
		
    public static void main(String[] args) {
		SpringApplication.run(WebAppConfig.class, args);
	} 
    
    /**
     * 配置拦截器
     * @author fengtao
     * @param registry
     */
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(new UserSecurityInterceptor()).addPathPatterns("/**").excludePathPatterns("/error","/login","/signin");
	}
    
}