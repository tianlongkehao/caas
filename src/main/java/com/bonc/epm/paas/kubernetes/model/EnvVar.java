package com.bonc.epm.paas.kubernetes.model;

public class EnvVar {
	private String name;
	private String value;
	private EnvVarSource valueFrom;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public EnvVarSource getValueFrom() {
		return valueFrom;
	}
	public void setValueFrom(EnvVarSource valueFrom) {
		this.valueFrom = valueFrom;
	}
	
}
