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

@Entity
@Table(name = "tkf_motion")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class Motion extends BaseVo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "tm_id")
	private String id;
	
	@OneToMany(mappedBy = "mgid", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
	@OrderBy("mgid")
	private Set<KfData> kfDataSet;

	@Column(name = "tm_smgid")
	private String stMotionId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="tm_smgid",updatable=false,insertable=false)
	@NotFound(action=NotFoundAction.IGNORE)
	private StandardMotion stMotion;
	
	@OneToMany(mappedBy = "mgid", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
	@OrderBy("bdays")
	private Set<Target> targetSet;
	
	@Column(name = "tm_sgid")
	private String sgid;
	
	@Column(name = "tm_ordering")
	private String ordering;

	@Column(name = "updatetime")
	private Date updateTime;

	@Column(name = "createtime")
	private Date createTime;

	@Column(name = "isdel")
	private String isdel;

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

	public String getSgid() {
		return sgid;
	}

	public void setSgid(String sgid) {
		this.sgid = sgid;
	}

	public Set<Target> getTargetSet() {
		return targetSet;
	}

	public void setTargetSet(Set<Target> targetSet) {
		this.targetSet = targetSet;
	}

	public String getOrdering() {
		return ordering;
	}

	public void setOrdering(String ordering) {
		this.ordering = ordering;
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

	public String getStMotionId() {
		return stMotionId;
	}

	public void setStMotionId(String stMotionId) {
		this.stMotionId = stMotionId;
	}

	public StandardMotion getStMotion() {
		return stMotion;
	}

	public void setStMotion(StandardMotion stMotion) {
		this.stMotion = stMotion;
	}

	public Set<KfData> getKfDataSet() {
		return kfDataSet;
	}

	public void setKfDataSet(Set<KfData> kfDataSet) {
		this.kfDataSet = kfDataSet;
	}

	
}
