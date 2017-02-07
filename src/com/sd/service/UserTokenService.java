package com.sd.service;

import javax.servlet.http.HttpServletRequest;

import com.sd.vo.UserToken;

public interface UserTokenService extends BaseService<UserToken, String> {

	/**
	 * 根据用户gid刷新token
	 * @param userGid
	 */
	UserToken freshToken(String userGid);
	/**
	 * 检查token是否有效
	 * @param userId
	 * @param token
	 * @return
	 */
	boolean checkToken(String userId, String token);
	/**
	 * 检查token是否有效
	 * @param userId
	 * @param token
	 * @return
	 */
	boolean checkToken(HttpServletRequest request);
	UserToken getByUserId(String gid);
	
}
