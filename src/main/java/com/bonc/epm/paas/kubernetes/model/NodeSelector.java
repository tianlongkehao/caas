package com.bonc.epm.paas.kubernetes.model;

import java.util.List;

/**
 * @author daien
 * @date 2017年7月12日
 */
public class NodeSelector {

	private List<NodeSelectorTerm> nodeSelectorTerms;

	public List<NodeSelectorTerm> getNodeSelectorTerms() {
		return nodeSelectorTerms;
	}

	public void setNodeSelectorTerms(List<NodeSelectorTerm> nodeSelectorTerms) {
		this.nodeSelectorTerms = nodeSelectorTerms;
	}

}
