/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:Iptable.java
 * Package Name:com.bonc.epm.paas.net.model
 * Date:2017年5月10日下午2:42:10
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
*/

package com.bonc.epm.paas.net.model;

import java.util.Map;

/**
 * ClassName: Iptable <br/>
 * Function: 用于解析/flash/jobs/check/iptables的返回数组. <br/>
 * date: 2017年5月10日 下午2:42:10 <br/>
 *
 * @author longkaixiang
 * @version
 */
public class Iptable {
	private String nodeName;
	private Filter filter;
	private Filter nat;
	private Mangle mangle;

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public Filter getNat() {
		return nat;
	}

	public void setNat(Filter nat) {
		this.nat = nat;
	}

	public Mangle getMangle() {
		return mangle;
	}

	public void setMangle(Mangle mangle) {
		this.mangle = mangle;
	}

	public Map<String, ServiceProblems> getProblems() {
		return problems;
	}

	public void setProblems(Map<String, ServiceProblems> problems) {
		this.problems = problems;
	}

	private Map<String, ServiceProblems> problems;

}
