package com.sd.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sd.dao.KfPatientSickDao;
import com.sd.vo.KfPatientSick;

@Repository
public class KfPatientSickDaoImpl extends BaseDaoImpl<KfPatientSick, String> implements KfPatientSickDao{

	/**
	 * 获取患者疾病
	 * @param pid
	 * @param oid 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public KfPatientSick get(String pid, String oid, int type) {
		Criteria criteria = getSession().createCriteria(KfPatientSick.class);
		criteria.add(Restrictions.eq("isdel", "0"));
		criteria.add(Restrictions.eq("pid", pid));
		criteria.add(Restrictions.eq("oid", oid));
		criteria.add(Restrictions.eq("type", type + ""));
		
		List<KfPatientSick> ret = criteria.list();
		if (ret.size() > 0)
			return ret.get(0);
		return null;
	}

}
