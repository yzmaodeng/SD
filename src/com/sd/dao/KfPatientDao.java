package com.sd.dao;

import com.sd.vo.KfPatient;

public interface KfPatientDao extends BaseDao<KfPatient, String> {
	/**
	 *根据患者ID 获取患者
	 * @param id  患者ID
	 * @return
	 */
	KfPatient getByPatientId(String id);
}
