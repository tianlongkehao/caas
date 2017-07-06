/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:KeyToPath.java
 * Package Name:com.bonc.epm.paas.kubernetes.model
 * Date:2017年6月23日上午9:25:53
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
*/

package com.bonc.epm.paas.kubernetes.model;

/**
 * ClassName:KeyToPath <br/>
 * Date: 2017年6月23日 上午9:25:53 <br/>
 *
 * @author longkaixiang
 * @version
 * @see
 */
public class KeyToPath {
	private String key;
	private Integer mode;
	private String path;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
