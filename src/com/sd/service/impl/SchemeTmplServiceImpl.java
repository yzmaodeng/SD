package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.SchemeTmplDao;
import com.sd.service.SchemeTmplService;
import com.sd.vo.SchemeTmpl;

@Service
public class SchemeTmplServiceImpl extends BaseServiceImpl<SchemeTmpl, String> implements SchemeTmplService{
	@Resource
	public void setBaseDao(SchemeTmplDao schemetmplDao) {
		super.setBaseDao(schemetmplDao);
	}
	@Resource private SchemeTmplDao schemetmplDao;
	
}