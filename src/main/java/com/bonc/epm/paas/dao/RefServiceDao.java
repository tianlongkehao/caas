/*
 * 文件名：RefServiceDao.java
 * 描述：
 * 修改人：YuanPeng
 * 修改时间：2016年9月12日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.dao;

import java.util.HashSet;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.RefService;

/**
 * 引用外部服务DAO类
 * @author YuanPeng
 * @version 2016年9月12日
 * @see RefServiceDao
 * @since
 */
@Transactional
public interface RefServiceDao extends CrudRepository<RefService, Long>{

    /**
     * 
     * Description: 查询出用户自己创建的且有权查看的数据
     * @param byCreate 创建者
     * @param viDomain 可见域
     * @return List<RefService>
     * @see
     */
    List<RefService> findByCreateByOrViDomainOrderByCreateDateDesc(long byCreate,int viDomain);
    /**
     * 根据创建者和服务名查询服务
     * @param byCreate
     * @param name
     * @return List<RefService>
     * @see
     */
    List<RefService> findByCreateByAndSerName(long byCreate,String name);
    /**
     * 
     * 根据服务名查询服务
     * @param name
     * @return List<RefService>
     * @see
     */
    List<RefService> findBySerName(String name);
    
    /**
     * 查询所有的映射端口信息
     * @return HashSet
     * @see
     */
    @Query("select rs.nodePort from RefService rs") 
    HashSet<Integer> findPortSets();
    
    /**
     * Description: <br>
     * 根据创建者id查询当前租户的服务
     * @param userId 用户Id
     * @return list
     * @see
     */
    List<RefService> findByCreateBy(long userId);
}
