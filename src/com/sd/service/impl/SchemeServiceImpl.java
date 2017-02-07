package com.sd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.SchemeDao;
import com.sd.service.SchemeService;
import com.sd.vo.Scheme;

@Service
public class SchemeServiceImpl extends BaseServiceImpl<Scheme, String> implements SchemeService{
	@Resource
	public void setBaseDao(SchemeDao schemeDao) {
		super.setBaseDao(schemeDao);
	}
	@Resource private SchemeDao schemeDao;
	
	/**
	 * 根据用户ID和患者gid获取患者方案 
	 * @param gid 		患者ID
	 * @param uid		用户ID
	 * @param leg		左右腿
	 * @return
	 */
	@Override
	public Scheme getByPatientUser(String gid, String uid, String leg) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT s.* ")
			.append(" FROM tkf_scheme s,tuser p ")
			.append(" WHERE s.ts_createuser in")
			.append(" (select user_gid from tuser where user_tmau=(select user_tmau from tuser where user_gid ='")
			.append(uid).append("')) ")
			.append(" AND s.ts_kfpid= '").append(gid).append("' ")
			.append(" AND s.isdel = '0' ")
			.append(" AND s.ts_leg = ").append(leg);
			
		List<Scheme> ret = schemeDao.getVoListBySql(sql.toString());
		if (ret.size() > 0)
			return ret.get(0);
		return null;
	}
	/**
	 * 根据团队ID和患者gid获取患者方案 
	 * @param gid 		患者ID
	 * @param tid		团队ID
	 * @param leg		左右腿
	 * @return
	 */
	@Override
	public Scheme getByPatientTeam(String gid, String tid, String leg) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT s.* ")
			.append(" FROM tkf_scheme s, tuser p ")
			.append(" WHERE s.ts_createuser = p.User_GID")
			.append(" AND s.isdel = '0' ")
			.append(" AND p.user_tmau = '").append(tid).append("' ")
			.append(" AND s.ts_kfpid= '").append(gid).append("' ")
			.append(" AND s.ts_leg = ").append(leg);
		
		List<Scheme> ret = schemeDao.getVoListBySql(sql.toString());
		if (ret.size() > 0)
			return ret.get(0);
		return null;
	}
	
	/**
	 * 根据团队ID和患者gid获取患者方案 
	 * @param gid 		患者ID
	 * @param tid		团队ID
	 * @param leg		左右腿
	 * @return
	 */
	@Override
	public List<Scheme> getByPatientTeam(String gid, String tid) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT s.* ")
			.append(" FROM tkf_scheme s, tuser p ")
			.append(" WHERE s.ts_createuser = p.User_GID")
			.append(" AND s.isdel = '0' ")
			.append(" AND p.user_tmau = '").append(tid).append("' ")
			.append(" AND s.ts_kfpid= '").append(gid).append("' ");
		
		List<Scheme> ret = schemeDao.getVoListBySql(sql.toString());
		return ret;
	}
	/**
	 * 利用视图查询方案及动作信息 
	 * @param schemeGid 方案Gid
	 * @param daysAfterOperation 术后第几天
	 * @return
	 */
	@Override
	public List<Object[]> getSchemeList(String schemeGid, int daysAfterOperation){
		String sql = "SELECT * from scheme_motion_target WHERE ts_gid = '"+schemeGid+"' and "+daysAfterOperation+" BETWEEN tt_bdays AND tt_edays";
		return schemeDao.getListBySql(sql);
	}
	
	/**
	 * 利用视图查询方案数据
	 * 某个动作最近一天的数据
	 * @param schemeGid 方案Gid
	 * @return
	 */
	@Override
	public List<Object[]> getSchemeData(String schemeGid,String dataDate){
		//利用视图查询
//		String sql = "SELECT * from patient_scheme_data WHERE ts_gid = '"+schemeGid+"'";
//		return schemeDao.getListBySql(sql.toString());
		//利用sql查询
		StringBuffer sql = new StringBuffer();
		sql.append("select ts.ts_gid,ts.ts_name,ts.ts_createteam,ts.ts_leg,tm.tm_id,tt.tt_bdays,tt.tt_edays,tt.tt_group,tt.tt_num,pd.tpd_score AS data_score,pd.tpd_num AS data_num,pd.tpd_date AS data_date,pd.tpd_type AS data_type,pd.days ")
			.append(" from (tkfpatient_data pd left join ((tkf_scheme ts left join tkf_motion tm on ts.ts_gid = tm.tm_sgid) left join tkf_target tt on tm.tm_id = tt.tt_mgid) on(pd.tpd_mgid = tm.tm_id and (pd.days between tt.tt_bdays and tt.tt_edays))) ")
			.append(" where  (ts.ts_gid = '").append(schemeGid).append("'")
			.append("and pd.tpd_date = '").append(dataDate).append("'")
			.append(" and ts.isdel = 0 and tm.isdel = 1 and tt.isdel = 0)");
		return schemeDao.getListBySql(sql.toString());
	}
	
	@Override
	public int countMainNumBySql( String gid) {
		return schemeDao.countMainNumBySql(gid);
	}
	

	
}
