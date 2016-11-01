package com.bonc.epm.paas.kubernetes.model;

public class Probe {
	private ExecAction exec;
	private HTTPGetAction httpGet;
	private TCPSocketAction tcpSocket;
	private Integer initialDelaySeconds;
	private Integer timeoutSeconds;
	private Integer periodSeconds;
	public ExecAction getExec() {
		return exec;
	}
	public void setExec(ExecAction exec) {
		this.exec = exec;
	}
	public HTTPGetAction getHttpGet() {
		return httpGet;
	}
	public void setHttpGet(HTTPGetAction httpGet) {
		this.httpGet = httpGet;
	}
	public TCPSocketAction getTcpSocket() {
		return tcpSocket;
	}
	public void setTcpSocket(TCPSocketAction tcpSocket) {
		this.tcpSocket = tcpSocket;
	}
	public Integer getInitialDelaySeconds() {
		return initialDelaySeconds;
	}
	public void setInitialDelaySeconds(Integer initialDelaySeconds) {
		this.initialDelaySeconds = initialDelaySeconds;
	}
	public Integer getTimeoutSeconds() {
		return timeoutSeconds;
	}
	public void setTimeoutSeconds(Integer timeoutSeconds) {
		this.timeoutSeconds = timeoutSeconds;
	}
    public Integer getPeriodSeconds() {
        return periodSeconds;
    }
    public void setPeriodSeconds(Integer periodSeconds) {
        this.periodSeconds = periodSeconds;
    }
	
}
