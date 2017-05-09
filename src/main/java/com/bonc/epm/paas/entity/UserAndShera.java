/*
 * 文件名：UserAndShera.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2016年12月8日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserAndShera {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 租户Id
	 */
	private long userId;

	/**
	 * sheraId
	 */
	private long sheraId;

	/**
	 * inUsed:是否正在使用.
	 */
	private Integer inUsed;

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

	public long getSheraId() {
		return sheraId;
	}

	public void setSheraId(long sheraId) {
		this.sheraId = sheraId;
	}

	public Integer getInUsed() {
		return inUsed;
	}

	public void setInUsed(Integer inUsed) {
		this.inUsed = inUsed;
	}

}
