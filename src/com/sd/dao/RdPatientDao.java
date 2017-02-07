package com.sd.dao;

import java.util.List;

import com.sd.vo.RdPatient;

public interface RdPatientDao extends BaseDao<RdPatient, String> {

	/**
	 * 查询用户的病例
	 * @param userId
	 * @return
	 */
	List<RdPatient> getmyPatients(String userId);

}
