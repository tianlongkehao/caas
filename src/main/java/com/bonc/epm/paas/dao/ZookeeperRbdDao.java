package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.ZookeeperRbd;

@Transactional
public interface ZookeeperRbdDao extends CrudRepository<ZookeeperRbd, Long>{

	List<ZookeeperRbd> findByZookeeperId(long zookeeperId);

	ZookeeperRbd findByRbdId(long rbdId);
}
