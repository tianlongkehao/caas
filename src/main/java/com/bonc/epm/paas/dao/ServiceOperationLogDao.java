package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.ServiceOperationLog;
/**
 * 服务操作记录信息DAO类
 * @author lkx
 * @version 2016年12月18日26
 * @see ServiceOperationLogDao
 * @since
 */
@Transactional
public interface ServiceOperationLogDao extends CrudRepository<ServiceOperationLog, Long>{
	
	/**
	 * 
	 * @param userId 
	 * @return  List<ServiceOperationLog>
	 * @see
	 */
    List<ServiceOperationLog> findByUserId(long userId);
    
    /**
     * 
     * @param serviceId 
     * @return List<ServiceOperationLog>
     * @see
     */
    List<ServiceOperationLog> findByServiceId(long serviceId);
    
}
