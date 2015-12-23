package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.Ci;

@Transactional
public interface CiDao extends CrudRepository<Ci, Long> {
	
	public List<Ci> findByCreateBy(Long createBy);
	
} 
