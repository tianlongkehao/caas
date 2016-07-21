package com.bonc.epm.paas.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
public class EnvVariable {
	
	/**
	 * 环境变量主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long envId;
	
	/**
	 * 创建者
	 */
	private long createBy;
	/**
	 * 环境变量Key
	 */
	private String envKey;
	
	/**
	 * 环境变量Value
	 */
	private String envValue;
	
	/**
	 * 关联服务Id
	 */
	private long serviceId;

	/**
	 * 创建时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	
	public long getEnvId() {
		return envId;
	}

	public void setEnvId(long envId) {
		this.envId = envId;
	}

	public long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(long createBy) {
		this.createBy = createBy;
	}

	public String getEnvKey() {
		return envKey;
	}

	public void setEnvKey(String envKey) {
		this.envKey = envKey;
	}

	public String getEnvValue() {
		return envValue;
	}

	public void setEnvValue(String envValue) {
		this.envValue = envValue;
	}

	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
}
