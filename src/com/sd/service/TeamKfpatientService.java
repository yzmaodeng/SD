package com.sd.service;

import com.sd.vo.TeamKfpatient;

public interface TeamKfpatientService extends BaseService<TeamKfpatient, String> {
	/**
	 * 获取患者ID获取团患关系的
	 * @param gid
	 * @return
	 */
	TeamKfpatient getByGId(String gid);
}
