package com.sd.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sd.dao.TeamKfpatientDao;
import com.sd.vo.TeamKfpatient;

@Repository
public class TeamKfpatientDaoImpl extends BaseDaoImpl<TeamKfpatient, String> implements TeamKfpatientDao{
	/**
	 * 获取患者ID获取团患关系的
	 * @param gid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public TeamKfpatient getByGId(String gid) {
		Criteria criteria = getSession().createCriteria(TeamKfpatient.class);
		criteria.add(Restrictions.eq("isdel", "0"));
		criteria.add(Restrictions.eq("pgid", gid));
		List<TeamKfpatient> ret = criteria.list();
		if (ret.size() > 0)
			return ret.get(0);
		return null;
	}
}
