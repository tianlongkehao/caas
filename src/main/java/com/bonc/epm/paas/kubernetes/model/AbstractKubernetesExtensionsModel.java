package com.bonc.epm.paas.kubernetes.model;

import com.bonc.epm.paas.kubernetes.apis.KubernetesAPISClientInterface;

public class AbstractKubernetesExtensionsModel {

    private Kind kind;
    private String apiVersion = KubernetesAPISClientInterface.VERSION;

    protected AbstractKubernetesExtensionsModel(Kind kind) {
        this.kind = kind;
    }

	public Kind getKind() {
		return kind;
	}
	public void setKind(Kind kind) {
		this.kind = kind;
	}
	public String getApiVersion() {
		return apiVersion;
	}
	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

}
