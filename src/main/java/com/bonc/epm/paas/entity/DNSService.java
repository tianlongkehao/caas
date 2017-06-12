package com.bonc.epm.paas.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
public class DNSService {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;

	private long createBy;

	/**
	 * 检测地址
	 */
	private String address;

	/**
	 * ip
	 */
	@Transient
	private String ip;

	/**
	 * status
	 */
	@Transient
	private String status;

	/**
	 * pingResult
	 */
	@Transient
	private String pingResult;

	/**
	 * isMonitor
	 */
	private Integer isMonitor;

	/**
	 * sleepTime
	 */
	private Integer sleepTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPingResult() {
		return pingResult;
	}

	public void setPingResult(String pingResult) {
		this.pingResult = pingResult;
	}

	public Integer getIsMonitor() {
		return isMonitor;
	}

	public void setIsMonitor(Integer isMonitor) {
		this.isMonitor = isMonitor;
	}

	public Integer getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(Integer sleepTime) {
		this.sleepTime = sleepTime;
	}

}
