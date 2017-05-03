package com.bonc.epm.paas.cluster.api;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bonc.epm.paas.cluster.entity.Response;
import com.bonc.epm.paas.net.exceptions.NetClientException;
import com.bonc.epm.paas.rest.util.RestFactory;

public class LocalHealthyClient {

	private static final Log LOG = LogFactory.getLog(LocalHealthyClient.class);

	private String nodeIp;
	private String port;
	private LocalHealthyAPI api;

	public LocalHealthyClient(String nodeIp,String port,RestFactory factory){
		this.nodeIp = nodeIp;
		this.port = port;
		api = factory.createLocalHealthyAPI( "http://" + this.nodeIp + ":" + this.port);
	}

	public LocalHealthyClient(String nodeIp,RestFactory factory){
		this(nodeIp,"8011",factory);
	}

	public Response dns(String type) throws NetClientException{
		try {
			LOG.info("集群测试，调用LocalHealthy测试dns："+type);
			return api.dns(type);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new NetClientException(e.getMessage());
		}
	}

	public Response ping(String ip) throws NetClientException{
		try {
			LOG.info("集群测试，调用ping,from:"+nodeIp+"to:"+ip);
			return api.ping(ip);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new NetClientException(e.getMessage());
		}
	}

	public Response tracepath(String ip) throws NetClientException{
		try {
			LOG.info("集群测试，调用tracepath,from:"+nodeIp+"to:"+ip);
			return api.tracepath(ip);
		} catch (NotFoundException e) {
			return null;
		} catch (WebApplicationException e) {
			throw new NetClientException(e.getMessage());
		}
	}
}
