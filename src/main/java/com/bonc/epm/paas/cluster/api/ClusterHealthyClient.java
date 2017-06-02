package com.bonc.epm.paas.cluster.api;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bonc.epm.paas.cluster.entity.Response;
import com.bonc.epm.paas.net.exceptions.NetClientException;
import com.bonc.epm.paas.rest.util.RestFactory;

public class ClusterHealthyClient {
	private static final Log LOG = LogFactory.getLog(ClusterHealthyClient.class);

	private String nodeIp;
	private String port;
	private ClusterHealthyAPI api;

	public ClusterHealthyClient(String nodeIp, String port, RestFactory factory) {
		this.nodeIp = nodeIp;
		this.port = port;
		api = factory.createClusterHealthyAPI("http://" + this.nodeIp + ":" + this.port);
	}

	public ClusterHealthyClient(String nodeIp, RestFactory factory) {
		this(nodeIp, "8022", factory);
	}

	public Response qperf(String hostaddr) throws NetClientException {
		try {
			LOG.info("集群测试，调用qperf,from:" + nodeIp + "to:" + hostaddr);
			return api.qperf(hostaddr);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new NetClientException(e.getMessage());
		}
	}

	public Response curl(String hostaddr) throws NetClientException {
		try {
			LOG.info("集群测试，调用curl,from:" + nodeIp + "to:" + hostaddr);
			return api.curl(hostaddr);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new NetClientException(e.getMessage());
		}
	}
}
