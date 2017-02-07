package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.TeamKfpatientDao;
import com.sd.service.TeamKfpatientService;
import com.sd.vo.TeamKfpatient;

@Service
public class TeamKfpatientServiceImpl extends BaseServiceImpl<TeamKfpatient, String> implements TeamKfpatientService{
	@Resource
	public void setBaseDao(TeamKfpatientDao teamKfpatientDao) {
		super.setBaseDao(teamKfpatientDao);
	}
	@Resource private TeamKfpatientDao teamKfpatientDao;
	@Override
	public TeamKfpatient getByGId(String gid) {
		return teamKfpatientDao.getByGId(gid);
	}
	
}