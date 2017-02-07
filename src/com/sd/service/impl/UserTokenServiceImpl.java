package com.sd.service.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.sd.dao.UserDao;
import com.sd.dao.UserTokenDao;
import com.sd.service.UserTokenService;
import com.sd.vo.User;
import com.sd.vo.UserToken;

@Service
public class UserTokenServiceImpl extends BaseServiceImpl<UserToken, String> implements UserTokenService{
	@Resource(name = "userTokenDaoImpl")
	public void setBaseDao(UserTokenDao userTokenDao) {
		super.setBaseDao(userTokenDao);
	}
	@Resource private UserTokenDao userTokenDao;
	@Resource private UserDao userDao;
	
	/**
	 * 根据用户gid刷新token
	 * @param userGid
	 */
	public UserToken freshToken(String userGid) {
		return userTokenDao.freshToken(userGid);
	}

	/**
	 * 检查token是否有效
	 * @param userId
	 * @param token
	 * @return
	 */
	public boolean checkToken(String userId, String token) {
		if (StringUtils.isBlank(userId))
			return false;
		User user = userDao.get(userId);
		if (user == null)
			return false;
		return userTokenDao.checkToken(userId, token);
	}
	/**
	 * 检查token是否有效
	 * @param userId
	 * @param token
	 * @return
	 */
	public boolean checkToken(HttpServletRequest request) {
		String userId = request.getHeader("userid");
		String token = request.getHeader("token");
		return checkToken(userId, token);
	}

	@Override
	public UserToken getByUserId(String gid) {
		String sqlString="SELECT  * FROM tusertoken WHERE  tokenuserGID='"+gid+"'";
		return userTokenDao.getVoListBySql(sqlString).get(0);
	}
}
