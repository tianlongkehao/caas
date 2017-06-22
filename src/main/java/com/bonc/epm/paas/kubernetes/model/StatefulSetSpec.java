package com.bonc.epm.paas.kubernetes.model;

public class StatefulSetSpec {
	private Integer targetCPUUtilizationPercentage;
	private Integer maxReplicas;
	private Integer minReplicas;
	private CrossVersionObjectReference scaleTargetRef;

	public Integer getTargetCPUUtilizationPercentage() {
		return targetCPUUtilizationPercentage;
	}

	public void setTargetCPUUtilizationPercentage(Integer targetCPUUtilizationPercentage) {
		this.targetCPUUtilizationPercentage = targetCPUUtilizationPercentage;
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

	public CrossVersionObjectReference getScaleTargetRef() {
		return scaleTargetRef;
	}

	public void setScaleTargetRef(CrossVersionObjectReference scaleTargetRef) {
		this.scaleTargetRef = scaleTargetRef;
	}

}
