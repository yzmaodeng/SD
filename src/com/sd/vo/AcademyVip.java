package com.sd.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name="academyVip")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class AcademyVip extends BaseVo {
	
	@Column
	private Integer id;
	@Id
	@Column
	private String gid;
	@Column
	private String name;
	
	@Column
	private String price;
	@Column
	private Integer resNum;
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public Integer getResNum() {
		return resNum;
	}
	public void setResNum(Integer resNum) {
		this.resNum = resNum;
	}
	public String getAcademyGid() {
		return academyGid;
	}
	public void setAcademyGid(String academyGid) {
		this.academyGid = academyGid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column
	private String academyGid;
	@Column
	private String description;
	

}
