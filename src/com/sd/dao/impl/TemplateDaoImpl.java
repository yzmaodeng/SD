package com.sd.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sd.dao.TemplateDao;
import com.sd.vo.Template;

@Repository
public class TemplateDaoImpl extends BaseDaoImpl<Template, String> implements TemplateDao{
	/**
	 * 获取团队下的模版
	 * @param teamId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Template> getTmplByTeam(String teamId) {
		Criteria criteria = getSession().createCriteria(Template.class);
		criteria.add(Restrictions.eq("isdel", "0"));
		criteria.add(Restrictions.sqlRestriction(" exists (select 1 from tuser t " +
				" where t.User_GID = this_.tw_user " +
				" and t.User_isdel='1' " +
				" and t.user_tmau ='" + teamId + "')"));
		return criteria.list();
	}
	/**
	 * 获取团队下的模版
	 * @param teamId
	 * @return
	 */
	public String getTmplNumByTeam(String teamId) {
		Criteria criteria = getSession().createCriteria(Template.class);
		criteria.add(Restrictions.eq("isdel", "0"));
		criteria.add(Restrictions.sqlRestriction(" exists (select 1 from tuser t " +
				" where t.User_GID = this_.tw_user " +
				" and t.User_isdel='1' " +
				" and t.user_tmau ='" + teamId + "')"));
		criteria.setProjection(Projections.rowCount());
		return criteria.uniqueResult().toString();
	}
}
