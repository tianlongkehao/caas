package com.bonc.epm.paas.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserFavor {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;// 索引
	//用户id
	private long userId;
	/*
	 * 监控设置 
	 * 0:没有监控
	 * 1:Pinpoint监控
	 */
	private Integer monitor;
	
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
	public Integer getMonitor() {
		return monitor;
	}
	public void setMonitor(Integer monitor) {
		this.monitor = monitor;
	}
}
