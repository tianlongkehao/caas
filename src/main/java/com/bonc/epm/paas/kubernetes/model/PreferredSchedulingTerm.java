package com.bonc.epm.paas.kubernetes.model;

/**
 * @author daien
 * @date 2017年7月12日
 */
public class PreferredSchedulingTerm {

	private NodeSelectorTerm preference;
	private int weight;

	public NodeSelectorTerm getPreference() {
		return preference;
	}

	public void setPreference(NodeSelectorTerm preference) {
		this.preference = preference;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

}
