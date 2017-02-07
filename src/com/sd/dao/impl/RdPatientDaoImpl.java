package com.sd.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sd.dao.RdPatientDao;
import com.sd.vo.RdPatient;

@Repository
public class RdPatientDaoImpl extends BaseDaoImpl<RdPatient, String> implements RdPatientDao{

	/**
	 * 查询用户的病例
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RdPatient> getmyPatients(String userId) {
		Criteria criteria = getSession().createCriteria(RdPatient.class);
		criteria.add(Restrictions.eq("isdel", "1"));
		criteria.add(Restrictions.eq("userGid", userId));
		criteria.add(Restrictions.sqlRestriction("(teamGid is null or teamGid='') "));
		criteria.addOrder(Order.desc("visitDate"));
		return criteria.list();
	}

}
