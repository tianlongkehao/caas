package com.bonc.epm.paas.constant;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.Indenter;

public class CommConstant {
	
	public static final Integer TYPE_YES_VALUE = 1;
	public static final Integer TYPE_NO_VALUE = 0;
	
	
	/*Common Operation*/
	// catalog Type
	public static final Integer DOCKFILE_TEMPLATE=10;                //dockerfile模板
	public static final Integer ENV_TEMPLATE=20;                      //环境变量模板
	public static final Integer CODE_CI=30;                           //代码构建
	public static final Integer QUICK_CI=40;                          //快速构建 
	public static final Integer DOCKER_FILE_CI=50;                   //dockerfile构建
	public static final Integer UPLOAD_IMAGE=60;                     //上传镜像
    public static final Integer IMAGE=70;                             //镜像中心
	public static final Integer USER_MANAGER=80;                     //用户管理
    public static final Integer USER_SCRETKEY=90;                    //秘钥管理
    public static final Integer TENANT_MANAGER=100;                  //租户管理
    public static final Integer SHERA_MANAGER=110;                   //shera管理 
    public static final Integer STORAGE=120;                          //存储与备份
    public static final Integer REF_SERVICE=130;                     //外部服务    	
	
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
