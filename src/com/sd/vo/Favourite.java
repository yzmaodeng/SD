package com.sd.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name="tfav")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class Favourite extends BaseVo {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="fav_id")
	private String id;
	
	@Column(name="fav_GID")
	private String gid;

	@Column(name="fav_usergid")
	private String userGid;
	
	@Column(name="fav_type")
	private String type;//1.文章、2.会议、3.帖子'

	@Column(name="fav_createtime")
	private Date createTime;

	@Column(name="fav_updatetime")
	private Date updateTime;

	@Column(name="fav_isdel")
	private String isdel;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getUserGid() {
		return userGid;
	}

	public void setUserGid(String userGid) {
		this.userGid = userGid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getIsdel() {
		return isdel;
	}

	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}

}
