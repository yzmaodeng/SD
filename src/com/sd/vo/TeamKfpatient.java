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
@Table(name = "tteam_kfpatient")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class TeamKfpatient extends BaseVo {

	@Id
	@Column(name = "ttpi_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;

	@Column(name = "ttpi_tgid")
	private String stgid;

	@Column(name = "ttpi_pgid")
	private String pgid;
	
	@Column(name = "updatetime")
	private Date updateTime;

	@Column(name = "createtime")
	private Date createtime;

	@Column(name = "isdel")
	private String isdel;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStgid() {
		return stgid;
	}

	public void setStgid(String stgid) {
		this.stgid = stgid;
	}

	public String getPgid() {
		return pgid;
	}

	public void setPgid(String pgid) {
		this.pgid = pgid;
	}


	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getIsdel() {
		return isdel;
	}

	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}

	

	
}
