package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.WardPatientDao;
import com.sd.service.WardPatientService;
import com.sd.vo.WardPatient;

@Service
public class WardPatientServiceImpl extends BaseServiceImpl<WardPatient, String> implements WardPatientService{
	@Resource
	public void setBaseDao(WardPatientDao wardPatientDao) {
		super.setBaseDao(wardPatientDao);
	}
	@Resource private WardPatientDao wardPatientDao;
	
}
