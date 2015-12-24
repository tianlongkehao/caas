package com.bonc.epm.paas.dao;

import com.bonc.epm.paas.entity.Host;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface HostDao extends CrudRepository<Host, Long>{

}
