/**
 *
 */
package com.bonc.epm.paas.shera.model;

/**
 * @author Administrator
 *
 */
public class SonarManager {
	boolean check;
	String sources;
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
}
