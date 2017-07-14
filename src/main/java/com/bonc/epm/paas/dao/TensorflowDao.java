package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.Tensorflow;

@Transactional
public interface TensorflowDao extends CrudRepository<Tensorflow,Long>{

	List<Tensorflow> findByCreateBy(long createBy);

	List<Tensorflow> findByNamespace(String namespace);

	Tensorflow findByNamespaceAndName(String namespace,String name);

	Tensorflow findByRbdId(long rbdId);

	void deleteByCreateBy(long createBy);

	void deleteByNamespace(String namespace);

}
