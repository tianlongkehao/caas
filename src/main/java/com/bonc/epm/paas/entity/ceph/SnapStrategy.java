package com.bonc.epm.paas.entity.ceph;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
public class SnapStrategy {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;

	private String namespace;

	private long userId;

	private int keep;

	private String time;

	private String week;

	private int bindCount;

	private int excutingCount;

	/**
	 * 创建时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;

	/**
	 * 结束时间
	 */
	@JSONField(format = "yyyy-MM-dd")
	private Date endData;

	public int getExcutingCount() {
		return excutingCount;
	}

	public void setExcutingCount(int excutingCount) {
		this.excutingCount = excutingCount;
	}

	public int getKeep() {
		return keep;
	}

	public void setKeep(int keep) {
		this.keep = keep;
	}

	public int getBindCount() {
		return bindCount;
	}

	public void setBindCount(int bindCount) {
		this.bindCount = bindCount;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getEndData() {
		return endData;
	}

	public void setEndData(Date endData) {
		this.endData = endData;
	}

}
