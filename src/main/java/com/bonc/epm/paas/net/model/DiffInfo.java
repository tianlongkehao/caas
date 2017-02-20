package com.bonc.epm.paas.net.model;

public class DiffInfo {
	String left;
	String right;
	boolean flag;
	Nat nat;
	Filter filter;
	public String getLeft() {
		return left;
	}
	public void setLeft(String left) {
		this.left = left;
	}
	public String getRight() {
		return right;
	}
	public void setRight(String right) {
		this.right = right;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
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
