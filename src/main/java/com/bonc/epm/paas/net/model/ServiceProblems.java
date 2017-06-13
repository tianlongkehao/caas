/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:ServiceProblems.java
 * Package Name:com.bonc.epm.paas.net.model
 * Date:2017年5月10日下午4:01:08
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
 */

package com.bonc.epm.paas.net.model;

import java.util.List;

/**
 * ClassName: ServiceProblems <br/>
 * Function: {@link Iptable} 的属性之一. <br/>
 * date: 2017年5月10日 下午4:01:08 <br/>
 *
 * @author longkaixiang
 * @version
 */
public class ServiceProblems {
	private List<String> internalAccess;
	private List<String> externalAccess;
	private List<String> others;

	public List<String> getInternalAccess() {
		return internalAccess;
	}

	public void setInternalAccess(List<String> internalAccess) {
		this.internalAccess = internalAccess;
	}

	public List<String> getExternalAccess() {
		return externalAccess;
	}

	public void setExternalAccess(List<String> externalAccess) {
		this.externalAccess = externalAccess;
	}

	public List<String> getOthers() {
		return others;
	}

	public void setOthers(List<String> others) {
		this.others = others;
	}

}
