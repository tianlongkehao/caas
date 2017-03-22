package com.bonc.epm.paas.kubernetes.model;

import java.util.Map;

public class ConfigMap extends AbstractKubernetesModel {

	private ObjectMeta metadata;
    private Map<String,String> data;

	public ConfigMap() {
		super(Kind.CONFIGMAP);
	}

	public ObjectMeta getMetadata() {
		return metadata;
	}

	public void setMetadata(ObjectMeta metadata) {
		this.metadata = metadata;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

}
