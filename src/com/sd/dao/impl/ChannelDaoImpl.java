package com.sd.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sd.dao.ChannelDao;
import com.sd.vo.Channel;
import com.sd.vo.User;

@Repository
public class ChannelDaoImpl extends BaseDaoImpl<Channel, String> implements ChannelDao{

	/**
	 * 查询频道中用户列表
	 * @param gid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<User> getUserList(String gid) {
		Criteria criteria = getSession().createCriteria(User.class);
		criteria.add(Restrictions.sqlRestriction("EXISTS (SELECT 1 FROM tchanneluser t " +
				" WHERE this_.user_gid = t.cu_ugid and t.cu_cgid = '"+gid+"')"));
		return criteria.list();
	}
	/**
	 * 查询用户所属的频道列表
	 * @param gid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Channel> getChannelList(String gid) {
		Criteria criteria = getSession().createCriteria(Channel.class);
		criteria.add(Restrictions.sqlRestriction("EXISTS (SELECT 1 FROM tchanneluser t " +
				" WHERE this_.ch_Gid = t.cu_cgid and t.cu_ugid = '"+gid+"')"));
		return criteria.list();
	}
	/**
	 * 查询频道列表
	 * @param pageNum
	 * @param count
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Channel> getAllList(String pageNum, String count) {
		int p = 1;
		int c = 20;
		int maxc = 100;// 每页最多100条
		Criteria criteria = getSession().createCriteria(Channel.class);
		criteria.add(Restrictions.eq("isdel", "1"));
		if (StringUtils.isNotBlank(pageNum)){
			try{
				p = Integer.parseInt(pageNum);
			} catch (Exception e){}
		}
		if (StringUtils.isNotBlank(count)){
			try{
				c = Integer.parseInt(count);
				c = c > maxc ? maxc : c;
			} catch (Exception e){}
		}
		criteria.add(Restrictions.sqlRestriction(" 1=1 order by this_.ch_createtime limit "+(p-1)*c+","+c));
		
		return criteria.list();
	}
	
}
