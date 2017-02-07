package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.SickDao;
import com.sd.service.SickService;
import com.sd.vo.Sick;

@Service
public class SickServiceImpl extends BaseServiceImpl<Sick, String> implements SickService{
	@Resource
	public void setBaseDao(SickDao sickDao) {
		super.setBaseDao(sickDao);
	}
	@Resource private SickDao sickDao;
	
	/**
	 * 根据三级疾病id，获取疾病全称
	 * @param gid
	 * @return
	 */
	public String getFullSickName(String gid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select CONCAT(s.ts_name,'(',t.ts_name,')'), '' ")
			.append(" from tsick t, tsick s ")
			.append(" where t.isdel = 0 ")
			.append(" and t.ts_gid = '").append(gid).append("' ")
			.append(" and t.ts_pid = s.ts_gid");
		return sickDao.getSingleBysql(sql.toString(), 0);
	}
	
}
