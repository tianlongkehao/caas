package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.Storage;
/**
 * 卷组的DAO类
 * @author YuanPeng
 * @version 2016年9月5日
 * @see StorageDao
 * @since
 */
@Transactional
public interface StorageDao extends CrudRepository<Storage, Long> {

    /**
     * 根据创建者查询卷组
     * @param createBy 创建者
     * @return List 
     * @see
     */
    List<Storage> findByCreateBy(long createBy);


	/**
	 * 
	 * 根据创建者和卷组的名字查找卷组信息
	 * @param createBy
	 * @param storageName
	 * @return Storage
	 * @see
	 */
    Storage findByCreateByAndStorageName(long createBy, String storageName);
    
    /**
     * 
     * Description: <br>
     * 查询当前租户未使用的存储卷
     * @param createBy
     * @param useType
     * @return 
     * @see
     */
    List<Storage> findByCreateByAndUseTypeOrderByCreateDateDesc(long createBy,Integer useType);

	/**
	 * 
	 * 根据创建者使用状态查找卷组并根据创建日期排序
	 * @param createBy
	 * @param pageable
	 * @param useType
	 * @return List<Storage>
	 * @see
	 */
    List<Storage> findAllByCreateByOrderByCreateDateDesc(long createBy, Pageable pageable);

	/**
	 * 
	 * 查询创建者创建的数量
	 * @param createBy 创建者
	 * @return long
	 * @see
	 */
    long countByCreateBy(long createBy);

	/**
	 * 根据卷组名查询记录<有逻辑漏洞，以后要重构>
	 * @param storageName 卷组名
	 * @return String
	 * @see
	 */
    @Query("select s.mountPoint from Storage s where 1=1 and s.storageName = ?1 ")
	String findByVolume(String storageName);

}
