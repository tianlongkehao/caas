package com.bonc.epm.paas.kubernetes.apis;

import javax.ws.rs.WebApplicationException;

import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.exceptions.Status;
import com.bonc.epm.paas.kubernetes.model.HorizontalPodAutoscaler;
import com.bonc.epm.paas.kubernetes.model.HorizontalPodAutoscalerList;
import com.bonc.epm.paas.kubernetes.model.StatefulSet;
import com.bonc.epm.paas.kubernetes.model.StatefulSetList;
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
	public Status deleteStatefulSet(String name) throws KubernetesClientException {
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
}
