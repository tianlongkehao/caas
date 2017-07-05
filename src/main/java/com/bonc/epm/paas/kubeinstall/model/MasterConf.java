/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:MasterConf.java
 * Package Name:com.bonc.epm.paas.kubeinstall.model
 * Date:2017年7月4日下午5:03:24
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
*/

package com.bonc.epm.paas.kubeinstall.model;

/**
 * ClassName:MasterConf <br/>
 * Date:     2017年7月4日 下午5:03:24 <br/>
 * @author   longkaixiang
 * @version
 * @see
 */
import java.util.List;

public class MasterConf extends KubeinstallAbstractModel {

	private List<String> k8sMasterIPSet;
	private String virtualIP;
	private String port;
	private String networkCNI;
	private String podNetworkCIDR;
	private String accessIPList;
	private String k8sVersion;

	public List<String> getK8sMasterIPSet() {
		return k8sMasterIPSet;
	}

	public void setK8sMasterIPSet(List<String> k8sMasterIPSet) {
		this.k8sMasterIPSet = k8sMasterIPSet;
	}

	public String getVirtualIP() {
		return virtualIP;
	}

	public void setVirtualIP(String virtualIP) {
		this.virtualIP = virtualIP;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getNetworkCNI() {
		return networkCNI;
	}

	public void setNetworkCNI(String networkCNI) {
		this.networkCNI = networkCNI;
	}

	public String getPodNetworkCIDR() {
		return podNetworkCIDR;
	}

	public void setPodNetworkCIDR(String podNetworkCIDR) {
		this.podNetworkCIDR = podNetworkCIDR;
	}

	public String getAccessIPList() {
		return accessIPList;
	}

	public void setAccessIPList(String accessIPList) {
		this.accessIPList = accessIPList;
	}

	public String getK8sVersion() {
		return k8sVersion;
	}

	public void setK8sVersion(String k8sVersion) {
		this.k8sVersion = k8sVersion;
	}

}
