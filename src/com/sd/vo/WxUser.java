package com.sd.vo;

import java.util.Calendar;
import java.util.Date;

public class WxUser {
	
	private String userId;
	private String openId;
	private String accessToken;
	private String refreshToken;
	private Date expireDate;
	
	/**
	 * 此构造方法为了测试方便添加，生产环境不通过此处实例化。
	 * @param userId
	 * @param openId
	 */
	public WxUser(String userId, String openId) {
		this.userId = userId;
		this.openId = openId;
	}
	
	public WxUser(String userId, String accessToken, String refreshToken, int expiresIn, String openId) {
		this.userId = userId;
		this.openId = openId;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, expiresIn);
		this.expireDate = calendar.getTime();
	}

	public String getUserId() {
		return userId;
	}

	public String getOpenId() {
		return openId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public Date getExpireDate() {
		return expireDate;
	}
}