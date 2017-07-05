package com.bonc.epm.paas.kubeinstall.util;

import org.springframework.beans.factory.annotation.Value;

import com.bonc.epm.paas.kubeinstall.api.KubeinstallApiClient;
import com.bonc.epm.paas.rest.util.RestFactory;

@org.springframework.stereotype.Service
public class KubeinstallClientService {

	@Value("${net.api.endpoint}")
	private String endpoint;

	public KubeinstallApiClient getClient() {
		return getClient(endpoint);
	}

	public KubeinstallApiClient getClient(String endpoint) {
		return new KubeinstallApiClient(endpoint, new RestFactory());
	}

	public KubeinstallApiClient getSpecifiedClient(String nodeIp, String port) {
		String endpoint = "http://" + nodeIp + ":" + port;
		return new KubeinstallApiClient(endpoint, new RestFactory());
	}
}
