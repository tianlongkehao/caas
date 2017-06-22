package com.bonc.epm.paas.kubernetes.apis;

import javax.ws.rs.PathParam;

import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.exceptions.Status;
import com.bonc.epm.paas.kubernetes.model.HorizontalPodAutoscaler;
import com.bonc.epm.paas.kubernetes.model.HorizontalPodAutoscalerList;
import com.bonc.epm.paas.kubernetes.model.StatefulSet;
import com.bonc.epm.paas.kubernetes.model.StatefulSetList;

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

	/**
	 * createStatefulSet:create a StatefulSet. <br/>
	 *
	 * @param statefulSet
	 * @return
	 * @throws KubernetesClientException StatefulSet
	 */
	public StatefulSet createStatefulSet(StatefulSet statefulSet) throws KubernetesClientException;

	/**
	 * replaceStatefulSet:replace the specified StatefulSet. <br/>
	 *
	 * @param name
	 * @param statefulSet
	 * @return
	 * @throws KubernetesClientException StatefulSet
	 */
	public StatefulSet replaceStatefulSet(@PathParam("name")String name, StatefulSet statefulSet) throws KubernetesClientException;

	/**
	 * deleteStatefulSet:delete a StatefulSet. <br/>
	 *
	 * @param name
	 * @return
	 * @throws KubernetesClientException StatefulSet
	 */
	public Status deleteStatefulSet(@PathParam("name")String name) throws KubernetesClientException;

	/**
	 * deleteStatefulSets:delete collection of StatefulSet. <br/>
	 *
	 * @return
	 * @throws KubernetesClientException StatefulSet
	 */
	public Status deleteStatefulSets() throws KubernetesClientException;

	/**
	 * getStatefulSets:read the specified StatefulSet. <br/>
	 *
	 * @param name
	 * @return
	 * @throws KubernetesClientException StatefulSet
	 */
	public StatefulSet getStatefulSet(@PathParam("name")String name) throws KubernetesClientException;

	/**
	 * getStatefulSets:list objects of kind StatefulSet. <br/>
	 *
	 * @return
	 * @throws KubernetesClientException StatefulSetList
	 */
	public StatefulSetList getStatefulSets() throws KubernetesClientException;

	/**
	 * getAllStatefulSets:list objects of kind StatefulSet. <br/>
	 *
	 * @return
	 * @throws KubernetesClientException StatefulSetList
	 */
	public StatefulSetList getAllStatefulSets() throws KubernetesClientException;

}
