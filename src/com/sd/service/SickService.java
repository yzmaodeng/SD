package com.sd.service;

import com.sd.vo.Sick;

public interface SickService extends BaseService<Sick, String> {
	/**
	 * 根据三级疾病id，获取疾病全称
	 * @param gid
	 * @return
	 */
	String getFullSickName(String gid);
	
}
