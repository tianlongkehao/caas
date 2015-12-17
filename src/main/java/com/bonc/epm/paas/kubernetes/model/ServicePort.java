package com.bonc.epm.paas.kubernetes.model;

public class ServicePort {
	private String name;
	private String protocol;
	private Integer port;
	private String targetPort;
	private Integer nodePort;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getTargetPort() {
		return targetPort;
	}
	public void setTargetPort(String targetPort) {
		this.targetPort = targetPort;
	}
	public Integer getNodePort() {
		return nodePort;
	}
	public void setNodePort(Integer nodePort) {
		this.nodePort = nodePort;
	}
	
}
