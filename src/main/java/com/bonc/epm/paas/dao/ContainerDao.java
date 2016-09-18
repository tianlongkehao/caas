package com.bonc.epm.paas.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.Container;
/**
 * ContainerDao类
 * @author unknow
 * @version 2016年9月18日
 * @see ContainerDao
 * @since
 */
@Transactional
public interface ContainerDao extends CrudRepository<Container, Long> {

}
