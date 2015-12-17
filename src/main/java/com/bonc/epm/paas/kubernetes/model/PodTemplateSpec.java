package com.bonc.epm.paas.kubernetes.model;

public class PodTemplateSpec {
	private ObjectMeta metadata;
	private PodSpec spec;
	public ObjectMeta getMetadata() {
		return metadata;
	}
	public void setMetadata(ObjectMeta metadata) {
		this.metadata = metadata;
	}
	public PodSpec getSpec() {
		return spec;
	}
	public void setSpec(PodSpec spec) {
		this.spec = spec;
	}
	
}
