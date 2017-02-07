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
@Table(name = "tstandard_motion")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class StandardMotion extends BaseVo {
	@Id
	@Column(name = "tsm_gid")
	private String gid;

	@Column(name = "tsm_id")
	private String id;
	
	@Column(name = "targetType")
	private String targetType;

	@Column(name = "tsm_name")
	private String name;

	@Column(name = "tsm_note")
	private String note;
	
	@Column(name = "tsm_pic")
	private String pic;
	
	@Column(name = "tsm_vid")
	private String vid;
	
	@Column(name = "videoDownload")
	private String videoDownload;
	
	@Column(name = "tsm_beginday")
	private String beginday;
	
	@Column(name = "tsm_type")
	private String type;
	
	@Column(name = "tsm_doctype")
	private String docType;
	
	@Column(name = "tsm_ordering")
	private String ordering;

	@Column(name = "updatetime")
	private Date updateTime;

	@Column(name = "createtime")
	private Date createTime;

	@Column(name = "isdel")
	private String isdel;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getIsdel() {
		return isdel;
	}

	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getBeginday() {
		return beginday;
	}

	public void setBeginday(String beginday) {
		this.beginday = beginday;
	}
	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getVideoDownload() {
		return videoDownload;
	}

	public void setVideoDownload(String videoDownload) {
		this.videoDownload = videoDownload;
	}

	public String getOrdering() {
		return ordering;
	}

	public void setOrdering(String ordering) {
		this.ordering = ordering;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

}
