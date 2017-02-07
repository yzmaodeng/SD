package com.sd.service;

import java.util.Map;

import com.sd.vo.Team;

public interface TeamService extends BaseService<Team, String> {
	/**
	 * 根据用户信息返回团队信息
	 * @param id
	 * @return
	 */
	Map<String, String> getUserTeamInfo(String userId);
	
}
