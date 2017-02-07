package com.sd.service;

import java.util.List;

import com.sd.vo.Target;

public interface TargetService extends BaseService<Target, String> {
	/**
	 * 根据患者gid获取某一天的动作列表
	 * @param pgid		
	 * @param days
	 * @return
	 */
	List<String> getDataByType(String pgid, String days);
	/**
	 * 根据json串，保存target
	 * @param mgid
	 * @param jsonTarget 
	 */
	void saveJsonTarget(String mgid, Object jsonTarget);
}
