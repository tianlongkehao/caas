/**
 *
 */
package com.bonc.epm.paas.shera.model;

/**
 * @author lkx
 *
 */
public class SonarConfig {

	boolean enabled;
	boolean hidden;
	boolean mandatory;
	Integer threshold;
	boolean breakable;
	String token;
	String string;
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	public Integer getThreshold() {
		return threshold;
	}
	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
	}
	public boolean isBreak() {
		return breakable;
	}
	public void setBreak(boolean breakable) {
		this.breakable = breakable;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}

}
