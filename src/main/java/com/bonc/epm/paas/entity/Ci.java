package com.bonc.epm.paas.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 构建的实体类
 * @author update
 * @version 2016年8月31日
 * @see Ci
 * @since
 */
@Entity
public class Ci {
    
    /**
     * 主键Id
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
	/**
	 * 代码名称（war包名称）
	 */
    private String resourceName;
	
	/**
	 * 基础镜像名称
	 */
    private String baseImageName;
    
	/**
	 * 基础镜像版本
	 */
    private String baseImageVersion;
    
	/**
	 * 基础镜像Id
	 */
    private long baseImageId;
    
	/**
	 * 项目名称
	 */
    private String projectName;
    
	/**
	 * 构建类型：1代码构建2DockerFile构建3快速构建
	 */
    private Integer type;
	
    /**
	 * 镜像名称-前缀
	 */
    private String imgNameFirst;
    
	/**
	 * 镜像名称-后缀
	 */
    private String imgNameLast;
    
	/**
	 * 镜像名称-版本号
	 */
    private String imgNameVersion;
	
    /**
	 * 镜像类型：1公有、2私有
	 */
    private Integer imgType;
	
    /**
	 * 简介
	 */
    private String description;
	
    /**
	 * 代码类型：0:none、1:git、2:svn
	 */
    private Integer codeType;
	
    /**
	 * 代码url
	 */
    private String codeUrl;
	
    /**
	 * 代码账号
	 */
    private String codeUsername;
	
    /**
	 * 代码密码
	 */
    private String codePassword;
	
    /**
	 * 代码分支
	 */
    private String codeBranch;
    
    /**
     * 代码认证方式
     */
    private String codeCredentials;
	
    /**
	 * 代码位置
	 */
    private String codeLocation;
	
    /**
	 * 代码包位置
	 */
    private String codePackageLocation;
	
    /**
	 * 程序类型:1java2php3go4node.js5python6other
	 */
    private String codeProjectType;
	
    /**
	 * dockerfile位置
	 */
    private String dockerFileLocation;
	
    /**
	 * docker版本
	 */
    private String dockerVersion;
	
    /**
	 * 构建节点:1国内节点2国际节点
	 */
    private Integer constructionNode;
	
    /**
	 * 构建状态：1未构建2构建中3完成4失败
	 */
    private Integer constructionStatus;
	
    /**
	 * 上次成功构建日期
	 */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date constructionDate;
    
    /**
     * 上次失败构建日期
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date constructionFailDate;
	
    /**
	 * 上次构建持续时间
	 */
    private long constructionTime;
	
    /**
	 * 镜像id
	 */
    private long imgId;
	
	/**
	 * 创建人
	 */
    private long createBy;
	
    /**
	 * 创建时间
	 */
    private Date createDate;
	
    /**
     * 代码构建jdk版本
     */
    private String jdkVersion;
    
    /**
     * 构建时调动的方式：1:maven、2:ant
     */
    private Integer invokeType;
    
    /**
     * ant版本
     */
    private String antVersion;
    
    /**
     * ant构建目标
     */
    private String antTargets;
    
    /**
     * ant构建文件路径
     */
    private String antBuildFileLocation;
    
    /**
     * ant性能
     */
    private String antProperties;
    
    /**
     * ant java选择项
     */
    private String antJavaOpts;
    
    /**
     * maven版本
     */
    private String mavenVersion;
    
    /**
     * maven目标
     */
    private String mavenGoals;
    
    /**
     * maven中的pom文件路径
     */
    private String pomLocation;
    
    /**
     * jvm选项
     */
    private String mavenJVMOptions;
    
    /**
     * maven属性
     */
    private String mavenProperty;
    
    /**
     * 是否使用maven私有仓库，0：不使用，1：使用
     */
    private Integer isUserPrivateRegistry;
    
    /**
     * maven配置文件
     */
    private String mavenSetFile;
    
    /**
     * maven全局配置文件
     */
    private String mavenGlobalSetFile;
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
	
    public String getProjectName() {
        return projectName;
    }
	
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
	
    public Integer getType() {
        return type;
    }
    
    public void setType(Integer type) {
        this.type = type;
    }
    
    public String getImgNameFirst() {
        return imgNameFirst;
    }
    
    public void setImgNameFirst(String imgNameFirst) {
        this.imgNameFirst = imgNameFirst;
    }
    
    public String getImgNameLast() {
        return imgNameLast;
    }
    
    public void setImgNameLast(String imgNameLast) {
        this.imgNameLast = imgNameLast;
    }
    
    public String getImgNameVersion() {
        return imgNameVersion;
    }
    
    public void setImgNameVersion(String imgNameVersion) {
        this.imgNameVersion = imgNameVersion;
    }
    
    public long getBaseImageId() {
        return baseImageId;
    }
    
    public void setBaseImageId(long baseImageId) {
        this.baseImageId = baseImageId;
    }
    
    public Integer getImgType() {
        return imgType;
    }
    
    public void setImgType(Integer imgType) {
        this.imgType = imgType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getCodeType() {
        return codeType;
    }
    
    public void setCodeType(Integer codeType) {
        this.codeType = codeType;
    }
    
    public String getCodeUrl() {
        return codeUrl;
    }
    
    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }
    
    public String getCodeUsername() {
        return codeUsername;
    }
    
    public void setCodeUsername(String codeUsername) {
        this.codeUsername = codeUsername;
    }
    
    public String getCodePassword() {
        return codePassword;
    }
    
    public void setCodePassword(String codePassword) {
        this.codePassword = codePassword;
    }
    
