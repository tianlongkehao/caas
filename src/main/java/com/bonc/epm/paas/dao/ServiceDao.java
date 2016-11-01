package com.bonc.epm.paas.dao;

import java.util.HashSet;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.Image;
import com.bonc.epm.paas.entity.Service;
/**
 * 服务DAO类
 * @author unknow
 * @version 2016年9月18日
 * @see ServiceDao
 * @since
 */
@Transactional
public interface ServiceDao extends CrudRepository<Service, Long>{
	/**
	 * 
	 * @param containerID 
	 * @return  List<Service>
	 * @see
	 */
    List<Service> findByContainerID(long containerID);
    /**
     * 
     * @param createBy 
     * @return List<Service>
     * @see
     */
    List<Service> findByCreateBy(long createBy);
    /**
     * 
     * @param createBy 
     * @param volName 
     * @return List<Service>
     * @see
     */
    List<Service> findByCreateByAndVolName (long createBy , String volName);
    /**
     * 
     * @param createBy 
     * @param serviceName 
     * @return List<Service>
     * @see
     */
    @Query("select i from Service i where  i.createBy = ?1 and i.serviceName like ?2 order by i.createDate")
    Page<Service> findByNameOf(long createBy,String serviceName,Pageable request);
    /**
     * 
     * @return HashSet<Integer>
     * @see
     */
    @Query("select i.portSet from Service i")
    HashSet<Integer> findPortSets();
    
    /**
     * Description: <br>
     * 分页查询数据
     * @param createBy：创建者
     * @param request：分页数据
     * @return Page
     */
    @Query("select i from Service i where  i.createBy = ?1 order by i.createDate desc")
    Page<Service> findByCreateBy(long createBy,Pageable request);
    
}
