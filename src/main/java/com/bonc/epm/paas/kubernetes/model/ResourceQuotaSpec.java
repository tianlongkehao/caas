package com.bonc.epm.paas.kubernetes.model;

import java.util.Map;

public class ResourceQuotaSpec {
	
	private Map<String,String> hard;

	public Map<String, String> getHard() {
		return hard;
	}

	public void setHard(Map<String, String> hard) {
		this.hard = hard;
	}



	
}
