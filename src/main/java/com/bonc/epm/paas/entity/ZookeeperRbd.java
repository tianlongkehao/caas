package com.bonc.epm.paas.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ZookeeperRbd {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private long zookeeperId;

	private String zookeeper;

	private long rbdId;

	private String rbd;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getZookeeperId() {
		return zookeeperId;
	}

	public void setZookeeperId(long zookeeperId) {
		this.zookeeperId = zookeeperId;
	}

	public String getZookeeper() {
		return zookeeper;
	}

	public void setZookeeper(String zookeeper) {
		this.zookeeper = zookeeper;
	}

	public long getRbdId() {
		return rbdId;
	}

	public void setRbdId(long rbdId) {
		this.rbdId = rbdId;
	}

	public String getRbd() {
		return rbd;
	}

	public void setRbd(String rbd) {
		this.rbd = rbd;
	}

}
