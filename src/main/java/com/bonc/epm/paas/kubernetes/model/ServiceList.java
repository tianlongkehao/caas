package com.bonc.epm.paas.kubernetes.model;

public class ServiceList extends AbstractKubernetesModelList<Service>{
	
	private ListMeta metadata;
	
	public ServiceList() {
		super(Kind.SERVICELIST);
	}

	public ListMeta getMetadata() {
		return metadata;
	}

	public void setMetadata(ListMeta metadata) {
		this.metadata = metadata;
	}
	
}
