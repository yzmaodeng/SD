package com.sd.vo;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name="trdpatient")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class RdPatient extends BaseVo {

	@Id
	@Column(name="repa_gid")
	private String gid;
	
	@Column(name="repa_id")
	private Integer id;

	@Column(name="repa_name")
	private String name;
	
	@Column(name="operationName")
	private String operationName;
	
	@Column(name="sickName")
	private String sickName;

	@Column(name="repa_age")
	private String age;

	@Column(name="repa_gender")
	private String gender;

	@Column(name="repa_sick")
	private String sick;

	@Column(name="head")
	private String head;
	
	@Column(name="pid")
	private String pid;
	
	@Column(name="teamGid")
	private String teamGid;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="mark")
	private String mark;
	
	@Column(name="state")
	private String state;
	
	@Column(name="relativesPhone")
	private String relativesPhone;
	
	@Column(name="address")
	private String address;
	
	@Column(name="postcode")
	private String postcode;
	
	@Column(name="other")
	private String other;
	
	@Column(name="caseNo")
	private String caseNo;
	
	@Column(name="bedNo")
	private String bedNo;
	
	@Column(name="patientNo")
	private String patientNo;
	
	@Column(name="IDCardNo")
	private String IDCardNo;
	
	@Column(name="operationTime")
	private Date operationTime;

	@Column(name="repa_visitdate")
	private Date visitDate;

	@Column(name="repa_usergid")
	private String userGid;

	@Column(name="repa_createtime")
	private Date createTime;

	@Column(name="repa_updatetime")
	private Date updateTime;

	@Column(name="repa_isdel")
	private String isdel;

	@OneToMany(mappedBy = "pgid", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
	@OrderBy("date")
	private Set<Record> recordSet;
	
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

	public String getSick() {
		return sick;
	}

	public void setSick(String sick) {
		this.sick = sick;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public String getUserGid() {
		return userGid;
	}

	public void setUserGid(String userGid) {
		this.userGid = userGid;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Set<Record> getRecordSet() {
		return recordSet;
	}

	public void setRecordSet(Set<Record> recordSet) {
		this.recordSet = recordSet;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIsdel() {
		return isdel;
	}

	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRelativesPhone() {
		return relativesPhone;
	}

	public void setRelativesPhone(String relativesPhone) {
		this.relativesPhone = relativesPhone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getPatientNo() {
		return patientNo;
	}

	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}

	public String getIDCardNo() {
		return IDCardNo;
	}

	public void setIDCardNo(String iDCardNo) {
		IDCardNo = iDCardNo;
	}

	public Date getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTeamGid() {
		return teamGid;
	}

	public void setTeamGid(String teamGid) {
		this.teamGid = teamGid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getSickName() {
		return sickName;
	}

	public void setSickName(String sickName) {
		this.sickName = sickName;
	}
	
	
	
}
