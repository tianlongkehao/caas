package com.bonc.epm.paas.kubernetes.model;

public class ObjectFieldSelector {
	private String apiVersion;
	private String fieldPath;
	public String getApiVersion() {
		return apiVersion;
	}
	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}
	public String getFieldPath() {
		return fieldPath;
	}
	public void setFieldPath(String fieldPath) {
		this.fieldPath = fieldPath;
	}
	
}
