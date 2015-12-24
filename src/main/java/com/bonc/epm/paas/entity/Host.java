package com.bonc.epm.paas.entity;

import javax.persistence.*;
import java.util.List;

/**
 * 集群
 *
 */
@Entity
public class Host {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String host;
	private Integer port;
	private String username;
	private String password;
	
	@ManyToMany(mappedBy = "favorImages")
	private List<User> favorUsers;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public List<User> getFavorUsers() {
		return favorUsers;
	}
	public void setFavorUsers(List<User> favorUsers) {
		this.favorUsers = favorUsers;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