    public String getCodeBranch() {
        return codeBranch;
    }
    
    public void setCodeBranch(String codeBranch) {
        this.codeBranch = codeBranch;
    }
    
    public String getCodeCredentials() {
        return codeCredentials;
    }

    public void setCodeCredentials(String codeCredentials) {
        this.codeCredentials = codeCredentials;
    }

    public String getCodeLocation() {
        return codeLocation;
    }
    
    public void setCodeLocation(String codeLocation) {
        this.codeLocation = codeLocation;
    }
    
    public String getCodePackageLocation() {
        return codePackageLocation;
    }
    
    public void setCodePackageLocation(String codePackageLocation) {
        this.codePackageLocation = codePackageLocation;
    }
    
    public String getCodeProjectType() {
        return codeProjectType;
    }
    
    public void setCodeProjectType(String codeProjectType) {
        this.codeProjectType = codeProjectType;
    }
    
    public String getDockerFileLocation() {
        return dockerFileLocation;
    }
    
    public void setDockerFileLocation(String dockerFileLocation) {
        this.dockerFileLocation = dockerFileLocation;
    }
    
    public String getDockerVersion() {
        return dockerVersion;
    }
    
    public void setDockerVersion(String dockerVersion) {
        this.dockerVersion = dockerVersion;
    }
    
    public Integer getConstructionNode() {
        return constructionNode;
    }
    
    public void setConstructionNode(Integer constructionNode) {
        this.constructionNode = constructionNode;
    }
    
    public Integer getConstructionStatus() {
        return constructionStatus;
    }
    
    public void setConstructionStatus(Integer constructionStatus) {
        this.constructionStatus = constructionStatus;
    }
    
    public Date getConstructionDate() {
        return constructionDate;
    }
    
    public void setConstructionDate(Date constructionDate) {
        this.constructionDate = constructionDate;
    }
    
    public Date getConstructionFailDate() {
        return constructionFailDate;
    }

    public void setConstructionFailDate(Date constructionFailDate) {
        this.constructionFailDate = constructionFailDate;
    }

    public long getConstructionTime() {
        return constructionTime;
    }
    
    public void setConstructionTime(long constructionTime) {
        this.constructionTime = constructionTime;
    }
    
    public long getImgId() {
        return imgId;
    }
    
    public void setImgId(long imgId) {
        this.imgId = imgId;
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
    
    public String getBaseImageName() {
        return baseImageName;
    }
    
    public void setBaseImageName(String baseImageName) {
        this.baseImageName = baseImageName;
    }
    
    public String getBaseImageVersion() {
        return baseImageVersion;
    }
    
    public void setBaseImageVersion(String baseImageVersion) {
        this.baseImageVersion = baseImageVersion;
    }
    
    public String getResourceName() {
        return resourceName;
    }
    
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getJdkVersion() {
        return jdkVersion;
    }

    public void setJdkVersion(String jdkVersion) {
        this.jdkVersion = jdkVersion;
    }

    public Integer getInvokeType() {
        return invokeType;
    }

    public void setInvokeType(Integer invokeType) {
        this.invokeType = invokeType;
    }

    public String getAntVersion() {
        return antVersion;
    }

    public void setAntVersion(String antVersion) {
        this.antVersion = antVersion;
    }

    public String getAntTargets() {
        return antTargets;
    }

    public void setAntTargets(String antTargets) {
        this.antTargets = antTargets;
    }

    public String getAntBuildFileLocation() {
        return antBuildFileLocation;
    }

    public void setAntBuildFileLocation(String antBuildFileLocation) {
        this.antBuildFileLocation = antBuildFileLocation;
    }

    public String getAntProperties() {
        return antProperties;
    }

    public void setAntProperties(String antProperties) {
        this.antProperties = antProperties;
    }

    public String getAntJavaOpts() {
        return antJavaOpts;
    }

    public void setAntJavaOpts(String antJavaOpts) {
        this.antJavaOpts = antJavaOpts;
    }

    public String getMavenVersion() {
        return mavenVersion;
    }

    public void setMavenVersion(String mavenVersion) {
        this.mavenVersion = mavenVersion;
    }

    public String getMavenGoals() {
        return mavenGoals;
    }

    public void setMavenGoals(String mavenGoals) {
        this.mavenGoals = mavenGoals;
    }

    public String getPomLocation() {
        return pomLocation;
    }

    public void setPomLocation(String pomLocation) {
        this.pomLocation = pomLocation;
    }

    public String getMavenJVMOptions() {
        return mavenJVMOptions;
    }

    public void setMavenJVMOptions(String mavenJVMOptions) {
        this.mavenJVMOptions = mavenJVMOptions;
    }

    public String getMavenProperty() {
        return mavenProperty;
    }

    public void setMavenProperty(String mavenProperty) {
        this.mavenProperty = mavenProperty;
    }

    public Integer getIsUserPrivateRegistry() {
        return isUserPrivateRegistry;
    }

    public void setIsUserPrivateRegistry(Integer isUserPrivateRegistry) {
        this.isUserPrivateRegistry = isUserPrivateRegistry;
    }

    public String getMavenSetFile() {
        return mavenSetFile;
    }

    public void setMavenSetFile(String mavenSetFile) {
        this.mavenSetFile = mavenSetFile;
    }

    public String getMavenGlobalSetFile() {
        return mavenGlobalSetFile;
    }

    public void setMavenGlobalSetFile(String mavenGlobalSetFile) {
        this.mavenGlobalSetFile = mavenGlobalSetFile;
    }

    
    
}