package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.SonarConfig;

/**
 *
 * SonarConfigDao接口
 *
 * @author lkx
 * @version 2017年2月21日 17:02:51
 * @see SonarConfigDao
 * @since
 */
@Transactional
public interface SonarConfigDao extends CrudRepository<SonarConfig, Long> {

	/**
	 * 根据创建id查询构建数据
	 *
	 * @param createBy ： 创建Id
	 * @return List<SonarConfig>
	 * @see
	 */
	List<SonarConfig> findByCreateBy(Long createBy);

}
