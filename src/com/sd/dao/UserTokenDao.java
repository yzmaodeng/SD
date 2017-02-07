package com.sd.dao;

import com.sd.vo.UserToken;

public interface UserTokenDao extends BaseDao<UserToken, String> {

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
	
}
