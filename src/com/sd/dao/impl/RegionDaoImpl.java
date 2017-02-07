package com.sd.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sd.dao.RegionDao;
import com.sd.vo.Area;
@Repository
public class RegionDaoImpl implements RegionDao {
	@Resource 
	private SessionFactory sessionFactory;
	@SuppressWarnings("unchecked")
	public List<Area> queryRegion(Area tarea){
		Session session = sessionFactory.openSession();
		Criteria criteria=session.createCriteria(Area.class);
		criteria.add(Restrictions.eq("taLevel",tarea.getTaLevel()));//eq是等于，gt是大于，lt是小于,or是或
		if(tarea.getTaCode()!=null){
			Criterion cron = Restrictions.like("taCode",tarea.getTaCode()+'%');
			criteria.add(cron);
		}
		List<Area> list= criteria.list();
		session.close();
		return list;
	}
//	@Override
//	public void abc(Posts posts){
//		sessionFactory.getCurrentSession().save(posts);
//	}
}
