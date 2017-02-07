package com.sd.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sd.dao.KfPatientDao;
import com.sd.vo.KfPatient;

@Repository
public class KfPatientDaoImpl extends BaseDaoImpl<KfPatient, String> implements KfPatientDao{
	/**
	 *根据患者ID 获取患者
	 * @param id  患者ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public KfPatient getByPatientId(String id) {
		Criteria criteria = getSession().createCriteria(KfPatient.class);
		criteria.add(Restrictions.eq("isdel", "0"));
		criteria.add(Restrictions.eq("id", id));
		List<KfPatient> ret = criteria.list();
		if (ret.size() > 0)
			return ret.get(0);
		return null;
	}
}
