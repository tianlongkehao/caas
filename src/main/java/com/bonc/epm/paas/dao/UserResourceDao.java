/*
 * 文件名：UserResourceDao.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2017年1月4日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.dao;

import org.springframework.data.repository.CrudRepository;

import com.bonc.epm.paas.entity.UserResource;

public interface UserResourceDao extends CrudRepository<UserResource, Long>{
    
    UserResource findByUserId(long userId);
    
}
