package com.sd.service;

import java.util.List;

import com.sd.vo.Template;

public interface TemplateService extends BaseService<Template, String> {
	/**
	 * 获取团队下的模版
	 * @param teamId
	 * @return
	 */
	List<Template> getTmplByTeam(String teamId);
	/**
	 * 获取团队下的模版的数量
	 * @param teamId
	 * @return
	 */
	String getTmplNumByTeam(String teamId);
}
