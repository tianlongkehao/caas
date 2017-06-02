/*
 * 文件名：GitConfig.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.shera.model;

import java.util.Map;

/**
 * ClassName: ExecConfig <br/>
 * Function: shera运行工具的配置. <br/>
 * date: 2017年5月2日 下午4:30:47 <br/>
 * @author longkaixiang
 * @version
 *
 *
 * 范例： {
 *        "proid": 1,
 *        "version": "Apache Maven 3.3.9",
 *        "userid": "admin",
 *        "env": {"JAVA_HOME":"/root/data01/bcm/jdk1.8.0_65","M2_HOME":"/opt/molen/maven","PATH":"$PATH:/opt/molen/maven/bin"}
 *      }
 */
public class ExecConfig {
	/**
	 * proid:1:maven 2：ant 3：sonar.
	 */
	private Integer proid;
	private String version;
	private String userid;
	/**
	 * key:uuid 创建时不需要设置.
	 */
	private String key;
	private Map<String, String> env;

	public Integer getProid() {
		return proid;
	}

	public void setProid(Integer proid) {
		this.proid = proid;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Map<String, String> getEnv() {
		return env;
	}

	public void setEnv(Map<String, String> env) {
		this.env = env;
	}
}
