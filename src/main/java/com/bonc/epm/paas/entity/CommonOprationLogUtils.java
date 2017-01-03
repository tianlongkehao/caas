package com.bonc.epm.paas.entity;

import java.util.Date;

import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.FileUtils;

/**
 * 
 *	公共操作日志记录工具类
 * @author zhangyunzhen
 * @version 2016年12月28日
 * @see CommonOprationLogUtils
 * @since
 */
public class CommonOprationLogUtils {
	
	
	/**
	 * 
	 * Description: <br>
     *    获取用户操作日志对象
	 * @param commonName    记录信息
	 * @param extraInfo     额外信息
	 * @param catalogType   类型
	 * @param oprationType  操作类型
	 */
	public static  CommonOperationLog getOprationLog(String commonName,String extraInfo,Integer catalogType,Integer oprationType){
		CommonOperationLog commOpLog = new CommonOperationLog();
		commOpLog.setCommonName(commonName);
		commOpLog.setExtraInfo(extraInfo);
		commOpLog.setCatalogType(catalogType);
		commOpLog.setOperationType(oprationType);
		User cUser = CurrentUserUtils.getInstance().getUser();
        commOpLog.setCreateDate(new Date());
        commOpLog.setCreateBy(cUser.getId());
        commOpLog.setCreateUsername(cUser.getUserName());
        return commOpLog;
	}
	
	

}
