package com.bonc.epm.paas.kubernetes.model;

/**
 * @author daien
 * @date 2017年7月12日
 */
public class PodDisruptionBudget extends AbstractKubernetesExtensionsModel{

	public PodDisruptionBudget() {
		super(Kind.STATEFULSET);
		setApiVersion("policy/v1beta1");
	}


}
