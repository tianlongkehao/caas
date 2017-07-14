package com.bonc.epm.paas.kubernetes.model;

/**
 * @author daien
 * @date 2017年7月13日
 */
public class PodDisruptionBudgetSpec {

	private LabelSelector selector;

	private Integer minAvailable;

	private Integer maxUnavailable;

	public LabelSelector getSelector() {
		return selector;
	}

	public void setSelector(LabelSelector selector) {
		this.selector = selector;
	}

	public Integer getMinAvailable() {
		return minAvailable;
	}

	public void setMinAvailable(Integer minAvailable) {
		this.minAvailable = minAvailable;
	}

	public Integer getMaxUnavailable() {
		return maxUnavailable;
	}

	public void setMaxUnavailable(Integer maxUnavailable) {
		this.maxUnavailable = maxUnavailable;
	}

}
