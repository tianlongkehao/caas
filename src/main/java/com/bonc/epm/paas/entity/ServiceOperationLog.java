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
	 * 服务id
	 */
	private long serviceId;
	
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

	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
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
