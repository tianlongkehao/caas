/*
 * 文件名：CiCode.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2016年12月27日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 
 * 代码构建特殊字段
 * @author zhoutao
 * @version 2016年12月27日
 * @see CiCode
 * @since
 */
@Entity
public class CiCode {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    /**
     * ciId
     */
    private long ciId;

    /**
     * 是否为基础镜像
     * 1 是基础镜像，2不是基础镜像；
     */
    private Integer isBaseImage; 
    
    /**
     * 代码类型：0:none、1:git、2:svn
     */
    private Integer codeType;
    
    /**
     * 代码url
     */
    private String codeUrl;
    
    /**
     * 是否需要挂钩代码，0：不挂钩、1：挂钩代码
     */
    private Integer isHookCode;
    
    /**
     * 代码挂钩Id
     */
    private long hookCodeId;
    
    /**
     * name;
     */
    private String codeName;
    
    /**
     * Refspec
     */
    private String codeRefspec;
    
    /**
     * 代码分支
     */
    private String codeBranch;
    
    /**
     * 代码认证方式
     */
    private Long codeCredentials;
    
    /**
     * 代码构建jdk版本
     */
    private String jdkVersion;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCiId() {
        return ciId;
    }

    public void setCiId(long ciId) {
        this.ciId = ciId;
    }

    public Integer getIsBaseImage() {
        return isBaseImage;
    }

    public void setIsBaseImage(Integer isBaseImage) {
        this.isBaseImage = isBaseImage;
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

    public Integer getIsHookCode() {
        return isHookCode;
    }

    public void setIsHookCode(Integer isHookCode) {
        this.isHookCode = isHookCode;
    }

    public long getHookCodeId() {
        return hookCodeId;
    }

    public void setHookCodeId(long hookCodeId) {
        this.hookCodeId = hookCodeId;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getCodeRefspec() {
        return codeRefspec;
    }

    public void setCodeRefspec(String codeRefspec) {
        this.codeRefspec = codeRefspec;
    }

    public String getCodeBranch() {
        return codeBranch;
    }

    public void setCodeBranch(String codeBranch) {
        this.codeBranch = codeBranch;
    }

    public Long getCodeCredentials() {
        return codeCredentials;
    }

    public void setCodeCredentials(Long codeCredentials) {
        this.codeCredentials = codeCredentials;
    }

    public String getJdkVersion() {
        return jdkVersion;
    }

    public void setJdkVersion(String jdkVersion) {
        this.jdkVersion = jdkVersion;
    }
    
}
