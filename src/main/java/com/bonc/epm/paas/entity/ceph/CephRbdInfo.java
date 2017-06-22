package com.bonc.epm.paas.entity.ceph;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
public class CephRbdInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private long strategyId;

	private String name;

	private String pool;

	private long creator;

	private long size;

	private String detail;

	private boolean used;

	private boolean releaseWhenServiceDown;

	private boolean strategyexcuting;

	private String mountpath;

	private String mappath;

	public long getCreator() {
		return creator;
	}

	public void setCreator(long creator) {
		this.creator = creator;
	}

	public long getStrategyId() {
		return strategyId;
	}

	public void setStrategyId(long strategyId) {
		this.strategyId = strategyId;
	}

	public boolean isStrategyexcuting() {
		return strategyexcuting;
	}

	public void setStrategyexcuting(boolean strategyexcuting) {
		this.strategyexcuting = strategyexcuting;
	}

	/**
	 * 创建时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;

	/**
	 * 更新时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date updateDate;

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


	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public String getMountpath() {
		return mountpath;
	}

	public void setMountpath(String mountpath) {
		this.mountpath = mountpath;
	}

	public String getMappath() {
		return mappath;
	}

	public void setMappath(String mappath) {
		this.mappath = mappath;
	}

	public String getPool() {
		return pool;
	}

	public void setPool(String pool) {
		this.pool = pool;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public boolean isReleaseWhenServiceDown() {
		return releaseWhenServiceDown;
	}

	public void setReleaseWhenServiceDown(boolean releaseWhenServiceDown) {
		this.releaseWhenServiceDown = releaseWhenServiceDown;
	}

	@Override
	public int hashCode() {
		int hash = Objects.hash(id,name,pool);
		return hash;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o)return true;
        if(!(o instanceof CephRbdInfo))return false;
        CephRbdInfo obj = (CephRbdInfo)o;
        return (this.id == obj.getId() && this.name.equals(obj.getName())&&this.pool.equals(obj.getPool()));
	}

}