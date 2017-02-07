package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.DordersDao;
import com.sd.service.DordersService;
import com.sd.vo.Dorders;

@Service
public class DordersServiceImpl extends BaseServiceImpl<Dorders, String> implements DordersService{
	@Resource
	public void setBaseDao(DordersDao dordersDao) {
		super.setBaseDao(dordersDao);
	}
	@Resource private DordersDao dordersDao;

	
}
