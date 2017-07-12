package com.bonc.epm.paas.kubernetes.model;

import java.util.List;

/**
 * @author daien
 * @date 2017年7月12日
 */
public class Capabilities {

	private List<String> add;

	private List<String> drop;

	public List<String> getAdd() {
		return add;
	}

	public void setAdd(List<String> add) {
		this.add = add;
	}

	public List<String> getDrop() {
		return drop;
	}

	public void setDrop(List<String> drop) {
		this.drop = drop;
	}

}
