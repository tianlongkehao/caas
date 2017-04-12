package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.ConfigmapData;

@Transactional
public interface ConfigmapDataDao extends CrudRepository<ConfigmapData, Long> {

	public List<ConfigmapData> findByConfigmapId(long configmapId);

	public List<ConfigmapData> findByNamespace(String namespace);

    public void deleteByConfigmapId(long configmapId);

    public void deleteByNamespace(String namespace);
}
