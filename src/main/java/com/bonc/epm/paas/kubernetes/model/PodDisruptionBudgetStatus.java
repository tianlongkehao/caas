package com.bonc.epm.paas.kubernetes.model;

import java.util.Map;

public class PodDisruptionBudgetStatus {

	private Integer currentHealthy;

	private Integer desiredHealthy;

	private Map<String,String> disruptedPods;

	private Integer disruptionsAllowed;

	private Integer expectedPods;

	private Integer observedGeneration;

	public Integer getCurrentHealthy() {
		return currentHealthy;
	}

	public void setCurrentHealthy(Integer currentHealthy) {
		this.currentHealthy = currentHealthy;
	}

	public Integer getDesiredHealthy() {
		return desiredHealthy;
	}

	public void setDesiredHealthy(Integer desiredHealthy) {
		this.desiredHealthy = desiredHealthy;
	}

	public Map<String, String> getDisruptedPods() {
		return disruptedPods;
	}

	public void setDisruptedPods(Map<String, String> disruptedPods) {
		this.disruptedPods = disruptedPods;
	}

	public Integer getDisruptionsAllowed() {
		return disruptionsAllowed;
	}

	public void setDisruptionsAllowed(Integer disruptionsAllowed) {
		this.disruptionsAllowed = disruptionsAllowed;
	}

	public Integer getExpectedPods() {
		return expectedPods;
	}

	public void setExpectedPods(Integer expectedPods) {
		this.expectedPods = expectedPods;
	}

	public Integer getObservedGeneration() {
		return observedGeneration;
	}

	public void setObservedGeneration(Integer observedGeneration) {
		this.observedGeneration = observedGeneration;
	}

}
