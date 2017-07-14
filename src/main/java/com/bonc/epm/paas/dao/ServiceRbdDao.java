package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.ceph.ServiceCephRbd;

@Transactional
public interface ServiceRbdDao extends CrudRepository<ServiceCephRbd, Long>{

	public List<ServiceCephRbd> findByCephrbdId(long cephrbdId);

	public List<ServiceCephRbd> findByServiceId(long serviceId);

	public void deleteByServiceId(long serviceId);

	public void deleteByCephrbdId(long cephrbdId);

}
