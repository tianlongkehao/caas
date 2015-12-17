package com.bonc.epm.paas.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.User;

@Transactional
public interface UserDao extends CrudRepository<User, Long> {
	
	public User findByUserName(String userName);
} 
