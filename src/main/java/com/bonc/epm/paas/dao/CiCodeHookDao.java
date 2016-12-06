/*
 * 文件名：CiCodeHookDao.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2016年12月1日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.dao;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.bonc.epm.paas.entity.CiCodeHook;
@Transactional
public interface CiCodeHookDao extends CrudRepository<CiCodeHook,Long>{


}
