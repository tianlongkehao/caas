package com.bonc.epm.paas.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Service {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	/**
	 * 镜像名称
	 */
	private String imgName;
	/**
	 * 镜像版本
	 */
	private String imgVersion;
	/**
	 * 镜像类型
	 */
	private String imgType;
	/**
	 * 服务名称
	 */
	private String serviceName;
	/**
	 * 集群设置
	 */
	private String group;
	/**
	 * 容器设置
	 */
	private String containerSet;
	/**
	 * 实例数量
	 */
	private Integer instanceNum;
	/**
	 * 服务类型(1有状态 2无状态)
	 */
	private String serviceType;
	/**
	 * 镜像设置
	 */
	private String imgSet;
	/**
	 * 服务地址
	 */
	private String serviceAddr;
	/**
	 * 运行状态(1启动中 2运行中 3 停止)
	 */
	private Integer status;
	/**
	 * 链接服务
	 */
	private String serviceLink;
	/**
	 * 环境变量
	 */
	private String buildPath;
	/**
	 * 端口配置
	 */
	private String portSet;
	/**
	 * 创建时间
	 */
	private Date createDate;
	public String getServiceLink() {
		return serviceLink;
	}
	public void setServiceLink(String serviceLink) {
		this.serviceLink = serviceLink;
	}
	public String getBuildPath() {
		return buildPath;
	}
	public void setBuildPath(String buildPath) {
		this.buildPath = buildPath;
	}
	public String getPortSet() {
		return portSet;
	}
	public void setPortSet(String portSet) {
		this.portSet = portSet;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getImgName() {
		return imgName;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	public String getImgVersion() {
		return imgVersion;
	}
	public void setImgVersion(String imgVersion) {
		this.imgVersion = imgVersion;
	}
	public String getImgType() {
		return imgType;
	}
	public void setImgType(String imgType) {
		this.imgType = imgType;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getContainerSet() {
		return containerSet;
	}
	public void setContainerSet(String containerSet) {
		this.containerSet = containerSet;
	}
	public Integer getInstanceNum() {
		return instanceNum;
	}
	public void setInstanceNum(Integer instanceNum) {
		this.instanceNum = instanceNum;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getImgSet() {
		return imgSet;
	}
	public void setImgSet(String imgSet) {
		this.imgSet = imgSet;
	}
	public String getServiceAddr() {
		return serviceAddr;
	}
	public void setServiceAddr(String serviceAddr) {
		this.serviceAddr = serviceAddr;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	

}
