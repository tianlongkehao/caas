package com.bonc.epm.paas.kubernetes.model;

import java.util.HashMap;
import java.util.Map;

public class CPUTargetUtilization {
	private Integer targetPercentage;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Integer getTargetPercentage() {
		return targetPercentage;
	}

	public void setTargetPercentage(Integer targetPercentage) {
		this.targetPercentage = targetPercentage;
	}

	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}

	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
}
