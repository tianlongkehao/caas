package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.ServiceConfigmap;

@Transactional
public interface ServiceConfigmapDao extends CrudRepository<ServiceConfigmap, Long> {

	public List<ServiceConfigmap> findByServiceId(long serviceId);

	public List<ServiceConfigmap> findByCreateBy(long createBy);

	public List<ServiceConfigmap> findByName(String name);

	public List<ServiceConfigmap> findByConfigmapId(long configmapId);

    public void deleteByServiceId(long serviceId);

    public void deleteByCreateBy(long createBy);

    public void deleteByName(String name);

    public void deleteByConfigmapId(long configmapId);

}
