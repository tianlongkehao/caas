/*
 * 文件名：CommonOperationLogDao.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年12月26日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.CommonOperationLog;



/**
 * @author ke_wang
 * @version 2016年12月26日
 * @see CommonOperationLogDao
 * @since
 */
@Transactional
public interface CommonOperationLogDao extends CrudRepository<CommonOperationLog, Long>{

	//查询当前页数据
    @Query("select i from CommonOperationLog i  order by i.createDate desc")
	public Page<CommonOperationLog> findAlls(Pageable request);
    
	//条件查询当前页数据
    @Query("select i from CommonOperationLog i where  i.createUsername like ?1  order by i.createDate desc")
    public Page<CommonOperationLog> findAllByCreateUsername(String createUsername,Pageable request);
	
    public List<CommonOperationLog> findFourByCreateBy(long createBy,Pageable request);
    
}
