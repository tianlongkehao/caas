package com.bonc.epm.paas.cluster.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.bonc.epm.paas.cluster.entity.Response;

public interface LocalHealthyAPI {

	@GET
	@Path("/dns/{DNSServerType}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response dns(@PathParam("DNSServerType") String type);

	@GET
	@Path("/ping/{hostAddr} ")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response ping(@PathParam("hostAddr") String hostAddr);

	@GET
	@Path("/tracepath/{hostAddr}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response tracepath(@PathParam("hostAddr") String hostAddr);

}
