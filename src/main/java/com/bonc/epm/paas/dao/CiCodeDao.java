/*
 * 文件名：CICOdeDao.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2016年12月27日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bonc.epm.paas.entity.CiCode;

/**
 * 
 * CiCode数据层接口
 * @author zhoutao
 * @version 2016年12月27日
 * @see CiCodeDao
 * @since
 */
@Transactional
public interface CiCodeDao extends CrudRepository<CiCode, Long> {
    /**
     * Description: <br>
     * 根据构建id查询相关联的代码构建特殊信息
     * @param ciId
     * @return 
     * @see
     */
    CiCode findByCiId(long ciId);
    
    @Query("select ci from CiCode ci where ci.hookCodeId = "
          + "(select cck.id from CiCodeHook cck where cck.id = "
          + "(select hi.hookId from HookAndImages hi where hi.imageId = ?1))")
    CiCode fingByImageIdAndHookId(long imgId);
}
