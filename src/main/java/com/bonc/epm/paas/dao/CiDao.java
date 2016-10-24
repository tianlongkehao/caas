package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.Ci;

/**
 * 
 * ciDao构建接口
 * @author update
 * @version 2016年8月31日
 * @see CiDao
 * @since
 */
@Transactional
public interface CiDao extends CrudRepository<Ci, Long> {
	
    /**
     * 根据创建id查询构建数据
     * 
     * @param createBy ： 创建Id
     * @return list
     * @see
     */
    List<Ci> findByCreateBy(Long createBy);
	
    /**
     * 根据创建者Id和sort排序查询构建数据
     * 
     * @param createBy ： 创建者
     * @param sort ： sort排序
     * @return list
     * @see
     */
    List<Ci> findByCreateBy(Long createBy,Sort sort);
	
    /**
     * 根据构建镜像Id查询构建数据
     * 
     * @param imgID ： 镜像Id
     * @return ci
     * @see 
     */
    Ci findByImgId(Long imgID);

    /**
     * Description:
     * @param imgNameFirst
     * @param imgNameLast
     * @param imgNameVersion
     * @return 
     * @see 
     */
    @Query("select ci from Ci ci where ci.imgNameFirst =?1 and ci.imgNameLast = ?2 and ci.imgNameVersion = ?3")
    List<Ci> findByImgNameFirstAndImgNameLastAndImgNameVersion(String imgNameFirst, String imgNameLast,
                                                         String imgNameVersion);
} 
