/*
 * 文件名：CodeManager.java
 * 版权：Copyright by www.bonc.com.cn
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
 * @see CodeManager
 * @since
 */

public class CodeManager {
    
    private Integer codeChoice;  //swagger中没有显示！！
    
    private String uuid;
    
    private GitConfig gitConfig;
    
    private SvnConfig svnConfig;

    public Integer getCodeChoice() {
        return codeChoice;
    }

    public void setCodeChoice(Integer codeChoice) {
        this.codeChoice = codeChoice;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public GitConfig getGitConfig() {
        return gitConfig;
    }

    public void setGitConfig(GitConfig gitConfig) {
        this.gitConfig = gitConfig;
    }

    public SvnConfig getSvnConfig() {
        return svnConfig;
    }

    public void setSvnConfig(SvnConfig svnConfig) {
        this.svnConfig = svnConfig;
    }
    
}
