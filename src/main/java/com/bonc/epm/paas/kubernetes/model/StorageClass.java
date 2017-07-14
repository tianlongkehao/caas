package com.bonc.epm.paas.kubernetes.model;

import java.util.Map;

public class StorageClass extends AbstractKubernetesExtensionsModel{

	private ObjectMeta metadata;
	private String provisioner;
	private Map<String,String> parameters;

	public StorageClass() {
		super(Kind.STORAGECLASS);
		setApiVersion("storage.k8s.io/v1");
	}

	public ObjectMeta getMetadata() {
		return metadata;
	}
	public void setMetadata(ObjectMeta metadata) {
		this.metadata = metadata;
	}
	public String getProvisioner() {
		return provisioner;
	}
	public void setProvisioner(String provisioner) {
		this.provisioner = provisioner;
	}
	public Map<String, String> getParameters() {
		return parameters;
	}
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

}
