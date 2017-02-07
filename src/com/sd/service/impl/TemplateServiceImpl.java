package com.sd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.TemplateDao;
import com.sd.service.TemplateService;
import com.sd.vo.Template;

@Service
public class TemplateServiceImpl extends BaseServiceImpl<Template, String> implements TemplateService{
	@Resource
	public void setBaseDao(TemplateDao templateDao) {
		super.setBaseDao(templateDao);
	}
	@Resource private TemplateDao templateDao;
	
	/**
	 * 获取团队下的模版
	 * @param teamId
	 * @return
	 */
	public List<Template> getTmplByTeam(String teamId) {
		return templateDao.getTmplByTeam(teamId);
	}
	/**
	 * 获取团队下的模版的数量
	 * @param teamId
	 * @return
	 */
	public String getTmplNumByTeam(String teamId) {
		return templateDao.getTmplNumByTeam(teamId);
	}

}
