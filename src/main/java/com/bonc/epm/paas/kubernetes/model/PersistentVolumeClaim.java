/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:PersistentVolumeClaim.java
 * Package Name:com.bonc.epm.paas.kubernetes.model
 * Date:2017年6月22日上午11:26:21
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
*/
package com.bonc.epm.paas.kubernetes.model;

/**
 * ClassName:PersistentVolumeClaim <br/>
 * Date: 2017年6月22日 上午11:26:21 <br/>
 *
 * @author longkaixiang
 * @version
 * @see
 */
public class PersistentVolumeClaim {
	private String apiVersion;
	private Kind kind;
	private ObjectMeta metadata;
	private PersistentVolumeClaimSpec spec;
//	private PersistentVolumeClaimStatus status;

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public Kind getKind() {
		return kind;
	}

	public void setKind(Kind kind) {
		this.kind = kind;
	}

	public ObjectMeta getMetadata() {
		return metadata;
	}

	public void setMetadata(ObjectMeta metadata) {
		this.metadata = metadata;
	}

	public PersistentVolumeClaimSpec getSpec() {
		return spec;
	}

	public void setSpec(PersistentVolumeClaimSpec spec) {
		this.spec = spec;
	}

//	public PersistentVolumeClaimStatus getStatus() {
//		return status;
//	}
//
//	public void setStatus(PersistentVolumeClaimStatus status) {
//		this.status = status;
//	}

}
