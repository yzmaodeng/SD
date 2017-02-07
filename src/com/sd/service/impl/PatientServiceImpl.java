package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.PatientDao;
import com.sd.service.PatientService;
import com.sd.vo.Record;
@Service
public class PatientServiceImpl extends BaseServiceImpl<Record, String> implements PatientService {
	@Resource
	public void setBaseDao(PatientDao rdPatientDao) {
		super.setBaseDao(rdPatientDao);
	}
	@Resource private PatientDao rdPatientDao;
	
}
