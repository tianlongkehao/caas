package com.bonc.epm.paas.net.model;

public class DiffInfo {
	private String left;
	private String right;
	private boolean flag;
	private DiffNat nat;
	private DiffFilter filter;
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
	public DiffNat getNat() {
		return nat;
	}
	public void setNat(DiffNat nat) {
		this.nat = nat;
	}
	public DiffFilter getFilter() {
		return filter;
	}
	public void setFilter(DiffFilter filter) {
		this.filter = filter;
	}
}
