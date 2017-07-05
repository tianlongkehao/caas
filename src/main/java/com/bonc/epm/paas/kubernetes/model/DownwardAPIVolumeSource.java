/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:DownwardAPIVolumeSource.java
 * Package Name:com.bonc.epm.paas.kubernetes.model
 * Date:2017年6月23日上午9:41:42
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
*/

package com.bonc.epm.paas.kubernetes.model;

import java.util.List;

/**
 * ClassName:DownwardAPIVolumeSource <br/>
 * Date:     2017年6月23日 上午9:41:42 <br/>
 * @author   longkaixiang
 * @version
 * @see
 */
public class DownwardAPIVolumeSource {
	private Integer defaultMode;
	private List<DownwardAPIVolumeFile> items;
	public Integer getDefaultMode() {
		return defaultMode;
	}
	public void setDefaultMode(Integer defaultMode) {
		this.defaultMode = defaultMode;
	}
	public List<DownwardAPIVolumeFile> getItems() {
		return items;
	}
	public void setItems(List<DownwardAPIVolumeFile> items) {
		this.items = items;
	}

}
