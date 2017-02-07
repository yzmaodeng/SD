package com.sd.vo;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "tkf_scheme")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class Scheme extends BaseVo {
	
	@Id
	@Column(name = "ts_gid")
	private String gid;
	
	public String getCreateteam() {
		return createteam;
	}

	public void setCreateteam(String createteam) {
		this.createteam = createteam;
	}

	@Column(name = "ts_createteam")
	private String createteam;

	@Column(name = "ts_id")
	private String id;

	@Column(name = "ts_name")
	private String name;

	@Column(name = "ts_note")
	private String note;

	@Column(name = "ts_refertmpl")
	private String referTmpl;
	
	@Column(name = "ts_kfpid")
	private String kfPatientId;
	
	@Column(name = "ts_ismain")
	private String isMain;

	@OneToMany(mappedBy = "sgid", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
	@Where(clause="isdel=1")
	@OrderBy("tm_ordering")
	private Set<Motion> motionSet;
	
	@Column(name = "ts_patsickid")
	private String patSickId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ts_patsickid", updatable=false, insertable=false)
	@NotFound(action=NotFoundAction.IGNORE)
	private KfPatientSick patSick;
	
	@Column(name = "ts_createuser")
	private String createUser;

	@Column(name = "updatetime")
	private Date updateTime;

	@Column(name = "createtime")
	private Date createTime;

	@Column(name = "isdel")
	private String isdel;
	
	@Column(name = "ts_leg")
	private String leg;

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

	public String getKfPatientId() {
		return kfPatientId;
	}

	public void setKfPatientId(String kfPatientId) {
		this.kfPatientId = kfPatientId;
	}

	public Set<Motion> getMotionSet() {
		return motionSet;
	}

	public void setMotionSet(Set<Motion> motionSet) {
		this.motionSet = motionSet;
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

	public String getReferTmpl() {
		return referTmpl;
	}

	public void setReferTmpl(String referTmpl) {
		this.referTmpl = referTmpl;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getPatSickId() {
		return patSickId;
	}

	public void setPatSickId(String patSickId) {
		this.patSickId = patSickId;
	}

	public KfPatientSick getPatSick() {
		return patSick;
	}

	public void setPatSick(KfPatientSick patSick) {
		this.patSick = patSick;
	}

	public String getIsMain() {
		return isMain;
	}

	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}

	public String getLeg() {
		return leg;
	}

	public void setLeg(String leg) {
		this.leg = leg;
	}
	
}
