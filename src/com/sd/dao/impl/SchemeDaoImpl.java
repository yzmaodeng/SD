package com.sd.dao.impl;

import java.math.BigInteger;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.sd.dao.SchemeDao;
import com.sd.vo.Scheme;

@Repository
public class SchemeDaoImpl extends BaseDaoImpl<Scheme, String> implements SchemeDao{

	@Override
	public int countMainNumBySql(String gid) {
		String s="select count(*) from tkf_scheme where ts_kfpid = '"+gid+"' and ts_ismain='1'";
		SQLQuery createSQLQuery = getSession().createSQLQuery(s);
		Object uniqueResult = createSQLQuery.uniqueResult();
		Integer ii= new Integer(String.valueOf((BigInteger)uniqueResult));
		return ii;
	}
}
