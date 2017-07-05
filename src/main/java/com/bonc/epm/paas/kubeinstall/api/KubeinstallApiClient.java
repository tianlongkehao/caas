package com.bonc.epm.paas.kubeinstall.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bonc.epm.paas.kubeinstall.model.InstallPlan;
import com.bonc.epm.paas.kubeinstall.model.Response;
import com.bonc.epm.paas.rest.util.RestFactory;

public class KubeinstallApiClient implements KubeinstallAPIClientInterface {

	private static final Log LOG = LogFactory.getLog(KubeinstallApiClient.class);

	private String endpointURI;
	private KubeinstallAPI api;

	public KubeinstallApiClient(String endpointUrl, RestFactory factory) {
		this.endpointURI = endpointUrl;
		api = factory.createKubeinstallAPI(endpointURI);
	}

	/**
	 * @see com.bonc.epm.paas.kubeinstall.api.KubeinstallAPIClientInterface#step1CheckInstallPlan(com.bonc.epm.paas.kubeinstall.model.InstallPlan)
	 */
	@Override
	public Response step1CheckInstallPlan(InstallPlan installPlan) {
		LOG.info("调用接口step1CheckInstallPlan");
		return api.step1CheckInstallPlan(installPlan);
	}

	/**
	 * @see com.bonc.epm.paas.kubeinstall.api.KubeinstallAPIClientInterface#step2CreateYUMStorage(com.bonc.epm.paas.kubeinstall.model.InstallPlan)
	 */
	@Override
	public Response step2CreateYUMStorage(InstallPlan installPlan) {
		LOG.info("调用接口step2CreateYUMStorage");
		return api.step2CreateYUMStorage(installPlan);
	}

	/**
	 * @see com.bonc.epm.paas.kubeinstall.api.KubeinstallAPIClientInterface#step3InstallRPMs(com.bonc.epm.paas.kubeinstall.model.InstallPlan)
	 */
	@Override
	public Response step3InstallRPMs(InstallPlan installPlan) {
		LOG.info("调用接口step3InstallRPMs");
		return api.step3InstallRPMs(installPlan);
	}

	/**
	 * @see com.bonc.epm.paas.kubeinstall.api.KubeinstallAPIClientInterface#step4CreateDockerRegistry(com.bonc.epm.paas.kubeinstall.model.InstallPlan)
	 */
	@Override
	public Response step4CreateDockerRegistry(InstallPlan installPlan) {
		LOG.info("调用接口step4CreateDockerRegistry");
		return api.step4CreateDockerRegistry(installPlan);
	}

	/**
	 * @see com.bonc.epm.paas.kubeinstall.api.KubeinstallAPIClientInterface#step5InstallEtcd(com.bonc.epm.paas.kubeinstall.model.InstallPlan)
	 */
	@Override
	public Response step5InstallEtcd(InstallPlan installPlan) {
		LOG.info("调用接口step5InstallEtcd");
		return api.step5InstallEtcd(installPlan);
	}

	/**
	 * @see com.bonc.epm.paas.kubeinstall.api.KubeinstallAPIClientInterface#step6MasterInit(com.bonc.epm.paas.kubeinstall.model.InstallPlan)
	 */
	@Override
	public Response step6MasterInit(InstallPlan installPlan) {
		LOG.info("调用接口step6MasterInit");
		return api.step6MasterInit(installPlan);
	}

	/**
	 * @see com.bonc.epm.paas.kubeinstall.api.KubeinstallAPIClientInterface#step7NodesJoin(com.bonc.epm.paas.kubeinstall.model.InstallPlan)
	 */
	@Override
	public Response step7NodesJoin(InstallPlan installPlan) {
		LOG.info("调用接口step7NodesJoin");
		return api.step7NodesJoin(installPlan);
	}


}
