package com.sd.vo;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name="tnotice")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class Notice extends BaseVo{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="tn_id")
	private String id;
	
	@Column(name="tn_userGid")
	private String userId;
	
	@Column(name="tn_pstGid")
	private String postId;

	@Column(name="tn_replyGid")
	private String replyId;

	@Column(name="tn_isRead")
	private String isRead;

	@Column(name="tn_createtime")
	private Date createTime;

	@Column(name="tn_updatetime")
	private Date updateTime;

	@Column(name="tn_isdel")
	private String isdel;
	
	@Column(name="tn_type")
	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@OneToMany(mappedBy = "pid", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
	private Set<NoticeDetail> detailSet;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getReplyId() {
		return replyId;
	}

	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}

	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getIsdel() {
		return isdel;
	}

	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}

	public Set<NoticeDetail> getDetailSet() {
		return detailSet;
	}

	public void setDetailSet(Set<NoticeDetail> detailSet) {
		this.detailSet = detailSet;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
