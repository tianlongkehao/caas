package com.bonc.epm.paas.dao;

import java.util.HashSet;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.EnvVariable;
import com.bonc.epm.paas.entity.PortConfig;
@Transactional
public interface PortConfigDao extends CrudRepository<PortConfig, Long>{

    /**
     * Description:
     * @param id
     * @return 
     * @see 
     */
    public List<PortConfig> findByServiceId (long id);
    
    public void deleteByServiceId(long ServiceId);
	
    @Query("select i.mapPort from PortConfig i")
	 public HashSet<Integer> findPortSets();

    public PortConfig findByMapPort(String portSet);
}
