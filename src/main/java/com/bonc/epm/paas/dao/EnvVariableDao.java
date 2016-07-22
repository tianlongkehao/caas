package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.EnvVariable;
@Transactional
public interface EnvVariableDao extends CrudRepository<EnvVariable, Long>{

    /**
     * Description:
     * @param id
     * @return 
     * @see 
     */
    List<EnvVariable> findByServiceId(long id);
    
    List<EnvVariable> findByCreateBy(long id);
    
    public void deleteByServiceId(long serviceId);
	
}
