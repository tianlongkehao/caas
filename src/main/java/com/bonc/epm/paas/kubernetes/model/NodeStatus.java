package com.bonc.epm.paas.kubernetes.model;

import java.util.List;
import java.util.Map;

public class NodeStatus {

	private Map<String,String> capacity;
	private Map<String,String> allocatable;
	private List<NodeCondition> conditions;
	private List<NodeAddress> addresses;
	private Map<String,String> nodeInfo;

	public Map<String, String> getAllocatable() {
		return allocatable;
	}
	public void setAllocatable(Map<String, String> allocatable) {
		this.allocatable = allocatable;
	}
	public Map<String, String> getCapacity() {
		return capacity;
	}
	public void setCapacity(Map<String, String> capacity) {
		this.capacity = capacity;
	}
	public List<NodeCondition> getConditions() {
		return conditions;
	}
	public void setConditions(List<NodeCondition> conditions) {
		this.conditions = conditions;
	}
	public List<NodeAddress> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<NodeAddress> addresses) {
		this.addresses = addresses;
	}
	public Map<String, String> getNodeInfo() {
		return nodeInfo;
	}
	public void setNodeInfo(Map<String, String> nodeInfo) {
		this.nodeInfo = nodeInfo;
	}
}
