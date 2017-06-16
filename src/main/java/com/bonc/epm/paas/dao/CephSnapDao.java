package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.ceph.CephSnap;

@Transactional
public interface CephSnapDao extends CrudRepository<CephSnap, Long> {

	public List<CephSnap> findByImgname(String imgname);

	@Query("select i from CephSnap i where i.pool = ?1 order by createDate desc")
	public List<CephSnap> findByPoolDesc(String pool);

	@Query("select i from CephSnap i where i.creator = ?1 order by createDate desc")
	public List<CephSnap> findByCreatorDesc(long creator);

	@Query("select i from CephSnap i where i.imgId = ?1 order by createDate desc")
	public List<CephSnap> findByImgIdDesc(long imgId);

	public void deleteByName(String name);

	public void deleteByImgname(String imgname);

	public void deleteByNameAndImgname(String name, String imgname);

	public void deleteByPool(String pool);
}
