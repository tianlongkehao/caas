package com.bonc.epm.paas.kubernetes.model;

import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;

public class CrossVersionObjectReference {
	private String apiVersion = KubernetesAPIClientInterface.VERSION;
	private Kind kind;
	private String name;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
