package com.bonc.epm.paas.kubernetes.model;

/**
 * @author daien
 * @date 2017年7月12日
 */
public class WeightedPodAffinityTerm {

	private PodAffinityTerm podAffinityTerm;
	private Integer weight;

	public PodAffinityTerm getPodAffinityTerm() {
		return podAffinityTerm;
	}

	public void setPodAffinityTerm(PodAffinityTerm podAffinityTerm) {
		this.podAffinityTerm = podAffinityTerm;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

}
