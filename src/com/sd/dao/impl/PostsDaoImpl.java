package com.sd.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sd.dao.PostsDao;
import com.sd.util.ComUtil;
import com.sd.vo.Posts;

@Repository
public class PostsDaoImpl extends BaseDaoImpl<Posts, String> implements PostsDao{

	/**
	 * 查询频道帖子
	 * @param chGid
	 * @param dateBegin
	 * @param count
	 * @param isFav
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Posts> getPostsList(String chGid,String mark, String dateBegin,
			String count, String isFav, String userId,String orderNum) {
		Criteria criteria = getSession().createCriteria(Posts.class);
		

		//没有删除
		criteria.add(Restrictions.eq("isdel", "1"));
		if (StringUtils.isNotBlank(chGid)){
			//频道gid
			criteria.add(Restrictions.eq("channel.gid", chGid));
		}
//		if (StringUtils.isNotBlank(dateBegin)){
//			//创建的时间
//			criteria.add(Restrictions.lt("createTime", ComUtil.str2Time(dateBegin)));
//		}
		if (StringUtils.isNotBlank(mark)){
			if("其他".equals(mark)){
				criteria.add(Restrictions.sqlRestriction("mark is null or mark=''"));
			}else{
					criteria.add(Restrictions.sqlRestriction("locate('"+mark+"',mark)!=0"));
				
			}
		}
		if ("1".equals(isFav)){
			//添加收藏信息
			criteria.add(Restrictions.sqlRestriction(" EXISTS (select * from tfav c " +
					"where c.fav_isdel='1' and this_.pst_gid = c.fav_gid and c.fav_usergid='"+userId+"')"));
		} 
//		else if (StringUtils.isBlank(chGid)) {
//			// 非单一频道，且非收藏
//			// 需求调整，返回全部帖子
//			//criteria.add(Restrictions.eq("isRec", "1"));
//		}

               count = "10";//默认10条
		if (StringUtils.isNotBlank(orderNum)&&"2".equals(orderNum)){
			if (StringUtils.isNotBlank(dateBegin)&&!("".equals(dateBegin))){
				//创建的时间
				criteria.add(Restrictions.lt("readNumber", Integer.parseInt(dateBegin)));
			}
			criteria.add(Restrictions.sqlRestriction(" 1=1 order by readNumber desc limit " + count));
		}else if(StringUtils.isNotBlank(orderNum)&&"3".equals(orderNum)){
			if (StringUtils.isNotBlank(dateBegin)&&!("".equals(dateBegin))){
				//创建的时间
				criteria.add(Restrictions.lt("replyCount", Integer.parseInt(dateBegin)));
			}
			criteria.add(Restrictions.sqlRestriction(" 1=1 order by  pst_replycount desc limit " + count));
		}else{
			if (StringUtils.isNotBlank(dateBegin)&&!("".equals(dateBegin))){
				//创建的时间
				criteria.add(Restrictions.lt("createTime", ComUtil.str2Time(dateBegin)));
			}
			criteria.add(Restrictions.sqlRestriction(" 1=1 order by top desc ,pst_createtime desc limit " + count));
		}
		return criteria.list();
	}
	/**
	 * 查询用户的帖子
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Posts> getPostsList(String userId) {
		Criteria criteria = getSession().createCriteria(Posts.class);
		criteria.add(Restrictions.eq("isdel", "1"));
		criteria.add(Restrictions.eq("userGid", userId));
		return criteria.list();
	}
	@Override
	public int todayNum(String s) {
		SQLQuery createSQLQuery = getSession().createSQLQuery(s);
		Object uniqueResult = createSQLQuery.uniqueResult();
		Integer ii= new Integer(String.valueOf((BigInteger)uniqueResult));
		return ii;
	}

}
