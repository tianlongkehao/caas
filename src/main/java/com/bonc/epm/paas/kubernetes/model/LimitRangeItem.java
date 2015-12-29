package com.bonc.epm.paas.kubernetes.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LimitRangeItem {
	
	private String type;
	private Map<String,String> max;
	private Map<String,String> min;
	@JsonProperty("default")
	private Map<String,String> defaultVal;
	private Map<String,String> defaultRequest;
	private Map<String,String> maxLimitRequestRatio;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Map<String, String> getMax() {
		return max;
	}
	public void setMax(Map<String, String> max) {
		this.max = max;
	}
	public Map<String, String> getMin() {
		return min;
	}
	public void setMin(Map<String, String> min) {
		this.min = min;
	}
	public Map<String, String> getDefaultVal() {
		return defaultVal;
	}
	public void setDefaultVal(Map<String, String> defaultVal) {
		this.defaultVal = defaultVal;
	}
	public Map<String, String> getDefaultRequest() {
		return defaultRequest;
	}
	public void setDefaultRequest(Map<String, String> defaultRequest) {
		this.defaultRequest = defaultRequest;
	}
	public Map<String, String> getMaxLimitRequestRatio() {
		return maxLimitRequestRatio;
	}
	public void setMaxLimitRequestRatio(Map<String, String> maxLimitRequestRatio) {
		this.maxLimitRequestRatio = maxLimitRequestRatio;
	}
	
}
