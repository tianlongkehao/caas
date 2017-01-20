/*
 * 文件名：UserVisitingLogDao.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2016年12月30日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.VisitThirdInterfaceLog;
@Transactional
public interface VisitThirdInterfaceLogDao extends CrudRepository<VisitThirdInterfaceLog, Long> {

}
