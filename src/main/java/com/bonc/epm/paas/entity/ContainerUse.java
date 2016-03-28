package com.bonc.epm.paas.entity;

import java.util.List;

/**
 * @author Administrator
 *
 */
public class ContainerUse {
	
    private List<String> cpuUse;
    private List<String> cpuLimit;
    private List<String> memUse;
    private List<String> memLimit;
    private List<String> memWorkingSet;
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
	public List<String> getMemLimit() {
		return memLimit;
	}
	public void setMemLimit(List<String> memLimit) {
		this.memLimit = memLimit;
	}
	public List<String> getMemWorkingSet() {
		return memWorkingSet;
	}
	public void setMemWorkingSet(List<String> memWorkingSet) {
		this.memWorkingSet = memWorkingSet;
	}
    
    
}