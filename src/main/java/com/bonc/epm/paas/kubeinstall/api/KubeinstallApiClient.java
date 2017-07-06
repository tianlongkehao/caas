package com.bonc.epm.paas.kubeinstall.api;

import com.bonc.epm.paas.kubeinstall.exceptions.KubeinstallClientException;
import com.bonc.epm.paas.kubeinstall.model.InstallPlan;
import com.bonc.epm.paas.kubeinstall.model.Response;
import com.bonc.epm.paas.rest.util.RestFactory;

public class KubeinstallApiClient implements KubeinstallAPIClientInterface {

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
	public Response step1CheckInstallPlan(InstallPlan installPlan) throws KubeinstallClientException {
		Response response = api.step1CheckInstallPlan(installPlan);
		if (response.getResult()) {
			return response;
		} else {
			throw new KubeinstallClientException(response);
		}
	}

	/**
	 * @see com.bonc.epm.paas.kubeinstall.api.KubeinstallAPIClientInterface#step2CreateYUMStorage(com.bonc.epm.paas.kubeinstall.model.InstallPlan)
	 */
	@Override
	public Response step2CreateYUMStorage(InstallPlan installPlan) throws KubeinstallClientException {
		Response response = api.step2CreateYUMStorage(installPlan);
		if (response.getResult()) {
			return response;
		} else {
			throw new KubeinstallClientException(response);
		}
	}

	/**
	 * @see com.bonc.epm.paas.kubeinstall.api.KubeinstallAPIClientInterface#step3InstallRPMs(com.bonc.epm.paas.kubeinstall.model.InstallPlan)
	 */
	@Override
	public Response step3InstallRPMs(InstallPlan installPlan) throws KubeinstallClientException {
		Response response = api.step3InstallRPMs(installPlan);
		if (response.getResult()) {
			return response;
		} else {
			throw new KubeinstallClientException(response);
		}
	}

	/**
	 * @see com.bonc.epm.paas.kubeinstall.api.KubeinstallAPIClientInterface#step4CreateDockerRegistry(com.bonc.epm.paas.kubeinstall.model.InstallPlan)
	 */
	@Override
	public Response step4CreateDockerRegistry(InstallPlan installPlan) throws KubeinstallClientException {
		Response response = api.step4CreateDockerRegistry(installPlan);
		if (response.getResult()) {
			return response;
		} else {
			throw new KubeinstallClientException(response);
		}
	}

	/**
	 * @see com.bonc.epm.paas.kubeinstall.api.KubeinstallAPIClientInterface#step5InstallEtcd(com.bonc.epm.paas.kubeinstall.model.InstallPlan)
	 */
	@Override
	public Response step5InstallEtcd(InstallPlan installPlan) throws KubeinstallClientException {
		Response response = api.step5InstallEtcd(installPlan);
		if (response.getResult()) {
			return response;
		} else {
			throw new KubeinstallClientException(response);
		}
	}

	/**
	 * @see com.bonc.epm.paas.kubeinstall.api.KubeinstallAPIClientInterface#step6MasterInit(com.bonc.epm.paas.kubeinstall.model.InstallPlan)
	 */
	@Override
	public Response step6MasterInit(InstallPlan installPlan) throws KubeinstallClientException {
		Response response = api.step6MasterInit(installPlan);
		if (response.getResult()) {
			return response;
		} else {
			throw new KubeinstallClientException(response);
		}
	}

	/**
	 * @see com.bonc.epm.paas.kubeinstall.api.KubeinstallAPIClientInterface#step7NodesJoin(com.bonc.epm.paas.kubeinstall.model.InstallPlan)
	 */
	@Override
	public Response step7NodesJoin(InstallPlan installPlan) throws KubeinstallClientException {
		Response response = api.step7NodesJoin(installPlan);
		if (response.getResult()) {
			return response;
		} else {
			throw new KubeinstallClientException(response);
		}
	}


}
