package com.bonc.epm.paas.kubernetes.model;

public class StorageClassList extends AbstractKubernetesModelList<StorageClass>{

	private ListMeta metadata;

	public StorageClassList() {
		super(Kind.STORAGECLASSLIST);
	}

	public ListMeta getMetadata() {
		return metadata;
	}

	public void setMetadata(ListMeta metadata) {
		this.metadata = metadata;
	}

}
