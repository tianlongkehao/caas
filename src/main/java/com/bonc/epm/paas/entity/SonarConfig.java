/**
 *
 */
package com.bonc.epm.paas.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author lkx
 *
 */
@Entity
public class SonarConfig {
	/**
	 * 主键Id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * sonar功能总开关(true:使用)
	 */
	private boolean enabled;
	/**
	 * sonar前端是否显示()
	 */
	private boolean hidden;
	/**
	 * sonar是否必选(true必选)
	 */
	private boolean mandatory;
	/**
	 * 代码质量的阈值(12345)
	 */
	private Integer threshold;
	/**
	 * 是否需要等待sonar结果(true:等待)
	 */
	private boolean breakable;
	/**
	 * 访问sonar的Token
	 */
	private String token;
	/**
	 * sonar的地址 (http://code.bonc.com.cn/sonar)
	 */
	private String url;
	/**
	 * 创建人
	 */
	private long createBy;

	/**
	 * 创建时间
	 */
	private Date createDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public Integer getThreshold() {
		return threshold;
	}

	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
	}

	public boolean isBreakable() {
		return breakable;
	}

	public void setBreakable(boolean breakable) {
		this.breakable = breakable;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
