/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:EventSource.java
 * Package Name:com.bonc.epm.paas.kubernetes.model
 * Date:2017年5月25日下午4:52:00
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
*/

package com.bonc.epm.paas.kubernetes.model;

/**
 * ClassName:EventSource <br/>
 * Date: 2017年5月25日 下午4:52:00 <br/>
 *
 * @author longkaixiang
 * @version
 * @see
 */
public class EventSource {
	private String component;
	private String host;

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

}
