package com.sd.service;

import java.util.List;
import java.util.Map;

import com.sd.vo.KfData;
import com.sd.vo.Motion;

public interface KfDataService extends BaseService<KfData, String> {
	/**
	 * 根据动作和类型
	 * @param id
	 * @param type
	 * @param gid 
	 * @param teamIdString 
	 * @return
	 */
	List<Object[]> getDataByType(String id, String type,String leg, String teamIdString, String gid);
	List<KfData> getDateBySql(Map<String, String> condition);
	List<KfData> getDate(String teamGid,String time);
	List<KfData> queryTodayData();
	List<KfData> getMotionList(String gid, String string, String string2);
}
