package com.bonc.epm.paas.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity()
@Table(name = "user_favor_images")
public class UserFavorImages {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
	
	private long favor_users;
	
	private long favor_images;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getFavor_users() {
		return favor_users;
	}

	public void setFavor_users(long favor_users) {
		this.favor_users = favor_users;
	}

	public long getFavor_images() {
		return favor_images;
	}

	public void setFavor_images(long favor_images) {
		this.favor_images = favor_images;
	}
   
}
