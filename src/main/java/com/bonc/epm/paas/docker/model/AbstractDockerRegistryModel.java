package com.bonc.epm.paas.docker.model;

import com.bonc.epm.paas.docker.api.DockerRegistryAPIClientInterface;

public class AbstractDockerRegistryModel {

    private String apiVersion = DockerRegistryAPIClientInterface.VERSION;
    
	public String getApiVersion() {
		return apiVersion;
	}
	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

}
