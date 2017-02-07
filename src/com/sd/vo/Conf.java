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
@Table(name="tconf")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class Conf extends BaseVo {
	@Id
	@Column(name="cf_gid")
	private String gid;
	
	@Column(name="cf_id")
	private String id;

	@Column(name="cf_name")
	private String name;

	@Column(name="cf_avatars")
	private String avatars;

	@Column(name="cf_date")
	private Date dateBegin;

	@Column(name="cf_date_end")
	private Date dateEnd;

	@Column(name="cf_local")
	private String local;

	@Column(name="cf_localgps")
	private String localGps;

	@Column(name="cf_localdetail")
	private String localDetail;

	@Column(name="cf_slogan")
	private String slogan;

	@Column(name="cf_slogan_e")
	private String slogane;

	@Column(name="cf_detail")
	private String detail;

	@Column(name="cf_sponsor")
	private String sponsor;

	@Column(name="cf_contact")
	private String contact;

	@Column(name="cf_createtime")
	private Date createTime;

	@Column(name="cf_updatetime")
	private Date updateTime;

	@Column(name="cf_isdel")
	private String isdel;

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatars() {
		return avatars;
	}

	public void setAvatars(String avatars) {
		this.avatars = avatars;
	}

	public Date getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getLocalGps() {
		return localGps;
	}

	public void setLocalGps(String localGps) {
		this.localGps = localGps;
	}

	public String getLocalDetail() {
		return localDetail;
	}

	public void setLocalDetail(String localDetail) {
		this.localDetail = localDetail;
	}

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public String getSlogane() {
		return slogane;
	}

	public void setSlogane(String slogane) {
		this.slogane = slogane;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getSponsor() {
		return sponsor;
	}

	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsdel() {
		return isdel;
	}

	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}

}
