/*
 * 文件名：ChangeGit.java
 * 版权：Copyright by www.huawei.com
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月21日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.shera.model;

/**
 * @author ke_wang
 * @version 2016年11月21日
 * @see ChangeGit
 * @since
 */
public class ChangeGit {
    private String namespace;
    private String name;
    private String giturl;
    private String branch;
    private boolean flag;
    
    public String getNamespace() {
        return namespace;
    }
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGiturl() {
        return giturl;
    }
    public void setGiturl(String giturl) {
        this.giturl = giturl;
    }
    public String getBranch() {
        return branch;
    }
    public void setBranch(String branch) {
        this.branch = branch;
    }
    public boolean isFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
