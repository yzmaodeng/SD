package com.sd.vo;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.Where;

@Entity
@Table(name="tteam")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class Team extends BaseVo {

	@Id
	@Column(name="tdt_gid")
	private String gid;
	
	@Column(name="tdt_id")
	private String id;

	@Column(name="tdt_avatars")
	private String avatars;
	
	@Column(name="tdt_name")
	private String name;
	
	@OneToMany(mappedBy = "teamId", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
	private Set<User> userSet;
	
	@Column(name="tdt_slogan")
	private String slogan;
	
	@Column(name="tdt_local")
	private String local;
	
	@Column(name="updatetime")
	private Date updateTime;
	
	@Column(name="createtime")
	private Date createTime;
	
	@Column(name="isdel")
	private String isdel;
	
	@ManyToMany
	@JoinTable(name = "tteam_kfpatient", 
		joinColumns = {@JoinColumn(name = "ttpi_tgid") }, 
		inverseJoinColumns = { @JoinColumn(name = "ttpi_pgid") })
	@OrderBy("createtime desc")
	private Set<KfPatient> kfPatList;

	@OneToMany(mappedBy = "teamId")
	@OrderBy("no")
	@Where(clause="isdel = 0")
	private Set<WardPatient> wardPatList;
	
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

	public String getAvatars() {
		return avatars;
	}

	public void setAvatars(String avatars) {
		this.avatars = avatars;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public Set<KfPatient> getKfPatList() {
		return kfPatList;
	}

	public void setKfPatList(Set<KfPatient> kfPatList) {
		this.kfPatList = kfPatList;
	}

	public Set<WardPatient> getWardPatList() {
		return wardPatList;
	}

	public void setWardPatList(Set<WardPatient> wardPatList) {
		this.wardPatList = wardPatList;
	}

}
