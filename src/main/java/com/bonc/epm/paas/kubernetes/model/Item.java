package com.bonc.epm.paas.kubernetes.model;

import com.bonc.epm.paas.net.model.SameFilter;
import com.bonc.epm.paas.net.model.SameNat;

public class Item {
	String hostname;
	SameNat nat;
	SameFilter filter;
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public SameNat getNat() {
		return nat;
	}
	public void setNat(SameNat nat) {
		this.nat = nat;
	}
	public SameFilter getFilter() {
		return filter;
	}
	public void setFilter(SameFilter filter) {
		this.filter = filter;
	}
}
