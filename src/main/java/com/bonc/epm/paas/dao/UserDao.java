package com.bonc.epm.paas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.epm.paas.entity.Image;
import com.bonc.epm.paas.entity.User;

@Transactional
public interface UserDao extends CrudRepository<User, Long> {
	
	public User findByUserName(String userName);
	
	public User findById(long id);
	
	@Query("select i from Image i join i.favorUsers fu where fu.id= ?1 order by  i.name,i.createTime")
	public List<Image> findAllFavor(long creator);
	
	@Query("select i from Image i join i.favorUsers fu where fu.id= ?1 and i.name like ?2 order by  i.name,i.createTime")
	public List<Image> findByNameCondition(long creator,String imageName);
	
	@Query("select u from User u "
			+ "where 1=1 "
			+ "and u.company like %?1% "
			+ "and u.user_department like %?2% "
			+ "and u.user_autority= ?3 "
			+ "and u.user_realname like %?4% "
			+ "and u.user_province like %?5% ")
	public List<User> findBy4(String company, String user_department, String user_autority, String user_realname, String user_province);
	
	@Query("select u from User u "
			+ "where 1=1 "
			+ "and u.company like %?1% "
			+ "and u.user_department like %?2% "
			+ "and u.user_realname like %?3% "
			+ "and u.user_province like %?4% ")
	public List<User> findBy3(String company, String user_department, String user_realname,  String user_province);
	
	@Query("select u.userName from User u where u.userName = ?1")
	public List<String> checkUsername(String userName);

	@Query("select u from User u"
			+ " where 1=1 "
			+ " and u.user_autority= ?1"
			+ " and u.user_province = ?2")
	public List<User> checkUsermanage(String user_autority, String user_province);

	@Query("select u from User u "
			+ "where 1=1 "
			+ "and u.user_autority in(1,2)"
			+ "and u.parent_id = ?1")
	public List<User> checkUser(Long parent_id);

	@Query("select u from User u"
			+ " where 1=1 "
			+ " and u.user_autority in(3,4)"
			+ " and u.user_province = ?1")
	public List<User> checkUsermanage34(String user_province);

	@Query("select u from User u "
			+ "where 1=1 "
			+ "and u.user_autority in(1,2)"
			+ "and u.company like %?1% "
			+ "and u.user_department like %?2% "
			+ "and u.user_realname like %?3% "
			+ "and u.user_province like %?4% "
			+ "and u.parent_id like %?5% ")
	public List<User> find12By3(String company, String user_department, String user_realname, String user_province, Long parent_id);

	@Query("select u from User u "
			+ "where 1=1 "
			+ "and u.user_autority in(3,4)"
			+ "and u.company like %?1% "
			+ "and u.user_department like %?2% "
			+ "and u.user_realname like %?3% "
			+ "and u.user_province like %?4% "
			+ "and u.parent_id like %?5% ")
	public List<User> find34By3(String company, String user_department, String user_realname, String user_province, Long parent_id);
} 