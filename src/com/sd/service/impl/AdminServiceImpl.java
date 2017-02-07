package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.AdminDao;
import com.sd.service.AdminService;
import com.sd.vo.Admin;

@Service
public class AdminServiceImpl extends BaseServiceImpl<Admin, String> implements AdminService{
	@Resource
	public void setBaseDao(AdminDao adminDao) {
		super.setBaseDao(adminDao);
	}
	@Resource private AdminDao adminDao;
	
}
