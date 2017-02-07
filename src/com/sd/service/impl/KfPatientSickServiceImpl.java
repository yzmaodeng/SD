package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.KfPatientSickDao;
import com.sd.service.KfPatientSickService;
import com.sd.vo.KfPatientSick;

@Service
public class KfPatientSickServiceImpl extends BaseServiceImpl<KfPatientSick, String> implements KfPatientSickService{
	@Resource
	public void setBaseDao(KfPatientSickDao kfPatientSickDao) {
		super.setBaseDao(kfPatientSickDao);
	}
	@Resource private KfPatientSickDao kfPatientSickDao;
	
	/**
	 * 获取患者疾病
	 * @param pid
	 * @param oid 
	 * @param type
	 * @return
	 */
	public KfPatientSick get(String pid, String oid, int type) {
		return kfPatientSickDao.get(pid, oid, type);
	}

	/**
	 * 根据团队信息获取疾病信息
	 * @param gid
	 * @param teamGid
	 * @return
	 */
	public String getSickByTeam(String gid, String teamGid) {
		KfPatientSick pSick = kfPatientSickDao.get(gid, teamGid, 1);
		if (pSick != null)
			return pSick.getSid();
		return null;
	}
	
}
