/**
 *
 */
package com.bonc.epm.paas.shera.model;

/**
 * @author Administrator
 *
 */
public class SonarManager {
	private boolean check;
	private String sources;
	private String inclusions;
	private String exclusions;

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public String getSources() {
		return sources;
	}

	public void setSources(String sources) {
		this.sources = sources;
	}

	public String getInclusions() {
		return inclusions;
	}

	public void setInclusions(String inclusions) {
		this.inclusions = inclusions;
	}

	public String getExclusions() {
		return exclusions;
	}

	public void setExclusions(String exclusions) {
		this.exclusions = exclusions;
	}
}
