package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.KfDataResulDao;
import com.sd.service.KfDataResultService;
import com.sd.vo.KfPatientDataResult;

@Service
public class KfDataResultServiceImpl extends BaseServiceImpl<KfPatientDataResult, String> implements KfDataResultService{
	@Resource
	public void setBaseDao(KfDataResulDao kfDataResulDao) {
		super.setBaseDao(kfDataResulDao);
	}
	@Resource private KfDataResulDao kfDataResulDao;
	

	
}
