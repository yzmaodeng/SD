package com.sd.vo;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

/**
 * @author Administrator
 *
 */
@Entity
@Table(name="course")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class Course extends BaseVo{
	
	@Column(name="id")
	private Integer id;
	
	@Id
	@Column(name="gid")
	private String gid;
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Column()
	private String category;
	@Column(name="type")
	private String type;
	public String getPreprice() {
		return preprice;
	}

	public void setPreprice(String preprice) {
		this.preprice = preprice;
	}

	@Column
	private String preprice;
	






	@Column
	private float price;

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	@Column
	private String sequence;

	@OneToMany(mappedBy = "parentGid", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
	@OrderBy("createTime")
	private Set<CourseVideo> courseVideo;
	
	@Column(name="totalNumber")
	private String totalNumber;
	
	@Column(name="currentNumber")
	private String currentNumber;
	
	@Column(name="recommendation")
	private String recommendation;
	
	@Column(name="avatar")
	private String avatar;
	
	@Column(name="introduction")
	private String introduction;
	
	@Column(name="parentGid")
	private String parentGid;
	
	@Column(name="title")
	private String title;
	
	@Column(name="watchNumber")
	private Integer watchNumber;
	
	@Column(name="expertGid")
	private String expertGid;
	
	@Column(name="createTime")
	private Date createTime;
	
	@Column(name="updateTime")
	private Date updateTime;
	
	@Column(name="subjectGid")
	private String subjectGid;
	
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public Integer getWatchNumber() {
		return watchNumber;
	}

	public void setWatchNumber(Integer watchNumber) {
		this.watchNumber = watchNumber;
	}


	// 隶属的专题
	public String getExpertGid() {
		return expertGid;
	}

	public void setExpertGid(String expertGid) {
		this.expertGid = expertGid;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}


	public Set<CourseVideo> getCourseVideo() {
		return courseVideo;
	}

	public void setCourseVideo(Set<CourseVideo> courseVideo) {
		this.courseVideo = courseVideo;
	}

	public String getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(String totalNumber) {
		this.totalNumber = totalNumber;
	}

	public String getCurrentNumber() {
		return currentNumber;
	}

	public void setCurrentNumber(String currentNumber) {
		this.currentNumber = currentNumber;
	}

	public String getParentGid() {
		return parentGid;
	}

	public void setParentGid(String parentGid) {
		this.parentGid = parentGid;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getSubjectGid() {
		return subjectGid;
	}

	public void setSubjectGid(String subjectGid) {
		this.subjectGid = subjectGid;
	}
	
}
