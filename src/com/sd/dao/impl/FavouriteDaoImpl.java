package com.sd.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sd.dao.FavouriteDao;
import com.sd.vo.Favourite;

@Repository
public class FavouriteDaoImpl extends BaseDaoImpl<Favourite, String> implements FavouriteDao{
	/**
	 * 查询会议是否被收藏
	 * @param gid
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String isFav(String gid, String userId) {
		Criteria criteria = getSession().createCriteria(Favourite.class);
		criteria.add(Restrictions.eq("isdel", "1"));
		criteria.add(Restrictions.eq("gid", gid));
		criteria.add(Restrictions.eq("userGid", userId));
		List<Object> ret = criteria.list();
		if (ret == null || ret.size()==0)
			return "2";//未收藏
		return "1";
	}
	public void updateFav(String gid, String userId){
		Query query =getSession().createSQLQuery("update tfav t set t.fav_isdel =0 where fav_GID='"+gid+"' and fav_userGID='"+userId+"'");
		query.executeUpdate();
	}
	
	
}
