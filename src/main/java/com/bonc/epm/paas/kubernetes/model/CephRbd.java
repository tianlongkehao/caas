package com.bonc.epm.paas.kubernetes.model;

import java.util.List;

public class CephRbd {
	private List<String> monitors;
	private String pool;
	private String image;
	private String user;
	private String keyring;
	private String fsType;
	private boolean readOnly;
	private LocalObjectReference secretRef;
	public List<String> getMonitors() {
		return monitors;
	}
	public void setMonitors(List<String> monitors) {
		this.monitors = monitors;
	}
	public String getPool() {
		return pool;
	}
	public void setPool(String pool) {
		this.pool = pool;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getKeyring() {
		return keyring;
	}
	public void setKeyring(String keyring) {
		this.keyring = keyring;
	}
	public String getFsType() {
		return fsType;
	}
	public void setFsType(String fsType) {
		this.fsType = fsType;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	public LocalObjectReference getSecretRef() {
		return secretRef;
	}
	public void setSecretRef(LocalObjectReference secretRef) {
		this.secretRef = secretRef;
	}

}
