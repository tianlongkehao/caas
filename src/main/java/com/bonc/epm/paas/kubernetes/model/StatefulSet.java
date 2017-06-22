package com.bonc.epm.paas.kubernetes.model;

/**
 * ClassName: StatefulSet <br/>
 * date: 2017年6月22日 上午9:46:38 <br/>
 *
 * @author longkaixiang
 * @version
 */
public class StatefulSet extends AbstractKubernetesExtensionsModel {
	private ObjectMeta metadata;
	private StatefulSetSpec spec;
	private StatefulSetStatus status;

	public StatefulSet(){
		super(Kind.STATEFULSET);
	}

	public ObjectMeta getMetadata() {
		return metadata;
	}

	public void setMetadata(ObjectMeta metadata) {
		this.metadata = metadata;
	}

	public StatefulSetSpec getSpec() {
		return spec;
	}

	public void setSpec(StatefulSetSpec spec) {
		this.spec = spec;
	}

	public StatefulSetStatus getStatus() {
		return status;
	}

	public void setStatus(StatefulSetStatus status) {
		this.status = status;
	}
}
