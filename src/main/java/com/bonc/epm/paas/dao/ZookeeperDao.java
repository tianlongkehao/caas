package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.Zookeeper;

@Transactional
public interface ZookeeperDao extends CrudRepository<Zookeeper, Long>{

	Zookeeper findByNamespaceAndName(String namespace,String name);

	List<Zookeeper> findByNamespace(String namespace);
}
