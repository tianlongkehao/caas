package com.bonc.epm.paas.kubeinstall.api;

import com.bonc.epm.paas.kubeinstall.exceptions.KubeinstallClientException;
import com.bonc.epm.paas.kubeinstall.model.InstallPlan;
import com.bonc.epm.paas.kubeinstall.model.Response;

public interface KubeinstallAPIClientInterface {

	/**
	 * step1CheckInstallPlan. <br/>
	 *
	 * @param installPlan
	 * @return Response
	 */
	public Response step1CheckInstallPlan(InstallPlan installPlan) throws KubeinstallClientException;

	/**
	 * step2CreateYUMStorage. <br/>
	 *
	 * @param installPlan
	 * @return Response
	 */
	public Response step2CreateYUMStorage(InstallPlan installPlan) throws KubeinstallClientException;

	/**
	 * step3InstallRPMs. <br/>
	 *
	 * @param installPlan
	 * @return Response
	 */
	public Response step3InstallRPMs(InstallPlan installPlan) throws KubeinstallClientException;

	/**
	 * step4CreateDockerRegistry. <br/>
	 *
	 * @param installPlan
	 * @return Response
	 */
	public Response step4CreateDockerRegistry(InstallPlan installPlan) throws KubeinstallClientException;

	/**
	 * step5InstallEtcd. <br/>
	 *
	 * @param installPlan
	 * @return Response
	 */
	public Response step5InstallEtcd(InstallPlan installPlan) throws KubeinstallClientException;

	/**
	 * step6MasterInit. <br/>
	 *
	 * @param installPlan
	 * @return Response
	 */
	public Response step6MasterInit(InstallPlan installPlan) throws KubeinstallClientException;

	/**
	 * step7NodesJoin. <br/>
	 *
	 * @param installPlan
	 * @return Response
	 */
	public Response step7NodesJoin(InstallPlan installPlan) throws KubeinstallClientException;

}
