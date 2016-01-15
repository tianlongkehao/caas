package com.bonc.epm.paas.entity;

import javax.persistence.*;
import java.util.List;

/**
 * 集群
 */
public class ClusterUse {
    private String host;
    private String cpuUse;
    private String cpuLimit;
    private String memUse;
    private String memSet;
    private String memLimit;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getCpuUse() {
        return cpuUse;
    }

    public void setCpuUse(String cpuUse) {
        this.cpuUse = cpuUse;
    }

    public String getCpuLimit() {
        return cpuLimit;
    }

    public void setCpuLimit(String cpuLimit) {
        this.cpuLimit = cpuLimit;
    }

    public String getMemUse() {
        return memUse;
    }

    public void setMemUse(String memUse) {
        this.memUse = memUse;
    }

    public String getMemSet() {
        return memSet;
    }

    public void setMemSet(String memSet) {
        this.memSet = memSet;
    }

    public String getMemLimit() {
        return memLimit;
    }

    public void setMemLimit(String memLimit) {
        this.memLimit = memLimit;
    }

}
