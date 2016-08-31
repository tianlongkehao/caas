package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.CiRecord;

/**
 * 构建记录信息收集接口
 * 
 * @author update
 * @version 2016年8月31日
 * @see CiRecordDao
 * @since
 */
@Transactional
public interface CiRecordDao extends JpaRepository<CiRecord, Long> {
    
    /**
     * 根据构建Id和sort排序规则进行查询
     * 
     * @param ciId ： 构建Id
     * @param sort ： 查询数据的排序规则
     * @return list
     * @see
     */
    List<CiRecord> findByCiId(long ciId,Sort sort);
} 
