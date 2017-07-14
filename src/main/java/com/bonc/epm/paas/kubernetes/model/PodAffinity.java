package com.bonc.epm.paas.kubernetes.model;

import java.util.List;

/**
 * @author daien
 * @date 2017年7月12日
 */
public class PodAffinity {

	private List<WeightedPodAffinityTerm> preferredDuringSchedulingIgnoredDuringExecution;
	private List<PodAffinityTerm> requiredDuringSchedulingIgnoredDuringExecution;

	public List<WeightedPodAffinityTerm> getPreferredDuringSchedulingIgnoredDuringExecution() {
		return preferredDuringSchedulingIgnoredDuringExecution;
	}

	public void setPreferredDuringSchedulingIgnoredDuringExecution(
			List<WeightedPodAffinityTerm> preferredDuringSchedulingIgnoredDuringExecution) {
		this.preferredDuringSchedulingIgnoredDuringExecution = preferredDuringSchedulingIgnoredDuringExecution;
	}

	public List<PodAffinityTerm> getRequiredDuringSchedulingIgnoredDuringExecution() {
		return requiredDuringSchedulingIgnoredDuringExecution;
	}

	public void setRequiredDuringSchedulingIgnoredDuringExecution(
			List<PodAffinityTerm> requiredDuringSchedulingIgnoredDuringExecution) {
		this.requiredDuringSchedulingIgnoredDuringExecution = requiredDuringSchedulingIgnoredDuringExecution;
	}

}
