package com.bonc.epm.paas.constant;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.Indenter;

public class CommConstant {
	
	public static final Integer TYPE_YES_VALUE = 1;
	public static final Integer TYPE_NO_VALUE = 0;
	
	
	/*Common Operation*/
	// catalog Type
	public static final Integer DOCKFILE_TEMPLATE    = 10;    //dockerfile模板
	public static final Integer ENV_TEMPLATE         = 20;    //环境变量模板
	public static final Integer CI                   = 30;    //构建													      //存储与备份
	public static final Integer REF_SERVICE          = 40;	  //外部服务
														      //镜像中心
	public static final Integer USER_MANAGER         = 60;    //用户管理
														      //密钥管理
														      //shera管理
														      //租户管理
	                                                          //存储与备份
	
	
	
	
	
	
	// operation Type
	public static final Integer OPERATION_TYPE_CREATED    = 10;    //创建
	public static final Integer OPERATION_TYPE_UPDATE     = 20;    //更新
	public static final Integer OPERATION_TYPE_DELETE     = 30;    //删除
	public static final Integer OPERATION_TYPE_FORMAT     = 40;    //格式化
	public static final Integer OPERATION_TYPE_EXTEND     = 50;    //扩容
	public static final Integer OPERATION_TYPE_DEPLOY     = 60;    //部署
	public static final Integer OPERATION_TYPE_EXPORT     = 70;    //导出
	public static final Integer OPERATION_TYPE_START_CI   = 80;    //构建镜像
	public static final Integer OPERATION_TYPE_STOP_CI    = 90;    //停止构建镜像
	public static final Integer OPERATION_TYPE_DEL_CI     = 100;   //删除一个代码构建执行 
	

	
}
