package com.bonc.epm.paas.kubernetes.model;

import io.swagger.models.auth.In;

/**
 * @author daien
 * @date 2017年7月12日
 */
public class SecurityContext {

	private Capabilities capabilities;

	private Boolean privileged;

	private Boolean readOnlyRootFilesystem;

	private Boolean runAsNonRoot;

	private Integer runAsUser;

	private SELinuxOptions seLinuxOptions;

	public Capabilities getCapabilities() {
		return capabilities;
	}

	public void setCapabilities(Capabilities capabilities) {
		this.capabilities = capabilities;
	}

	public Boolean getPrivileged() {
		return privileged;
	}

	public void setPrivileged(Boolean privileged) {
		this.privileged = privileged;
	}

	public Boolean getReadOnlyRootFilesystem() {
		return readOnlyRootFilesystem;
	}

	public void setReadOnlyRootFilesystem(Boolean readOnlyRootFilesystem) {
		this.readOnlyRootFilesystem = readOnlyRootFilesystem;
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

}
