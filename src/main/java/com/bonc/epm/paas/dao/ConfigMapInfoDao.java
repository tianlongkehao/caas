package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.ConfigMapInfo;

@Transactional
public interface ConfigMapInfoDao extends CrudRepository<ConfigMapInfo, Long> {

	public List<ConfigMapInfo> findByServiceId(long id);

	public List<ConfigMapInfo> findByCreateBy(long id);

    public void deleteByServiceId(long serviceId);

    @Query("delete from ConfigMapInfo  where name = ?1")
    public void deleteByConfigMapName(String configMapName);

    @Query("select * from ConfigMapInfo where name = ?1")
    public List<ConfigMapInfo> findByConfigMapName(String configMapName);

}
