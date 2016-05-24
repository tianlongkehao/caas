package com.bonc.epm.paas.entity;

import javax.persistence.*;

import java.util.List;

/**
 * 集群
 */
public class ClusterUse {
	
    private String host;
    private List<String> cpuUse;
    private List<String> cpuLimit;
    private List<String> memUse;
    private List<String> memSet;
    private List<String> memLimit;
    private List<String> diskUse;
    private List<String> diskLimit;
    private List<String> networkTx;
    private List<String> networkRx;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

	public List<String> getCpuUse() {
		return cpuUse;
	}

	public void setCpuUse(List<String> cpuUse) {
		this.cpuUse = cpuUse;
	}

	public List<String> getCpuLimit() {
		return cpuLimit;
	}

	public void setCpuLimit(List<String> cpuLimit) {
		this.cpuLimit = cpuLimit;
	}

	public List<String> getMemUse() {
		return memUse;
	}

	public void setMemUse(List<String> memUse) {
		this.memUse = memUse;
	}

	public List<String> getMemSet() {
		return memSet;
	}

	public void setMemSet(List<String> memSet) {
		this.memSet = memSet;
	}

	public List<String> getMemLimit() {
		return memLimit;
	}

	public void setMemLimit(List<String> memLimit) {
		this.memLimit = memLimit;
	}

	public List<String> getDiskUse() {
		return diskUse;
	}

	public void setDiskUse(List<String> diskUse) {
		this.diskUse = diskUse;
	}

	public List<String> getDiskLimit() {
		return diskLimit;
	}

	public void setDiskLimit(List<String> diskLimit) {
		this.diskLimit = diskLimit;
	}

	public List<String> getNetworkTx() {
		return networkTx;
	}

	public void setNetworkTx(List<String> networkTx) {
		this.networkTx = networkTx;
	}

	public List<String> getNetworkRx() {
		return networkRx;
	}

	public void setNetworkRx(List<String> networkRx) {
		this.networkRx = networkRx;
	}
    
}

