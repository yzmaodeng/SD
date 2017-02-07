package com.sd.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name="coupon")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class Coupon extends BaseVo {
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAccomplish() {
		return accomplish;
	}
	public void setAccomplish(String accomplish) {
		this.accomplish = accomplish;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getInuse() {
		return inuse;
	}
	public void setInuse(String inuse) {
		this.inuse = inuse;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	@Id
	@Column
	private String gid;
	@Column
	private Integer id;
	@Column
	private String accomplish;
	@Column
	private Date beginTime;
	@Column
	private Date endTime;
	@Column
	private String userId; 
	@Column        
	private String inuse;
	@Column        
	private String code;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Column        
	private Date createTime;
	@Column        
	private String type;
	@Column        
	private String discount;
	@Column        
	private String coupongid;
	
	public String getCoupongid() {
		return coupongid;
	}
	public void setCoupongid(String coupongid) {
		this.coupongid = coupongid;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	@Column        
	private String telephone;
	

	

}
