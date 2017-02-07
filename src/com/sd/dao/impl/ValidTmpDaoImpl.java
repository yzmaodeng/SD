package com.sd.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sd.dao.ValidTmpDao;
import com.sd.vo.ValidTmp;

@Repository
public class ValidTmpDaoImpl extends BaseDaoImpl<ValidTmp, String> implements ValidTmpDao{

	/**
	 * 校验验证码
	 * @param mobile
	 * @param identityCode
	 */
	@SuppressWarnings("unchecked")
	public boolean checkValidTmp(String mobile, String identityCode) {
		Criteria criteria = getSession().createCriteria(ValidTmp.class);
		criteria.add(Restrictions.eq("phone", mobile));
		criteria.add(Restrictions.eq("validtext", identityCode));
//		criteria.add(Restrictions.eq("status", "0"));
		criteria.add(Restrictions.gt("validtime", new Date()));
		List<Object> ret = criteria.list();
		if (ret == null || ret.size()==0)
			return false;
		return true;
	}
	/**
	 * 获取本手机号最近一次发送的时间
	 * @param mobile
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Date getNewestDate(String mobile) {
		Criteria criteria = getSession().createCriteria(ValidTmp.class);
		criteria.add(Restrictions.eq("phone", mobile));
		criteria.addOrder(Order.desc("createtime"));
		List<ValidTmp> ret = criteria.list();
		if (ret == null || ret.size()==0)
			return null;
		ValidTmp newest = ret.get(0);
		if (newest == null || newest.getCreatetime() == null)
			return null;
		
		return newest.getCreatetime();
	}
	/**
	 * 获取本IP最近一次发送的时间
	 * @param mobile
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Date getNewestIPDate(String ip) {
		Criteria criteria = getSession().createCriteria(ValidTmp.class);
		criteria.add(Restrictions.eq("ipaddress", ip));
		criteria.addOrder(Order.desc("createtime"));
		List<ValidTmp> ret = criteria.list();
		if (ret == null || ret.size()==0)
			return null;
		ValidTmp newest = ret.get(0);
		if (newest == null || newest.getCreatetime() == null)
			return null;
		
		return newest.getCreatetime();
	}
	/**
	 * 获取本手机号今天发送的次数
	 * @param mobile
	 * @return
	 */
	public Integer getTodayCount(String mobile) {
		Criteria criteria = getSession().createCriteria(ValidTmp.class);
		criteria.add(Restrictions.eq("phone", mobile));
		criteria.add(Restrictions.sqlRestriction(" date_format(this_.vt_createtime,'%Y-%m-%d') = date_format(now(),'%Y-%m-%d') "));
		criteria.setProjection(Projections.rowCount());
		return Integer.parseInt(criteria.uniqueResult().toString());
	}
	/**
	 * 获取ip今天发送的次数
	 * @param mobile
	 * @return
	 */
	public Integer getTodayIPCount(String ip) {
		Criteria criteria = getSession().createCriteria(ValidTmp.class);
		criteria.add(Restrictions.eq("ipaddress", ip));
		criteria.add(Restrictions.sqlRestriction(" date_format(this_.vt_createtime,'%Y-%m-%d') = date_format(now(),'%Y-%m-%d') "));
		criteria.setProjection(Projections.rowCount());
		return Integer.parseInt(criteria.uniqueResult().toString());
	}
}
