package com.sd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.KfPatientDao;
import com.sd.service.KfPatientService;
import com.sd.vo.KfPatient;

@Service
public class KfPatientServiceImpl extends BaseServiceImpl<KfPatient, String> implements KfPatientService{
	@Resource
	public void setBaseDao(KfPatientDao kfPatientDao) {
		super.setBaseDao(kfPatientDao);
	}
	@Resource private KfPatientDao kfPatientDao;
	
	/**
	 * 查询个人所属的患者
	 * @param userId
	 * @return
	 */
	public List<KfPatient> getPensonalPatients(String userId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.* from tkfpatient t,tkfpatient_user u ")
			.append(" where t.tp_gid=u.tku_pgid ")
			.append(" and u.tku_ugid = '")
			.append(userId).append("' ")
			.append(" and t.isdel='0' ");
		
		return kfPatientDao.getVoListBySql(sql.toString());
	}
	/**
	 * 查询团队所属的患者
	 * @param userId
	 * @return
	 */
	public List<KfPatient> getGroupPatients(String teamGid) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.* from tkfpatient t,tteam_kfpatient u ")
			.append(" where t.tp_gid=u.ttpi_pgid ")
			.append(" and u.ttpi_tgid = '")
			.append(teamGid).append("' ")
			.append(" and t.isdel='0' ");
		
		return kfPatientDao.getVoListBySql(sql.toString());
	}
	/**
	 * 查询新申请患者
	 * @param userId
	 * @return
	 */
	public List<Object[]> getNewApplyPatients(String userId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.tp_gid,t.tp_avatars,t.tp_name,u.tta_info ")
			.append(" from tkfpatient t,tkfpatient_apply u ")
			.append(" where t.tp_gid=u.tta_pgid ")
			.append(" and u.tta_ugid = '")
			.append(userId).append("'")
			.append(" and u.isdel='1' ");
		
		return kfPatientDao.getListBySql(sql.toString());
	}
	@Override
	public KfPatient getByPatientId(String id) {
		return kfPatientDao.getByPatientId(id);
	}
	@Override
	public List<Object[]> queryPatientOperationInfo(String patientGid,String teamGid) {
		String sql = "select * from patient_operation where tp_gid = '"+patientGid+"' and teamGid = '" +teamGid+"'";
		
		return kfPatientDao.getListBySql(sql);
	}

}
