package com.bonc.epm.paas.kubernetes.model;

import java.util.List;

/**
 * @author daien
 * @date 2017年7月12日
 */
public class PodSecurityContext {

	private int fsGroup;

	private boolean runAsNonRoot;

	private int runAsUser;

	private SELinuxOptions seLinuxOptions;

	private List<Integer> supplementalGroups;

	public int getFsGroup() {
		return fsGroup;
	}

	public void setFsGroup(int fsGroup) {
		this.fsGroup = fsGroup;
	}

	public boolean isRunAsNonRoot() {
		return runAsNonRoot;
	}

	public void setRunAsNonRoot(boolean runAsNonRoot) {
		this.runAsNonRoot = runAsNonRoot;
	}

	public int getRunAsUser() {
		return runAsUser;
	}

	public void setRunAsUser(int runAsUser) {
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
