package com.sd.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name="treply")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class Reply extends BaseVo {

	@Id
	@Column(name="rpy_gid")
	private String gid;
	
	@Column(name="rpy_id")
	private String id;
	
	@Column(name="praiseNumber")
	private Integer praiseNumber;
	
	@Column(name="replyGid")
	private String replyGid;
	
	@Column(name="pic")
	private String pic;
	
	@Column(name="location")
	private String location;
	
	@Column(name="rpy_usergid")
	private String userId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="rpy_usergid",updatable=false,insertable=false)
	@NotFound(action=NotFoundAction.IGNORE)
	private User user;

	@Column(name="rpy_pstgid")
	private String postGid;

	@Column(name="rpy_detail")
	private String detail;
	
	@Column(name="rpy_tousergid")
	private String toUserId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="rpy_tousergid",updatable=false,insertable=false)
	@NotFound(action=NotFoundAction.IGNORE)
	private User toUser;

	@Column(name="rpy_createtime")
	private Date createTime;

	@Column(name="rpy_updatetime")
	private Date updateTime;

	@Column(name="rpy_isdel")
	private String isdel;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPostGid() {
		return postGid;
	}

	public void setPostGid(String postGid) {
		this.postGid = postGid;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public User getToUser() {
		return toUser;
	}

	public void setToUser(User toUser) {
		this.toUser = toUser;
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

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}


	public Integer getPraiseNumber() {
		return praiseNumber;
	}

	public void setPraiseNumber(Integer praiseNumber) {
		this.praiseNumber = praiseNumber;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getReplyGid() {
		return replyGid;
	}

	public void setReplyGid(String replyGid) {
		this.replyGid = replyGid;
	}

}
