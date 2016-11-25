/*
 * 文件名：CiInvokeDao.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2016年11月11日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.CiInvoke;

/**
 * ciInvoke数据接口
 * 
 * @author zhoutao
 * @version 2016年11月11日
 * @see CiInvokeDao
 * @since
 */
@Transactional
public interface CiInvokeDao extends CrudRepository<CiInvoke, Long>{
    
    /**
     * Description: <br>
     * 根据ciId查询ciInvoke中的构建信息
     * @param ciId
     * @return 
     * @see
     */
    List<CiInvoke> findByCiId(long ciId);
    
    /**
     * 
     * Description: <br>
     * 根据ciId删除ciInvoke中的构建信息
     * @param ciId 
     * @see
     */
    void deleteByCiId(long ciId);

}
