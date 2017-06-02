package com.bonc.epm.paas.kubernetes.model;

public class ConfigMapList extends AbstractKubernetesModelList<ConfigMap> {

private ListMeta metadata;

	public ConfigMapList() {
		super(Kind.CONFIGMAPLIST);
	}

	public ListMeta getMetadata() {
		return metadata;
	}

	public void setMetadata(ListMeta metadata) {
		this.metadata = metadata;
	}

}
