package com.bonc.epm.paas.kubernetes.model;

public class ReplicationControllerStatus {
	private Integer replicas;
	private Integer observedGeneration;
	public Integer getReplicas() {
		return replicas;
	}
	public void setReplicas(Integer replicas) {
		this.replicas = replicas;
	}
	public Integer getObservedGeneration() {
		return observedGeneration;
	}
	public void setObservedGeneration(Integer observedGeneration) {
		this.observedGeneration = observedGeneration;
	}
}
