package com.bonc.epm.paas.kubernetes.model;

/**
 * @author daien
 * @date 2017年7月13日
 */
public class PodDisruptionBudgetList extends AbstractKubernetesModelList<PodDisruptionBudget> {

	private ListMeta metadata;

	public PodDisruptionBudgetList() {
		super(Kind.PODDISRUPTIONBUDGETLIST);
	}

	public ListMeta getMetadata() {
		return metadata;
	}

	public void setMetadata(ListMeta metadata) {
		this.metadata = metadata;
	}
}
