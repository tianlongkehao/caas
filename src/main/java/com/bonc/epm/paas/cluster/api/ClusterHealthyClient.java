package com.bonc.epm.paas.cluster.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bonc.epm.paas.rest.util.RestFactory;

public class ClusterHealthyClient {
	private static final Log LOG = LogFactory.getLog(ClusterHealthyClient.class);

	private String nodeIp;
	private String port;
	private ClusterHealthyAPI api;

	public ClusterHealthyClient(String nodeIp,String port,RestFactory factory){
		this.nodeIp = nodeIp;
		this.port = port;
		api = factory.createClusterHealthyAPI( "http://" + this.nodeIp + ":" + this.port);
	}

	public ClusterHealthyClient(String nodeIp,RestFactory factory){
		this(nodeIp,"8011",factory);
	}
}
