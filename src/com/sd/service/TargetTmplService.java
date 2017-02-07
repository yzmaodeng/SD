package com.sd.service;

import com.sd.vo.TargetTmpl;

public interface TargetTmplService extends BaseService<TargetTmpl, String> {
	/**
	 * 根据json串，保存target
	 * @param mgid
	 * @param jsonTarget 
	 */
	void saveJsonTarget(String mgid, Object jsonTarget);
}
