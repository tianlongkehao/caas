package com.bonc.epm.paas.net.model;

import java.util.List;

public class KubeSep {
	boolean flag;
	List<String> same;
	List<String> diffLeft;
	List<String> diffRight;
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public List<String> getSame() {
		return same;
	}
	public void setSame(List<String> same) {
		this.same = same;
	}
	public List<String> getDiffLeft() {
		return diffLeft;
	}
	public void setDiffLeft(List<String> diffLeft) {
		this.diffLeft = diffLeft;
	}
	public List<String> getDiffRight() {
		return diffRight;
	}
	public void setDiffRight(List<String> diffRight) {
		this.diffRight = diffRight;
	}
}
