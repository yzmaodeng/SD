package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.StandardMotionDao;
import com.sd.service.StandardMotionService;
import com.sd.vo.StandardMotion;

@Service
public class StandardMotionServiceImpl extends BaseServiceImpl<StandardMotion, String> implements StandardMotionService{
	@Resource
	public void setBaseDao(StandardMotionDao standardMotionDao) {
		super.setBaseDao(standardMotionDao);
	}
	@Resource private StandardMotionDao standardMotionDao;
	
}
