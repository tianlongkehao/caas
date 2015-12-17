package com.bonc.epm.paas.kubernetes.model;

public class Handler {
	private ExecAction exec;
	private HTTPGetAction httpGet;
	private TCPSocketAction tcpSocket;
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
	
}
