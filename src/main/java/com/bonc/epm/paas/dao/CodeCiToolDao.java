package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.CodeCiTool;
import com.bonc.epm.paas.entity.Service;

/**
 *
 * toolDao代码构建工具接口
 * @author lkx
 * @version 2017年2月8日 14:39:43
 * @see CodeCiToolDao
 * @since
 */
@Transactional
public interface CodeCiToolDao extends CrudRepository<CodeCiTool, Long> {

    @Query("select i from CodeCiTool i order by i.toolGroup asc")
    List<CodeCiTool> findAll();
}
