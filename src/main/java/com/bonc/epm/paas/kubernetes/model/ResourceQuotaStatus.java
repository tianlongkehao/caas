package com.bonc.epm.paas.kubernetes.model;

import java.util.Map;

public class ResourceQuotaStatus {
	
	private Map<String, String> hard;
	private Map<String, String> used;
	
	public Map<String, String> getHard() {
		return hard;
	}
	public void setHard(Map<String, String> hard) {
		this.hard = hard;
	}
	public Map<String, String> getUsed() {
		return used;
	}
	public void setUsed(Map<String, String> used) {
		this.used = used;
	}
	
}
