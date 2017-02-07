package com.sd.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.sd.dao.TeamApplyDao;
import com.sd.dao.TeamDao;
import com.sd.dao.UserDao;
import com.sd.service.TeamService;
import com.sd.vo.Team;
import com.sd.vo.User;

@Service
public class TeamServiceImpl extends BaseServiceImpl<Team, String> implements TeamService{
	@Resource
	public void setBaseDao(TeamDao teamDao) {
		super.setBaseDao(teamDao);
	}
	@Resource private TeamDao teamDao;
	@Resource private UserDao userDao;
	@Resource private TeamApplyDao teamApplyDao;
	
	/**
	 * 根据用户信息返回团队信息
	 * @param id
	 * @return
	 */
	public Map<String, String> getUserTeamInfo(String userId) {
		Map<String, String> ret = new HashMap<String, String>();
		ret.put("id", "");
		ret.put("name", "");
		ret.put("status", "0");// 无团队
		
		if (StringUtils.isBlank(userId))
			return ret;
		
		User user = userDao.get(userId);
		if (user == null)
			return ret;
		
		String teamId = user.getTeamId();
		if (StringUtils.isNotBlank(teamId)){
			Team team = teamDao.get(teamId);
			if (team == null)
				return ret;
			
			ret.put("id", team.getGid());
			ret.put("name", team.getName());
			ret.put("status", "2");		// 有团队
			
			return ret;
		} else {
			Team team = teamApplyDao.getByUser(userId);
			if (team == null)
				return ret;
			
			ret.put("id", team.getGid());
			ret.put("name", team.getName());
			ret.put("status", "1");		// 无团队，有申请
			
			return ret;
		}
	}

}
