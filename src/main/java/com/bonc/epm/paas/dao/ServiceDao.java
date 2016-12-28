package com.bonc.epm.paas.dao;

import java.util.HashSet;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.CiCodeHook;
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
     * @param createBy 
     * @return List<Service>
     * @see
     */
    List<Service> findByCreateBy(long createBy);
    /**
     * 
     * @param createBy 
     * @param serviceName 
     * @return List<Service>
     * @see
     */
    @Query("select i from Service i where  i.createBy = ?1 and i.serviceName like ?2 order by i.createDate")
    Page<Service> findByNameOf(long createBy,String serviceName,Pageable request);
    
    @Query("select i from Service i where  i.createBy = ?1 and i.serviceName = ?2 order by  i.serviceName,i.createDate")
    List<Service> findByNameOf(long createBy,String serviceName);
    
    /**
     * Description: <br>
     * 根据服务名称和创建者查询服务
     * @param createBy
     * @param serviceName
     * @return 
     * @see
     */
    @Query("select i from Service i where  i.createBy = ?1 and i.proxyPath like ?2")
    List<Service> findByCreateByAndProxyPath (long createBy,String proxyPath);
  
    /**
     * Description: <br>
     * 分页查询数据
     * @param createBy：创建者
     * @param request：分页数据
     * @return Page
     */
    @Query("select i from Service i where  i.createBy = ?1 order by i.createDate desc")
    Page<Service> findByCreateBy(long createBy,Pageable request);
    
        /**
         * 
         * 查找相同服务地址的数据
         * @param serviceAddr
         * @return list
         * @see
         */
     List<Service> findByServiceAddrAndProxyPath (String serviceAddr,String proxyPath);
     
     /**
      * Description: <br>
      * 根据镜像Id查询ciCodeHook数据
      * @param imgId 镜像Id
      * @return 
      * @see
      */
     @Query("select cck from CiCodeHook cck where cck.id = (select hi.hookId from HookAndImages hi where hi.imageId = ?1)")
     CiCodeHook findByImgId(long imgId);
}
