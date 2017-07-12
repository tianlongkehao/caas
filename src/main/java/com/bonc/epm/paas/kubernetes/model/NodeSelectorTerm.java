package com.bonc.epm.paas.kubernetes.model;

import java.util.List;

/**
 * @author daien
 * @date 2017年7月12日
 */
public class NodeSelectorTerm {

	private List<NodeSelectorRequirement> matchExpressions;

	public List<NodeSelectorRequirement> getMatchExpressions() {
		return matchExpressions;
	}

	public void setMatchExpressions(List<NodeSelectorRequirement> matchExpressions) {
		this.matchExpressions = matchExpressions;
	}

}
