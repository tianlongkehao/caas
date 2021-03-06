package com.bonc.epm.paas.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
	private String groupSet;
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
	 * 运行状态(1运行中 2等待中 3创建中 4已停止 5创建失败 6服务异常)
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
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	private long containerID;
	private long createBy;
	private double cpuNum;
	private String ram;
	private long imgID;
	
//	private List<String> podName;
//	public List<String> getPodName() {
//		return podName;
//	}
//	public void setPodName(List<String> podName) {
//		this.podName = podName;
//	}
	public long getImgID() {
		return imgID;
	}
	public void setImgID(long imgID) {
		this.imgID = imgID;
	}
	public double getCpuNum() {
		return cpuNum;
	}
	public void setCpuNum(double cpuNum) {
		this.cpuNum = cpuNum;
	}
	public String getRam() {
		return ram;
	}
	public void setRam(String ram) {
		this.ram = ram;
	}
	
	public long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(long createBy) {
		this.createBy = createBy;
	}
	public long getContainerID() {
		return containerID;
	}
	public void setContainerID(long containerID) {
		this.containerID = containerID;
	}
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
	public String getGroupSet() {
		return groupSet;
	}
	public void setGroupSet(String groupSet) {
		this.groupSet = groupSet;
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
