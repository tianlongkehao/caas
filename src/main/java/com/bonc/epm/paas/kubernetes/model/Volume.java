package com.bonc.epm.paas.kubernetes.model;

public class Volume {

	private String name;

	private CephFSVolumeSource cephfs;

	private ConfigMapTemplate configMap;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CephFSVolumeSource getCephfs() {
		return cephfs;
	}

	public void setCephfs(CephFSVolumeSource cephfs) {
		this.cephfs = cephfs;
	}

	public ConfigMapTemplate getConfigMap() {
		return configMap;
	}

	public void setConfigMap(ConfigMapTemplate configMap) {
		this.configMap = configMap;
	}

}
