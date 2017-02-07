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
@Table(name="ttopic")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class Topic extends BaseVo {
	@Id
	@Column(name="tpc_gid")
	private String gid;
	
	@Column(name="tpc_id")
	private String id;

	@Column(name="tpc_cfgid")
	private String confGid;

	@Column(name="tpc_parentgid")
	private String parentGid;

	@Column(name="tpc_moderator")
	private String moderator;

	@Column(name="tpc_reviewer")
	private String reviewer;

	@Column(name="tpc_speaker")
	private String speaker;

	@Column(name="tpc_sponsor")
	private String sponsor;

	@Column(name="tpc_sponsordesc")
	private String sponsorDesc;

	@Column(name="tpc_title")
	private String title;

	@Column(name="tpc_title_e")
	private String titlee;

	@Column(name="tpc_detail")
	private String detail;

	@Column(name="tpc_detail_e")
	private String detaile;

	@Column(name="tpc_type")
	private String type;

	@Column(name="tpc_level")
	private String level;

	@Column(name="tpc_datetime_begin")
	private Date datetimeBegin;

	@Column(name="tpc_datetime_end")
	private Date datetimeEnd;

	@Column(name="tpc_createtime")
	private Date createTime;

	@Column(name="tpc_updatetime")
	private Date updateTime;

	@Column(name="tpc_isdel")
	private String isdel;

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getConfGid() {
		return confGid;
	}

	public void setConfGid(String confGid) {
		this.confGid = confGid;
	}

	public String getParentGid() {
		return parentGid;
	}

	public void setParentGid(String parentGid) {
		this.parentGid = parentGid;
	}

	public String getModerator() {
		return moderator;
	}

	public void setModerator(String moderator) {
		this.moderator = moderator;
	}

	public String getReviewer() {
		return reviewer;
	}

	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	public String getSpeaker() {
		return speaker;
	}

	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}

	public String getSponsor() {
		return sponsor;
	}

	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}

	public String getSponsorDesc() {
		return sponsorDesc;
	}

	public void setSponsorDesc(String sponsorDesc) {
		this.sponsorDesc = sponsorDesc;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitlee() {
		return titlee;
	}

	public void setTitlee(String titlee) {
		this.titlee = titlee;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getDetaile() {
		return detaile;
	}

	public void setDetaile(String detaile) {
		this.detaile = detaile;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Date getDatetimeBegin() {
		return datetimeBegin;
	}

	public void setDatetimeBegin(Date datetimeBegin) {
		this.datetimeBegin = datetimeBegin;
	}

	public Date getDatetimeEnd() {
		return datetimeEnd;
	}

	public void setDatetimeEnd(Date datetimeEnd) {
		this.datetimeEnd = datetimeEnd;
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
