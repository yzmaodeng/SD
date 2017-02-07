package com.sd.vo;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name="tpost")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class Posts extends BaseVo {

	@Id
	@Column(name="pst_gid")
	private String gid;
	
	@OneToMany(mappedBy = "parentGid", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
	@OrderBy("createTime asc")
	private Set<ChildPost> childPost;
	
	@Column(name="location")
	private String location;
	
	@Column(name="title")
	private String title;
	
	@Column(name="top")
	private String top;
	
	@Column(name="pst_id")
	private String id;
	
	@Column(name="htmltext")
	private String htmltext;
	


	public String getHtmltext() {
		return htmltext;
	}

	public void setHtmltext(String htmltext) {
		this.htmltext = htmltext;
	}

	@Column(name="type")
	private String type;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="pst_chgid",updatable=false,insertable=false)
	@NotFound(action=NotFoundAction.IGNORE)
	private Channel channel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="pst_userGid",updatable=false,insertable=false)
	@NotFound(action=NotFoundAction.IGNORE)
	private User user;
	
	@Column(name="pst_ordering")
	private String order;
	
	@Column(name="pst_chgid")
	private String channelGid;
	
	@Column(name="pst_userGid")
	private String userGid;

	@Column(name="pst_detail", columnDefinition="TEXT")
	private String detail;

	@Column(name="pst_createtime")
	private Date createTime;

	@Column(name="pst_pic")
	private String pic;

	@Column(name="pst_vid")
	private String vid;

	@Column(name="pst_updatetime")
	private Date updateTime;

	@Column(name="pst_isdel")
	private String isdel;

	@Column(name="pst_favcount")
	private String favCount;

	@Column(name="pst_replycount")
	private Integer replyCount;
	
	@Column(name="readNumberR")
	private Integer readNumberR;
	
	@Column(name="readNumber")
	private Integer readNumber;
	
	@Column(name="mark")
	private String mark;
	
	@Column(name="pst_isRec")
	private String isRec;

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
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

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsdel() {
		return isdel;
	}

	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}

	public String getFavCount() {
		return favCount;
	}

	public void setFavCount(String favCount) {
		this.favCount = favCount;
	}

	public Integer getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(Integer replyCount) {
		this.replyCount = replyCount;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public String getChannelGid() {
		return channelGid;
	}

	public void setChannelGid(String channelGid) {
		this.channelGid = channelGid;
	}

	public String getUserGid() {
		return userGid;
	}

	public void setUserGid(String userGid) {
		this.userGid = userGid;
	}

	public String getIsRec() {
		return isRec;
	}

	public void setIsRec(String isRec) {
		this.isRec = isRec;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}
	public Integer getReadNumberR() {
		return readNumberR;
	}

	public void setReadNumberR(Integer readNumberR) {
		this.readNumberR = readNumberR;
	}
	
	public Integer getReadNumber() {
		return readNumber;
	}
	
	public void setReadNumber(Integer readNumber) {
		this.readNumber = readNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Set<ChildPost> getChildPost() {
		return childPost;
	}

	public void setChildPost(Set<ChildPost> childPost) {
		this.childPost = childPost;
	}


	
}
