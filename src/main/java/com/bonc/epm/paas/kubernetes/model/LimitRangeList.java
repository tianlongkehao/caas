package com.bonc.epm.paas.kubernetes.model;

public class LimitRangeList extends AbstractKubernetesModelList<LimitRange>{
	
	private ListMeta metadata;
	
	public LimitRangeList() {
		super(Kind.LIMITRANGELIST);
	}

	public ListMeta getMetadata() {
		return metadata;
	}

	public void setMetadata(ListMeta metadata) {
		this.metadata = metadata;
	}
	
}
