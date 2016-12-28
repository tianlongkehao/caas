package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * Description: <br>
     * 根据分页信息查询数据
     * @param createBy ：创建者
     * @param request ：分页
     * @return page
     */
    @Query("select i from Ci i where  i.createBy = ?1 and i.type in (2,3) order by i.createDate desc")
    Page<Ci> findByCreateByQuickCi(long createBy,Pageable request);
	
    /**
     * Description: <br>
     * 根据项目名称搜索构建数据
     * @param createBy 创建者
     * @param projectName ：搜索名称
     * @param request ： 分页信息
     * @return page
     */
    @Query("select i from Ci i where  i.createBy = ?1 and i.type in (2,3) and i.projectName like ?2 order by i.createDate desc")
    Page<Ci> findByNameOfQuickCi(long createBy,String projectName,Pageable request);
    
    /**
     * Description: <br>
     * 根据分页信息查询数据
     * @param createBy ：创建者
     * @param request ：分页
     * @return page
     */
    @Query("select i from Ci i where  i.createBy = ?1 and i.type = 1 order by i.createDate desc")
    Page<Ci> findByCreateByCodeCi(long createBy,Pageable request);
    
    /**
     * Description: <br>
     * 根据项目名称搜索构建数据
     * @param createBy 创建者
     * @param projectName ：搜索名称
     * @param request ： 分页信息
     * @return page
     */
    @Query("select i from Ci i where  i.createBy = ?1 and i.type = 1 and i.projectName like ?2 order by i.createDate desc")
    Page<Ci> findByNameOfCodeCi(long createBy,String projectName,Pageable request);
    
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
    /**
     * Description: <br>
     * 代码构建项目名称查重
     * @param projectName:项目名称
     * @param createBy ：创建id
     * @return list
     */
    List<Ci> findByProjectNameAndCreateBy(String projectName,long createBy);
    
    /**
     * Description: <br>
     * 根据镜像Id，查询关联的hookid，在查询ci数据
     * @param imgId :镜像Id
     * @return ci
     * @see
     */
//    @Query("select ci from Ci ci where ci.hookCodeId = "
//        + "(select cck.id from CiCodeHook cck where cck.id = "
//        + "(select hi.hookId from HookAndImages hi where hi.imageId = ?1))")
//    Ci fingByImageIdAndHookId(long imgId);
} 
