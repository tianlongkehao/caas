package com.bonc.epm.paas.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
public class PingResult {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String host;
	/**
	 * 创建者
	 */
	private long createBy;

	/**
	 * 创建时间
	 */
	@GeneratedValue
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String pingResult;

	@Transient
	private String ip;

	@Transient
	private boolean success;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(long createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getPingResult() {
		return pingResult;
	}

	public void setPingResult(String pingResult) {
		this.pingResult = pingResult;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
