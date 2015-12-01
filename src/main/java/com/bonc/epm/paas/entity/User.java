package com.bonc.epm.paas.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
	
	private String loginName;
	
	public User(String loginName){
		super();
		this.loginName=loginName;
	}
	public User(long id, String loginName) {
		super();
		this.id = id;
		this.loginName = loginName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	
}