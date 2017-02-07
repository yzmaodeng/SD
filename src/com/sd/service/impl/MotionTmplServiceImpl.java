package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.MotionTmplDao;
import com.sd.service.MotionTmplService;
import com.sd.vo.MotionTmpl;

@Service
public class MotionTmplServiceImpl extends BaseServiceImpl<MotionTmpl, String> implements MotionTmplService{
	@Resource
	public void setBaseDao(MotionTmplDao motionTmplDao) {
		super.setBaseDao(motionTmplDao);
	}
	@Resource private MotionTmplDao motionTmplDao;
	
}