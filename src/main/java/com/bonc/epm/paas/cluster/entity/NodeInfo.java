package com.bonc.epm.paas.cluster.entity;

public class NodeInfo {

	private String nodename;

	private NodeTestInfo nodeTestInfo;

	private boolean deploystatus;

	private boolean teststatus;

	public String getNodename() {
		return nodename;
	}

	public void setNodename(String nodename) {
		this.nodename = nodename;
	}

	public NodeTestInfo getNodeTestInfo() {
		return nodeTestInfo;
	}

	public void setNodeTestInfo(NodeTestInfo nodeTestInfo) {
		this.nodeTestInfo = nodeTestInfo;
	}

	public boolean isDeploystatus() {
		return deploystatus;
	}

	public void setDeploystatus(boolean deploystatus) {
		this.deploystatus = deploystatus;
	}

	public boolean isTeststatus() {
		return teststatus;
	}

	public void setTeststatus(boolean teststatus) {
		this.teststatus = teststatus;
	}

}
