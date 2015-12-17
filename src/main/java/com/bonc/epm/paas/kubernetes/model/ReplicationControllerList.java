package com.bonc.epm.paas.kubernetes.model;

public class ReplicationControllerList extends AbstractKubernetesModelList<ReplicationController>{
	
	private ListMeta metadata;
	
	public ReplicationControllerList() {
		super(Kind.REPLICATIONCONTROLLERLIST);
	}

	public ListMeta getMetadata() {
		return metadata;
	}

	public void setMetadata(ListMeta metadata) {
		this.metadata = metadata;
	}
	
}
