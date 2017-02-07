package com.sd.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sd.dao.OrderInfoDao;
import com.sd.vo.OrderInfo;

@Repository
public class OrderInfoDaoImpl extends BaseDaoImpl<OrderInfo,String> implements OrderInfoDao{
	
	@SuppressWarnings("unchecked")
	public List<OrderInfo> getOrderListByQbc(String userGid,int type){
		Criteria criteria = getSession().createCriteria(OrderInfo.class);
		criteria.add(Restrictions.eq("userGid", userGid));
		criteria.add(Restrictions.eq("type", type));
		criteria.add(Restrictions.ne("status",0));
		return criteria.list();
	}
	@SuppressWarnings("unchecked")
	public List<OrderInfo> getOrderCourse(String userGid,int type,String orderType,String gid){
		Criteria criteria = getSession().createCriteria(OrderInfo.class);
		criteria.add(Restrictions.eq("userGid", userGid));
		criteria.add(Restrictions.eq("type", type));
		criteria.add(Restrictions.eq("orderType", orderType));
		criteria.add(Restrictions.eq("academyGid", gid));
		
		criteria.add(Restrictions.eq("status",2 ));
		return criteria.list();
	}
	
	public List<OrderInfo> getOrderCourseOrNum(String userGid,int type,String orderType,String gid){
		Criteria criteria = getSession().createCriteria(OrderInfo.class);
		criteria.add(Restrictions.eq("userGid", userGid));
		criteria.add(Restrictions.eq("type", type));
		criteria.add(Restrictions.eq("orderType", orderType));
		criteria.add(Restrictions.eq("academyGid", gid));
		criteria.add(Restrictions.eq("status",1));
		return criteria.list();
	}
	
	public int updateOrderStatus(String gid,int status){
		try{
			Query query =getSession().createSQLQuery("update orderInfo o set o.status ="+status+" where o.gid='"+gid+"'");
			if(status==2){
				//根据订单号更新状态
				query =getSession().createSQLQuery("update orderInfo o set o.status ="+status+" where o.orderNum='"+gid+"'");
			}
			query.executeUpdate();
			return 1;
		}catch (Exception e) {
			return 0;
		}
	}

	@Override
	public int totleNum(String string) {
		SQLQuery createSQLQuery = getSession().createSQLQuery(string);
		Object uniqueResult = createSQLQuery.uniqueResult();
		if(uniqueResult!=null){
			int parseInt = Integer.parseInt(uniqueResult.toString());
			return parseInt;
			
		}else{
			return 0;
		}
	}
	}

