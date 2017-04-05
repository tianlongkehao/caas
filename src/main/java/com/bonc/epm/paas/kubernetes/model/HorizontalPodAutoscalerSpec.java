package com.bonc.epm.paas.kubernetes.model;

public class HorizontalPodAutoscalerSpec {
	private CPUTargetUtilization cpuUtilization;
	private Integer maxReplicas;
	private Integer minReplicas;
	private SubresourceReference scaleRef;

	public CPUTargetUtilization getCpuUtilization() {
		return cpuUtilization;
	}

	public void setCpuUtilization(CPUTargetUtilization cpuUtilization) {
		this.cpuUtilization = cpuUtilization;
	}

	public Integer getMaxReplicas() {
		return maxReplicas;
	}

	public void setMaxReplicas(Integer maxReplicas) {
		this.maxReplicas = maxReplicas;
	}

	public Integer getMinReplicas() {
		return minReplicas;
	}

	public void setMinReplicas(Integer minReplicas) {
		this.minReplicas = minReplicas;
	}

	public SubresourceReference getScaleRef() {
		return scaleRef;
	}

	public void setScaleRef(SubresourceReference scaleRef) {
		this.scaleRef = scaleRef;
	}

}
