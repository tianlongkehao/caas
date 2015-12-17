package com.bonc.epm.paas.kubernetes.model;

import java.util.Map;

public class ReplicationControllerSpec {
	private Integer replicas;
	private Map<String,String> selector;
	private PodTemplateSpec template;
	public Integer getReplicas() {
		return replicas;
	}
	public void setReplicas(Integer replicas) {
		this.replicas = replicas;
	}
	public Map<String, String> getSelector() {
		return selector;
	}
	public void setSelector(Map<String, String> selector) {
		this.selector = selector;
	}
	public PodTemplateSpec getTemplate() {
		return template;
	}
	public void setTemplate(PodTemplateSpec template) {
		this.template = template;
	}
	
	
}
