package com.bonc.epm.paas.kubeinstall.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.bonc.epm.paas.kubeinstall.model.InstallPlan;
import com.bonc.epm.paas.kubeinstall.model.Response;

public interface KubeinstallAPI {

	/**
	 * step1CheckInstallPlan. <br/>
	 *
	 * @param installPlan
	 * @return Response
	 */
	@POST
	@Path("/cluster/create/step1CheckInstallPlan")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response step1CheckInstallPlan(InstallPlan installPlan);

	/**
	 * step2CreateYUMStorage. <br/>
	 *
	 * @param installPlan
	 * @return Response
	 */
	@POST
	@Path("/cluster/create/step2CreateYUMStorage")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response step2CreateYUMStorage(InstallPlan installPlan);

	/**
	 * step3InstallRPMs. <br/>
	 *
	 * @param installPlan
	 * @return Response
	 */
	@POST
	@Path("/cluster/create/step3InstallRPMs")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response step3InstallRPMs(InstallPlan installPlan);

	/**
	 * step4CreateDockerRegistry. <br/>
	 *
	 * @param installPlan
	 * @return Response
	 */
	@POST
	@Path("/cluster/create/step4CreateDockerRegistry")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response step4CreateDockerRegistry(InstallPlan installPlan);

	/**
	 * step5InstallEtcd. <br/>
	 *
	 * @param installPlan
	 * @return Response
	 */
	@POST
	@Path("/cluster/create/step5InstallEtcd")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response step5InstallEtcd(InstallPlan installPlan);

	/**
	 * step6MasterInit. <br/>
	 *
	 * @param installPlan
	 * @return Response
	 */
	@POST
	@Path("/cluster/create/step6MasterInit")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response step6MasterInit(InstallPlan installPlan);

	/**
	 * step7NodesJoin. <br/>
	 *
	 * @param installPlan
	 * @return Response
	 */
	@POST
	@Path("/cluster/create/step7NodesJoin")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response step7NodesJoin(InstallPlan installPlan);

}
