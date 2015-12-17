package com.bonc.epm.paas.kubernetes.model;

import java.util.List;

public class LoadBalancerStatus {
	private List<LoadBalancerIngress> ingress;

	public List<LoadBalancerIngress> getIngress() {
		return ingress;
	}

	public void setIngress(List<LoadBalancerIngress> ingress) {
		this.ingress = ingress;
	}
	
}
