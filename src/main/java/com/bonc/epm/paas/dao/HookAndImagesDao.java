/*
 * 文件名：HookAndImagesDao.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2016年12月2日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.dao;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.bonc.epm.paas.entity.HookAndImages;

@Transactional
public interface HookAndImagesDao extends CrudRepository<HookAndImages, Long> {
    /**
     * Description: <br>
     * 根据hookid删除中间表数据
     * @param hookId  hookId
     * @see
     */
    void deleteByHookId(long hookId);
    void deleteByImageId(long imageId);
}
