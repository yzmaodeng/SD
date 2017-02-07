package com.sd.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sd.dao.TeamApplyDao;
import com.sd.vo.Team;
import com.sd.vo.TeamApply;

@Repository
public class TeamApplyDaoImpl extends BaseDaoImpl<TeamApply, String> implements TeamApplyDao{
	/**
	 * 获得用户的团队申请信息
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Team getByUser(String userId) {
		Criteria criteria = getSession().createCriteria(TeamApply.class);
		criteria.add(Restrictions.eq("flag", "1"));
		criteria.add(Restrictions.eq("userId", userId));
		criteria.addOrder(Order.desc("createTime"));
		List<TeamApply> ret = criteria.list();
		if (ret.size() > 0){
			TeamApply apply = ret.get(0);
			return apply.getTeam();
		}
		
		return null;
	}
}
