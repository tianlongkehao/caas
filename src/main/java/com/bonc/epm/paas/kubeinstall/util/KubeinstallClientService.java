package com.bonc.epm.paas.kubeinstall.util;

import org.springframework.beans.factory.annotation.Value;

import com.bonc.epm.paas.net.api.NetAPIClientInterface;
import com.bonc.epm.paas.net.api.NetApiClient;
import com.bonc.epm.paas.rest.util.RestFactory;

@org.springframework.stereotype.Service
public class KubeinstallClientService {

	@Value("${net.api.endpoint}")
	private String endpoint;
	@Value("${net.api.username}")
	private String username;
	@Value("${net.api.password}")
	private String password;
	@Value("${net.api.port}")
	private String port;

	public NetAPIClientInterface getClient() {
		return getClient(endpoint);
	}

	public NetAPIClientInterface getClient(String endpoint) {
		return new NetApiClient(endpoint, username, password, new RestFactory());
	}

	public NetAPIClientInterface getSpecifiedClient(String nodeIp) {
		String endpoint = "http://" + nodeIp + ":" + port;
		return new NetApiClient(endpoint, username, password, new RestFactory());
	}
}
