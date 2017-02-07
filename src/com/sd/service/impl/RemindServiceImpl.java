package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.RemindDao;
import com.sd.service.RemindService;
import com.sd.vo.Remind;

@Service
public class RemindServiceImpl extends BaseServiceImpl<Remind, String> implements RemindService{
	@Resource
	public void setBaseDao(RemindDao remindDao) {
		super.setBaseDao(remindDao);
	}
	@Resource private RemindDao remindDao;
	
}
