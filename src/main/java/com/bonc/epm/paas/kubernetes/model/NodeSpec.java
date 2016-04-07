package com.bonc.epm.paas.kubernetes.model;

public class NodeSpec {
	private String podCIDR;
	private String externalID;
	private String providerID;
	private Boolean unschedulable;
	
	public String getPodCIDR() {
		return podCIDR;
	}
	public void setPodCIDR(String podCIDR) {
		this.podCIDR = podCIDR;
	}
	public String getExternalID() {
		return externalID;
	}
	public void setExternalID(String externalID) {
		this.externalID = externalID;
	}
	public String getProviderID() {
		return providerID;
	}
	public void setProviderID(String providerID) {
		this.providerID = providerID;
	}
	public Boolean getunschedulable() {
		return unschedulable;
	}
	public void setunschedulable(Boolean unschedulable) {
		this.unschedulable = unschedulable;
	}
}
