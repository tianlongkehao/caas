package com.bonc.epm.paas.kubernetes.model;

import java.util.List;

public class LimitRangeSpec {
	private List<LimitRangeItem> limits;
	
	public List<LimitRangeItem> getLimits() {
		return limits;
	}

	public void setLimits(List<LimitRangeItem> limits) {
		this.limits = limits;
	}
	
	
}
