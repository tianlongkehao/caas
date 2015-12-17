package com.bonc.epm.paas.kubernetes.model;

public class PodList extends AbstractKubernetesModelList<Pod>{
	
	private ListMeta metadata;
	
	public PodList() {
		super(Kind.PODLIST);
	}

	public ListMeta getMetadata() {
		return metadata;
	}

	public void setMetadata(ListMeta metadata) {
		this.metadata = metadata;
	}
	
}
