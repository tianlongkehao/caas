package com.bonc.epm.paas.net.model;

public class DiffNat {
	private boolean flag;
	private KubeSep kubeSep;
	private KubeServices kubeServices;
	private KubeNodePorts kubeNodePorts;
	private KubeSvc kubeSvc;
	private Others others;
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public KubeSep getKubeSep() {
		return kubeSep;
	}
	public void setKubeSep(KubeSep kubeSep) {
		this.kubeSep = kubeSep;
	}
	public KubeServices getKubeServices() {
		return kubeServices;
	}
	public void setKubeServices(KubeServices kubeServices) {
		this.kubeServices = kubeServices;
	}
	public KubeNodePorts getKubeNodePorts() {
		return kubeNodePorts;
	}
	public void setKubeNodePorts(KubeNodePorts kubeNodePorts) {
		this.kubeNodePorts = kubeNodePorts;
	}
	public KubeSvc getKubeSvc() {
		return kubeSvc;
	}
	public void setKubeSvc(KubeSvc kubeSvc) {
		this.kubeSvc = kubeSvc;
	}
	public Others getOthers() {
		return others;
	}
	public void setOthers(Others others) {
		this.others = others;
	}
}
