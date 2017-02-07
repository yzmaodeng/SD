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
@Table(name="academy")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class Academy extends BaseVo {
	

	

	public Date getStageTime() {
		return stageTime;
	}

	public void setStageTime(Date stageTime) {
		this.stageTime = stageTime;
	}

	public Date getStageTimeII() {
		return stageTimeII;
	}

	public void setStageTimeII(Date stageTimeII) {
		this.stageTimeII = stageTimeII;
	}
   
	@Column
	private String detailhtml;
	@Column
	private String lecturerhtml;
	@Column
	private String schedulehtml;
	@Column
	private String kcxxhtml;
	
	
	    
	
	
	
	public String getKcxxhtml() {
		return kcxxhtml;
	}

	public void setKcxxhtml(String kcxxhtml) {
		this.kcxxhtml = kcxxhtml;
	}

	public String getDetailhtml() {
		return detailhtml;
	}

	public void setDetailhtml(String detailhtml) {
		this.detailhtml = detailhtml;
	}

	public String getLecturerhtml() {
		return lecturerhtml;
	}

	public void setLecturerhtml(String lecturerhtml) {
		this.lecturerhtml = lecturerhtml;
	}

	public String getSchedulehtml() {
		return schedulehtml;
	}

	public void setSchedulehtml(String schedulehtml) {
		this.schedulehtml = schedulehtml;
	}

	@Column
	private String priceI;
	@Column
	private String priceII;
	@Column
	private String priceIII;
	@Column
	private Date stageTime;
	@Column
	private Date stageTimeI;
	@Column
	private String imageGids;
	
	
	public String getImageGids() {
		return imageGids;
	}

	public void setImageGids(String imageGids) {
		this.imageGids = imageGids;
	}

	public String getPriceI() {
		return priceI;
	}

	public void setPriceI(String priceI) {
		this.priceI = priceI;
	}

	public String getPriceII() {
		return priceII;
	}

	public void setPriceII(String priceII) {
		this.priceII = priceII;
	}

	public String getPriceIII() {
		return priceIII;
	}

	public void setPriceIII(String priceIII) {
		this.priceIII = priceIII;
	}

	public Date getStageTimeI() {
		return stageTimeI;
	}

	public void setStageTimeI(Date stageTimeI) {
		this.stageTimeI = stageTimeI;
	}

	@Column
	private Date stageTimeII;
	
	
	
	
	
	
	
	
	
	
	@Id
	@Column
	private String gid;
	
	@Column
	private Integer id;
	
	@Column
	private String price;
	
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Column
	private String pic;
	
	@Column
	private String title;
	
	@Column
	private String local;
	
	@Column
	private Date beginDate;
	
	@Column
	private Date endDate;
	
	@Column
	private String detail;
	
	@Column
	private String lecturer;
	
	@Column
	private String schedule;
	
	@Column
	private Date createTime;
	
	@Column
	private Integer totleNum;
	
	@Column
	private String preprice;
	
	public String getPreprice() {
		return preprice;
	}

	public void setPreprice(String preprice) {
		this.preprice = preprice;
	}

	public Integer getTotleNum() {
		return totleNum;
	}

	public void setTotleNum(Integer totleNum) {
		this.totleNum = totleNum;
	}

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

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getLecturer() {
		return lecturer;
	}

	public void setLecturer(String lecturer) {
		this.lecturer = lecturer;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	
}
