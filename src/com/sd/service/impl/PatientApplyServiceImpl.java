package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.PatientApplyDao;
import com.sd.service.PatientApplyService;
import com.sd.vo.KfPatientApply;

@Service
public class PatientApplyServiceImpl extends BaseServiceImpl<KfPatientApply, String> implements PatientApplyService{
	@Resource
	public void setBaseDao(PatientApplyDao userDao) {
		super.setBaseDao(userDao);
	}
	@Resource private PatientApplyDao userDao;
	
}
