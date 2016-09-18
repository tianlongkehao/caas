/*
 * 文件名：EnvTemplateDao.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2016年8月18日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.EnvTemplate;
@Transactional
public interface EnvTemplateDao extends CrudRepository<EnvTemplate, Long>{
	
    public List<EnvTemplate> findByCreateBy(long id);
    
    @Query("select distinct env.templateName from EnvTemplate env where env.createBy = ?1")
    public List<String> findTemplateName(long createBy);
    
    public List<EnvTemplate> findByCreateByAndTemplateName(long createBy,String templateName);
    
}
