package com.bonc.epm.paas.dao;

import java.util.List;

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
	
}
