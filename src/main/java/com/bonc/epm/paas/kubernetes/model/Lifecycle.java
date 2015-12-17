package com.bonc.epm.paas.kubernetes.model;

public class Lifecycle {
	private Handler postStart;
	private Handler preStop;
	public Handler getPostStart() {
		return postStart;
	}
	public void setPostStart(Handler postStart) {
		this.postStart = postStart;
	}
	public Handler getPreStop() {
		return preStop;
	}
	public void setPreStop(Handler preStop) {
		this.preStop = preStop;
	}
	
}
