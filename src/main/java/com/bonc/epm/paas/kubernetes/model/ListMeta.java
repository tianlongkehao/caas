package com.bonc.epm.paas.kubernetes.model;

public class ListMeta {
	private String selfLink;
	private String resourceVersion;
	public String getSelfLink() {
		return selfLink;
	}
	public void setSelfLink(String selfLink) {
		this.selfLink = selfLink;
	}
	public String getResourceVersion() {
		return resourceVersion;
	}
	public void setResourceVersion(String resourceVersion) {
		this.resourceVersion = resourceVersion;
	}

}
