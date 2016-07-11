package com.bonc.epm.paas.dao;

import org.springframework.data.repository.CrudRepository;

import com.bonc.epm.paas.entity.EnvVariable;

public interface EnvVariableDao extends CrudRepository<EnvVariable, Long>{
	
}
