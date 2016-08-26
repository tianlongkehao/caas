/*
 * 文件名：DockerFileTemplateDao.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2016年8月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.DockerFileTemplate;

/**
 * dokerFile模板的Dao接口
 * 
 * @author zhoutao
 * @version 2016年8月10日
 * @see DockerFileTemplateDao
 * @since
 */
@Transactional
public interface DockerFileTemplateDao extends CrudRepository<DockerFileTemplate, Long>{
    
    public List<DockerFileTemplate> findByCreateBy(long createBy);
    
}
