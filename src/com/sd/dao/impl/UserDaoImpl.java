package com.sd.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sd.dao.UserDao;
import com.sd.vo.User;

@Repository
public class UserDaoImpl extends BaseDaoImpl<User, String> implements UserDao{

	/**
	 * 通过手机号获取用户信息
	 * @param mobile
	 */
	@SuppressWarnings("unchecked")
	public User getUserByPhone(String mobile) {
		Criteria criteria = getSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("isdel", "1"));
		criteria.add(Restrictions.eq("phone", mobile));
		List<User> ret = criteria.list();
		if (ret.size() > 0)
			return ret.get(0);
		return null ;
	}

	/**
	 * 根据频道信息、用户ID、名字中一个或多个条件查询用户
	 * @param chGid
	 * @param userSelectGid
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<User> getUserList(String chGid, String userSelectGid,
			String name) {
		Criteria criteria = getSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("isdel", "1"));
		if (StringUtils.isNotBlank(chGid))
			criteria.add(Restrictions.sqlRestriction("EXISTS (SELECT 1 FROM tchanneluser t " +
					" WHERE this_.user_gid = t.cu_ugid and t.cu_cgid = '" + chGid + "')"));
		if (StringUtils.isNotBlank(userSelectGid))
			criteria.add(Restrictions.eq("gid", userSelectGid));
		if (StringUtils.isNotBlank(name))
			criteria.add(Restrictions.like("name", name, MatchMode.ANYWHERE));
		return criteria.list();
	}
	
}
