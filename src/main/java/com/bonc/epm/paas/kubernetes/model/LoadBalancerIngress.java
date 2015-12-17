package com.bonc.epm.paas.kubernetes.model;

public class LoadBalancerIngress {
	private String ip;
	private String hostname;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
}
