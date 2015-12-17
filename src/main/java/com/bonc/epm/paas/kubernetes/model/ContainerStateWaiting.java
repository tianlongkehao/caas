package com.bonc.epm.paas.kubernetes.model;

public class ContainerStateWaiting {
	private String reason;
	private String message;
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
