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
@Table(name="duiba_orders")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class Dorders extends BaseVo {
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getCredits() {
		return credits;
	}
	public void setCredits(Integer credits) {
		this.credits = credits;
	}
	public Integer getActualPrice() {
		return actualPrice;
	}
	public void setActualPrice(Integer actualPrice) {
		this.actualPrice = actualPrice;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getCreditStatus() {
		return creditStatus;
	}
	public void setCreditStatus(String creditStatus) {
		this.creditStatus = creditStatus;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public Date getGmtModified() {
		return gmtModified;
	}
	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}
	@Id
	@Column()
	private String id;
	@Column()
	private String bizId;
	public String getBizId() {
		return bizId;
	}
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	@Column(name="app_id")
	private String appId;
	@Column(name="user_id")
	private String userId;
	@Column(name="credits")
	private Integer credits;
	@Column(name="actual_price")
	private Integer actualPrice;
	@Column(name="duiba_order_num")
	private String orderNum;
	@Column(name="order_status")
	private String orderStatus;
	@Column(name="credits_status")
	private String creditStatus;
	@Column(name="type")
	private String type;
	@Column(name="description")
	private String description;
	@Column(name="gmt_create")
	private Date gmtCreate;
	@Column(name="gmt_modified")
	private Date gmtModified;
	public Integer getFacePrice() {
		return facePrice;
	}
	public void setFacePrice(int facePrice) {
		this.facePrice = facePrice;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public boolean getWaitAudit() {
		return waitAudit;
	}
	public void setWaitAudit(boolean waitAudit) {
		this.waitAudit = waitAudit;
	}
	@Column()
	private Integer facePrice; 
	@Column()
	private String params;
	@Column()
	private boolean waitAudit;
	          
	       

                
	            
	             
	             
	        
	     
	        
	      
	                
	         
	          
	  
	
	

	
}
