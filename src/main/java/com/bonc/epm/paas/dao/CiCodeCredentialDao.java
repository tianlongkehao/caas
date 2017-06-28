/*
 * 文件名：CiCodeCredentialDao.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2016年11月24日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bonc.epm.paas.entity.CiCodeCredential;
@Transactional
public interface CiCodeCredentialDao extends CrudRepository<CiCodeCredential,Long> {

    /**
     * Description: <br>
     * 根据创建者查询所有的数据
     * @param createBy 创建者
     * @return  list
     */
    List<CiCodeCredential> findByCreateBy(long createBy);

    /**
     * Description: <br>
     * 根据创建者和代码仓库类型查询数据
     * @param createBy 创建者
     * @param codeType 代码仓库类型
     * @return list
     */
    List<CiCodeCredential> findByCreateByAndCodeType(long createBy,int codeType);


    @Query("select c from CiCodeCredential c where c.Type = 2 and c.userName = ?1")
    List<CiCodeCredential> findRepeat(String userName);
}
