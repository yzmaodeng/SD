package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.TemplateDetailDao;
import com.sd.service.TemplateDetailService;
import com.sd.vo.TemplateDetail;

@Service
public class TemplateDetailServiceImpl extends BaseServiceImpl<TemplateDetail, String> implements TemplateDetailService{
	@Resource
	public void setBaseDao(TemplateDetailDao templateDetailDao) {
		super.setBaseDao(templateDetailDao);
	}
	@Resource private TemplateDetailDao templateDetailDao;
	
	/**
	 * 获取模版中具体某一天的内容
	 * @param gid
	 * @param bet
	 * @return
	 */
	public TemplateDetail getOne(String gid, long bet) {
		return templateDetailDao.getOne(gid, bet);
	}

}
