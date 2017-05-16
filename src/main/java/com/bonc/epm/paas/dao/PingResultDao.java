package com.bonc.epm.paas.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.PingResult;

/**
 * ClassName: PingResultDao <br/>
 * date: 2017年5月16日 下午4:32:35 <br/>
 *
 * @author longkaixiang
 * @version
 */
@Transactional
public interface PingResultDao extends CrudRepository<PingResult, Long> {
	@Query("select i from PingResult i where i.host = ?1 order by createDate desc")
	Iterable<PingResult> findByHost(String host);

	void deleteByHost(String host);
}
