package com.bonc.epm.paas.kubernetes.model;

public class Volume {

	private String name;

	private CephFSVolumeSource cephfs;

	private CephRbd rbd;

	private ConfigMapVolumeSource configMap;

	private PersistentVolumeClaimVolumeSource persistentVolumeClaim;

	private DownwardAPIVolumeSource downwardAPI;

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

	public ConfigMapVolumeSource getConfigMap() {
		return configMap;
	}

	public void setConfigMap(ConfigMapVolumeSource configMap) {
		this.configMap = configMap;
	}

	public PersistentVolumeClaimVolumeSource getPersistentVolumeClaim() {
		return persistentVolumeClaim;
	}

	public void setPersistentVolumeClaim(PersistentVolumeClaimVolumeSource persistentVolumeClaim) {
		this.persistentVolumeClaim = persistentVolumeClaim;
	}

	public DownwardAPIVolumeSource getDownwardAPI() {
		return downwardAPI;
	}

	public void setDownwardAPI(DownwardAPIVolumeSource downwardAPI) {
		this.downwardAPI = downwardAPI;
	}

}
