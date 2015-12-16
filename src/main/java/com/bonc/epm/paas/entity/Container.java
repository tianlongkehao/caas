package com.bonc.epm.paas.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Container {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private Timestamp createTimestap;
	private Date createDate;
	private String serviceAddr;
	private String exposedPort;
	private String bindPort;
	
	private String containerName;
	private String imageName;
	private Integer containerStatus;
	private Integer serviceNum;
	public Integer getServiceNum() {
		return serviceNum;
	}
	public void setServiceNum(Integer serviceNum) {
		this.serviceNum = serviceNum;
	}
	public Integer getContainerStatus() {
		return containerStatus;
	}
	public void setContainerStatus(Integer containerStatus) {
		this.containerStatus = containerStatus;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getContainerName() {
		return containerName;
	}
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}
	public Timestamp getCreateTimestap() {
		return createTimestap;
	}
	public void setCreateTimestap(Timestamp createTimestap) {
		this.createTimestap = createTimestap;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getServiceAddr() {
		return serviceAddr;
	}
	public void setServiceAddr(String serviceAddr) {
		this.serviceAddr = serviceAddr;
	}
	public String getExposedPort() {
		return exposedPort;
	}
	public void setExposedPort(String exposedPort) {
		this.exposedPort = exposedPort;
	}
	public String getBindPort() {
		return bindPort;
	}
	public void setBindPort(String bindPort) {
		this.bindPort = bindPort;
	}
	

}
