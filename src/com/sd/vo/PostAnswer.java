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
@Table(name="tpostsubject_answer")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class PostAnswer extends BaseVo {

	@Id
	@Column
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPostsubjectGid() {
		return postsubjectGid;
	}
	public void setPostsubjectGid(String postsubjectGid) {
		this.postsubjectGid = postsubjectGid;
	}

	
	@Column
	private String gid;    
	@Column
	private String userId;               
	@Column
	private String postsubjectGid;             
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}


	@Column
	private String options;
 
	             
	
}
