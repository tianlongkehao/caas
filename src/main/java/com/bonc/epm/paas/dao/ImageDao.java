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
	public Image findById(long id);
	
	@Query("select i from Image i where i.name like ?")
	public List<Image> findByNameCondition(String name);
	
//	public List<Image> findAllByUser();
//	public List<Image> findByUserFavor();
}
