package com.bonc.epm.paas.kubernetes.apis;

import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.model.HorizontalPodAutoscaler;

public interface KubernetesAPISClientInterface {

	public static final String VERSION = "v1";

	public HorizontalPodAutoscaler getHPA(String name) throws KubernetesClientException;

}
