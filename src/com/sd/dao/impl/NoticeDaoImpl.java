package com.sd.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sd.dao.NoticeDao;
import com.sd.util.ComUtil;
import com.sd.vo.Notice;

@Repository
public class NoticeDaoImpl extends BaseDaoImpl<Notice, String> implements NoticeDao{
	/**
	 * 根据用户和帖子信息获得未读消息
	 * @param user
	 * @param gid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Notice getByUserPost(String user, String gid) {
		Criteria criteria = getSession().createCriteria(Notice.class);
		criteria.add(Restrictions.eq("userId", user));
		criteria.add(Restrictions.eq("postId", gid));
		criteria.add(Restrictions.eq("isRead", "0"));
		List<Notice> ret = criteria.list();
		if (ret.size() == 0)
			return null;
		else
			return ret.get(0);
	}
	/**
	 * 获取消息列表
	 * @param userId
	 * @param dateBegin
	 * @param count
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Notice> getNoticeList(String userId, String dateBegin,
			String count, String type) {
		Criteria criteria = getSession().createCriteria(Notice.class);
		int c = 10;
		int maxc = 100;
		criteria.add(Restrictions.eq("userId", userId));
		if (StringUtils.isNotBlank(dateBegin))
			criteria.add(Restrictions.lt("createTime", ComUtil.str2Time(dateBegin)));
		if ("0".equals(type)){
			criteria.add(Restrictions.eq("isRead", "0"));
		} 
			
		criteria.addOrder(Order.desc("createTime"));
		if (StringUtils.isNotBlank(count)){
			try{
				c = Integer.parseInt(count);
				c = c > maxc ? maxc : c;
			} catch (Exception e){}
		}
		criteria.setMaxResults(c);
		return criteria.list();
	}
}
