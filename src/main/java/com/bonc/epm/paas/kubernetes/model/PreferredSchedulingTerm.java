package com.bonc.epm.paas.kubernetes.model;

/**
 * @author daien
 * @date 2017年7月12日
 */
public class PreferredSchedulingTerm {

	private NodeSelectorTerm preference;
	private Integer weight;

	public NodeSelectorTerm getPreference() {
		return preference;
	}

	public void setPreference(NodeSelectorTerm preference) {
		this.preference = preference;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

}
