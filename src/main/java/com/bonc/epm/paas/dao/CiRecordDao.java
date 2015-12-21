package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.CiRecord;

@Transactional
public interface CiRecordDao extends JpaRepository<CiRecord, Long> {
	public List<CiRecord> findByCiId(long ciId,Sort sort);
} 
