package com.bonc.epm.paas.kubernetes.model;

import java.util.List;

/**
 * @author daien
 * @date 2017年7月12日
 */
public class PodAffinityTerm {

	private LabelSelector labelSelector;
	private List<String> namespaces;
	private String topologyKey;

	public LabelSelector getLabelSelector() {
		return labelSelector;
	}

	public void setLabelSelector(LabelSelector labelSelector) {
		this.labelSelector = labelSelector;
	}

	public List<String> getNamespaces() {
		return namespaces;
	}

	public void setNamespaces(List<String> namespaces) {
		this.namespaces = namespaces;
	}

	public String getTopologyKey() {
		return topologyKey;
	}

	public void setTopologyKey(String topologyKey) {
		this.topologyKey = topologyKey;
	}

}
