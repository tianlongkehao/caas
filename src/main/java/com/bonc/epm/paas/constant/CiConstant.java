package com.bonc.epm.paas.constant;

public class CiConstant {
	//代码类型     1代码构建 2 dockerFile构建3快速构建
	public static final Integer TYPE_CODE = 1;
	public static final Integer TYPE_DOCKERFILE = 2;
	public static final Integer TYPE_QUICK = 3;
	
	//代码来源  1git 2svn
	public static final Integer CODE_TYPE_GIT = 1;
	public static final Integer CODE_TYPE_SVN = 2;
	
	//构建状态：1未构建2构建中3完成4失败
	public static final Integer CONSTRUCTION_STATUS_WAIT = 1;
	public static final Integer CONSTRUCTION_STATUS_ING = 2;
	public static final Integer CONSTRUCTION_STATUS_OK = 3;
	public static final Integer CONSTRUCTION_STATUS_FAIL = 4;
	
	//构建结果：1成功2失败3构建中
	public static final Integer CONSTRUCTION_RESULT_OK = 1;
	public static final Integer CONSTRUCTION_RESULT_FAIL = 2;
	public static final Integer CONSTRUCTION_RESULT_ING = 3;
	
	//镜像类型 ： 1公有镜像2私有镜像
	public static final Integer IMG_TYPE_PUBLIC = 1;
	public static final Integer IMG_TYPE_PRIVATE = 2;
}
