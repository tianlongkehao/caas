package com.bonc.epm.paas.kubernetes.model;

public class ResourceQuotaList extends AbstractKubernetesModelList<ResourceQuota>{
	
	private ListMeta metadata;
	
	public ResourceQuotaList() {
		super(Kind.RESOURCEQUOTALIST);
	}

	public ListMeta getMetadata() {
		return metadata;
	}

	public void setMetadata(ListMeta metadata) {
		this.metadata = metadata;
	}
	
}
