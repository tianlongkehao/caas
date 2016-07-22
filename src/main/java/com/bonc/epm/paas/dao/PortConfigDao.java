package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bonc.epm.paas.entity.EnvVariable;
import com.bonc.epm.paas.entity.PortConfig;

public interface PortConfigDao extends CrudRepository<PortConfig, Long>{

    /**
     * Description:
     * @param id
     * @return 
     * @see 
     */
    List<PortConfig> findByServiceId(long id);
	
}
