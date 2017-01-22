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
	private long createBy;              //createBy
	@Transient
	private String creatorName;
	private Date createDate;          //createDate
	private Integer isDelete;
	@Transient
	private Integer currUserFavor;    //当前用户是否收藏当前镜像 0:没有收藏，1：收藏
	@Transient
    private Integer currUserFavorCount;    //收藏当前镜像的用户个数
	@Transient 
	List<PortConfig> portConfigs;
	
	@ManyToMany(mappedBy = "favorImages")
	private List<User> favorUsers;
	
    /**
	 * 是否为基础镜像
	 * 1 是基础镜像，2不是基础镜像；
	 */
	private Integer isBaseImage;
	
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
    public Integer getCurrUserFavor() {
		return currUserFavor;
	}
	public void setCurrUserFavor(Integer currUserFavor) {
		this.currUserFavor = currUserFavor;
	}
	public Integer getCurrUserFavorCount() {
        return currUserFavorCount;
    }
    public void setCurrUserFavorCount(Integer currUserFavorCount) {
        this.currUserFavorCount = currUserFavorCount;
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
	public Integer getIsBaseImage() {
		return isBaseImage;
	}
	public void setIsBaseImage(Integer isBaseImage) {
		this.isBaseImage = isBaseImage;
	}
	public List<PortConfig> getPortConfigs() {
		return portConfigs;
	}
	public void setPortConfigs(List<PortConfig> portConfigs) {
		this.portConfigs = portConfigs;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	
   public Integer getIsDelete() {
        return isDelete;
    }
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }	

}
