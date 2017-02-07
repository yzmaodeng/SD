package com.sd.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.sd.dao.TargetDao;
import com.sd.service.TargetService;
import com.sd.vo.Target;

@Service
public class TargetServiceImpl extends BaseServiceImpl<Target, String> implements TargetService{
	@Resource
	public void setBaseDao(TargetDao targetDao) {
		super.setBaseDao(targetDao);
	}
	@Resource private TargetDao targetDao;
	
	/**
	 * 根据患者gid获取某一天的动作列表
	 * @param pgid		
	 * @param days
	 * @return
	 */
	public List<String> getDataByType(String gid, String days) {
		List<String> list = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT st.tsm_name, t.tt_group, t.tt_num, st.tsm_type ")
			.append(" FROM tkf_scheme s, tkf_motion m, tkf_target t, tstandard_motion st ")
			.append(" WHERE s.ts_gid = m.tm_sgid ")
			.append(" AND m.tm_id = t.tt_mgid ")
			.append(" AND st.tsm_gid = m.tm_smgid ")
			.append(" AND s.ts_kfpid= '").append(gid).append("' ")
			.append(" AND t.tt_edays >= ").append(days).append(" ")
			.append(" AND t.tt_bdays <= ").append(days).append(" ")
			.append(" order by m.tm_ordering ");
			
		List<Object[]> ret = targetDao.getListBySql(sql.toString());
		if (ret.size() > 0)
			for (Object[] obj : ret){
				// TODO 动作类型区别待完善
				if ("1".equals(obj[3]))
					list.add(obj[0] + ":" + obj[1] + " X " + obj[2]);
				else if ("2".equals(obj[3]))
					list.add(obj[0] + ":" + obj[1] + " X " + obj[2]);
				else if ("3".equals(obj[3]))
					list.add(obj[0] + ":" + obj[1] + " X " + obj[2]);
			}
		
		return list;
	}
	/**
	 * 根据json串，保存target
	 * @param mgid
	 * @param jsonTarget 
	 */
	@SuppressWarnings({ "static-access", "rawtypes" })
	public void saveJsonTarget(String mgid, Object jsonTarget) {
		StringBuffer sql = new StringBuffer();
		sql.append(" delete from tkf_target ")
			.append("where tt_mgid ='").append(mgid).append("'");
		targetDao.executeUpdateSql(sql.toString());
		
		if (jsonTarget != null){
			Date now = new Date();
			JSONArray json = new JSONArray().fromObject(jsonTarget);
			Iterator iter = json.iterator();
			while (iter.hasNext()) {
				JSONObject obj = new JSONObject().fromObject(iter.next());
				String beginDay = obj.get("beginDay") + "";
				String endDay = obj.get("endDay") + "";
				String gCount = obj.get("gCount") + "";
				String eCount = obj.get("eCount") + "";
				
				Target target = new Target();
				target.setBdays(beginDay);
				target.setEdays(endDay);
				target.setGroup(gCount);
				target.setNum(eCount);
				target.setTarget(obj.get("target") + "");
				target.setUpdateTime(now);
				target.setCreateTime(now);
				target.setIsdel("0");
				target.setMgid(mgid);
				targetDao.save(target);
			}
		}
	}

}
