package com.bonc.epm.paas.dao;

import com.bonc.epm.paas.entity.Cluster;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ClusterDao extends CrudRepository<Cluster, Long> {

    public Cluster findByHost(String host);

    @Query("select c from Cluster c "
            + "where 1=1 "
            + "and c.host like %?1% ")
    public List<Cluster> findByHostLike(String host);
    
    @Query("select c from Cluster c "
            + "where 1=1 "
            + "and c.hostType = ?1 "
            + "order by length(c.host),c.host ")
    public List<Cluster> getByHostType(String hostType);
    
}
