package com.bonc.epm.paas.kubernetes.model;

public class ReplicationControllerStatus {
	private Integer replicas;
	private Integer fullyLabeledReplicas;
	private Integer readyReplicas;
	private Integer observedGeneration;
	
	public Integer getFullyLabeledReplicas() {
        return fullyLabeledReplicas;
    }
    public void setFullyLabeledReplicas(Integer fullyLabeledReplicas) {
        this.fullyLabeledReplicas = fullyLabeledReplicas;
    }
    public Integer getReadyReplicas() {
        return readyReplicas;
    }
    public void setReadyReplicas(Integer readyReplicas) {
        this.readyReplicas = readyReplicas;
    }
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
