package com.sd.dao;

import com.sd.vo.TemplateDetail;

public interface TemplateDetailDao extends BaseDao<TemplateDetail, String> {
	/**
	 * 获取模版中具体某一天的内容
	 * @param gid
	 * @param bet
	 * @return
	 */
	TemplateDetail getOne(String gid, long bet);

}
