package com.bonc.epm.paas.net.model;

import java.util.List;

public class Diff {
	private Service serv;
	private List<DiffInfo> diff;
	private List<SameInfo> same;
	public Service getServ() {
		return serv;
	}
	public void setServ(Service serv) {
		this.serv = serv;
	}
	public List<DiffInfo> getDiff() {
		return diff;
	}
	public void setDiff(List<DiffInfo> diff) {
		this.diff = diff;
	}
	public List<SameInfo> getSame() {
		return same;
	}
	public void setSame(List<SameInfo> same) {
		this.same = same;
	}
}
