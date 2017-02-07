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
@Table(name="tpostfav")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class PostFav extends BaseVo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="pf_id")
	private String id;
	
	@Column(name="pf_usergid")
	private String userGid;

	@Column(name="pf_pstgid")
	private String postGid;

	@Column(name="pf_createtime")
	private Date createTime;

	@Column(name="pf_updatetime")
	private Date updateTime;

	@Column(name="pf_isdel")
	private String isdel;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserGid() {
		return userGid;
	}

	public void setUserGid(String userGid) {
		this.userGid = userGid;
	}

	public String getPostGid() {
		return postGid;
	}

	public void setPostGid(String postGid) {
		this.postGid = postGid;
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
