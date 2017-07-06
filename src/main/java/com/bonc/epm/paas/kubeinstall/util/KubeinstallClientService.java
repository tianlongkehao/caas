package com.bonc.epm.paas.kubeinstall.util;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.bonc.epm.paas.kubeinstall.api.KubeinstallApiClient;
import com.bonc.epm.paas.kubeinstall.model.DockerConf;
import com.bonc.epm.paas.kubeinstall.model.EtcdConf;
import com.bonc.epm.paas.kubeinstall.model.InstallPlan;
import com.bonc.epm.paas.kubeinstall.model.LoginInfo;
import com.bonc.epm.paas.kubeinstall.model.MasterConf;
import com.bonc.epm.paas.kubeinstall.model.NodesConf;
import com.bonc.epm.paas.kubeinstall.model.YUMConf;
import com.bonc.epm.paas.rest.util.RestFactory;

@org.springframework.stereotype.Service
public class KubeinstallClientService {

	@Value("${kubeinstall.api.endpoint}")
	private String endpoint;

	public KubeinstallApiClient getClient() {
		return getClient(endpoint);
	}

	public KubeinstallApiClient getClient(String endpoint) {
		return new KubeinstallApiClient(endpoint, new RestFactory());
	}

	public KubeinstallApiClient getSpecifiedClient(String nodeIp, String port) {
		String endpoint = "http://" + nodeIp + ":" + port;
		return new KubeinstallApiClient(endpoint, new RestFactory());
	}

	public InstallPlan generateInstallPlan(Map<String, LoginInfo> machineSSHSet, EtcdConf etcdCfg, MasterConf masterCfg,
			DockerConf dockerCfg, YUMConf yumCfg, NodesConf nodesCfg) {
		InstallPlan installPlan = new InstallPlan();
		if (machineSSHSet != null) {
			installPlan.setMachineSSHSet(machineSSHSet);
		}
		if (etcdCfg != null) {
			installPlan.setEtcdCfg(etcdCfg);
		}
		if (masterCfg != null) {
			installPlan.setMasterCfg(masterCfg);
		}
		if (dockerCfg != null) {
			installPlan.setDockerCfg(dockerCfg);
		}
		if (yumCfg != null) {
			installPlan.setYumCfg(yumCfg);
		}
		if (nodesCfg != null) {
			installPlan.setNodesCfg(nodesCfg);
		}
		return installPlan;
	}
}
