package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.PatientDoctorDao;
import com.sd.service.PatientDoctorService;
import com.sd.vo.KfPatientDoctor;

@Service
public class PatientDoctorServiceImpl extends BaseServiceImpl<KfPatientDoctor, String> implements PatientDoctorService{
	@Resource
	public void setBaseDao(PatientDoctorDao userDao) {
		super.setBaseDao(userDao);
	}
	@Resource private PatientDoctorDao userDao;
	
}
