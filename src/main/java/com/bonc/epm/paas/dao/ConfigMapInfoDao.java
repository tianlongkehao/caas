package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.ConfigMapInfo;

@Transactional
public interface ConfigMapInfoDao extends CrudRepository<ConfigMapInfo, Long> {

	public List<ConfigMapInfo> findByServiceId(long serviceId);

	public List<ConfigMapInfo> findByCreateBy(long createBy);

    public void deleteByServiceId(long serviceId);

    @Query("delete from ConfigMapInfo  where name = ?1")
    public void deleteByName(String name);

    public List<ConfigMapInfo> findByName(String name);

}
