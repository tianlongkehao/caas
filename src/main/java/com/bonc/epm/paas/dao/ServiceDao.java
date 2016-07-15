package com.bonc.epm.paas.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.Service;

@Transactional
public interface ServiceDao extends CrudRepository<Service, Long>{
	
	public List<Service> findByContainerID(long containerID);
	
	public List<Service> findByCreateBy(long createBy);
	
	public List<Service> findByCreateByAndVolName (long createBy , String volName);
	
	@Query("select i from Service i where  i.createBy = ?1 and i.serviceName like ?2 order by  i.serviceName,i.createDate")
	public List<Service> findByNameOf(long createBy,String serviceName);
	
	@Query("select i.portSet from Service i")
	public HashSet<Integer> findPortSets();
}
