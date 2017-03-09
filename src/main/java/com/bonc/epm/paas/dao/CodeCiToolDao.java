package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.CodeCiTool;

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

    @Query("select i from CodeCiTool i where i.toolGroup <> 'image' order by i.toolGroup asc")
    List<CodeCiTool> findAllTools();

    @Query("select i from CodeCiTool i where i.toolGroup = 'image' order by i.toolGroup asc")
    List<CodeCiTool> findAllImages();
}
