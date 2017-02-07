package com.sd.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sd.dao.KfDataDao;
import com.sd.vo.KfData;

@Repository
public class KfDataDaoImpl extends BaseDaoImpl<KfData, String> implements KfDataDao{
	public List<KfData> getDateBySql(Map<String, String> condition){
		String sql="select * from tkfpatient_data d where pid='"+condition.get("pid")
			+"' and teamGid='"+condition.get("teamGid")+"' and leg='"+condition.get("leg")
			+"' order by (select tm_ordering from tkf_motion where tm_smgid=d.tpd_mgid)";
		List<KfData> list = getSession().createSQLQuery(sql).list();  
		return list;
	}
	
	public List<KfData> getDate(String teamGid,String time){
		String sql="select tpd_id, leg, teamGid, pid, tpd_mgid, tpd_num, tpd_type, tpd_score, days, updatetime, createtime,max(STR_TO_DATE(tpd_date,'%Y-%m-%d ')) as tpd_date, isdel  from tkfpatient_data where STR_TO_DATE(tpd_date,'%Y-%m-%d')>'"+time+"' and teamGid='"+teamGid+"' GROUP BY pid  ORDER BY tpd_date desc";
		List<KfData> list = getSession().createSQLQuery(sql).addEntity(KfData.class).list();  
		return list;
	}
	public List<KfData> queryTodayData() {
		String Sql="select * from tkfpatient_data where date(tpd_date) = curdate() and  teamGid<>'' and teamGid is not null  order by leg desc,teamGid desc,pid desc,tpd_mgid desc";
		return this.getVoListBySql(Sql);
	}
}
