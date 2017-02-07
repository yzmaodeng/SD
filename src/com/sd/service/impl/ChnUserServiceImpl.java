package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.ChnUserDao;
import com.sd.service.ChnUserService;
import com.sd.vo.ChnUser;
@Service
public class ChnUserServiceImpl extends BaseServiceImpl<ChnUser,String> implements ChnUserService {
	@Resource
	public void setBaseDao(ChnUserDao chnUserDao) {
		super.setBaseDao(chnUserDao);
	}
	@Resource private ChnUserDao chnUserDao;
	
}
