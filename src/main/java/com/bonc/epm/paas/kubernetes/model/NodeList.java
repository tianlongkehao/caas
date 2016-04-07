package com.bonc.epm.paas.kubernetes.model;

public class NodeList extends AbstractKubernetesModelList<Node>{
	
	private ListMeta metadata;
	
	public NodeList() {
		super(Kind.NODELIST);
	}

	public ListMeta getMetadata() {
		return metadata;
	}

	public void setMetadata(ListMeta metadata) {
		this.metadata = metadata;
	}
	
}
