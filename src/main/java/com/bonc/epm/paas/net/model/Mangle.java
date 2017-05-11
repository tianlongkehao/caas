/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:Mangle.java
 * Package Name:com.bonc.epm.paas.net.model
 * Date:2017年5月10日下午3:54:47
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
*/

package com.bonc.epm.paas.net.model;

import java.util.List;

/**
 * ClassName:Mangle <br/>
 * Function: {@link Iptable} 的属性之一. <br/>
 * Date: 2017年5月10日 下午3:54:47 <br/>
 *
 * @author longkaixiang
 * @version
 * @see
 */
public class Mangle {
	private String table;
	private List<String> cmd;
	private List<String> end;

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public List<String> getCmd() {
		return cmd;
	}

	public void setCmd(List<String> cmd) {
		this.cmd = cmd;
	}

	public List<String> getEnd() {
		return end;
	}

	public void setEnd(List<String> end) {
		this.end = end;
	}

}
