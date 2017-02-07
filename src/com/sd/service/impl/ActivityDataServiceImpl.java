package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.ActivityDataDao;
import com.sd.service.ActivityDataService;
import com.sd.vo.ActivityData;
import com.sd.vo.Admin;

@Service
public class ActivityDataServiceImpl extends BaseServiceImpl<ActivityData, String> implements ActivityDataService{
	@Resource
	public void setBaseDao(ActivityDataDao activityDataDao) {
		super.setBaseDao(activityDataDao);
	}
	@Resource private ActivityDataDao activityDataDao;
	
}
