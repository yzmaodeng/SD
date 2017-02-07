package com.sd.dao.impl;

import java.math.BigInteger;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.sd.dao.AdminDao;
import com.sd.dao.ScoreDao;
import com.sd.dao.SignDao;
import com.sd.vo.Admin;
import com.sd.vo.Score;
import com.sd.vo.Sign;

@Repository
public class ScoreDaoImpl extends BaseDaoImpl<Score, String> implements ScoreDao{


	@Override
	public int todayNum(String s) {
		SQLQuery createSQLQuery = getSession().createSQLQuery(s);
		Object uniqueResult = createSQLQuery.uniqueResult();
		Integer ii= new Integer(String.valueOf((BigInteger)uniqueResult));
		return ii;
	}
	public int totleNum(String s) {
		SQLQuery createSQLQuery = getSession().createSQLQuery(s);
		Object uniqueResult = createSQLQuery.uniqueResult();
		if(uniqueResult!=null){
			int parseInt = Integer.parseInt(uniqueResult.toString());
			return parseInt;
			
		}else{
			return 0;
		}
	}
	
}
