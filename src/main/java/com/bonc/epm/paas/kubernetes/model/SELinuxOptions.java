package com.bonc.epm.paas.kubernetes.model;

/**
 * @author daien
 * @date 2017年7月12日
 */
public class SELinuxOptions {

	private String level;
	private String role;
	private String type;
	private String user;

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
