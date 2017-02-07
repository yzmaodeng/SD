package com.sd.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sd.dao.ReplyDao;
import com.sd.vo.Reply;

@Repository
public class ReplyDaoImpl extends BaseDaoImpl<Reply, String> implements ReplyDao{

	/**
	 * 根据帖子id查询回复列表
	 * @param gid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Reply> getReplyClient(String gid) {
		Criteria criteria = getSession().createCriteria(Reply.class);
		criteria.add(Restrictions.eq("isdel", "1"));
		criteria.add(Restrictions.eq("postGid", gid));
		criteria.addOrder(Order.desc("createTime"));
		return criteria.list();
	}

}
