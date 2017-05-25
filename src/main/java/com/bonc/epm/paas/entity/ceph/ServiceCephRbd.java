package com.bonc.epm.paas.entity.ceph;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ServiceCephRbd {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private long cephrbdId;

	private long serviceId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCephrbdId() {
		return cephrbdId;
	}

	public void setCephrbdId(long cephrbdId) {
		this.cephrbdId = cephrbdId;
	}

	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

}
