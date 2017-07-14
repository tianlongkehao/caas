package com.bonc.epm.paas.kubernetes.model;

import java.util.List;

/**
 * @author daien
 * @date 2017年7月12日
 */
public class PodSecurityContext {

	private Integer fsGroup;

	private Boolean runAsNonRoot;

	private Integer runAsUser;

	private SELinuxOptions seLinuxOptions;

	private List<Integer> supplementalGroups;

	public Integer getFsGroup() {
		return fsGroup;
	}

	public void setFsGroup(Integer fsGroup) {
		this.fsGroup = fsGroup;
	}

	public Boolean getRunAsNonRoot() {
		return runAsNonRoot;
	}

	public void setRunAsNonRoot(Boolean runAsNonRoot) {
		this.runAsNonRoot = runAsNonRoot;
	}

	public Integer getRunAsUser() {
		return runAsUser;
	}

	public void setRunAsUser(Integer runAsUser) {
		this.runAsUser = runAsUser;
	}

	public SELinuxOptions getSeLinuxOptions() {
		return seLinuxOptions;
	}

	public void setSeLinuxOptions(SELinuxOptions seLinuxOptions) {
		this.seLinuxOptions = seLinuxOptions;
	}

	public List<Integer> getSupplementalGroups() {
		return supplementalGroups;
	}

	public void setSupplementalGroups(List<Integer> supplementalGroups) {
		this.supplementalGroups = supplementalGroups;
	}

}
