package com.bonc.epm.paas.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
public class Storage {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	/**
	 * 存储Id
	 */
	private long id;
	/**
	 * 存储名称
	 */
	private String storageName;
	/**
	 * 使用状态（0 未使用 ，1 使用）
	 */
	private Integer useType;
	/**
	 * 存储格式
	 */
	private String format;
	/**
	 * 挂载点
	 */
	private String mountPoint;
	/**
	 * 存储大小
	 */
	private long storageSize;
	/**
	 * 创建时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	/**
	 * 创建用户
	 */
	private long createBy;
	public long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(long createBy) {
		this.createBy = createBy;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getStorageName() {
		return storageName;
	}
	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}
	public Integer getUseType() {
		return useType;
	}
	public void setUseType(Integer useType) {
		this.useType = useType;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getMountPoint() {
		return mountPoint;
	}
	public void setMountPoint(String mountPoint) {
		this.mountPoint = mountPoint;
	}
	public long getStorageSize() {
		return storageSize;
	}
	public void setStorageSize(long storageSize) {
		this.storageSize = storageSize;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	
}
