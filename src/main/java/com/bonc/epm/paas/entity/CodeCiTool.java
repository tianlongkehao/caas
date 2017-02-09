package com.bonc.epm.paas.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 代码构建工具
 *
 */
@Entity
public class CodeCiTool {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 工具分组
	 */
	private String toolGroup;

	/**
	 * 工具名称
	 */
	private String name;

	/**
	 * 添加工具所需的dockerfile代码
	 */
	private String toolCode;

	/**
	 * 创建者
	 */
	private long createBy;

	/**
	 * 创建时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String gettoolGroup() {
		return toolGroup;
	}

	public void settoolGroup(String group) {
		this.toolGroup = group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String gettoolCode() {
		return toolCode;
	}

	public void settoolCode(String code) {
		this.toolCode = code;
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
}
