package com.sd.vo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
@Entity
@Table(name="columns")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class Columns extends BaseVo{
	@Id
	@Column
	private String gid;
	@Column
	private String pic;
	@Column
	private Date createtime;
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	@Column
	private String recommend;
	@Column
	private String title;
	@Column
	private Integer id;
	@Column
	private String suitablecrowd;
	@Column
	private String price;
	public String getSuitablecrowd() {
		return suitablecrowd;
	}
	public void setSuitablecrowd(String suitablecrowd) {
		this.suitablecrowd = suitablecrowd;
	}
	@Column
	private Integer subscriberNum;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Column
	private String author;
	public String getRecommend() {
		return recommend;
	}
	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public Integer getSubscriberNum() {
		return subscriberNum;
	}
	public void setSubscriberNum(Integer subscriberNum) {
		this.subscriberNum = subscriberNum;
	}
	@Column
	private String intruduction;
	@Column
	private String notice;
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
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getIntruduction() {
		return intruduction;
	}
	public void setIntruduction(String intruduction) {
		this.intruduction = intruduction;
	}
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
}
