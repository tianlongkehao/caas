package com.bonc.epm.paas.entity;

public class Restriction {
	private String pod_cpu_default;
	private String pod_cpu_max;
	private String pod_cpu_min;
	private String pod_memory_default;
	private String pod_memory_max;
	private String pod_memory_min;
	
	private String container_cpu_default;
	private String container_cpu_max;
	private String container_cpu_min;
	private String container_memory_default;
	private String container_memory_max;
	private String container_memory_min;
	
	public String getPod_cpu_default() {
		return pod_cpu_default;
	}
	public void setPod_cpu_default(String pod_cpu_default) {
		this.pod_cpu_default = pod_cpu_default;
	}
	public String getPod_cpu_max() {
		return pod_cpu_max;
	}
	public void setPod_cpu_max(String pod_cpu_max) {
		this.pod_cpu_max = pod_cpu_max;
	}
	public String getPod_cpu_min() {
		return pod_cpu_min;
	}
	public void setPod_cpu_min(String pod_cpu_min) {
		this.pod_cpu_min = pod_cpu_min;
	}
	public String getPod_memory_default() {
		return pod_memory_default;
	}
	public void setPod_memory_default(String pod_memory_default) {
		this.pod_memory_default = pod_memory_default;
	}
	public String getPod_memory_max() {
		return pod_memory_max;
	}
	public void setPod_memory_max(String pod_memory_max) {
		this.pod_memory_max = pod_memory_max;
	}
	public String getPod_memory_min() {
		return pod_memory_min;
	}
	public void setPod_memory_min(String pod_memory_min) {
		this.pod_memory_min = pod_memory_min;
	}
	public String getContainer_cpu_default() {
		return container_cpu_default;
	}
	public void setContainer_cpu_default(String container_cpu_default) {
		this.container_cpu_default = container_cpu_default;
	}
	public String getContainer_cpu_max() {
		return container_cpu_max;
	}
	public void setContainer_cpu_max(String container_cpu_max) {
		this.container_cpu_max = container_cpu_max;
	}
	public String getContainer_cpu_min() {
		return container_cpu_min;
	}
	public void setContainer_cpu_min(String container_cpu_min) {
		this.container_cpu_min = container_cpu_min;
	}
	public String getContainer_memory_default() {
		return container_memory_default;
	}
	public void setContainer_memory_default(String container_memory_default) {
		this.container_memory_default = container_memory_default;
	}
	public String getContainer_memory_max() {
		return container_memory_max;
	}
	public void setContainer_memory_max(String container_memory_max) {
		this.container_memory_max = container_memory_max;
	}
	public String getContainer_memory_min() {
		return container_memory_min;
	}
	public void setContainer_memory_min(String container_memory_min) {
		this.container_memory_min = container_memory_min;
	}
	
}
