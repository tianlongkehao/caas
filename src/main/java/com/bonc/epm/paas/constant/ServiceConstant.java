package com.bonc.epm.paas.constant;

/**
 * 容器状态常量类
 * @author all
 * @version 2016年9月18日
 * @see ServiceConstant
 * @since
 */
public class ServiceConstant {
	/**
	 * 容器状态 未启动
	 */
    public static final Integer CONSTRUCTION_STATUS_WAITING = 1;
    /**
     * 容器状态 启动中
     */
    public static final Integer CONSTRUCTION_STATUS_CREATING = 2;
    /**
     * 容器状态 运行中
     */
    public static final Integer CONSTRUCTION_STATUS_RUNNING = 3;
    /**
     * 容器状态 已停止
     */
    public static final Integer CONSTRUCTION_STATUS_STOPPED = 4;
    /**
     * 容器状态 启动失败
     */
    public static final Integer CONSTRUCTION_STATUS_FAILED  = 5;
	

}
