package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bonc.epm.paas.entity.EnvVariable;

public interface EnvVariableDao extends CrudRepository<EnvVariable, Long>{

    /**
     * Description:
     * @param id
     * @return 
     * @see 
     */
    List<EnvVariable> findByServiceId(long id);
	
}
