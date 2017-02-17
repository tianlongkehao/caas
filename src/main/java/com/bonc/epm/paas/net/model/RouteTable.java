package com.bonc.epm.paas.net.model;

import java.util.List;

public class RouteTable {
	private String node;
	private boolean problem;
	private List<Route> items;

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public boolean isProblem() {
		return problem;
	}

	public void setProblem(boolean problem) {
		this.problem = problem;
	}

	public List<Route> getItems() {
		return items;
	}

	public void setItems(List<Route> items) {
		this.items = items;
	}
}
