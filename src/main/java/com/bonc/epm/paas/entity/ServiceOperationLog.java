package com.bonc.epm.paas.entity;

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
	private String userId;
	
	/**
	 * 服务id
	 */
	private String serviceId;
	
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
	private String operationType;
	
	/**
	 * 创建时间
	 */
	private String createDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
}
