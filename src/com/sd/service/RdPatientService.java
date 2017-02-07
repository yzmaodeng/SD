package com.sd.service;

import java.util.List;

import com.sd.vo.RdPatient;

public interface RdPatientService extends BaseService<RdPatient, String> {
	/**
	 * 查询用户的病例
	 * @param userId
	 * @return
	 */
	List<RdPatient> getmyPatients(String userId);

}
