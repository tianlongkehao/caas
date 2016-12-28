package com.bonc.epm.paas.dao;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.ServiceOperationLog;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.util.CurrentUserUtils;
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
     * @param String serviceName
     * @param String serviceExtraInfo
     * @param long operationType
     * @return ServiceOperationLog
     * @see
     */
    default ServiceOperationLog save(String serviceName, String serviceExtraInfo, long operationType){
    	User currentUser = CurrentUserUtils.getInstance().getUser();
		ServiceOperationLog log = new ServiceOperationLog();
		log.setServiceName(serviceName);
		log.setServiceExtraInfo(serviceExtraInfo);
		log.setOperationType(operationType);
		log.setCreateDate(new Date());
		log.setCreateBy(currentUser.getId());
		log.setCreateUserName(currentUser.getUserName());
		log = save(log);
    	return log;
    }
}
