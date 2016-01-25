package com.bonc.epm.paas.kubernetes.exceptions;

import java.util.List;

import com.google.common.base.MoreObjects;

public class StatusDetails {
    private String name;
    private String kind;
    private int retryAfterSeconds;
    private List<StatusCause> causes;

    public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getKind() {
		return kind;
	}


	public void setKind(String kind) {
		this.kind = kind;
	}


	public int getRetryAfterSeconds() {
		return retryAfterSeconds;
	}


	public void setRetryAfterSeconds(int retryAfterSeconds) {
		this.retryAfterSeconds = retryAfterSeconds;
	}


	public List<StatusCause> getCauses() {
		return causes;
	}


	public void setCauses(List<StatusCause> causes) {
		this.causes = causes;
	}


	@Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("name", name).add("causes", causes).toString();
    }
}
