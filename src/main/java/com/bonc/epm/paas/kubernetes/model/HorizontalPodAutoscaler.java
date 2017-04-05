package com.bonc.epm.paas.kubernetes.model;

import java.util.HashMap;
import java.util.Map;

public class HorizontalPodAutoscaler {

	private String apiVersion = "extensions/v1beta1";
	private String kind = "HorizontalPodAutoscaler";
	private ObjectMeta metadata;
	private HorizontalPodAutoscalerSpec spec;
	private HorizontalPodAutoscalerStatus status;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

	public ObjectMeta getMetadata() {
		return metadata;
	}

	public void setMetadata(ObjectMeta metadata) {
		this.metadata = metadata;
	}

	public HorizontalPodAutoscalerSpec getSpec() {
		return spec;
	}

	public void setSpec(HorizontalPodAutoscalerSpec spec) {
		this.spec = spec;
	}

	public HorizontalPodAutoscalerStatus getStatus() {
		return status;
	}

	public void setStatus(HorizontalPodAutoscalerStatus status) {
		this.status = status;
	}

	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}

	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}

}
