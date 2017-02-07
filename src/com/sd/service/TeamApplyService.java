package com.sd.service;

import com.sd.vo.Team;
import com.sd.vo.TeamApply;

public interface TeamApplyService extends BaseService<TeamApply, String> {
	/**
	 * 获得用户的团队申请信息
	 * @param userId
	 * @return
	 */
	Team getByUser(String userId);
}
