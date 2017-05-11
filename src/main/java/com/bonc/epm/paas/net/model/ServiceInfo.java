/**
 * Project Name:EPM_PAAS_CLOUD
 * File Name:ServiceInfo.java
 * Package Name:com.bonc.epm.paas.net.model
 * Date:2017年5月10日下午2:52:12
 * Copyright (c) 2017, longkaixiang@bonc.com.cn All Rights Reserved.
 *
*/

package com.bonc.epm.paas.net.model;

import java.util.List;

/**
 * ClassName: ServiceInfo <br/>
 * Function: {@link Filter} 的属性之一. <br/>
 * date: 2017年5月10日 下午2:52:12 <br/>
 *
 * @author longkaixiang
 * @version
 */
public class ServiceInfo {
	private List<String> kubeSep;
	private List<String> kubeServices;
	private List<String> kubeNodePorts;
	private List<String> kubeSvc;
	private List<String> kubeXlb;
	private List<String> others;

	public List<String> getKubeSep() {
		return kubeSep;
	}

	public void setKubeSep(List<String> kubeSep) {
		this.kubeSep = kubeSep;
	}

	public List<String> getKubeServices() {
		return kubeServices;
	}

	public void setKubeServices(List<String> kubeServices) {
		this.kubeServices = kubeServices;
	}

	public List<String> getKubeNodePorts() {
		return kubeNodePorts;
	}

	public void setKubeNodePorts(List<String> kubeNodePorts) {
		this.kubeNodePorts = kubeNodePorts;
	}

	public List<String> getKubeSvc() {
		return kubeSvc;
	}

	public void setKubeSvc(List<String> kubeSvc) {
		this.kubeSvc = kubeSvc;
	}

	public List<String> getKubeXlb() {
		return kubeXlb;
	}

	public void setKubeXlb(List<String> kubeXlb) {
		this.kubeXlb = kubeXlb;
	}

	public List<String> getOthers() {
		return others;
	}

	public void setOthers(List<String> others) {
		this.others = others;
	}
}
