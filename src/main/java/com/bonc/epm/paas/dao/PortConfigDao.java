package com.bonc.epm.paas.dao;

import java.util.HashSet;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.PortConfig;
/**
 * 映射端口Dao类
 * @author YuanPeng
 * @version 2016年9月5日
 * @see PortConfigDao
 * @since
 */
@Transactional
public interface PortConfigDao extends CrudRepository<PortConfig, Long>{
    /**
     * 根据id查找映射端口信息
     * @param id 
     * @return List<PortConfig>
     * @see
     */
    List<PortConfig> findByServiceId (long id);
    /**
     * 根据id删除映射端口信息
     * @param serviceId long
     * @see
     */
    void deleteByServiceId(long serviceId);
    /**
     * 查询所有的映射端口信息
     * @return HashSet
     * @see
     */
    @Query("select i.mapPort from PortConfig i") 
    HashSet<Integer> findPortSets();
    /**
     * 通过通过mapport查找记录
     * @param portSet String
     * @return PortConfig
     * @see
     */
    PortConfig findByMapPort(String portSet);
}
