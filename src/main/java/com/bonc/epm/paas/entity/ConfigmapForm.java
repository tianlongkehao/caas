package com.bonc.epm.paas.entity;

import java.util.List;

public class ConfigmapForm {
	private String configmapId;
	private String configMapName;
    private List<KeyValue> keyValues;

	public String getConfigmapId() {
		return configmapId;
	}

	public void setConfigmapId(String configmapId) {
		this.configmapId = configmapId;
	}

	public String getConfigMapName() {
		return configMapName;
	}

	public void setConfigMapName(String configMapName) {
		this.configMapName = configMapName;
	}

	public List<KeyValue> getKeyValues() {
		return keyValues;
	}

	public void setKeyValues(List<KeyValue> keyValues) {
		this.keyValues = keyValues;
	}

}
