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

    @Query("select i from Image i where i.createBy=?1 and i.isDelete != 1")
    public List<Image> findByCreateBy(long createBy);

    @Query("select i from Image i where i.createBy=?1 and i.isDelete != 1 order by i.name, i.createDate desc")
    public List<Image> findByCreateByOrderByName(long createBy);

	@Query("select i from Image i where i.createBy=?1 and i.isDelete != 1 order by i.createDate desc")
	public List<Image> findByCreateByOrderByCreatTime(long createBy);

	@Query("select i from Image i where i.id=?1 and i.isDelete != 1")
	public Image findById(long id);

	@Query("select i from Image i where (i.imageType = 1 or i.createBy = ?1) and i.isDelete != 1 order by i.createDate DESC ")
	public Page<Image> findByImageType(long userId,Pageable request);

	@Query("select i from Image i where  i.createBy = ?1 and i.isDelete != 1 order by  i.name,i.createDate")
	public Page<Image> findAllByCreateBy(long createBy,Pageable request);

	@Query("select i from Image i join i.favorUsers fu where fu.id= ?1 order by  i.name,i.createDate")
    public Page<Image> findAllFavor(long createBy,Pageable request);

	@Query("select i from Image i where (i.imageType = 1 or i.createBy = ?1) and i.isDelete != 1 order by  i.name,i.createDate")
	public List<Image> findAll(long createBy);

	@Query("select i from Image i where (i.imageType = 1 or i.createBy = ?2) and i.isDelete != 1 and i.name like ?1 order by  i.name,i.createDate")
	public Page<Image> findByNameCondition(String name,long userId,Pageable request);

	@Query("select i from Image i where (i.imageType = 1 or i.createBy = ?2) and i.isDelete != 1 and i.name like ?1 order by  i.name,i.createDate")
	public List<Image> findByNameCondition(String name,long userId);

	@Query("select i from Image i where (i.imageType = 1 or i.createBy = ?2) and i.isDelete != 1 and i.name like ?1 order by  i.exportCount desc")
	public List<Image> findByNameConditionOrderbyexportCount(String name,long userId);

	@Query("select i from Image i where (i.imageType = 1 or i.createBy = ?2) and i.isDelete != 1 and i.name like ?1 order by  i.createDate desc")
	public List<Image> findByNameConditionOrderbycreateDate(String name,long userId);

	@Query("select i from Image i where (i.imageType = 1 or i.createBy = ?1) and i.isDelete != 1 and i.name like ?2 order by  i.name,i.createDate")
	public List<Image> findByNameOf(long createBy,String name);

	@Query("select i from Image i where (i.imageType = 2 or i.createBy = ?1) and i.isDelete != 1 and i.name like ?2 order by  i.name,i.createDate")
	public Page<Image> findByNameOfUser(long createBy,String name,Pageable request);

	@Query("select count(u) from User u join u.favorImages fi where fi.id= ?1 and fi.isDelete != 1")
	public int findAllUserById(long imageId);

	@Query("select COUNT(ufi) from UserFavorImages ufi where ufi.favor_images =?1 and ufi.favor_users = ?2")
	public int findByUserIdAndImageId(long imageId,long userId);

	@Query("select i from Image i where  i.isBaseImage= 1 and (i.createBy = ?1 or i.imageType = 1) and i.isDelete != 1")
	public List<Image> findByBaseImage(long createBy);

	@Query("select i from Image i where  i.name = ?2 and i.isBaseImage= 1 and (i.createBy = ?1 or i.imageType = 1) and i.isDelete != 1")
	public List<Image> findByBaseImageVarsionOfName(long createBy,String name);

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

	@Query("select i from Image i where (i.imageType = 1 or i.createBy = ?1) and i.name = ?2 and i.isDelete != 1")
	public List<Image> findByImageVarsionOfName(long createBy,String name,Sort sort);

	@Query(" select i from Image i where "
	      + " i.id in (select ufi.favor_images from UserFavorImages ufi group by ufi.favor_images)"
	      + " and i.imageType = 1 and i.isDelete != 1")
	public List<Image> findAllUserFavor();

}
