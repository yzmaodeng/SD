package com.sd.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sd.dao.TemplateDetailDao;
import com.sd.vo.TemplateDetail;

@Repository
public class TemplateDetailDaoImpl extends BaseDaoImpl<TemplateDetail, String> implements TemplateDetailDao{

	/**
	 * 获取模版中具体某一天的内容
	 * @param gid
	 * @param bet
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public TemplateDetail getOne(String gid, long bet) {
		Criteria criteria = getSession().createCriteria(TemplateDetail.class);
		criteria.add(Restrictions.eq("pid", gid));
		criteria.add(Restrictions.eq("ordering", bet + ""));
		criteria.add(Restrictions.eq("isdel", "0"));
		List<TemplateDetail> ret = criteria.list();
		if (ret.size() == 0)
			return null;
		return ret.get(0);
	}

}
