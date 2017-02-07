package com.sd.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name="subject")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class Subject {
	
	@Id
	@Column(name="gid")
	private String gid;
	
	@Column(name="id")
	private String id;
	@Column
	private Integer resnum;
	public Integer getResnum() {
		return resnum;
	}

	public void setResnum(Integer resnum) {
		this.resnum = resnum;
	}
	
	@Column(name="parentGid")
	private String parentGid;
	@Column
	private String category;
	
	@Column
	private Date createTime;
	
	public String getCreateTime() {
		return  new SimpleDateFormat("yyyy-MM-dd").format(createTime);
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Column(name="title")
	private String title;
	
	@Column(name="description")
	private String description;
	
	@Column(name="pic")
	private String pic;
	
	@Column(name="isRecommend")
	private String isRecommend;

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

	public String getParentGid() {
		return parentGid;
	}

	public void setParentGid(String parentGid) {
		this.parentGid = parentGid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(String isRecommend) {
		this.isRecommend = isRecommend;
	}
	
}
