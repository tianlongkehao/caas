/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:PersistentVolumeClaimSpec.java
 * Package Name:com.bonc.epm.paas.kubernetes.model
 * Date:2017年6月22日上午11:33:09
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
*/
package com.bonc.epm.paas.kubernetes.model;

import java.util.List;

/**
 * ClassName:PersistentVolumeClaimSpec <br/>
 * Date: 2017年6月22日 上午11:33:09 <br/>
 *
 * @author longkaixiang
 * @version
 * @see
 */
public class PersistentVolumeClaimSpec {
	private List<String> accessModes;
	private ResourceRequirements resources;
	private LabelSelector selector;
	private String storageClassName;
	private String volumeName;

	public List<String> getAccessModes() {
		return accessModes;
	}

	public void setAccessModes(List<String> accessModes) {
		this.accessModes = accessModes;
	}

	public ResourceRequirements getResources() {
		return resources;
	}

	public void setResources(ResourceRequirements resources) {
		this.resources = resources;
	}

	public LabelSelector getSelector() {
		return selector;
	}

	public void setSelector(LabelSelector selector) {
		this.selector = selector;
	}

	public String getStorageClassName() {
		return storageClassName;
	}

	public void setStorageClassName(String storageClassName) {
		this.storageClassName = storageClassName;
	}

	public String getVolumeName() {
		return volumeName;
	}

	public void setVolumeName(String volumeName) {
		this.volumeName = volumeName;
	}

}
