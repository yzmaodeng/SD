package com.sd.vo;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
@Entity
@Table(name="advertisement")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class Advertisement extends BaseVo {
	@Id
	@Column
	private String gid;
	@Column
	private Integer id;
	@Column
	private String url;
	@Column
	private String author;
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypegid() {
		return typegid;
	}
	public void setTypegid(String typegid) {
		this.typegid = typegid;
	}
	@Column
	private String type;
	@Column
	private String typegid;
	@Column
	private String title;
	@Column
	private String iosUrl;
	
	public String getIosUrl() {
		return iosUrl;
	}
	public void setIosUrl(String iosUrl) {
		this.iosUrl = iosUrl;
	}
	@Column
	private String res1;
	@Column
	private String res2;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Column
	private String res3;
	@Column
	private String res4;
	@Column
	private String res5;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRes1() {
		return res1;
	}
	public void setRes1(String res1) {
		this.res1 = res1;
	}
	public String getRes2() {
		return res2;
	}
	public void setRes2(String res2) {
		this.res2 = res2;
	}
	public String getRes3() {
		return res3;
	}
	public void setRes3(String res3) {
		this.res3 = res3;
	}
	public String getRes4() {
		return res4;
	}
	public void setRes4(String res4) {
		this.res4 = res4;
	}
	public String getRes5() {
		return res5;
	}
	public void setRes5(String res5) {
		this.res5 = res5;
	}


}
