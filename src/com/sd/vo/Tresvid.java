package com.sd.vo;

// Generated 2015-5-18 9:04:31 by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.Date;

/**
 * Tresvid generated by hbm2java
 */
public class Tresvid implements java.io.Serializable {

	private Integer rvId;
	private String rvUrl;
	private Integer rvUid;
	private Date rvDate;
	private String rvIp;
	private String rvProtocol;
	private Integer rvWidth;
	private Integer rvHeight;
	private BigDecimal rvAngle;
	private BigDecimal rvFps;
	private Integer rvSize;
	private String rvNewurl;
	private String rvThumbnail;
	private Date rvUpdatetime;
	private Integer rvIsdel;

	public Tresvid() {
	}

	public Tresvid(String rvThumbnail) {
		this.rvThumbnail = rvThumbnail;
	}

	public Tresvid(String rvUrl, Integer rvUid, Date rvDate, String rvIp,
			String rvProtocol, Integer rvWidth, Integer rvHeight,
			BigDecimal rvAngle, BigDecimal rvFps, Integer rvSize,
			String rvNewurl, String rvThumbnail, Date rvUpdatetime,
			Integer rvIsdel) {
		this.rvUrl = rvUrl;
		this.rvUid = rvUid;
		this.rvDate = rvDate;
		this.rvIp = rvIp;
		this.rvProtocol = rvProtocol;
		this.rvWidth = rvWidth;
		this.rvHeight = rvHeight;
		this.rvAngle = rvAngle;
		this.rvFps = rvFps;
		this.rvSize = rvSize;
		this.rvNewurl = rvNewurl;
		this.rvThumbnail = rvThumbnail;
		this.rvUpdatetime = rvUpdatetime;
		this.rvIsdel = rvIsdel;
	}

	public Integer getRvId() {
		return this.rvId;
	}

	public void setRvId(Integer rvId) {
		this.rvId = rvId;
	}

	public String getRvUrl() {
		return this.rvUrl;
	}

	public void setRvUrl(String rvUrl) {
		this.rvUrl = rvUrl;
	}

	public Integer getRvUid() {
		return this.rvUid;
	}

	public void setRvUid(Integer rvUid) {
		this.rvUid = rvUid;
	}

	public Date getRvDate() {
		return this.rvDate;
	}

	public void setRvDate(Date rvDate) {
		this.rvDate = rvDate;
	}

	public String getRvIp() {
		return this.rvIp;
	}

	public void setRvIp(String rvIp) {
		this.rvIp = rvIp;
	}

	public String getRvProtocol() {
		return this.rvProtocol;
	}

	public void setRvProtocol(String rvProtocol) {
		this.rvProtocol = rvProtocol;
	}

	public Integer getRvWidth() {
		return this.rvWidth;
	}

	public void setRvWidth(Integer rvWidth) {
		this.rvWidth = rvWidth;
	}

	public Integer getRvHeight() {
		return this.rvHeight;
	}

	public void setRvHeight(Integer rvHeight) {
		this.rvHeight = rvHeight;
	}

	public BigDecimal getRvAngle() {
		return this.rvAngle;
	}

	public void setRvAngle(BigDecimal rvAngle) {
		this.rvAngle = rvAngle;
	}

	public BigDecimal getRvFps() {
		return this.rvFps;
	}

	public void setRvFps(BigDecimal rvFps) {
		this.rvFps = rvFps;
	}

	public Integer getRvSize() {
		return this.rvSize;
	}

	public void setRvSize(Integer rvSize) {
		this.rvSize = rvSize;
	}

	public String getRvNewurl() {
		return this.rvNewurl;
	}

	public void setRvNewurl(String rvNewurl) {
		this.rvNewurl = rvNewurl;
	}

	public String getRvThumbnail() {
		return this.rvThumbnail;
	}

	public void setRvThumbnail(String rvThumbnail) {
		this.rvThumbnail = rvThumbnail;
	}

	public Date getRvUpdatetime() {
		return this.rvUpdatetime;
	}

	public void setRvUpdatetime(Date rvUpdatetime) {
		this.rvUpdatetime = rvUpdatetime;
	}

	public Integer getRvIsdel() {
		return this.rvIsdel;
	}

	public void setRvIsdel(Integer rvIsdel) {
		this.rvIsdel = rvIsdel;
	}

}
