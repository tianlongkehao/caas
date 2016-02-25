package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.Storage;


@Transactional
public interface StorageDao extends CrudRepository<Storage, Long>{

	public List<Storage> findByUserId (long userId);
	
	@Query("select count(*) from Storage s where s.userId = ?1 and s.storageName = ?2 ")
	public int findByName (long userId , String storageName);
	
}
