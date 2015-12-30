package com.bonc.epm.paas.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.UserFavorImages;
@Transactional
public interface FavorDao extends CrudRepository<UserFavorImages, Long>{
	
	@Query("select ufi from UserFavorImages ufi where ufi.favor_images = ?1 and ufi.favor_users = ?2")
	public UserFavorImages findByImageIdAndUserId(long imageId,long userId);
}
