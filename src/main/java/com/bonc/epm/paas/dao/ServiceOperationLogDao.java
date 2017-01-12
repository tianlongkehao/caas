package com.bonc.epm.paas.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.CommonOperationLog;
import com.bonc.epm.paas.entity.Image;
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
    default ServiceOperationLog save(String serviceName, String serviceExtraInfo, Integer operationType){
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
    
    
    @Query("select i from ServiceOperationLog i  order by i.createDate desc")
	public Page<ServiceOperationLog> findAlls(Pageable request);
    
    @Query("select i from ServiceOperationLog i where  i.createUserName like ?1  order by i.createDate desc")
    public Page<ServiceOperationLog> findAllByCreateUserName(String createUserName,Pageable request);
    
 

    public List<ServiceOperationLog> findFourByCreateBy(long createBy,Pageable request);
}
