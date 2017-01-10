package com.bonc.epm.paas.constant;

import java.util.HashMap;
import java.util.Map;

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
    /**
     * 容器状态 调试中
     */
    public static final Integer CONSTRUCTION_STATUS_DEBUG = 6;
	
    /**
     * 默认检测延迟
     */
    public static final Integer INNIALDELAY  = 600;
    /**
     * 默认检测超时
     */
    public static final Integer TIMEOUT  = 5;
    /**
     *检测频率
     */
    public static final Integer PERIOD  = 10;
    /**
     * operation_type
     */
    public static final Long OPERATION_TYPE_UPDATE        = 10L;  //更新
    public static final Long OPERATION_TYPE_CREATE        = 20L;  //创建
    public static final Long OPERATION_TYPE_START         = 30L;  //启动
    public static final Long OPERATION_TYPE_STOP          = 40L;  //停止
    public static final Long OPERATION_TYPE_DEBUG         = 50L;  //调试
    public static final Long OPERATION_TYPE_SCALING       = 60L;  //弹性伸缩
    public static final Long OPERATION_TYPE_ROLLINGUPDATE = 70L;  //版本升级
    public static final Long OPERATION_TYPE_CONFIGURE     = 80L;  //更改配置
    public static final Long OPERATION_TYPE_DELETE        = 90L;  //删除
    
    

    
    /**
     * 监控设置
     */
    public static final Integer MONITOR_NONE                 = 0;  //没有监控
    public static final Integer MONITOR_PINPOINT             = 1;  //Pinpoint监控

}
