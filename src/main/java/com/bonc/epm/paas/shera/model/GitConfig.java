/*
 * 文件名：GitConfig.java
 * 版权：Copyright by www.huawei.com
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.shera.model;

/**
 * @author ke_wang
 * @version 2016年11月10日
 * @see GitConfig
 * @since
 */
public class GitConfig {
    private String branch;
    private Repository repo;
    
    public String getBranch() {
        return branch;
    }
    public void setBranch(String branch) {
        this.branch = branch;
    }
    public Repository getRepo() {
        return repo;
    }
    public void setRepo(Repository repo) {
        this.repo = repo;
    }
    
}
