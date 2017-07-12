package com.bonc.epm.paas.kubernetes.model;

/**
 * @author daien
 * @date 2017年7月12日
 */
public class SecurityContext {

	private Capabilities capabilities;

	private boolean privileged;

	private boolean readOnlyRootFilesystem;

	private boolean runAsNonRoot;

	private int runAsUser;

	private SELinuxOptions seLinuxOptions;

	public Capabilities getCapabilities() {
		return capabilities;
	}

	public void setCapabilities(Capabilities capabilities) {
		this.capabilities = capabilities;
	}

	public boolean isPrivileged() {
		return privileged;
	}

	public void setPrivileged(boolean privileged) {
		this.privileged = privileged;
	}

	public boolean isReadOnlyRootFilesystem() {
		return readOnlyRootFilesystem;
	}

	public void setReadOnlyRootFilesystem(boolean readOnlyRootFilesystem) {
		this.readOnlyRootFilesystem = readOnlyRootFilesystem;
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

}
