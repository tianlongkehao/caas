package com.bonc.epm.paas.kubernetes.model;

public class ServiceStatus {
	private LoadBalancerStatus loadBalancer;

	public LoadBalancerStatus getLoadBalancer() {
		return loadBalancer;
	}

	public void setLoadBalancer(LoadBalancerStatus loadBalancer) {
		this.loadBalancer = loadBalancer;
	}
	
}
