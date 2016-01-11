package com.bonc.epm.paas.kubernetes.model;

import java.util.Map;

public class ResourceRequirements {
	private Map<String,Object> limits;
	private Map<String,Object> requests;
	public Map<String, Object> getLimits() {
		return limits;
	}
	public void setLimits(Map<String, Object> limits) {
		this.limits = limits;
	}
	public Map<String, Object> getRequests() {
		return requests;
	}
	public void setRequests(Map<String, Object> requests) {
		this.requests = requests;
	}
	
}
