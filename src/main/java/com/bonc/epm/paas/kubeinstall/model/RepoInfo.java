/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:RepoInfo.java
 * Package Name:com.bonc.epm.paas.kubeinstall.model
 * Date:2017年7月5日上午10:46:02
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
 */

package com.bonc.epm.paas.kubeinstall.model;

/**
 * ClassName: RepoInfo <br/>
 * date: 2017年7月5日 上午10:46:02 <br/>
 *
 * @author longkaixiang
 * @version
 */
public class RepoInfo extends KubeinstallAbstractModel {
	private String name;
	private String baseurl;
	private Boolean enabled;
	private Boolean gpgcheck;
	private String gpgkey;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBaseurl() {
		return baseurl;
	}

	public void setBaseurl(String baseurl) {
		this.baseurl = baseurl;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getGpgcheck() {
		return gpgcheck;
	}

	public void setGpgcheck(Boolean gpgcheck) {
		this.gpgcheck = gpgcheck;
	}

	public String getGpgkey() {
		return gpgkey;
	}

	public void setGpgkey(String gpgkey) {
		this.gpgkey = gpgkey;
	}
}
