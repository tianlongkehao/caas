package com.bonc.epm.paas.kubernetes.apis;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import com.bonc.epm.paas.kubernetes.exceptions.KubernetesClientException;
import com.bonc.epm.paas.kubernetes.model.Pod;
import com.bonc.epm.paas.rest.util.RestFactory;

public class KubernetesApisClient implements KubernetesAPISClientInterface {

	private String endpointURI;
	private KubernetesAPIS api;
	private String namespace;

	public KubernetesApisClient(String namespace, String endpointUrl, String username, String password,
			RestFactory factory) {
		this.endpointURI = endpointUrl + "apis/" + KubernetesAPISClientInterface.VERSION;
		this.namespace = namespace;
		api = factory.createKubernetesAPIS(endpointURI, username, password);
	}

	public Pod getPod(String name) throws KubernetesClientException {
		try {
			return api.getPod(namespace, name);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new KubernetesClientException(e);
		}
	}
}
