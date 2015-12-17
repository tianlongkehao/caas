package com.bonc.epm.paas.kubernetes.model;

public class ContainerState {
	private ContainerStateWaiting waiting;
	private ContainerStateRunning running;
	private ContainerStateTerminated terminated;
	
	public ContainerStateWaiting getWaiting() {
		return waiting;
	}
	public void setWaiting(ContainerStateWaiting waiting) {
		this.waiting = waiting;
	}
	public ContainerStateRunning getRunning() {
		return running;
	}
	public void setRunning(ContainerStateRunning running) {
		this.running = running;
	}
	public ContainerStateTerminated getTerminated() {
		return terminated;
	}
	public void setTerminated(ContainerStateTerminated terminated) {
		this.terminated = terminated;
	}
	
}
