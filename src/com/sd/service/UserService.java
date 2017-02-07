package com.sd.service;

import java.util.List;

import com.sd.vo.User;

public interface UserService extends BaseService<User, String> {

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
	/**
	 * 生成用户ID,骨典号
	 * @return
	 */
	String getRandomUserId();
	/**
	 * 检查医生和患者的关系
	 * 团患关系优先，即医生所在团队和患者存在关系
	 * 如果医生无团队，或者医生所在团队和患者无关系，定为医患关系
	 * @param pid	患者gid
	 * @param user	医生
	 * @return	1-团患关系，2-医患关系
	 */
	int checkRelationship(String pid, User user);
	void changeScore(String userId, int score);
	void reduceScore(String uid, Long credits);
	
	
}
