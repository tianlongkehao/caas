package com.bonc.epm.paas.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.bonc.epm.paas.entity.Redis;

/**
 * ClassName: RedisDao <br/>
 * date: 2017年6月23日 下午3:17:50 <br/>
 *
 * @author longkaixiang
 * @version
 */
@Transactional
public interface RedisDao extends CrudRepository<Redis, Long> {
	List<Redis> findByName(String name);
}
