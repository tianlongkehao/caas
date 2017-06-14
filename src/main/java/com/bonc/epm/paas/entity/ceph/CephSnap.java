package com.bonc.epm.paas.entity.ceph;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
public class CephSnap {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;

	private String imgname;

	private String snapdetail;

	private String pool;

	private long imgId;

	/**
	 * 创建时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;

	public String getPool() {
		return pool;
	}

	public void setPool(String pool) {
		this.pool = pool;
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

	public String getImgname() {
		return imgname;
	}

	public void setImgname(String imgname) {
		this.imgname = imgname;
	}

	public String getSnapdetail() {
		return snapdetail;
	}

	public void setSnapdetail(String snapdetail) {
		this.snapdetail = snapdetail;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public long getImgId() {
		return imgId;
	}

	public void setImgId(long imgId) {
		this.imgId = imgId;
	}

}
