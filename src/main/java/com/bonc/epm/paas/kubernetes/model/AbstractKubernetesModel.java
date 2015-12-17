package com.bonc.epm.paas.kubernetes.model;

import com.bonc.epm.paas.kubernetes.api.KubernetesAPIClientInterface;

public class AbstractKubernetesModel {

    private Kind kind;
    private String apiVersion = KubernetesAPIClientInterface.VERSION;
    
    protected AbstractKubernetesModel(Kind kind) {
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
