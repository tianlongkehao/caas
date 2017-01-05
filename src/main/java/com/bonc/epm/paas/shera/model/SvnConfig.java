/*
 * 文件名：SvnConfig.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2017年1月3日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.shera.model;

public class SvnConfig {
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
