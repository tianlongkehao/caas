package com.bonc.epm.paas.kubernetes.model;

import java.util.List;
import java.util.Map;

public class ServiceSpec {
	private List<ServicePort> ports;
	private Map<String,String> selector;
	private String clusterIP;
	private String type;
	private List<String> externalIPs;
	private List<String> deprecatedPublicIPs;
	private String sessionAffinity;
	private String loadBalancerIP;
	public List<ServicePort> getPorts() {
		return ports;
	}
	public void setPorts(List<ServicePort> ports) {
		this.ports = ports;
	}
	public Map<String, String> getSelector() {
		return selector;
	}
	public void setSelector(Map<String, String> selector) {
		this.selector = selector;
	}
	public String getClusterIP() {
		return clusterIP;
	}
	public void setClusterIP(String clusterIP) {
		this.clusterIP = clusterIP;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<String> getExternalIPs() {
		return externalIPs;
	}
	public void setExternalIPs(List<String> externalIPs) {
		this.externalIPs = externalIPs;
	}
	public List<String> getDeprecatedPublicIPs() {
		return deprecatedPublicIPs;
	}
	public void setDeprecatedPublicIPs(List<String> deprecatedPublicIPs) {
		this.deprecatedPublicIPs = deprecatedPublicIPs;
	}
	public String getSessionAffinity() {
		return sessionAffinity;
	}
	public void setSessionAffinity(String sessionAffinity) {
		this.sessionAffinity = sessionAffinity;
	}
	public String getLoadBalancerIP() {
		return loadBalancerIP;
	}
	public void setLoadBalancerIP(String loadBalancerIP) {
		this.loadBalancerIP = loadBalancerIP;
	}
	
}
