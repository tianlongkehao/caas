/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:LoginInfo.java
 * Package Name:com.bonc.epm.paas.kubeinstall.model
 * Date:2017年7月4日下午4:58:03
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
*/
package com.bonc.epm.paas.kubeinstall.model;

/**
 * ClassName: LoginInfo <br/>
 * date: 2017年7月4日 下午4:58:03 <br/>
 *
 * @author longkaixiang
 * @version
 */
public class LoginInfo {
	private String userName;
	private String password;
	private String hostAddr;
	private Integer port;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHostAddr() {
		return hostAddr;
	}

	public void setHostAddr(String hostAddr) {
		this.hostAddr = hostAddr;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

}
