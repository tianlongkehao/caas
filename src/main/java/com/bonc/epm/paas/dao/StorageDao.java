package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.Storage;

@Transactional
public interface StorageDao extends CrudRepository<Storage, Long> {

	public List<Storage> findByCreateBy(long createBy);

	//@Query("select s from Storage s where 1=1 and s.createBy= ?1 and s.storageName = ?2")
	public Storage findByCreateByAndStorageName(long createBy, String storageName);

	public List<Storage> findAllByCreateByAndUseTypeOrderByCreateDateDesc(long createBy, Pageable pageable,Integer useType);

	public long countByCreateBy(long createBy);

	@Query("select s.mountPoint from Storage s where 1=1 and s.storageName = ?1 ")
  public String findByVolume(String storageName);

}
