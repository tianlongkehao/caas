package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.DNSService;

/**
 * ClassName: DNSServiceDao <br/>
 * date: 2017年5月16日 上午9:16:21 <br/>
 *
 * @author longkaixiang
 * @version
 */
@Transactional
public interface DNSServiceDao extends CrudRepository<DNSService, Long> {

	@Query("select i from DNSService i where i.address = ?1")
	List<DNSService> findByAddress(String address);

	List<DNSService> findByIsMonitor(Integer isMonitor);
}
