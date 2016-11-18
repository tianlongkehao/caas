/*
 * 文件名：BuildManager.java
 * 版权：Copyright by www.huawei.com
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.shera.model;

import java.util.List;

/**
 * @author ke_wang
 * @version 2016年11月10日
 * @see BuildManager
 * @since
 */
public class BuildManager {
    //0:none、1：ant、2：maven、3：shell
    private List<Integer> seqNo;
    private List<String> cmds;
    private List<AntConfig> antConfigs;
    private List<MvnConfig> mvnConfigs;
    
    public List<Integer> getSeqNo() {
        return seqNo;
    }
    public void setSeqNo(List<Integer> seqNos) {
        this.seqNo = seqNos;
    }
    public List<String> getCmds() {
        return cmds;
    }
    public void setCmds(List<String> cmds) {
        this.cmds = cmds;
    }
    public List<AntConfig> getAntConfigs() {
        return antConfigs;
    }
    public void setAntConfigs(List<AntConfig> antConfigs) {
        this.antConfigs = antConfigs;
    }
    public List<MvnConfig> getMvnConfigs() {
        return mvnConfigs;
    }
    public void setMvnConfigs(List<MvnConfig> mvnConfigs) {
        this.mvnConfigs = mvnConfigs;
    }
}
