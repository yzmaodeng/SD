package com.sd.vo;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.Where;

@Entity
@Table(name="tward_tmpl")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class Template extends BaseVo {

	@Id
	@Column(name="tw_gid")
	private String gid;
	
	@Column(name="tw_id")
	private String id;

	@Column(name="tw_slogan")
	private String slogan;
	
	@Column(name="tw_name")
	private String name;
	
	@Column(name="tw_user")
	private String userId;
	
	@Column(name="updatetime")
	private Date updateTime;
	
	@Column(name="createtime")
	private Date createTime;
	
	@Column(name="isdel")
	private String isdel;
	
	@OneToMany(mappedBy = "pid")
	@OrderBy("ordering")
	@Where(clause="isdel = 0")
	private Set<TemplateDetail> detailList;

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getIsdel() {
		return isdel;
	}

	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}

	public Set<TemplateDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(Set<TemplateDetail> detailList) {
		this.detailList = detailList;
	}

}
