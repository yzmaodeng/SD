package com.sd.dao.impl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.sd.dao.BaseDao;
import com.sd.util.Conditions;
import com.sd.util.MyPage;
import com.sd.vo.BaseVo;

public class BaseDaoImpl<T, PK extends Serializable> implements BaseDao<T, PK> {
	private Class<T> entityClass;
	protected SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BaseDaoImpl() {
		Class c = getClass();
		Type type = c.getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			Type[] parameterizedType = ((ParameterizedType) type)
					.getActualTypeArguments();
			this.entityClass = (Class<T>) parameterizedType[0];
		}
	}

	@Resource(name = "sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public T get(PK id) {
		return (T) getSession().get(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	public T load(PK id) {
		return (T) getSession().load(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	public List<T> getAllList() {
		Criteria criteria = getSession().createCriteria(entityClass);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> getAllListOnDel(String isdel) {
		Criteria criteria = getSession().createCriteria(entityClass);
		criteria.add(Restrictions.eq("isdel", isdel));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> getConditonList(Map<String, String> condition, String order,
			boolean isDesc, MyPage myPage) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (String key : condition.keySet()) {
			criteria.add(Restrictions.eq(key, condition.get(key)));
		}
		if (StringUtils.isNotBlank(order)) {
			if (isDesc)
				criteria.addOrder(Order.desc(order));
			else
				criteria.addOrder(Order.asc(order));
		}
		if (myPage != null) {
			int page = myPage.getPage();
			int num = Integer.parseInt(myPage.getRows().get(0).toString());
			criteria.setFirstResult((page - 1) * num);
			criteria.setMaxResults(num);
		}
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> getConditonsList(List<Conditions> conditions, String order,
			boolean isDesc, MyPage myPage) {
		Criteria criteria = getSession().createCriteria(entityClass);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			for (Conditions myConditions : conditions) {
				if ("like".equals(myConditions.getType())) {
					Criterion cron = Restrictions.like(myConditions.getKey(),
							'%' + myConditions.getValue() + '%');
					criteria.add(cron);
				} else if ("timeA".equals(myConditions.getType())) {
					criteria.add(Restrictions.ge(myConditions.getKey(),
							sdf.parse(myConditions.getValue())));
				} else if ("timeB".equals(myConditions.getType())) {
					criteria.add(Restrictions.le(myConditions.getKey(),
							sdf.parse(myConditions.getValue())));
				} else if ("time".equals(myConditions.getType())) {
					criteria.add(Restrictions.eq(myConditions.getKey(),
							sdf.parse(myConditions.getValue())));
				} else if ("map".equals(myConditions.getType())) {
					for (String key : myConditions.getMap().keySet()) {
						if ("price".equals(key)) {
							criteria.add(Restrictions.eq("price", 0f));
						}else{
							
							criteria.add(Restrictions.eq(key, myConditions.getMap().get(key)));
						}
						
					}
				}
			}
			if (StringUtils.isNotBlank(order)) {
				if (isDesc)
					criteria.addOrder(Order.desc(order));
				else
					criteria.addOrder(Order.asc(order));
			}
			if (myPage != null) {
				int page = myPage.getPage();
				int num = Integer.parseInt(myPage.getRows().get(0).toString());
				criteria.setFirstResult((page - 1) * num);
				criteria.setMaxResults(num);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return criteria.list();
	}

	public String getConditonCount(Map<String, String> condition) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (String key : condition.keySet()) {
			criteria.add(Restrictions.eq(key, condition.get(key)));
		}
		criteria.setProjection(Projections.rowCount());
		return criteria.uniqueResult().toString();
	}

	public String getConditonsCount(List<Conditions> conditions) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (Conditions myConditions : conditions) {
			if ("like".equals(myConditions.getType())) {
				Criterion cron = Restrictions.like(myConditions.getKey(),
						'%' + myConditions.getValue() + '%');
				criteria.add(cron);
			} else if ("timeA".equals(myConditions.getType())) {
				criteria.add(Restrictions.ge(myConditions.getKey(),
						Date.valueOf(myConditions.getValue())));
			} else if ("timeB".equals(myConditions.getType())) {
				criteria.add(Restrictions.le(myConditions.getKey(),
						Date.valueOf(myConditions.getValue())));
			} else if ("map".equals(myConditions.getType())) {
				for (String key : myConditions.getMap().keySet()) {
					criteria.add(Restrictions.eq(key, myConditions.getMap()
							.get(key)));
				}
			}
		}
		criteria.setProjection(Projections.rowCount());
		return criteria.uniqueResult().toString();
	}

	public Long getTotalCount() {
		String hql = "select count(*) from " + entityClass.getName();
		return (Long) getSession().createQuery(hql).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public PK save(T entity) {
		if (entity instanceof BaseVo) {
			try {
				Method method = entity.getClass().getMethod(
						BaseVo.ON_SAVE_METHOD_NAME);
				method.invoke(entity);
				PK ret = (PK) getSession().save(entity);
				return ret;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	public void update(T entity) {
		if (entity instanceof BaseVo) {
			try {
				Method method = entity.getClass().getMethod(
						BaseVo.ON_UPDATE_METHOD_NAME);
				method.invoke(entity);
				getSession().update(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void delete(T entity) {
		getSession().delete(entity);
	}

	@SuppressWarnings("unchecked")
	public void delete(PK id) {
		T entity = (T) getSession().load(entityClass, id);
		getSession().delete(entity);
	}

	@SuppressWarnings("unchecked")
	public void delete(PK[] ids) {
		for (PK id : ids) {
			T entity = (T) getSession().load(entityClass, id);
			getSession().delete(entity);
		}
	}

	/**
	 * 根据sql返回特定位置的值
	 * 
	 * @param sql
	 * @param position
	 *            字段位置
	 * @return 结果集第一条position位置的值
	 */
	@SuppressWarnings("unchecked")
	public String getSingleBysql(String sql, int position) {
		if (sql == null || position < 0)
			return "";
		Query query = getSession().createSQLQuery(sql);
		if (StringUtils.isBlank(query.getQueryString()))
			return "";
		List<Object[]> result = query.list();
		if (result != null && result.size() > 0 && result.get(0) != null) {
			if (result.get(0).length > position) {
				return result.get(0)[position] == null ? ""
						: result.get(0)[position].toString();
			} else {
				return "";
			}
		}
		return "";
	}

	/**
	 * 原生sql
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getListBySql(String sql) {
		return getSession().createSQLQuery(sql).list();
	}

	/**
	 * 根据sql获得相应结果集
	 */
	@SuppressWarnings("unchecked")
	public List<T> getVoListBySql(String sql) {
		return getSession().createSQLQuery(sql).addEntity(entityClass).list();
	}

	/**
	 * 执行update sql
	 */
	public void executeUpdateSql(String sql) {
		getSession().createSQLQuery(sql).executeUpdate();
	}

	public void flush() {
		getSession().flush();
	}

	public void evict(Object object) {
		getSession().evict(object);
	}

	public void clear() {
		getSession().clear();
	}

	public List HQLSqlBindParameter(String hql, List list) {
		Session session = getSession();
		Query createQuery = session.createQuery(hql);
		int i = 0;
		for (Object object : list) {
			createQuery.setParameter(i++, object);
		}
		return createQuery.list();
	}

	public List<T> executeSql(String sql, String keyword, String type) {
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.addEntity(entityClass);
		sqlQuery.setParameter(0, keyword);
		sqlQuery.setParameter(1, type);
		return sqlQuery.list();
	}


}
