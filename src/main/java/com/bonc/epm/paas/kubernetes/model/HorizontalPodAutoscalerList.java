package com.bonc.epm.paas.kubernetes.model;

public class HorizontalPodAutoscalerList extends AbstractKubernetesModelList<HorizontalPodAutoscaler> {

	private ListMeta metadata;

	public HorizontalPodAutoscalerList() {
		super(Kind.HORIZONTALPODAUTOSCALERLIST);
	}

	public ListMeta getMetadata() {
		return metadata;
	}

	public void setMetadata(ListMeta metadata) {
		this.metadata = metadata;
	}

}
