package com.sd.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sd.dao.UserTokenDao;
import com.sd.util.ComUtil;
import com.sd.vo.UserToken;

@Repository
public class UserTokenDaoImpl extends BaseDaoImpl<UserToken, String> implements UserTokenDao{

	/**
	 * 根据用户gid刷新token
	 * @param userGid
	 */
	public UserToken freshToken(String userGid) {
		Date now = new Date();
		
		UserToken token = this.get(userGid);
		if (token == null){
			// 不存在即创建新token
			token = new UserToken();
			token.setCreateTime(now);
			token.setUserGid(userGid);
			token.setContent(ComUtil.getuuid());
			token.setStartTime(now);
			token.setEndTime(new Date(now.getTime() + 365*24*60*60*1000));
			token.setUpdateTime(now);
			save(token);
		}else{
			token.setContent(ComUtil.getuuid());
			token.setStartTime(now);
			token.setEndTime(new Date(now.getTime() + 365*24*60*60*1000));
			token.setUpdateTime(now);
			update(token);
		}
		this.flush();
		return token;
	}

	/**
	 * 检查token是否有效
	 * @param userId
	 * @param token
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean checkToken(String userId, String token) {
		Criteria criteria = getSession().createCriteria(UserToken.class);
		criteria.add(Restrictions.eq("userGid", userId));
		criteria.add(Restrictions.eq("content", token));
		criteria.add(Restrictions.gt("endTime", new Date()));
		List<Object> ret = criteria.list();
		if (ret == null || ret.size() == 0)
			return false;
		return true;
	}
	
}
