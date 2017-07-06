package com.bonc.epm.paas.kubernetes.model;

import java.util.HashMap;
import java.util.Map;

public class StatefulSetStatus {
	private Integer currentCPUUtilizationPercentage;
	private Integer currentReplicas;
	private Integer desiredReplicas;
	private String lastScaleTime;
	private Long observedGeneration;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Integer getCurrentCPUUtilizationPercentage() {
		return currentCPUUtilizationPercentage;
	}

	public void setCurrentCPUUtilizationPercentage(Integer currentCPUUtilizationPercentage) {
		this.currentCPUUtilizationPercentage = currentCPUUtilizationPercentage;
	}

	public Integer getCurrentReplicas() {
		return currentReplicas;
	}

	public void setCurrentReplicas(Integer currentReplicas) {
		this.currentReplicas = currentReplicas;
	}

	public Integer getDesiredReplicas() {
		return desiredReplicas;
	}

	public void setDesiredReplicas(Integer desiredReplicas) {
		this.desiredReplicas = desiredReplicas;
	}

	public String getLastScaleTime() {
		return lastScaleTime;
	}

	public void setLastScaleTime(String lastScaleTime) {
		this.lastScaleTime = lastScaleTime;
	}

	public Long getObservedGeneration() {
		return observedGeneration;
	}

	public void setObservedGeneration(Long observedGeneration) {
		this.observedGeneration = observedGeneration;
	}

	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}

	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}

}
