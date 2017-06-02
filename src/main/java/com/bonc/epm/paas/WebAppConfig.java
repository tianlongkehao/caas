package com.bonc.epm.paas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.bonc.epm.paas.constant.esConf;
import com.bonc.epm.paas.sso.casclient.CasClientConfigurationProperties;
import com.bonc.epm.paas.sso.casclient.EnableCasClient;

/**
 * 系统入口
 *
 * @author ke_wang
 * @version 2016年9月2日
 * @see WebAppConfig
 * @since
 */
@EnableConfigurationProperties({ esConf.class, CasClientConfigurationProperties.class })
@SpringBootApplication
@EnableCasClient
public class WebAppConfig extends WebMvcConfigurerAdapter {
	/**
	 *
	 * Description: main方法
	 *
	 * @param args
	 * @see
	 */
	public static void main(String[] args) {
		/*SpringApplication springApplication = new SpringApplication(WebAppConfig.class);
		springApplication.addListeners(new SnapListener());
		springApplication.run(args);*/
		SpringApplication.run(WebAppConfig.class, args);
	}

	/**
	 * 配置拦截器
	 *
	 * @param registry
	 *            InterceptorRegistry
	 */
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new UserSecurityInterceptor()).addPathPatterns("/**").excludePathPatterns("/error",
				"/login", "/signin", "/authCode", "/api/**", "/swagger-resources/**", "/v2/**");
	}

}
