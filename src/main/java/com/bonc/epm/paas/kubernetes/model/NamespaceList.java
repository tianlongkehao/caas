package com.bonc.epm.paas.kubernetes.model;

public class NamespaceList extends AbstractKubernetesModelList<Namespace>{

	private ListMeta metadata;

	public NamespaceList() {
		super(Kind.NAMESPACELIST);
	}

	public ListMeta getMetadata() {
		return metadata;
	}

	public void setMetadata(ListMeta metadata) {
		this.metadata = metadata;
	}

}
