package com.sd.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name="tkfpatient_sick")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class KfPatientSick extends BaseVo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ts_id")
	private String id;
	
	@Column(name="ts_pid")
	private String pid;
	
	@Column(name="ts_sid")
	private String sid;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="operationGidL",updatable=false,insertable=false)
	@NotFound(action=NotFoundAction.IGNORE)
	private Operation operationL;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="operationGidR",updatable=false,insertable=false)
	@NotFound(action=NotFoundAction.IGNORE)
	private Operation operationR;
	
	@Column(name="ts_oid")
	private String oid;
	
	@Column(name="ts_type")
	private String type;
	
	@Column(name="operationGidL")
	private String operationGidL;
	
	@Column(name="operationGidR")
	private String operationGidR;
	
	@Column(name="ts_operRDate")
	private Date operRDate;
	
	@Column(name="ts_operLDate")
	private Date operLDate;
	
	@Column(name="createtime")
	private Date createtime;
	
	@Column(name="updatetime")
	private Date updatetime;
	
	@Column(name="isdel")
	private String isdel;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getIsdel() {
		return isdel;
	}

	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}

	public Date getOperRDate() {
		return operRDate;
	}

	public void setOperRDate(Date operRDate) {
		this.operRDate = operRDate;
	}

	public Date getOperLDate() {
		return operLDate;
	}

	public void setOperLDate(Date operLDate) {
		this.operLDate = operLDate;
	}


	public String getOperationGidL() {
		return operationGidL;
	}

	public void setOperationGidL(String operationGidL) {
		this.operationGidL = operationGidL;
	}

	public String getOperationGidR() {
		return operationGidR;
	}

	public void setOperationGidR(String operationGidR) {
		this.operationGidR = operationGidR;
	}

	public Operation getOperationL() {
		return operationL;
	}

	public void setOperationL(Operation operationL) {
		this.operationL = operationL;
	}

	public Operation getOperationR() {
		return operationR;
	}

	public void setOperationR(Operation operationR) {
		this.operationR = operationR;
	}

}
