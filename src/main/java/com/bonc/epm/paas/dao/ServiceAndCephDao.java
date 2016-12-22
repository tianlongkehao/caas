/*
 * 文件名：ServiceAndCephDao.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2016年12月20日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bonc.epm.paas.entity.ServiceAndCeph;

public interface ServiceAndCephDao extends CrudRepository<ServiceAndCeph,Long>{
    
    void deleteByServiceId(long serviceId);
    
    List<ServiceAndCeph> findByServiceId(long serviceId);
}
