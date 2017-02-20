package com.bonc.epm.paas.kubernetes.model;

import com.bonc.epm.paas.net.model.Filter;
import com.bonc.epm.paas.net.model.Nat;

public class Item {
	String hostname;
	Nat nat;
	Filter filter;
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public Nat getNat() {
		return nat;
	}
	public void setNat(Nat nat) {
		this.nat = nat;
	}
	public Filter getFilter() {
		return filter;
	}
	public void setFilter(Filter filter) {
		this.filter = filter;
	}
}
