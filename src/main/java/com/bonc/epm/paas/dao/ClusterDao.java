package com.bonc.epm.paas.dao;

import com.bonc.epm.paas.entity.Cluster;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ClusterDao
 * @author zhoutao
 * @version 2016年9月5日
 * @see ClusterDao
 * @since
 */
@Transactional
public interface ClusterDao extends CrudRepository<Cluster, Long> {
    
    /**
     * Description: <br>
     * 根据host字段查询数据
     * 
     * @param host host
     * @return Cluster
     * @see
     */
    Cluster findByHost(String host);
    
    /**
     * Description: <br>
     * 根据host模糊查询数据
     * 
     * @param host host
     * @return List
     * @see
     */
    @Query("select c from Cluster c "
            + "where 1=1 "
            + "and c.host like %?1% ")
    List<Cluster> findByHostLike(String host);
    
    /**
     * Description: <br>
     * 根据hostType查询数据
     * 
     * @param hostType hostType
     * @return list
     * @see
     */
    @Query("select c from Cluster c "
            + "where 1=1 "
            + "and c.hostType = ?1 "
            + "order by length(c.host),c.host ")
    List<Cluster> getByHostType(String hostType);
    
}
