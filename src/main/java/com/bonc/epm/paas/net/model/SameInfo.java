package com.bonc.epm.paas.net.model;

import java.util.List;

import com.bonc.epm.paas.kubernetes.model.Item;

public class SameInfo {
	List<String> hosts;
	Item item;
	public List<String> getHosts() {
		return hosts;
	}
	public void setHosts(List<String> hosts) {
		this.hosts = hosts;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
}
