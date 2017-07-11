package com.bonc.epm.paas.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.elasticsearch.common.inject.spi.PrivateElements;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
public class Zookeeper {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;

	private long imageId;

	private String image;

	private String namespace;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;

	private long createBy;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String detail;

	private long cpu;

	private long memory;

	private long storage;

	/**
	 * 0:停止，1：运行中
	 */
	private int status;

	private int timeoutdeadline;

	private int starttimeout;

	private int syntimeout;

	private int maxnode;

	private int maxrequest;


	public long getStorage() {
		return storage;
	}

	public void setStorage(long storage) {
		this.storage = storage;
	}

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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTimeoutdeadline() {
		return timeoutdeadline;
	}

	public void setTimeoutdeadline(int timeoutdeadline) {
		this.timeoutdeadline = timeoutdeadline;
	}

	public int getStarttimeout() {
		return starttimeout;
	}

	public void setStarttimeout(int starttimeout) {
		this.starttimeout = starttimeout;
	}

	public int getSyntimeout() {
		return syntimeout;
	}

	public void setSyntimeout(int syntimeout) {
		this.syntimeout = syntimeout;
	}

	public int getMaxnode() {
		return maxnode;
	}

	public void setMaxnode(int maxnode) {
		this.maxnode = maxnode;
	}

	public int getMaxrequest() {
		return maxrequest;
	}

	public void setMaxrequest(int maxrequest) {
		this.maxrequest = maxrequest;
	}

}
