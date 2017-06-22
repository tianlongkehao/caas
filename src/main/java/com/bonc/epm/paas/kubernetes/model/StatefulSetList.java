package com.bonc.epm.paas.kubernetes.model;

public class StatefulSetList extends AbstractKubernetesModelList<StatefulSet> {

	private ListMeta metadata;

	public StatefulSetList() {
		super(Kind.STATEFULSETLIST);
	}

	public ListMeta getMetadata() {
		return metadata;
	}

	public void setMetadata(ListMeta metadata) {
		this.metadata = metadata;
	}

}
