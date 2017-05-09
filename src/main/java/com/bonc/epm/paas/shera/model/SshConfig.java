package com.bonc.epm.paas.shera.model;

public class SshConfig {
	private String user;
	private String host;
	private Boolean proxy;
	private String ip;
	private String port;
	private String policy;
	private String identifyFile;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Boolean getProxy() {
		return proxy;
	}

	public void setProxy(Boolean proxy) {
		this.proxy = proxy;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public String getIdentifyFile() {
		return identifyFile;
	}

	public void setIdentifyFile(String identifyFile) {
		this.identifyFile = identifyFile;
	}
}
