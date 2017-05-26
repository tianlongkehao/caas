/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:ObjectReference.java
 * Package Name:com.bonc.epm.paas.kubernetes.model
 * Date:2017年5月25日下午4:45:40
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
*/

package com.bonc.epm.paas.kubernetes.model;

import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;

/**
 * ClassName: ObjectReference <br/>
 * date: 2017年5月25日 下午4:45:40 <br/>
 *
 * @author longkaixiang
 * @version
 */
public class ObjectReference {
	private String apiVersion = KubernetesAPIClientInterface.VERSION;
	private Kind kind;
	private String fieldPath;
	private String name;
	private String namespace;
	private String resourceVersion;
	private String uid;

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

	public String getFieldPath() {
		return fieldPath;
	}

	public void setFieldPath(String fieldPath) {
		this.fieldPath = fieldPath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getResourceVersion() {
		return resourceVersion;
	}

	public void setResourceVersion(String resourceVersion) {
		this.resourceVersion = resourceVersion;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
