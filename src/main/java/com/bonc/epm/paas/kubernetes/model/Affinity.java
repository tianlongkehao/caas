package com.bonc.epm.paas.kubernetes.model;

/**
 * @author daien
 * @date 2017年7月12日
 */
public class Affinity {

	private NodeAffinity nodeAffinity;
	private PodAffinity podAffinity;
	private PodAntiAffinity podAntiAffinity;

	public NodeAffinity getNodeAffinity() {
		return nodeAffinity;
	}

	public void setNodeAffinity(NodeAffinity nodeAffinity) {
		this.nodeAffinity = nodeAffinity;
	}

	public PodAffinity getPodAffinity() {
		return podAffinity;
	}

	public void setPodAffinity(PodAffinity podAffinity) {
		this.podAffinity = podAffinity;
	}

	public PodAntiAffinity getPodAntiAffinity() {
		return podAntiAffinity;
	}

	public void setPodAntiAffinity(PodAntiAffinity podAntiAffinity) {
		this.podAntiAffinity = podAntiAffinity;
	}

}
