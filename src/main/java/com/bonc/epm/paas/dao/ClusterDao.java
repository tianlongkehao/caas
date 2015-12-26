package com.bonc.epm.paas.dao;

import com.bonc.epm.paas.entity.Cluster;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ClusterDao extends CrudRepository<Cluster, Long>{

    public Cluster findByHost(String host);
}
