/*
 * 文件名：CephAddressDao.java
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

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bonc.epm.paas.entity.CephAddress;

public interface CephAddressDao extends CrudRepository<CephAddress,Long> {
    /**
     * 
     * 根据服务Id，查询中间表cephid，再查询所需要的ceph数据
     * @param serviceId 服务id
     * @return list
     * @see
     */
    @Query("select ceph from CephAddress ceph where ceph.id in (select sac.cephId from ServiceAndCeph sac where sac.serviceId = ?1)")
    List<CephAddress> findByServiceId(long serviceId);
}
