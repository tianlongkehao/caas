/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:DockerConf.java
 * Package Name:com.bonc.epm.paas.kubeinstall.model
 * Date:2017年7月4日下午5:07:12
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
 */

package com.bonc.epm.paas.kubeinstall.model;

/**
 * ClassName:DockerConf <br/>
 * Date: 2017年7月4日 下午5:07:12 <br/>
 *
 * @author longkaixiang
 * @version
 * @see
 */
public class DockerConf extends KubeinstallAbstractModel {
	private String registryIP;
	private String registryPort;
	private String userDockerRegistryURL;

	public String getRegistryIP() {
		return registryIP;
	}

	public void setRegistryIP(String registryIP) {
		this.registryIP = registryIP;
	}

	public String getRegistryPort() {
		return registryPort;
	}

	public void setRegistryPort(String registryPort) {
		this.registryPort = registryPort;
	}

	public String getUserDockerRegistryURL() {
		return userDockerRegistryURL;
	}

	public void setUserDockerRegistryURL(String userDockerRegistryURL) {
		this.userDockerRegistryURL = userDockerRegistryURL;
	}
}
