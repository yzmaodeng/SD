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
@Table(name = "courseVideo")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class CourseVideo {
	

	@Column(name = "id")
	private Integer id;
	@Column
	private String sequence;

	@Id
	@Column(name = "gid")
	private String gid;
	
	@Column
	private String category;
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Column(name = "avatar")
	private String avatar;

	@Column(name = "introduction")
	private String introduction;

	@Column(name = "parentGid")
	private String parentGid;

	@Column(name = "title")
	private String title;
	@Column
	private float price;

	



	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	@Column(name = "videoUrl")
	private String videoUrl;

	@Column(name = "downloadUrl")
	private String downloadUrl;

	@Column(name = "watchNumber")
	private Integer watchNumber;

	@Column(name = "expertGid")
	private String expertGid;

	@Column(name = "type")
	private String type;

	@Column(name = "recommendation")
	private String recommendation;

	@Column(name = "createTime")
	private Date createTime;

	@Column(name = "updateTime")
	private Date updateTime;

	@Column
	private String duration;
	@Column
	private String size;
	
	@Column(name = "subjectGid")
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

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
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

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getSubjectGid() {
		return subjectGid;
	}

	public void setSubjectGid(String subjectGid) {
		this.subjectGid = subjectGid;
	}
	
}
