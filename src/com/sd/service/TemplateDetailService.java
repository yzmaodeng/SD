package com.sd.service;

import com.sd.vo.TemplateDetail;

public interface TemplateDetailService extends BaseService<TemplateDetail, String> {
	/**
	 * 获取模版中具体某一天的内容
	 * @param gid
	 * @param bet
	 * @return
	 */
	TemplateDetail getOne(String gid, long bet);
	
}
