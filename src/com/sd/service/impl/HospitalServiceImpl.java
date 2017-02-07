package com.sd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.HospitalDao;
import com.sd.service.HospitalService;
import com.sd.vo.Thospital;
@Service
public class HospitalServiceImpl extends BaseServiceImpl<Thospital, String> implements HospitalService {
	@Resource
	public void setBaseDao(HospitalDao hospitalDao) {
		super.setBaseDao(hospitalDao);
	}
	@Resource private HospitalDao hospitalDao;
	
	public List<Thospital> query(Thospital thospital){
		return hospitalDao.query(thospital);
	}
}
