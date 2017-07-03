package com.bonc.epm.paas.kubernetes.model;

public class PersistentVolume extends AbstractKubernetesModel {

	private ObjectMeta metadata;
	private PersistentVolumeSpec spec;
	private PersistentVolumeStatus status;

	public PersistentVolume() {
		super(Kind.PERSISTENTVOLUME);
	}

	public ObjectMeta getMetadata() {
		return metadata;
	}

	public void setMetadata(ObjectMeta metadata) {
		this.metadata = metadata;
	}

	public PersistentVolumeSpec getSpec() {
		return spec;
	}

	public void setSpec(PersistentVolumeSpec spec) {
		this.spec = spec;
	}

	public PersistentVolumeStatus getStatus() {
		return status;
	}

	public void setStatus(PersistentVolumeStatus status) {
		this.status = status;
	}

}
