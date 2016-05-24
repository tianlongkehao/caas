package com.bonc.epm.paas.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

/**
 * 镜像
 *
 */
@Entity
public class Image {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String name;
	private String version;
	private String imageId;
	/**
	 * 代码名称（war包名称，冗余ci的字段）
	 */
	private String resourceName;
	//1公用2私有
	private Integer imageType;
	private String remark;
	private String summary;
	private long creator;
	private Date createTime;
	@Transient
	private Integer currUserFavor;
	
	@ManyToMany(mappedBy = "favorImages")
	private List<User> favorUsers;
	
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
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public Integer getImageType() {
		return imageType;
	}
	public void setImageType(Integer imageType) {
		this.imageType = imageType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public long getCreator() {
		return creator;
	}
	public void setCreator(long creator) {
		this.creator = creator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Integer getCurrUserFavor() {
		return currUserFavor;
	}
	public void setCurrUserFavor(Integer currUserFavor) {
		this.currUserFavor = currUserFavor;
	}
	public List<User> getFavorUsers() {
		return favorUsers;
	}
	public void setFavorUsers(List<User> favorUsers) {
		this.favorUsers = favorUsers;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	
}
