/*
 * 文件名：UserVisitingLog.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2016年12月30日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.alibaba.fastjson.annotation.JSONField;
@Entity
public class UserVisitingLog {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    /**
     * 登录用户Id
     */
    private long userId;
    
    /**
     * 登录名称
     */
    private String userName;
    
    /**
     * 登录ip
     */
    private String hostIp;
    
    /**
     * 所属组
     */
    private String userGroup;
    
    /**
     * 浏览器
     */
    private String browser;
    
    /**
     * 访问时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date visitingTime;
    
    /**
     * 是否合法
     */
    private boolean isLegal;
    
    /**
     * 登录地区
     */
    private String area;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public Date getVisitingTime() {
        return visitingTime;
    }

    public void setVisitingTime(Date visitingTime) {
        this.visitingTime = visitingTime;
    }

    public boolean isLegal() {
        return isLegal;
    }

    public void setLegal(boolean isLegal) {
        this.isLegal = isLegal;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
    
    
}
