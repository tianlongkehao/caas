/*
 * 文件名：CiInvoke.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2016年11月11日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.entity;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.alibaba.fastjson.annotation.JSONField;

public class CiInvoke {
    /**
     * 主键Id
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    /**
     * 关联ci表
     */
    private long ciId;
    
    /**
     * 创建人
     */
    private long createBy;
    
    /**
     * 工作顺序id
     */
    private Integer jobOrderId;
    
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    
    /**
     * 构建时调动的方式：1:maven、2:ant、3：shell;
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
     * 是否使用maven私有仓库，0：不使用、1：使用
     */
    private Integer isUserPrivateRegistry;
    
    /**
     * 是否注入建立变量， 0：不注入、1：注入
     */
    private Integer injectBuildVariables;
    
    /**
     * maven配置文件
     */
    private String mavenSetFile;
    
    /**
     * maven全局配置文件
     */
    private String mavenGlobalSetFile;
    
    /**
     * shell脚本命令
     */
    private String shellCommand;
}
