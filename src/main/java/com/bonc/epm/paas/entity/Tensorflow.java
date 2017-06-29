package com.bonc.epm.paas.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
public class Tensorflow {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;

	private long imageId;

	private long rbdId;

	private String namespace;

	private String url;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;

	private long createBy;

	private String password;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String detail;

	private long cpu;

	private long memory;

	/**
	 * 0:停止，1：运行中
	 */
	private byte status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getImageId() {
		return imageId;
	}

	public void setImageId(long imageId) {
		this.imageId = imageId;
	}

	public long getRbdId() {
		return rbdId;
	}

	public void setRbdId(long rbdId) {
		this.rbdId = rbdId;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public long getCreator() {
		return createBy;
	}

	public void setCreator(long createBy) {
		this.createBy = createBy;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public long getCpu() {
		return cpu;
	}

	public void setCpu(long cpu) {
		this.cpu = cpu;
	}

	public long getMemory() {
		return memory;
	}

	public void setMemory(long memory) {
		this.memory = memory;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

}
