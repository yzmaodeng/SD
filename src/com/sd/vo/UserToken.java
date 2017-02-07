package com.sd.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name="tusertoken")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class UserToken extends BaseVo {
	@Id
	@Column(name="tokenusergid")
	private String userGid;

	@Column(name="tk_id")
	private Integer id;
	
	@Column(name="starttime")
	private Date startTime;
	
	@Column(name="endtime")
	private Date endTime;
	
	@Column(name="updatetime")
	private Date updateTime;
	
	@Column(name="createtime")
	private Date createTime;
	
	@Column(name="status")
	private Integer status;
	
	@Column(name="tokencontent")
	private String content;
	
	@Column(name="tokentype")
	private Integer type;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getUserGid() {
		return userGid;
	}

	public void setUserGid(String userGid) {
		this.userGid = userGid;
	}

}
