package com.sd.dao;

import com.sd.vo.KfPatientSick;

public interface KfPatientSickDao extends BaseDao<KfPatientSick, String> {

	/**
	 * 获取患者疾病
	 * @param pid
	 * @param oid 
	 * @param type
	 * @return
	 */
	KfPatientSick get(String pid, String oid, int type);

}
