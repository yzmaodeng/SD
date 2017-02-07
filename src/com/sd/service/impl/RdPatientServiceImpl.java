package com.sd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.RdPatientDao;
import com.sd.service.RdPatientService;
import com.sd.vo.RdPatient;

@Service
public class RdPatientServiceImpl extends BaseServiceImpl<RdPatient, String> implements RdPatientService{
	@Resource
	public void setBaseDao(RdPatientDao rdPatientDao) {
		super.setBaseDao(rdPatientDao);
	}
	@Resource private RdPatientDao rdPatientDao;
	
	/**
	 * 查询用户的病例
	 * @param userId
	 * @return
	 */
	public List<RdPatient> getmyPatients(String userId) {
		return rdPatientDao.getmyPatients(userId);
	}

}
