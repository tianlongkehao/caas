/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:DownwardAPIVolumeFile.java
 * Package Name:com.bonc.epm.paas.kubernetes.model
 * Date:2017年6月23日上午9:43:06
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
*/

package com.bonc.epm.paas.kubernetes.model;
/**
 * ClassName:DownwardAPIVolumeFile <br/>
 * Date:     2017年6月23日 上午9:43:06 <br/>
 * @author   longkaixiang
 * @version
 * @see
 */
public class DownwardAPIVolumeFile {
	private ObjectFieldSelector fieldRef;
	private Integer mode;
	private String path;
	private ResourceFieldSelector resourceFieldRef;
	public ObjectFieldSelector getFieldRef() {
		return fieldRef;
	}
	public void setFieldRef(ObjectFieldSelector fieldRef) {
		this.fieldRef = fieldRef;
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
	public ResourceFieldSelector getResourceFieldRef() {
		return resourceFieldRef;
	}
	public void setResourceFieldRef(ResourceFieldSelector resourceFieldRef) {
		this.resourceFieldRef = resourceFieldRef;
	}

}
