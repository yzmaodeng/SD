package com.sd.vo;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name = "orderInfo")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class OrderInfo extends BaseVo {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "orderNum")
	private String orderNum;
	@Column(name = "couponGid")
	private String couponId;
	@Column()
	private String orderType;


	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(String totalprice) {
		this.totalprice = totalprice;
	}

	@Column(name = "userGid")
	private String userGid;

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}



	@Column(name = "type")
	private Integer type;

	@Column(name = "academyGid")
	private String academyGid;

	@Column(name = "contactGids")
	private String contactGids;

	@Column(name = "status")
	private Integer status;

	@Column(name = "createTime")
	private Date createTime;
	
	@Column(name = "gid")
	private String gid;

	@Column(name = "invoiceGid")
	private String invoiceId;

	@Column(name = "remark")
	private String remark;

	@Column(name = "totalprice")
	private String totalprice;

	@Column(name = "hotline")
	private String hotline;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "invoiceGid", updatable=false, insertable=false)
	@NotFound(action=NotFoundAction.IGNORE)
	private Invoice invoice;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "couponGid", updatable=false, insertable=false)
	@NotFound(action=NotFoundAction.IGNORE)
	private Coupon coupon;
	public String getHotline() {
		return hotline;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public void setHotline(String hotline) {
		this.hotline = hotline;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getUserGid() {
		return userGid;
	}

	public void setUserGid(String userGid) {
		this.userGid = userGid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getAcademyGid() {
		return academyGid;
	}

	public void setAcademyGid(String academyGid) {
		this.academyGid = academyGid;
	}

	public String getContactGids() {
		return contactGids;
	}

	public void setContactGids(String contactGids) {
		this.contactGids = contactGids;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
