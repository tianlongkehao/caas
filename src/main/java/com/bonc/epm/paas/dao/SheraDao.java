/*
 * 文件名：SheraDao.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2016年12月8日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bonc.epm.paas.entity.Shera;
/**
 * shera数据层的接口
 * @author zhoutao
 * @version 2016年12月8日
 * @see SheraDao
 * @since
 */
public interface SheraDao extends CrudRepository<Shera, Long> {
    
    /**
     * Description: <br>
     * 根据userid查询相关联的shera数据
     * @param userId id
     * @return shera
     */
    @Query("select sh from Shera sh where sh.id = "
        + "(select uas.sheraId from UserAndShera uas where uas.userId = ?1 )")
    Shera findByUserId(long userId);
}
