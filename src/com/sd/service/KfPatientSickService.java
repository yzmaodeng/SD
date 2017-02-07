package com.sd.service;

import com.sd.vo.KfPatientSick;

public interface KfPatientSickService extends BaseService<KfPatientSick, String> {
	/**
	 * 获取患者疾病
	 * @param pid
	 * @param oid 
	 * @param type
	 * @return
	 */
	KfPatientSick get(String pid, String oid, int type);
	/**
	 * 根据团队信息获取疾病信息
	 * @param gid
	 * @param teamGid
	 * @return
	 */
	String getSickByTeam(String gid, String teamGid);
	
}
