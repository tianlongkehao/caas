/*
 * 文件名：SpringApplicationContext.java
 * 版权：Copyright by www.huawei.com
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年10月8日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * SpringApplicationContext
 * @author ke_wang
 * @version 2016年10月8日
 * @see SpringApplicationContext
 * @since
 */
@Component
public class SpringApplicationContext implements ApplicationContextAware {
    /**
     * 
     */
    public static ApplicationContext CONTEXT;

    @Override
    public void setApplicationContext(final ApplicationContext context) throws BeansException {
        CONTEXT = context;
    }
    
    /**
     * 
     * Description:
     * getBean
     * @param className 
     * @param <T> bean
     * @return <T> 
     * @throws ClassNotFoundException 
     * @throws BeansException 
     * @see
     */
/*    @SuppressWarnings("unchecked")
    public static <T> T getBean(String className) throws BeansException, ClassNotFoundException {
        return (T)CONTEXT.getBean(Class.forName(className));
    }*/
}
