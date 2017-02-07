package com.sd.service;

import java.util.List;

import com.sd.vo.KfPatient;

public interface KfPatientService extends BaseService<KfPatient, String> {
	/**
	 * 查询个人所属的患者
	 * @param userId
	 * @return
	 */
	List<KfPatient> getPensonalPatients(String userId);
	/**
	 * 查询团队所属的患者
	 * @param userId
	 * @return
	 */
	List<KfPatient> getGroupPatients(String teamGid);
	/**
	 * 查询新申请患者
	 * @param userId
	 * @return
	 */
	List<Object[]> getNewApplyPatients(String userId);
	/**
	 *根据患者ID 获取患者
	 * @param id  患者ID
	 * @return
	 */
	KfPatient getByPatientId(String id);
	
	/**
	 * 获取患者当前团队下的手术及疾病信息
	 * @return
	 */
	List<Object[]> queryPatientOperationInfo(String patientGid,String teamGid) ;
}
