package com.bonc.epm.paas.dao;

import org.springframework.data.repository.CrudRepository;

import com.bonc.epm.paas.entity.User;

public interface UserDao extends CrudRepository<User, Long> {
	
	public User findByLoginName(String loginName);
} 
