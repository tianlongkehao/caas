package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
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
    public List<EnvVariable> findByServiceId(long id);
    
    public List<EnvVariable> findByCreateBy(long id);
    
    public void deleteByServiceId(long serviceId);
    
    @Query("select distinct env.templateName from EnvVariable env where env.createBy = ?1")
    public List<String> findTemplateName(long createBy);
    
    public List<EnvVariable> findByCreateByAndTemplateName(long createBy,String templateName);
	
}
