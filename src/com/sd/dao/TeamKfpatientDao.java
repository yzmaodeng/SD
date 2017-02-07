package com.sd.dao;

import com.sd.vo.TeamKfpatient;

public interface TeamKfpatientDao extends BaseDao<TeamKfpatient, String> {
	/**
	 * 获取患者ID获取团患关系的
	 * @param gid
	 * @return
	 */
	TeamKfpatient getByGId(String gid);
}
