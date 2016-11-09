package com.bonc.epm.paas.constant;

/**
 * 引入外部服务常量类
 * @author all
 * @version 2016年9月18日
 * @see RefServiceConstant
 * @since
 */
public class RefServiceConstant {
	/**
	 * 外部服务可见域 0 本租户
	 */
    public static final Integer SELF_TENANT = 0;
    
    /**
     * 外部服务可见域 1 所有租户
     */
    public static final Integer ALL_TENANT= 1;
    
    /**
     * 服务引入方式--service方式
     */
    public static final Integer SERVICE_MODE = 0;
    
    /**
     * 服务引入方式--etcd方式
     */
    public static final Integer ETCD_MODE = 1;
}