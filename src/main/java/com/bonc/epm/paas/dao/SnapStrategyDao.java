package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.ceph.SnapStrategy;

@Transactional
public interface SnapStrategyDao extends CrudRepository<SnapStrategy,Long>{

	public void deleteByName(String name);

	public void deleteById(long id);

	public void deleteByNamespace(String namespace);

	public void deleteByUserId(long userId);

	public SnapStrategy findById(long id);

	public List<SnapStrategy> findByNamespace(String namespace);

	public List<SnapStrategy> findByUserId(long userId);

	public List<SnapStrategy> findByName(String name);

}
