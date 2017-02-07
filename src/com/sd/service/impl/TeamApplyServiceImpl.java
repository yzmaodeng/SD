package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.TeamApplyDao;
import com.sd.service.TeamApplyService;
import com.sd.vo.Team;
import com.sd.vo.TeamApply;

@Service
public class TeamApplyServiceImpl extends BaseServiceImpl<TeamApply, String> implements TeamApplyService{
	@Resource
	public void setBaseDao(TeamApplyDao teamApplyDao) {
		super.setBaseDao(teamApplyDao);
	}
	@Resource private TeamApplyDao teamApplyDao;
	
	/**
	 * 获得用户的团队申请信息
	 * @param userId
	 * @return
	 */
	public Team getByUser(String userId) {
		return teamApplyDao.getByUser(userId);
	}
}
