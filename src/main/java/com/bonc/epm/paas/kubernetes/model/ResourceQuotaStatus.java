package com.bonc.epm.paas.kubernetes.model;

public class ResourceQuotaStatus {
	
	private String hard;
	private String used;
	
	public String getHard() {
		return hard;
	}
	public void setHard(String hard) {
		this.hard = hard;
	}
	public String getUsed() {
		return used;
	}
	public void setUsed(String used) {
		this.used = used;
	}
	
	
}
