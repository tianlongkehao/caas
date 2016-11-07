package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.Image;

@Transactional
public interface ImageDao extends CrudRepository<Image, Long>{
    @Query("select i from Image i where i.name=?1 and i.isDelete != 1")
	public List<Image> findByName(String name);
	
	@Query("select i from Image i where i.creator=?1 and i.isDelete != 1")
	public List<Image> findByCreator(long creator);
	
	@Query("select i from Image i where i.id=?1 and i.isDelete != 1")
	public Image findById(long id);
	
	@Query("select i from Image i where (i.imageType = 1 or i.creator = ?1) and i.isDelete != 1 order by  i.name,i.createTime ")
	public Page<Image> findByImageType(long userId,Pageable request);
	
	@Query("select i from Image i where  i.creator = ?1 and i.isDelete != 1 order by  i.name,i.createTime")
	public Page<Image> findAllByCreator(long creator,Pageable request);
	
	@Query("select i from Image i join i.favorUsers fu where fu.id= ?1 order by  i.name,i.createTime")
    public Page<Image> findAllFavor(long creator,Pageable request);
	
	@Query("select i from Image i where (i.imageType = 1 or i.creator = ?1) and i.isDelete != 1 order by  i.name,i.createTime")
	public List<Image> findAll(long creator);
	
	@Query("select i from Image i where (i.imageType = 1 or i.creator = ?2) and i.isDelete != 1 and i.name like ?1 order by  i.name,i.createTime")
	public Page<Image> findByNameCondition(String name,long userId,Pageable request);
	
	@Query("select i from Image i where (i.imageType = 1 or i.creator = ?1) and i.isDelete != 1 and i.name like ?2 order by  i.name,i.createTime")
	public List<Image> findByNameOf(long creator,String name);
	
	@Query("select i from Image i where (i.imageType = 2 or i.creator = ?1) and i.isDelete != 1 and i.name like ?2 order by  i.name,i.createTime")
	public Page<Image> findByNameOfUser(long creator,String name,Pageable request);
	
	@Query("select count(u) from User u join u.favorImages fi where fi.id= ?1 and fi.isDelete != 1")
	public int findAllUserById(long imageId);
	
	@Query("select COUNT(ufi) from UserFavorImages ufi where ufi.favor_images =?1 and ufi.favor_users = ?2")
	public int findByUserIdAndImageId(long imageId,long userId);
	
	@Query("select i from Image i where  i.isBaseImage= 1 and (i.creator = ?1 or i.imageType = 1) and i.isDelete != 1")
	public List<Image> findByBaseImage(long creator);
	
	@Query("select i from Image i where  i.name = ?2 and i.isBaseImage= 1 and (i.creator = ?1 or i.imageType = 1) and i.isDelete != 1")
	public List<Image> findByBaseImageVarsionOfName(long creator,String name);
    
	/**
     * Description:
     * 
     * @param name
     * @param version
     * @return 
     * @see 
     */
	@Query("select i from Image i where i.name = ?1 and i.version = ?2 and i.isDelete != 1")
    public Image findByNameAndVersion(String name, String version);
	
	@Query("select i from Image i where (i.imageType = 1 or i.creator = ?1) and i.name = ?2 and i.isDelete != 1")
	public List<Image> findByImageVarsionOfName(long creator,String name,Sort sort);
	
}
