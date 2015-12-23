package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.Image;

@Transactional
public interface ImageDao extends CrudRepository<Image, Long>{
	public List<Image> findAll();
	
	public List<Image> findByName(String name);
	
	public List<Image> findByImageType(Integer type);
	
	public List<Image> findByCreator(long creator);
	
	public Image findById(long id);
	
	@Query("select i from Image i where i.imageType = 1 and i.name like ?1")
	public List<Image> findByNameCondition(String name);
	
	@Query("select i from Image i where i.imageType = 2 and i.creator = ?1 and i.name like ?2")
	public List<Image> findByNameOfUser(long creator,String name);
	
	@Query("select i from Image i where i.imageType = ?1 and i.creator = ?2")
	public List<Image> findAllByCreator(Integer type,long creator);
	
	@Query("select count(u) from User u join u.favorImages fi where fi.id= ?1 ")
	public int findAllUserById(long imageId);
	
}
