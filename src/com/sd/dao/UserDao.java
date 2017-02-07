package com.sd.dao;

import java.util.List;

import com.sd.vo.User;

public interface UserDao extends BaseDao<User, String> {

	/**
	 * 通过手机号获取用户信息
	 * @param mobile
	 */
	User getUserByPhone(String mobile);

	/**
	 * 根据频道信息、用户ID、名字中一个或多个条件查询用户
	 * @param chGid
	 * @param userSelectGid
	 * @param name
	 * @return
	 */
	List<User> getUserList(String chGid, String userSelectGid, String name);
	
}
