package com.sd.vo;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name = "tkftmpl_scheme")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class SchemeTmpl extends BaseVo {
	
	@Id
	@Column(name = "tts_gid")
	private String gid;

	@Column(name = "tts_id")
	private String id;
	
	@Column(name = "tts_name")
	private String name;
	
	@Column(name = "tts_note")
	private String note;
	
	@OneToMany(mappedBy = "sgid", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
	@OrderBy(clause = "ttm_ordering")
	private Set<MotionTmpl> motionSet;
	
	@Column(name = "tts_createteam")
	private String createTeam;
	
	@Column(name = "updatetime")
	private Date updateTime;
	
	@Column(name = "createtime")
	private Date createTime;
	
	@Column(name = "isdel")
	private String isdel;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getIsdel() {
		return isdel;
	}

	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}

	public Set<MotionTmpl> getMotionSet() {
		return motionSet;
	}

	public void setMotionSet(Set<MotionTmpl> motionSet) {
		this.motionSet = motionSet;
	}

	public String getCreateTeam() {
		return createTeam;
	}

	public void setCreateTeam(String createTeam) {
		this.createTeam = createTeam;
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
}
