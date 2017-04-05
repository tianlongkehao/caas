package com.bonc.epm.paas.kubernetes.model;

import java.util.HashMap;
import java.util.Map;

public class SubresourceReference {

	private String apiVersion;

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubresource() {
		return subresource;
	}

	public void setSubresource(String subresource) {
		this.subresource = subresource;
	}

	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}

	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}

	private String kind;
	private String name;
	private String subresource;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
