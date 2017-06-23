package com.bonc.epm.paas.kubernetes.model;

import java.util.List;

public class ConfigMapVolumeSource {

	private Integer defaultMode;
	private List<KeyToPath> items;
	private String name;
	private Boolean itoptional;

	public Integer getDefaultMode() {
		return defaultMode;
	}

	public void setDefaultMode(Integer defaultMode) {
		this.defaultMode = defaultMode;
	}

	public List<KeyToPath> getItems() {
		return items;
	}

	public void setItems(List<KeyToPath> items) {
		this.items = items;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getItoptional() {
		return itoptional;
	}

	public void setItoptional(Boolean itoptional) {
		this.itoptional = itoptional;
	}

}
