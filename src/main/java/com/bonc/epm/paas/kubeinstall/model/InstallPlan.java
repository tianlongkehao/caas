/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:InstallPlan.java
 * Package Name:com.bonc.epm.paas.kubeinstall.model
 * Date:2017年7月5日上午11:00:03
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
 */

package com.bonc.epm.paas.kubeinstall.model;

/**
 * ClassName: InstallPlan <br/>
 * date: 2017年7月5日 上午11:00:03 <br/>
 *
 * @author longkaixiang
 * @version
 */
import java.util.Map;

public class InstallPlan extends KubeinstallAbstractModel {
	private Map<String, LoginInfo> machineSSHSet;
	private EtcdConf etcdCfg;
	private MasterConf masterCfg;
	private DockerConf dockerCfg;
	private YUMConf yumCfg;
	private NodesConf nodesCfg;

	public Map<String, LoginInfo> getMachineSSHSet() {
		return machineSSHSet;
	}

	public void setMachineSSHSet(Map<String, LoginInfo> machineSSHSet) {
		this.machineSSHSet = machineSSHSet;
	}

	public EtcdConf getEtcdCfg() {
		return etcdCfg;
	}

	public void setEtcdCfg(EtcdConf etcdCfg) {
		this.etcdCfg = etcdCfg;
	}

	public MasterConf getMasterCfg() {
		return masterCfg;
	}

	public void setMasterCfg(MasterConf masterCfg) {
		this.masterCfg = masterCfg;
	}

	public DockerConf getDockerCfg() {
		return dockerCfg;
	}

	public void setDockerCfg(DockerConf dockerCfg) {
		this.dockerCfg = dockerCfg;
	}

	public YUMConf getYumCfg() {
		return yumCfg;
	}

	public void setYumCfg(YUMConf yumCfg) {
		this.yumCfg = yumCfg;
	}

	public NodesConf getNodesCfg() {
		return nodesCfg;
	}

	public void setNodesCfg(NodesConf nodesCfg) {
		this.nodesCfg = nodesCfg;
	}
}
