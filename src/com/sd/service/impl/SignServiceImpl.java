package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.AdminDao;
import com.sd.dao.SignDao;
import com.sd.service.AdminService;
import com.sd.service.SignService;
import com.sd.vo.Admin;
import com.sd.vo.Sign;

@Service
public class SignServiceImpl extends BaseServiceImpl<Sign, String> implements SignService{
	@Resource
	public void setBaseDao(SignDao signDao) {
		super.setBaseDao(signDao);
	}
	@Resource private SignDao signDao;
	@Override
	public int countNumBySql(String s) {
		return signDao.todayNum(s);
	}
	
}
