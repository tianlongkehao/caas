/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:PersistentVolumeClaimVolumeSource.java
 * Package Name:com.bonc.epm.paas.kubernetes.model
 * Date:2017年6月22日下午5:41:45
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
*/

package com.bonc.epm.paas.kubernetes.model;
/**
 * ClassName:PersistentVolumeClaimVolumeSource <br/>
 * Date:     2017年6月22日 下午5:41:45 <br/>
 * @author   longkaixiang
 * @version
 * @see
 */
public class PersistentVolumeClaimVolumeSource {
	private String claimName;

	private Boolean readOnly;

	public String getClaimName() {
		return claimName;
	}

	public void setClaimName(String claimName) {
		this.claimName = claimName;
	}

	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}


}
