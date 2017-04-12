package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.Configmap;

@Transactional
public interface ConfigmapDao extends CrudRepository<Configmap, Long> {

	public List<Configmap> findById(long id);

	public List<Configmap> findByCreateBy(long createBy);

	public List<Configmap> findByName(String name);

	public List<Configmap> findByNamespace(String namespace);

	public List<Configmap> findByNamespaceAndName(String namespace,String name);

    public void deleteById(long id);

    public void deleteByCreateBy(long createBy);

    public void deleteByName(String name);

    public void deleteByNamespace(String namespace);

}
