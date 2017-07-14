package com.bonc.epm.paas.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.ZookeeperImage;

@Transactional
public interface ZookeeperImageDao extends CrudRepository<ZookeeperImage, Long>{

}
