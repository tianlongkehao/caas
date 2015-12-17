package com.bonc.epm.paas.kubernetes.model;

public class EnvVarSource {
	private ObjectFieldSelector fieldRef;

	public ObjectFieldSelector getFieldRef() {
		return fieldRef;
	}

	public void setFieldRef(ObjectFieldSelector fieldRef) {
		this.fieldRef = fieldRef;
	}
	
}
