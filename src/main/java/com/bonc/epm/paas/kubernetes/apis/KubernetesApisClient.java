package com.bonc.epm.paas.kubernetes.apis;

import javax.ws.rs.WebApplicationException;

import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.exceptions.Status;
import com.bonc.epm.paas.kubernetes.model.HorizontalPodAutoscaler;
import com.bonc.epm.paas.kubernetes.model.HorizontalPodAutoscalerList;
import com.bonc.epm.paas.kubernetes.model.PodDisruptionBudget;
import com.bonc.epm.paas.kubernetes.model.PodDisruptionBudgetList;
import com.bonc.epm.paas.kubernetes.model.StatefulSet;
import com.bonc.epm.paas.kubernetes.model.StatefulSetList;
import com.bonc.epm.paas.kubernetes.model.StorageClass;
import com.bonc.epm.paas.kubernetes.model.StorageClassList;
import com.bonc.epm.paas.rest.util.RestFactory;

public class KubernetesApisClient implements KubernetesAPISClientInterface {

	private String endpointURI;
	private KubernetesAPIS apis;
	private String namespace;
	private final String NOTFOUND = "NotFound";

	public KubernetesApisClient(String namespace, String endpointUrl, String username, String password,
			RestFactory factory) {
		this.endpointURI = endpointUrl + "apis";
		this.namespace = namespace;
		apis = factory.createKubernetesAPIS(endpointURI, username, password);
	}
	/**
	 * create a HorizontalPodAutoscaler
	 *
	 * @param namespace
	 * @param name
	 * @return
	 */
	public HorizontalPodAutoscaler createHorizontalPodAutoscaler(HorizontalPodAutoscaler horizontalPodAutoscaler) throws KubernetesClientException{
		try {
			return apis.createHorizontalPodAutoscaler(namespace, horizontalPodAutoscaler);
		} catch (KubernetesClientException e) {
			if (e.getStatus().getReason().equals(NOTFOUND)) {
				return null;
			} else {
				throw new KubernetesClientException(e);
			}
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}

	/**
	 * replace the specified HorizontalPodAutoscaler
	 *
	 * @param namespace
	 * @param name
	 * @return
	 */
	public HorizontalPodAutoscaler replaceHorizontalPodAutoscaler(String name, HorizontalPodAutoscaler horizontalPodAutoscaler){
		try {
			return apis.replaceHorizontalPodAutoscaler(namespace, name, horizontalPodAutoscaler);
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}

	/**
	 * delete a HorizontalPodAutoscaler
	 *
	 * @param namespace
	 * @param name
	 * @return
	 */
	public Status deleteHorizontalPodAutoscaler(String name) throws KubernetesClientException{
		try {
			return apis.deleteHorizontalPodAutoscaler(namespace, name);
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}

	/**
	 * delete collection of HorizontalPodAutoscaler
	 *
	 * @param namespace
	 * @param name
	 * @return
	 */
	public Status deleteHorizontalPodAutoscalers() throws KubernetesClientException{
		try {
			return apis.deleteHorizontalPodAutoscalers(namespace);
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}

	/**
	 * read the specified HorizontalPodAutoscaler
	 *
	 * @param namespace
	 * @param name
	 * @return
	 */
	public HorizontalPodAutoscaler getHorizontalPodAutoscaler(String name) throws KubernetesClientException{
		try {
			return apis.getHorizontalPodAutoscaler(namespace, name);
		} catch (KubernetesClientException e) {
			if (e.getStatus().getReason().equals(NOTFOUND)) {
				return null;
			} else {
				throw new KubernetesClientException(e);
			}
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}

	/**
	 * list or watch objects of kind HorizontalPodAutoscaler
	 *
	 * @param namespace
	 * @param name
	 * @return
	 */
	public HorizontalPodAutoscalerList listHorizontalPodAutoscalers() throws KubernetesClientException{
		try {
			return apis.listHorizontalPodAutoscalers(namespace);
		} catch (KubernetesClientException e) {
			if (e.getStatus().getReason().equals(NOTFOUND)) {
				return null;
			} else {
				throw new KubernetesClientException(e);
			}
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}

	/**
	 * list or watch all objects of kind HorizontalPodAutoscaler
	 *
	 * @param namespace
	 * @param name
	 * @return
	 */
	public HorizontalPodAutoscalerList listAllHorizontalPodAutoscalers() throws KubernetesClientException{
		try {
			return apis.listAllHorizontalPodAutoscalers();
		} catch (KubernetesClientException e) {
			if (e.getStatus().getReason().equals(NOTFOUND)) {
				return null;
			} else {
				throw new KubernetesClientException(e);
			}
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}
	/**
	 * @see com.bonc.epm.paas.kubernetes.apis.KubernetesAPISClientInterface#createStatefulSet(com.bonc.epm.paas.kubernetes.model.StatefulSet)
	 */
	@Override
	public StatefulSet createStatefulSet(StatefulSet statefulSet) throws KubernetesClientException {
		try {
			return apis.createStatefulSet(namespace, statefulSet);
		} catch (KubernetesClientException e) {
			throw new KubernetesClientException(e);
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}
	/**
	 * @see com.bonc.epm.paas.kubernetes.apis.KubernetesAPISClientInterface#replaceStatefulSet(java.lang.String, com.bonc.epm.paas.kubernetes.model.StatefulSet)
	 */
	@Override
	public StatefulSet replaceStatefulSet(String name, StatefulSet statefulSet) throws KubernetesClientException {
		try {
			return apis.replaceStatefulSet(namespace, name, statefulSet);
		} catch (KubernetesClientException e) {
			throw new KubernetesClientException(e);
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}
	/**
	 * @see com.bonc.epm.paas.kubernetes.apis.KubernetesAPISClientInterface#deleteStatefulSet(java.lang.String)
	 */
	@Override
	public StatefulSet deleteStatefulSet(String name) throws KubernetesClientException {
		try {
			return apis.deleteStatefulSet(namespace, name);
		} catch (KubernetesClientException e) {
			throw new KubernetesClientException(e);
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}
	/**
	 * @see com.bonc.epm.paas.kubernetes.apis.KubernetesAPISClientInterface#deleteStatefulSets()
	 */
	@Override
	public Status deleteStatefulSets() throws KubernetesClientException {
		try {
			return apis.deleteStatefulSets(namespace);
		} catch (KubernetesClientException e) {
			throw new KubernetesClientException(e);
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}
	/**
	 * @see com.bonc.epm.paas.kubernetes.apis.KubernetesAPISClientInterface#getStatefulSet(java.lang.String)
	 */
	@Override
	public StatefulSet getStatefulSet(String name) throws KubernetesClientException {
		try {
			return apis.getStatefulSet(namespace, name);
		} catch (KubernetesClientException e) {
			if (e.getStatus().getReason().equals(NOTFOUND)) {
				return null;
			} else {
				throw new KubernetesClientException(e);
			}
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}
	/**
	 * @see com.bonc.epm.paas.kubernetes.apis.KubernetesAPISClientInterface#getStatefulSets()
	 */
	@Override
	public StatefulSetList getStatefulSets() throws KubernetesClientException {
		try {
			return apis.getStatefulSets(namespace);
		} catch (KubernetesClientException e) {
			if (e.getStatus().getReason().equals(NOTFOUND)) {
				return null;
			} else {
				throw new KubernetesClientException(e);
			}
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}
	/**
	 * @see com.bonc.epm.paas.kubernetes.apis.KubernetesAPISClientInterface#getAllStatefulSets()
	 */
	@Override
	public StatefulSetList getAllStatefulSets() throws KubernetesClientException {
		try {
			return apis.getStatefulSets();
		} catch (KubernetesClientException e) {
			if (e.getStatus().getReason().equals(NOTFOUND)) {
				return null;
			} else {
				throw new KubernetesClientException(e);
			}
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}

	@Override
	public StorageClass createStorageClass(StorageClass storageClass) throws KubernetesClientException {
		try {
			return apis.createStorageClass(storageClass);
		} catch (KubernetesClientException e) {
			throw new KubernetesClientException(e);
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}

	@Override
	public StorageClass replaceStorageClass(String name, StorageClass storageClass) throws KubernetesClientException {
		try {
			return apis.replaceStorageClass(name, storageClass);
		} catch (KubernetesClientException e) {
			throw new KubernetesClientException(e);
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}

	@Override
	public Status deleteStorageClass(String name) throws KubernetesClientException {
		try {
			return apis.deleteStorageClass(name);
		} catch (KubernetesClientException e) {
			throw new KubernetesClientException(e);
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}

	@Override
	public Status deleteStorageClasses() throws KubernetesClientException {
		try {
			return apis.deleteStorageClasses();
		} catch (KubernetesClientException e) {
			throw new KubernetesClientException(e);
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}

	@Override
	public StorageClass getStorageClass(String name) throws KubernetesClientException {
		try {
			return apis.getStorageClass(name);
		} catch (KubernetesClientException e) {
			if (e.getStatus().getReason().equals(NOTFOUND)) {
				return null;
			} else {
				throw new KubernetesClientException(e);
			}
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}

	@Override
	public StorageClassList getStorageClasses() throws KubernetesClientException {
		try {
			return apis.getStorageClasses();
		} catch (KubernetesClientException e) {
			if (e.getStatus().getReason().equals(NOTFOUND)) {
				return null;
			} else {
				throw new KubernetesClientException(e);
			}
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}

	@Override
	public PodDisruptionBudget createPodDisruptionBudget(PodDisruptionBudget podDisruptionBudget)
			throws KubernetesClientException {
		try {
			return apis.createPodDisruptionBudget(namespace, podDisruptionBudget);
		} catch (KubernetesClientException e) {
			throw new KubernetesClientException(e);
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}

	@Override
	public PodDisruptionBudget replacePodDisruptionBudget(String name, PodDisruptionBudget podDisruptionBudget)
			throws KubernetesClientException {
		try {
			return apis.replacePodDisruptionBudget(namespace, name, podDisruptionBudget);
		} catch (KubernetesClientException e) {
			throw new KubernetesClientException(e);
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}

	@Override
	public Status deletePodDisruptionBudget(String name) throws KubernetesClientException {
		try {
			return apis.deletePodDisruptionBudget(namespace, name);
		} catch (KubernetesClientException e) {
			throw new KubernetesClientException(e);
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}

	@Override
	public Status deletePodDisruptionBudgets() throws KubernetesClientException {
		try {
			return apis.deletePodDisruptionBudgets(namespace);
		} catch (KubernetesClientException e) {
			throw new KubernetesClientException(e);
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}

	@Override
	public PodDisruptionBudget getPodDisruptionBudget(String name) throws KubernetesClientException {
		try {
			return apis.getPodDisruptionBudget(namespace, name);
		} catch (KubernetesClientException e) {
			if (e.getStatus().getReason().equals(NOTFOUND)) {
				return null;
			} else {
				throw new KubernetesClientException(e);
			}
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}

	@Override
	public PodDisruptionBudgetList getPodDisruptionBudgets() throws KubernetesClientException {
		try {
			return apis.getPodDisruptionBudgets(namespace);
		} catch (KubernetesClientException e) {
			if (e.getStatus().getReason().equals(NOTFOUND)) {
				return null;
			} else {
				throw new KubernetesClientException(e);
			}
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}
}
