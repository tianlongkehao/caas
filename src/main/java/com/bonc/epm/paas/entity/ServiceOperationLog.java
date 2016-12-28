package com.bonc.epm.paas.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class ServiceOperationLog {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * 服务名称
	 */
	private String serviceName;
	
	/**
	 * 额外信息(以字符串来记录主要信息)
	 */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String serviceExtraInfo;
	
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
	 * 创建日期
	 */
	private Date createDate;

	/**
	 * 创建人
	 */
	private long createBy;
	
	/**
	 * 创建人姓名
	 */
	private String createUserName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceExtraInfo() {
		return serviceExtraInfo;
	}

	public void setServiceExtraInfo(String serviceExtraInfo) {
		this.serviceExtraInfo = serviceExtraInfo;
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

	public long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(long createBy) {
		this.createBy = createBy;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	
}
