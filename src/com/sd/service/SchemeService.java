package com.sd.service;

import java.util.List;

import com.sd.vo.Scheme;

public interface SchemeService extends BaseService<Scheme, String> {
	/**
	 * 根据用户ID和患者gid获取患者方案 
	 * @param gid 		患者ID
	 * @param uid		用户ID
	 * @param leg		左右腿
	 * @return
	 */
	Scheme getByPatientUser(String gid, String uid, String leg);
	
	
	/**
	 * 根据团队ID和患者gid获取患者方案 
	 * @param gid 		患者ID
	 * @param tid		团队ID
	 * @param leg		左右腿
	 * @return
	 */
	Scheme getByPatientTeam(String gid, String tid, String leg);
	
	List<Scheme> getByPatientTeam(String gid, String tid);
	
	/**
	 * 利用视图查询方案及动作信息 
	 * @param schemeGid 方案Gid
	 * @param daysAfterOperation 术后第几天
	 * @return
	 */
	List<Object[]> getSchemeList(String schemeGid, int daysAfterOperation);
	
	/**
	 * 利用视图查询方案数据
	 * @param schemeGid 方案Gid
	 * @return
	 */
	List<Object[]> getSchemeData(String schemeGid,String dataDate);
	
	int countMainNumBySql(String gid);
}
