package com.bonc.epm.paas.net.api;

import java.util.List;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.net.exceptions.NetClientException;
import com.bonc.epm.paas.net.model.Diff;
import com.bonc.epm.paas.net.model.Iptable;
import com.bonc.epm.paas.net.model.NodeInfo;
import com.bonc.epm.paas.net.model.Nodes;
import com.bonc.epm.paas.net.model.RecoverResult;
import com.bonc.epm.paas.net.model.RouteTable;
import com.bonc.epm.paas.net.model.Service;
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

	@Override
	public Diff getDiff(Service service) throws NetClientException {
		try {
			LOG.info("调用net获取Diff接口");
			return api.getDiff(service);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new NetClientException(e.getMessage());
		}
	}

	/**
	 * @see com.bonc.epm.paas.net.api.NetAPIClientInterface#checkIptable()
	 */
	@Override
	public List<Iptable> checkIptable() throws NetClientException {
		String checkIptable = "";
		try {
			LOG.info("调用net检查Iptable接口");
			checkIptable = api.checkIptable();
			try {
				return JSON.parseArray(checkIptable, Iptable.class);
			} catch (Exception e) {
				LOG.error(e.getMessage());
				return null;
			}
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new NetClientException(e.getMessage());
		}
	}

	/**
	 * @see com.bonc.epm.paas.net.api.NetAPIClientInterface#recoverRoutetable(com.bonc.epm.paas.net.model.NodeInfo)
	 */
	@Override
	public RecoverResult recoverRoutetable(NodeInfo node) throws NetClientException {
		try {
			LOG.info("调用net恢复Routetable接口");
			return api.recoverRoutetable(node);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new NetClientException(e.getMessage());
		}
	}

	/**
	 * @see com.bonc.epm.paas.net.api.NetAPIClientInterface#recoverIptables()
	 */
	@Override
	public RecoverResult recoverIptables() throws NetClientException {
		try {
			LOG.info("调用net恢复Iptables接口");
			return api.recoverIptables();
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new NetClientException(e.getMessage());
		}
	}
}
