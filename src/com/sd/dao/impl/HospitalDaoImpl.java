package com.sd.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sd.dao.HospitalDao;
import com.sd.vo.Thospital;
@Repository
public class HospitalDaoImpl extends BaseDaoImpl<Thospital,String> implements HospitalDao {
	@Resource 
	private SessionFactory sessionFactory;
	@SuppressWarnings("unchecked")
	public List<Thospital> query(Thospital thospital){
		Session session = sessionFactory.openSession();
		Criteria criteria=session.createCriteria(Thospital.class);
		if(thospital.getHosCode()!=null)criteria.add(Restrictions.eq("hosCode",thospital.getHosCode()));//eq是等于，gt是大于，lt是小于,or是或
		if(thospital.getHosLocal()!=null)criteria.add(Restrictions.eq("hosLocal",thospital.getHosLocal()));
		if(thospital.getHosName()!=null){
			Criterion cron = Restrictions.like("hosName",'%'+thospital.getHosName()+'%');
			criteria.add(cron);
		}
		List<Thospital> list= criteria.list();
		session.close();
		return list;
	}
}
