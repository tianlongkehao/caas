package com.bonc.epm.paas.cluster.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.bonc.epm.paas.cluster.entity.Response;
import com.bonc.epm.paas.net.exceptions.NetClientException;

public interface ClusterHealthyAPI {

	@GET
	@Path("/qperf/{hostAddr}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response qperf(@PathParam("hostAddr") String hostAddr) throws NetClientException;

	@GET
	@Path("/curl/{hostAddr} ")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response curl(@PathParam("hostAddr") String hostAddr) throws NetClientException;

}
