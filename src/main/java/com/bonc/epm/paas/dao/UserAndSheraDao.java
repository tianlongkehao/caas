/*
 * 文件名：UserAndSheraDao.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2016年12月8日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.dao;

import org.springframework.data.repository.CrudRepository;

import com.bonc.epm.paas.entity.UserAndShera;
/**
 * 
 * 租户和shera关联表接口
 * @author zhoutao
 * @version 2016年12月8日
 * @see UserAndSheraDao
 * @since
 */
public interface UserAndSheraDao extends CrudRepository<UserAndShera, Long> {
    /**
     * Description: <br>
     * 根据sheraId删除关联变中的数据
     * @param sheraId sheraId
     * @see
     */
    void deleteBySheraId(long sheraId);
}
