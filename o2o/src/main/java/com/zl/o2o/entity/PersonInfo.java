package com.zl.o2o.entity;

import java.util.Date;

public class PersonInfo {
	
	//id
	private Long userId;
	//姓名
	private String name;
	
	//private Date birthday;
	//性别
	private String gender;
	//private String phone;
	//邮箱
	private String email;
	//头像
	private String profileImg;
	//1.顾客，2.店家，3.超级管理员
	private Integer userType;
//	private Integer customerFlag;
//	private Integer shopOwnerFlag;
//	private Integer adminFlag;
	//创建时间
	private Date createTime;
	//用户编辑时间
	private Date lastEditTime;
	//状态，0不能登录，1能登录.
	private Integer enableStatus;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getProfileImg() {
		return profileImg;
	}
	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastEditTime() {
		return lastEditTime;
	}
	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}
	public Integer getEnableStatus() {
		return enableStatus;
	}
	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
	}
	
}
