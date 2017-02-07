package com.sd.vo;

// Generated 2015-5-18 9:04:31 by Hibernate Tools 3.4.0.CR1

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

/**
 * Ttextbook generated by hbm2java
 */
@Entity
@Table(name="ttextbook")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class Textbook extends BaseVo {
	
	@Column(name="tb_id")
	private Integer tbId;
	
	@Id
	@Column(name="tb_gid")
	private String gid;
	
	@Column(name="readNumber")
	private Integer readNumber;
	
	@Column(name="readNumberR")
	private Integer readNumberR;
	
	@Column(name="subjectGid")
	private String subjectGid;
	
	@Column
	private String category;
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Column(name="tb_category")
	private String tbCategory;
	
	@Column(name="tb_avatars")
	private String tbAvatars;
	
	@Column(name="tb_title")
	private String tbTitle;
	
	@Column(name="tb_publish")
	private Date time;
	
	@Column(name="tb_count")
	private Integer tbCount;
	
	@Column(name="tb_author")
	private String tbAuthor;
	
	@Column(name="tb_selected")
	private String tbSelected;
	
	@Column(name="tb_detail")
	private String tbDetail;
	
	@Column(name="tb_createtime")
	private Date tbCreatetime;
	
	@Column(name="tb_updatetime")
	private Date tbUpdatetime;
	
	@Column(name="tb_isdel")
	private String tbIsdel;
	

	public Textbook() {
	}

	public Textbook(String tbSelected) {
		this.tbSelected = tbSelected;
	}

	public Textbook(String tbCategory, String tbAvatars, String tbTitle,
			Date tbPublish, Integer tbCount, String tbAuthor, String tbSelected,
			String tbDetail, Date tbCreatetime, Date tbUpdatetime,
			String tbIsdel) {
		this.tbCategory = tbCategory;
		this.tbAvatars = tbAvatars;
		this.tbTitle = tbTitle;
		this.time = tbPublish;
		this.tbCount = tbCount;
		this.tbAuthor = tbAuthor;
		this.tbSelected = tbSelected;
		this.tbDetail = tbDetail;
		this.tbCreatetime = tbCreatetime;
		this.tbUpdatetime = tbUpdatetime;
		this.tbIsdel = tbIsdel;
	}

	public Integer getTbId() {
		return this.tbId;
	}

	public void setTbId(Integer tbId) {
		this.tbId = tbId;
	}

	public String getTbCategory() {
		return this.tbCategory;
	}

	public void setTbCategory(String tbCategory) {
		this.tbCategory = tbCategory;
	}

	public String getTbAvatars() {
		return this.tbAvatars;
	}

	public void setTbAvatars(String tbAvatars) {
		this.tbAvatars = tbAvatars;
	}

	public String getTbTitle() {
		return this.tbTitle;
	}

	public void setTbTitle(String tbTitle) {
		this.tbTitle = tbTitle;
	}


	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Integer getTbCount() {
		return this.tbCount;
	}

	public void setTbCount(Integer tbCount) {
		this.tbCount = tbCount;
	}

	public String getTbAuthor() {
		return this.tbAuthor;
	}

	public void setTbAuthor(String tbAuthor) {
		this.tbAuthor = tbAuthor;
	}

	public String getTbSelected() {
		return tbSelected;
	}

	public void setTbSelected(String tbSelected) {
		this.tbSelected = tbSelected;
	}

	public String getTbDetail() {
		return this.tbDetail;
	}

	public void setTbDetail(String tbDetail) {
		this.tbDetail = tbDetail;
	}

	public Date getTbCreatetime() {
		return this.tbCreatetime;
	}

	public void setTbCreatetime(Date tbCreatetime) {
		this.tbCreatetime = tbCreatetime;
	}

	public Date getTbUpdatetime() {
		return this.tbUpdatetime;
	}

	public void setTbUpdatetime(Date tbUpdatetime) {
		this.tbUpdatetime = tbUpdatetime;
	}

	public String getTbIsdel() {
		return tbIsdel;
	}

	public void setTbIsdel(String tbIsdel) {
		this.tbIsdel = tbIsdel;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getSubjectGid() {
		return subjectGid;
	}

	public void setSubjectGid(String subjectGid) {
		this.subjectGid = subjectGid;
	}

	public Integer getReadNumber() {
		return readNumber;
	}

	public void setReadNumber(Integer readNumber) {
		this.readNumber = readNumber;
	}

	public Integer getReadNumberR() {
		return readNumberR;
	}

	public void setReadNumberR(Integer readNumberR) {
		this.readNumberR = readNumberR;
	}


}
