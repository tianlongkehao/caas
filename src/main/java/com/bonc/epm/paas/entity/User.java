package com.bonc.epm.paas.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

@Entity
public class User {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;//索引
	private String password;//登陆密码
	private String userName;//登陆名
	private String company;//公司名称
	private String email;//邮箱
	
	private String user_realname;//用户姓名
	private String user_autority;//用户权限
	private String user_department; //部门
	private String user_employee_id;//工号
	private String user_cellphone;//手机号码
	private String user_phone;//固定电话
	
	@ManyToMany
	@JoinTable(name="user_favor_images", 
	joinColumns={@JoinColumn(name="favor_users")},
	inverseJoinColumns={@JoinColumn(name="favor_images")})
	private List<Image> favorImages;
	
	public User (){
	}
	
	public List<Image> getFavorImages() {
		return favorImages;
	}
	
	public void setFavorImages(List<Image> favorImages) {
		this.favorImages = favorImages;
	}
	

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", password="
				+ password + ", email=" + email + ", company=" + company
				+ ", favorImages=" + favorImages + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getUser_realname() {
		return user_realname;
	}

	public void setUser_realname(String user_realname) {
		this.user_realname = user_realname;
	}

	public String getUser_autority() {
		return user_autority;
	}

	public void setUser_autority(String user_autority) {
		this.user_autority = user_autority;
	}

	public String getUser_department() {
		return user_department;
	}

	public void setUser_department(String user_department) {
		this.user_department = user_department;
	}

	public String getUser_employee_id() {
		return user_employee_id;
	}

	public void setUser_employee_id(String user_employee_id) {
		this.user_employee_id = user_employee_id;
	}

	public String getUser_cellphone() {
		return user_cellphone;
	}

	public void setUser_cellphone(String user_cellphone) {
		this.user_cellphone = user_cellphone;
	}

	public String getUser_phone() {
		return user_phone;
	}

	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}

}
