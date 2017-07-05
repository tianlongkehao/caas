package com.bonc.epm.paas.kubeinstall.api;

import com.bonc.epm.paas.kubeinstall.model.InstallPlan;
import com.bonc.epm.paas.kubeinstall.model.Response;

public interface KubeinstallAPIClientInterface {

	/**
	 * step1CheckInstallPlan. <br/>
	 *
	 * @param installPlan
	 * @return Response
	 */
	public Response step1CheckInstallPlan(InstallPlan installPlan);

	/**
	 * step2CreateYUMStorage. <br/>
	 *
	 * @param installPlan
	 * @return Response
	 */
	public Response step2CreateYUMStorage(InstallPlan installPlan);

	/**
	 * step3InstallRPMs. <br/>
	 *
	 * @param installPlan
	 * @return Response
	 */
	public Response step3InstallRPMs(InstallPlan installPlan);

	/**
	 * step4CreateDockerRegistry. <br/>
	 *
	 * @param installPlan
	 * @return Response
	 */
	public Response step4CreateDockerRegistry(InstallPlan installPlan);

	/**
	 * step5InstallEtcd. <br/>
	 *
	 * @param installPlan
	 * @return Response
	 */
	public Response step5InstallEtcd(InstallPlan installPlan);

	/**
	 * step6MasterInit. <br/>
	 *
	 * @param installPlan
	 * @return Response
	 */
	public Response step6MasterInit(InstallPlan installPlan);

	/**
	 * step7NodesJoin. <br/>
	 *
	 * @param installPlan
	 * @return Response
	 */
	public Response step7NodesJoin(InstallPlan installPlan);

}
