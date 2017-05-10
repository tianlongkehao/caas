package com.bonc.epm.paas.net.model;

public class Route {
	private String targetIP;
	private String expectedGW;
	private String realGW;
	private String success;

	public String getTargetIP() {
		return targetIP;
	}

	public void setTargetIP(String targetIP) {
		this.targetIP = targetIP;
	}

	public String getExpectedGW() {
		return expectedGW;
	}

	public void setExpectedGW(String expectedGW) {
		this.expectedGW = expectedGW;
	}

	public String getRealGW() {
		return realGW;
	}

	public void setRealGW(String realGW) {
		this.realGW = realGW;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}
}
