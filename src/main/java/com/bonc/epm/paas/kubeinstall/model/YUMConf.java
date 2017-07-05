/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:YUMConf.java
 * Package Name:com.bonc.epm.paas.kubeinstall.model
 * Date:2017年7月4日下午5:47:25
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
*/

package com.bonc.epm.paas.kubeinstall.model;

/**
 * ClassName: YUMConf <br/>
 * date: 2017年7月4日 下午5:47:25 <br/>
 *
 * @author longkaixiang
 * @version
 */
public class YUMConf {
	private String ip;
	private RepoInfo repo;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public RepoInfo getRepo() {
		return repo;
	}

	public void setRepo(RepoInfo repo) {
		this.repo = repo;
	}

}
