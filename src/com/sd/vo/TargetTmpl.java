package com.sd.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name = "tkftmpl_target")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class TargetTmpl extends BaseVo {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ttt_id")
	private String gid;

	@Column(name = "ttt_gid")
	private String id;
	
	@Column(name = "target")
	private String target;

	@Column(name = "ttt_mgid")
	private String mgid;

	@Column(name = "ttt_bdays")
	private String bdays;
	
	@Column(name = "ttt_edays")
	private String edays;

	@Column(name = "ttt_group")
	private String group;

	@Column(name = "ttt_num")
	private String num;

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
	public String getMgid() {
		return mgid;
	}
	public void setMgid(String mgid) {
		this.mgid = mgid;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getIsdel() {
		return isdel;
	}
	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}
	
	public String getBdays() {
		return bdays;
	}
	public void setBdays(String bdays) {
		this.bdays = bdays;
	}
	public String getEdays() {
		return edays;
	}
	public void setEdays(String edays) {
		this.edays = edays;
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
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	
}
