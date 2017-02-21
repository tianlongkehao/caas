package com.bonc.epm.paas.net.model;

import java.util.List;

public class SameNat {
	private List<String> kubeSep;
	private List<String> kubeServices;
	private List<String> kubeNodePorts;
	private List<String> kubeSvc;
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
	public List<String> getOthers() {
		return others;
	}
	public void setOthers(List<String> others) {
		this.others = others;
	}

}
