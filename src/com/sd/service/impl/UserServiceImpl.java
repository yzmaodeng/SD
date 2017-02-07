package com.sd.service.impl;

import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.sd.dao.UserDao;
import com.sd.service.UserService;
import com.sd.vo.User;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, String> implements UserService{
	@Resource
	public void setBaseDao(UserDao userDao) {
		super.setBaseDao(userDao);
	}
	@Resource private UserDao userDao;
	
	/**
	 * 通过手机号获取用户信息
	 * @param mobile
	 */
	public User getUserByPhone(String mobile) {
		return userDao.getUserByPhone(mobile);
	}

	/**
	 * 生成用户古典号
	 */
	public String getRandomUserId(){
		long init = 10101010;
		StringBuffer sql = new StringBuffer();
		sql.append("select max(t.user_id),'' from tuser t ");
		String max = userDao.getSingleBysql(sql.toString(), 0);
		try{
			init = Long.parseLong(max);
		} catch (Exception e){}
		int incremental = new Random().nextInt(10)*10+new Random().nextInt(10);
		long ret = init + incremental;
		
		sql.delete(0, sql.length());
		sql.append("select count(*),'' from tuser t ")
			.append("where t.user_id = '")
			.append(ret).append("' ");
		String check = userDao.getSingleBysql(sql.toString(), 0);
		if ("0".equals(check))
			return ret+"";
		else
			return getRandomUserId();
	}
	/**
	 * 根据频道信息、用户ID、名字中一个或多个条件查询用户
	 * @param chGid
	 * @param userSelectGid
	 * @param name
	 * @return
	 */
	public List<User> getUserList(String chGid, String userSelectGid,
			String name) {
		return userDao.getUserList(chGid, userSelectGid, name);
	}

	/**
	 * 检查医生和患者的关系
	 * 团患关系优先，即医生所在团队和患者存在关系
	 * 如果医生无团队，或者医生所在团队和患者无关系，定为医患关系
	 * @param pid	患者gid
	 * @param user	医生
	 * @return	1-团患关系，2-医患关系
	 */
	public int checkRelationship(String pid, User user) {
		if (StringUtils.isBlank(user.getTeamId()))
			return 2;	// 医生无团队，必然无团患关系
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*), '' from tteam_kfpatient t ")
			.append(" where t.ttpi_tgid = '").append(user.getTeamId()).append("' ")
			.append(" and t.ttpi_pgid = '").append(pid).append("' ")
			.append(" and t.isdel = '0' ");
		String count = userDao.getSingleBysql(sql.toString(), 0);
		
		if ("0".equals(count))	// 医生所在团队和患者无关系
			return 2;
		
		return 1;
	}


	public void changeScore(String userId, int score) {
		String s="update  tuser   set score=score+"+score+"  where User_GID='"+userId+"'";
		userDao.executeUpdateSql(s);
	}

	@Override
	public void reduceScore(String uid, Long credits) {
		String s="update  tuser   set score=score-"+credits+"  where User_GID='"+uid+"'";
		userDao.executeUpdateSql(s);
		
	}


}
