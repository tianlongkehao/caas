package com.bonc.epm.paas.kubernetes.apis;

import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.exceptions.Status;
import com.bonc.epm.paas.kubernetes.model.HorizontalPodAutoscaler;
import com.bonc.epm.paas.kubernetes.model.HorizontalPodAutoscalerList;

public interface KubernetesAPISClientInterface {

	public static final String VERSION = "autoscaling/v1";

	/**
	 * create a HorizontalPodAutoscaler
	 *
	 * @param namespace
	 * @param name
	 * @return
	 */
	public HorizontalPodAutoscaler createHorizontalPodAutoscaler(HorizontalPodAutoscaler horizontalPodAutoscaler) throws KubernetesClientException;

	/**
	 * replace the specified HorizontalPodAutoscaler
	 *
	 * @param namespace
	 * @param name
	 * @return
	 */
	public HorizontalPodAutoscaler replaceHorizontalPodAutoscaler(String name, HorizontalPodAutoscaler horizontalPodAutoscaler) throws KubernetesClientException;

	/**
	 * delete a HorizontalPodAutoscaler
	 *
	 * @param namespace
	 * @param name
	 * @return
	 */
	public Status deleteHorizontalPodAutoscaler(String name) throws KubernetesClientException;

	/**
	 * delete collection of HorizontalPodAutoscaler
	 *
	 * @param namespace
	 * @param name
	 * @return
	 */
	public Status deleteHorizontalPodAutoscalers() throws KubernetesClientException;

	/**
	 * read the specified HorizontalPodAutoscaler
	 *
	 * @param namespace
	 * @param name
	 * @return
	 */
	public HorizontalPodAutoscaler getHorizontalPodAutoscaler(String name) throws KubernetesClientException;

	/**
	 * list or watch objects of kind HorizontalPodAutoscaler
	 *
	 * @param namespace
	 * @param name
	 * @return
	 */
	public HorizontalPodAutoscalerList listHorizontalPodAutoscalers() throws KubernetesClientException;

	/**
	 * list or watch all objects of kind HorizontalPodAutoscaler
	 *
	 * @param namespace
	 * @param name
	 * @return
	 */
	public HorizontalPodAutoscalerList listAllHorizontalPodAutoscalers() throws KubernetesClientException;
}
