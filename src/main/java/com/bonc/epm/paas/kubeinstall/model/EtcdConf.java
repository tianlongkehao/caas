/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:EtcdConf.java
 * Package Name:com.bonc.epm.paas.kubeinstall.model
 * Date:2017年7月4日下午4:59:48
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
 */

package com.bonc.epm.paas.kubeinstall.model;

import java.util.List;
import java.util.Map;

/**
 * ClassName: EtcdConf <br/>
 * date: 2017年7月4日 下午4:59:48 <br/>
 *
 * @author longkaixiang
 * @version
 */
public class EtcdConf {
	private List<String> hosts;
	private Map<String, LoginInfo> sshHosts;

	public List<String> getHosts() {
		return hosts;
	}

	public void setHosts(List<String> hosts) {
		this.hosts = hosts;
	}

	public Map<String, LoginInfo> getSshHosts() {
		return sshHosts;
	}

	public void setSshHosts(Map<String, LoginInfo> sshHosts) {
		this.sshHosts = sshHosts;
	}
}
