package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.ceph.CephRbdInfo;

@Transactional
public interface CephRbdInfoDao extends CrudRepository<CephRbdInfo, Long>{

	public List<CephRbdInfo> findByPool(String pool);

	public List<CephRbdInfo> findByName(String name);

	public CephRbdInfo deleteByName(String name);

}
