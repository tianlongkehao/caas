package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.Storage;


@Transactional
public interface StorageDao extends CrudRepository<Storage, Long>{

	public List<Storage> findByCreateBy (long createBy);
	
	public Storage findByCreateByAndStorageName (long createBy , String storageName);
	
	public List<Storage> findAllByCreateByOrderByCreateDateDesc(long createBy,Pageable pageable);
	
	public long countByCreateBy(long createBy);
	
}
