package com.sd.vo;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name="tuser")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class User extends BaseVo {
	@Id
	@Column(name="user_gid")
	private String gid;
	
	
	
	@Column(name="Annual_membership")
	private Date Annualmembership;
	
	
	public Date getAnnualmembership() {
		return Annualmembership;
	}

	public void setAnnualmembership(Date annualmembership) {
		Annualmembership = annualmembership;
	}

	@Column(name="User_wxUnionid")
	private String unionid;
	
	@Column(name="score")
	private Integer score;
	
	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	@Column(name="invitation")
	private String invitation;
	
	@Column(name="department")
	private String department;
	
	@Column(name="realName")
	private String realName;
	
	@Column(name="user_id")
	private String id;
	
	@Column(name="user_name")
	private String name;
	
	@Column(name="user_pwd")
	private String password;
	
	@Column(name="user_email")
	private String email;
	
	@Column(name="user_phone")
	private String phone;
	
	@Column(name="user_adddate")
	private Date createTime;
	
	@Column(name="approveDate")
	private Date approveDate;
	
	@Column
	private Date applyDate;
	
	@Column(name="user_approveinfo")
	private String approveInfo;
	
	@Column(name="user_approvestatus")
	private String approveStatus;
	
	@Column(name="user_chau")
	private String chAu;
	
	@Column(name="user_cfau")
	private String cfAu;
	
	@Column(name="user_tmau")
	private String teamId;
	
	@Column(name="user_online")
	private String online;
	
	@Column(name="user_ipv4")
	private String ipv4;
	
	@Column(name="user_ipv6")
	private String ipv6;
	
	@Column(name="user_logindate")
	private Date loginDate;
	
	@Column(name="user_logintime")
	private String loginTime;
	
	@Column(name="user_ostype")
	private String osType;
	
	@Column(name="user_ossn")
	private String osSn;
	
	@Column(name="user_updatetime")
	private Date updateTime;
	
	@Column(name="user_isdel")
	private String isdel;
	
	@Column(name="user_sex")
	private String sex;
	
	@Column(name="user_birthday")
	private Date birthday;
	
	@Column(name="user_local")
	private String local;
	
	@Column(name="user_descript")
	private String descript;
	
	@Column(name="user_title")
	private String title;
	
	@Column(name="user_avatars")
	private String avatars;
	
	@Column(name="user_hospital")
	private String hospital;
	
	@Column(name="user_jobtitle")
	private String jobtitle;
	
	@Column(name="user_wx")
	private String wx;
	
	@Column(name="user_wb")
	private String wb;
	
	@Column(name="user_qq")
	private String qq;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIpv4() {
		return ipv4;
	}

	public void setIpv4(String ipv4) {
		this.ipv4 = ipv4;
	}

	public String getIpv6() {
		return ipv6;
	}

	public void setIpv6(String ipv6) {
		this.ipv6 = ipv6;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getOsSn() {
		return osSn;
	}

	public void setOsSn(String osSn) {
		this.osSn = osSn;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAvatars() {
		return avatars;
	}

	public void setAvatars(String avatars) {
		this.avatars = avatars;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getJobtitle() {
		return jobtitle;
	}

	public void setJobtitle(String jobtitle) {
		this.jobtitle = jobtitle;
	}

	public String getWx() {
		return wx;
	}

	public void setWx(String wx) {
		this.wx = wx;
	}

	public String getWb() {
		return wb;
	}

	public void setWb(String wb) {
		this.wb = wb;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getChAu() {
		return chAu;
	}

	public void setChAu(String chAu) {
		this.chAu = chAu;
	}

	public String getCfAu() {
		return cfAu;
	}

	public void setCfAu(String cfAu) {
		this.cfAu = cfAu;
	}

	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getApproveInfo() {
		return approveInfo;
	}

	public void setApproveInfo(String approveInfo) {
		this.approveInfo = approveInfo;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Date getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getInvitation() {
		return invitation;
	}

	public void setInvitation(String invitation) {
		this.invitation = invitation;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	
	
}
