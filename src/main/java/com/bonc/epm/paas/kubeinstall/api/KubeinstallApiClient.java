package com.bonc.epm.paas.kubeinstall.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bonc.epm.paas.rest.util.RestFactory;

public class KubeinstallApiClient implements KubeinstallAPIClientInterface {

	private static final Log LOG = LogFactory.getLog(KubeinstallApiClient.class);

	private String endpointURI;
	private KubeinstallAPI api;

	public KubeinstallApiClient(String endpointUrl, String username, String password, RestFactory factory) {
		this.endpointURI = endpointUrl + "/flash/jobs";
		api = factory.createKubeinstallAPI(endpointURI);
	}

}
