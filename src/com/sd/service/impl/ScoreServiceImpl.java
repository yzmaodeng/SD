package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.AdminDao;
import com.sd.dao.ScoreDao;
import com.sd.dao.SignDao;
import com.sd.service.AdminService;
import com.sd.service.ScoreService;
import com.sd.service.SignService;
import com.sd.vo.Admin;
import com.sd.vo.Score;
import com.sd.vo.Sign;

@Service
public class ScoreServiceImpl extends BaseServiceImpl<Score, String> implements ScoreService{
	@Resource
	public void setBaseDao(ScoreDao scoreDao) {
		super.setBaseDao(scoreDao);
	}
	@Resource private ScoreDao scoreDao;
	
	
	@Override
	public int countNumBySql(String s) {
		return scoreDao.todayNum(s);
	}


	@Override
	public int totleNumBySql(String s) {
		// TODO Auto-generated method stub
		return scoreDao.totleNum(s);
	}
	
}
