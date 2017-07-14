package com.bonc.epm.paas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 *
 * ServletInitializer〉
 * @author ke_wang
 * @version 2016年9月2日
 * @see ServletInitializer
 * @since
 */
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    	SpringApplication springApplication = application.application();
    	springApplication.addListeners(new SnapListener());
        return application.sources(WebAppConfig.class);
    }

}
