package com.sd.vo;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

//import org.hibernate.annotations.CascadeType;

@Entity
@Table(name="courseReply")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class CourseReply extends BaseVo {
	
	@Column(name="id")
	private Integer id;
	@Id
	@Column(name="gid")
	private String gid;
	
	@Column(name="type")
	private String type;
	
	@Column(name="courseGid")
	private String courseGid;
	
	@Column(name="beReplyGid")
	private String beReplyGid;
	
	@Column(name="userGid")
	private String userGid;
	
	@Column(name="content")
	private String content;
	
	@Column(name="createTime")
	private Date createTime;
	
	@Column(name="location")
	private String location;
	
//    @OneToMany(targetEntity=CourseReply.class, mappedBy="children", cascade=CascadeType.ALL, fetch = FetchType.EAGER)  
//    @JoinColumn(name = "PARENT_CODE")  
//    @OrderBy("seq")  
//    private Set<CourseReply> children;  

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCourseGid() {
		return courseGid;
	}

	public void setCourseGid(String courseGid) {
		this.courseGid = courseGid;
	}

	public String getBeReplyGid() {
		return beReplyGid;
	}

	public void setBeReplyGid(String beReplyGid) {
		this.beReplyGid = beReplyGid;
	}

	public String getUserGid() {
		return userGid;
	}

	public void setUserGid(String userGid) {
		this.userGid = userGid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	
	
     
}
