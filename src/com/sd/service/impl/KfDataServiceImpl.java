package com.sd.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.KfDataDao;
import com.sd.service.KfDataService;
import com.sd.service.MotionService;
import com.sd.util.ComUtil;
import com.sd.vo.KfData;

@Service
public class KfDataServiceImpl extends BaseServiceImpl<KfData, String> implements KfDataService{
	@Resource
	public void setBaseDao(KfDataDao kfData) {
		super.setBaseDao(kfData);
	}
	@Resource private KfDataDao kfDataDao;
	@Resource private MotionService motionService;
	public List<KfData> getDateBySql(Map<String, String> condition){
		return kfDataDao.getDateBySql(condition);
	}
	public List<KfData> getDate(String teamGid,String time){
		return kfDataDao.getDate(teamGid,time);
	}
	/**
	 * 根据动作和类型
	 * @param id
	 * @param type
	 * @return
	 */
	public List<Object[]> getDataByType(String id, String type,String leg,String teamIdString, String gid) {
		String now = ComUtil.date2Str(new Date());
		StringBuffer sql = new StringBuffer();
		if ("1".equals(type)){// 日
			sql.append(" SELECT @rownum\\:=@rownum + 1 AS rownum,t.tpd_type,t.tpd_score, t.tpd_num ")
				.append(" FROM tkfpatient_data t, (SELECT @rownum\\:= 0) r ")
				.append(" WHERE t.isdel = 0 ")
				.append(" AND t.pid = '").append(gid).append("' ")
				.append(" AND t.teamGid ='").append(teamIdString).append("' ")
				.append(" AND t.tpd_mgid = '").append(id).append("' ")
				.append(" AND t.tpd_date = '").append(now).append("' ")
				.append(" ORDER BY t.createtime ");
		} else{
			String before = "";
			if ("2".equals(type)) // 周
				before = ComUtil.date2Str(new Date(new Date().getTime()-(7*24*60*60*1000)));
			else if ("3".equals(type)) // 月
				before = ComUtil.date2Str(new Date(new Date().getTime()-(new Long(30)*24*60*60*1000)));
			
			sql.append(" SELECT t.tpd_date,t.tpd_type,t.tpd_score, avg(t.tpd_num),leg ")
				.append(" FROM tkfpatient_data t ")
				.append(" WHERE t.isdel = 0 ")
				.append(" AND t.tpd_mgid = '").append(id).append("' ")
				.append(" AND t.pid = '").append(gid).append("' ")
				.append(" AND t.teamGid ='").append(teamIdString).append("' ")
				.append(" AND t.tpd_date >= '").append(before).append("' ")
				.append(" AND t.tpd_date <= '").append(now).append("' ")
				.append(" AND t.leg = '").append(leg).append("' ")
				.append(" GROUP BY t.tpd_date ")
				.append(" ORDER BY t.tpd_date ");
		} 
		return kfDataDao.getListBySql(sql.toString());
	}
	@Override
	public List<KfData> queryTodayData() {
		String Sql="select * from tkfpatient_data where date(tpd_date) = curdate() and  teamGid<>'' and teamGid is not null  order by leg desc,teamGid desc,pid desc,tpd_mgid desc";
		return kfDataDao.getVoListBySql(Sql);
	}
	@Override
	public List<KfData> getMotionList(String gid, String string, String string2) {

		String Sql="select * from tkfpatient_data where pid='"+gid+"' and teamGid='"+string2+"' and leg='"+string+"' group by tpd_mgid";
		
		return kfDataDao.getVoListBySql(Sql);
	}
	
	
}
