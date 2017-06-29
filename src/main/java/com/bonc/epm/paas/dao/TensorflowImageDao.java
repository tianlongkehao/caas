package com.bonc.epm.paas.dao;

import org.springframework.data.repository.CrudRepository;

import com.bonc.epm.paas.entity.TensorflowImage;

public interface TensorflowImageDao extends CrudRepository<TensorflowImage, Long> {

}
