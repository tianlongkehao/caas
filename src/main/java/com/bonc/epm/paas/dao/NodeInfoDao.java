package com.bonc.epm.paas.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.cluster.entity.NodeTestInfo;

@Transactional
public interface NodeInfoDao extends CrudRepository<NodeTestInfo, Long>{

	public NodeTestInfo findById(long id);

	public NodeTestInfo findByNodename(String nodename);

    public void deleteById(long id);

    public void deleteByNodename(String nodename);

}
