/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:Filter.java
 * Package Name:com.bonc.epm.paas.net.model
 * Date:2017年5月10日下午2:44:25
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
*/

package com.bonc.epm.paas.net.model;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * ClassName: Filter <br/>
 * Function: {@link Iptable} 的属性之一. <br/>
 * date: 2017年5月10日 下午2:44:25 <br/>
 *
 * @author longkaixiang
 * @version
 */
public class Filter {
	private String table;
	@JSONField(name = "Service")
	private Map<String, ServiceInfo> Service;
	private List<String> others;

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public Map<String, ServiceInfo> getService() {
		return Service;
	}

	public void setService(Map<String, ServiceInfo> service) {
		Service = service;
	}

	public List<String> getOthers() {
		return others;
	}

	public void setOthers(List<String> others) {
		this.others = others;
	}

}
