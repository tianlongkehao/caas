package com.bonc.epm.paas.kubernetes.model;

public class Volume {

	private String name;

	private CephFSVolumeSource cephfs;

	private ConfigMapTemplate configMap;

	private CephRbd rbd;

	public CephRbd getRbd() {
		return rbd;
	}

	public void setRbd(CephRbd rbd) {
		this.rbd = rbd;
	}

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
