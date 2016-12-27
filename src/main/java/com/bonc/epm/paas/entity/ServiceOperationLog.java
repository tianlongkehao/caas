package com.bonc.epm.paas.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ServiceOperationLog {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 用户id
	 */
	private long userId;
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 服务id
	 */
	private long serviceId;
	
	/**
	 * 服务名称
	 */
	private String serviceName;
	
	/**
	 * 服务中文名称
	 */
	private String serviceChName;
	
	/**
	 * 操作类型
	 * 10:更新
	 * 20:创建
	 * 30:启动
	 * 40:停止
	 * 50:调试
	 * 60:弹性伸缩
	 * 70:版本升级
	 * 80:更改配置
	 * 90:删除
	 */
	private long operationType;
	
	/**
	 * 创建时间
	 */
	private Date createDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceChName() {
		return serviceChName;
	}

	public void setServiceChName(String serviceChName) {
		this.serviceChName = serviceChName;
	}

	public long getOperationType() {
		return operationType;
	}

	public void setOperationType(long operationType) {
		this.operationType = operationType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
