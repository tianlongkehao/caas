package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.Image;
import com.bonc.epm.paas.entity.User;

@Transactional
public interface UserDao extends CrudRepository<User, Long> {
	
	public User findByUserName(String userName);
	
	public User findById(long id);
	
	@Query("select i from Image i join i.favorUsers fu where fu.id= ?1 order by  i.name,i.createTime")
	public List<Image> findAllFavor(long creator);
	
	@Query("select i from Image i join i.favorUsers fu where fu.id= ?1 and i.name like ?2 order by  i.name,i.createTime")
	public List<Image> findByNameCondition(long creator,String imageName);
} 
