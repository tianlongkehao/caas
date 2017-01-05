package com.bonc.epm.paas.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.UserFavor;

@Transactional
@Repository
public interface UserFavorDao extends CrudRepository<UserFavor, Long> {
	
	public UserFavor findById(long id);
	
	public UserFavor findByUserId(long userId);

} 