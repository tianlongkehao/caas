package com.bonc.epm.paas.kubernetes.model;

/**
 * @author daien
 * @date 2017年7月12日
 */
public class PodDisruptionBudget extends AbstractKubernetesExtensionsModel {

	private ObjectMeta metadata;
	private PodDisruptionBudgetSpec spec;
	private PodDisruptionBudgetStatus status;

	public PodDisruptionBudget() {
		super(Kind.PODDISRUPTIONBUDGET);
		setApiVersion("policy/v1beta1");
	}

	public ObjectMeta getMetadata() {
		return metadata;
	}

	public void setMetadata(ObjectMeta metadata) {
		this.metadata = metadata;
	}

	public PodDisruptionBudgetSpec getSpec() {
		return spec;
	}

	public void setSpec(PodDisruptionBudgetSpec spec) {
		this.spec = spec;
	}

	public PodDisruptionBudgetStatus getStatus() {
		return status;
	}

	public void setStatus(PodDisruptionBudgetStatus status) {
		this.status = status;
	}

}
