package com.sd.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sd.dao.PostFavDao;
import com.sd.vo.PostFav;

@Repository
public class PostFavDaoImpl extends BaseDaoImpl<PostFav, String> implements PostFavDao{

	/**
	 * 获取帖子是否点赞
	 * @param gid
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String isPraised(String gid, String userId) {
		Criteria criteria = getSession().createCriteria(PostFav.class);
		criteria.add(Restrictions.eq("isdel", "1"));
		criteria.add(Restrictions.eq("postGid", gid));
		criteria.add(Restrictions.eq("userGid", userId));
		List<Object> ret = criteria.list();
		if (ret == null || ret.size()==0)
			return "0";//未点赞
		return "1";
	}
	
}
