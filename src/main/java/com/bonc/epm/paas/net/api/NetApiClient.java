package com.bonc.epm.paas.net.api;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bonc.epm.paas.net.exceptions.NetClientException;
import com.bonc.epm.paas.net.model.Nodes;
import com.bonc.epm.paas.net.model.RouteTable;
import com.bonc.epm.paas.rest.util.RestFactory;

public class NetApiClient implements NetAPIClientInterface {

	private static final Log LOG = LogFactory.getLog(NetApiClient.class);

	private String endpointURI;
	private NetAPI api;

	public NetApiClient(String endpointUrl, String username, String password, RestFactory factory) {
		this.endpointURI = endpointUrl + "/flash/jobs";
		api = factory.createNetAPI(endpointURI, username, password);
	}

	@Override
	public Nodes getNodes() throws NetClientException {

		try {
			LOG.info("调用net获取所有nodes接口");
			return api.getNodes();
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new NetClientException(e.getMessage());
		}

	}

	@Override
	public RouteTable checkRoutetable() throws NetClientException {
		try {
			LOG.info("调用net获取所有Routetable接口");
			return api.checkRoutetable();
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new NetClientException(e.getMessage());
		}

	}

}
