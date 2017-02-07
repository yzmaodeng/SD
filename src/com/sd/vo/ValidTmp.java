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
@Table(name="tvalidtmp")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class ValidTmp extends BaseVo {
	@Id
	@Column(name="vt_gid")
	private String gid;
	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="vt_id")
	private String id;
	
	@Column(name="vt_validtext")
	private String validtext;

	@Column(name="vt_phonenumber")
	private String phone;

	@Column(name="vt_ipaddress")
	private String ipaddress;

	@Column(name="vt_createtime")
	private Date createtime;

	@Column(name="vt_validtime")
	private Date validtime;

	@Column(name="vt_status")
	private String status;

	@Column(name="vt_updatetime")
	private Date updatetime;

	public String getValidtext() {
		return validtext;
	}

	public void setValidtext(String validtext) {
		this.validtext = validtext;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getValidtime() {
		return validtime;
	}

	public void setValidtime(Date validtime) {
		this.validtime = validtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
