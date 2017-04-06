package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.Configmap;
import com.bonc.epm.paas.entity.ServiceConfigmap;

@Transactional
public interface ConfigmapDap extends CrudRepository<Configmap, Long> {

	public List<ServiceConfigmap> findById(long id);

	public List<ServiceConfigmap> findByCreateBy(long createBy);

	public List<ServiceConfigmap> findByName(String name);

	public List<ServiceConfigmap> findByNamespace(String namespace);

    public void deleteById(long id);

    public void deleteByCreateBy(long createBy);

    public void deleteByName(String name);

    public void deleteByNamespace(String namespace);

}
