package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.AdminDao;
import com.sd.dao.ChildPostDao;
import com.sd.service.AdminService;
import com.sd.service.ChildPostService;
import com.sd.vo.Admin;
import com.sd.vo.ChildPost;

@Service
public class ChildPostServiceImpl extends BaseServiceImpl<ChildPost, String> implements ChildPostService{
	@Resource
	public void setBaseDao(ChildPostDao childPostDao) {
		super.setBaseDao(childPostDao);
	}
	@Resource private ChildPostDao childPostDao;
	
}
