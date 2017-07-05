package com.bonc.epm.paas.kubernetes.model;

import java.util.List;

/**
 * ClassName: StatefulSetSpec <br/>
 * date: 2017年6月22日 上午11:26:05 <br/>
 *
 * @author longkaixiang
 * @version
 */
public class StatefulSetSpec {
	private Integer replicas;
	private LabelSelector selector;
	private String serviceName;
	private PodTemplateSpec template;
	private List<PersistentVolumeClaim> volumeClaimTemplates ;
	public Integer getReplicas() {
		return replicas;
	}
	public void setReplicas(Integer replicas) {
		this.replicas = replicas;
	}
	public LabelSelector getSelector() {
		return selector;
	}
	public void setSelector(LabelSelector selector) {
		this.selector = selector;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public PodTemplateSpec getTemplate() {
		return template;
	}
	public void setTemplate(PodTemplateSpec template) {
		this.template = template;
	}
	public List<PersistentVolumeClaim> getVolumeClaimTemplates() {
		return volumeClaimTemplates;
	}
	public void setVolumeClaimTemplates(List<PersistentVolumeClaim> volumeClaimTemplates) {
		this.volumeClaimTemplates = volumeClaimTemplates;
	}

}
