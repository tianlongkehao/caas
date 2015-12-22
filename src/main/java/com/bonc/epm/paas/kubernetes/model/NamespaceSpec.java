package com.bonc.epm.paas.kubernetes.model;

import java.util.List;

public class NamespaceSpec {
	private List<String> finalizers;

	public List<String> getFinalizers() {
		return finalizers;
	}

	public void setFinalizers(List<String> finalizers) {
		this.finalizers = finalizers;
	}
	
}
