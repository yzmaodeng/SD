package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.AdminDao;
import com.sd.dao.TaskCompleteDao;
import com.sd.dao.TaskDao;
import com.sd.service.AdminService;
import com.sd.service.TaskCompleteService;
import com.sd.service.TaskService;
import com.sd.vo.Admin;
import com.sd.vo.Task;
import com.sd.vo.TaskComplete;

@Service
public class TaskCompleteServiceImpl extends BaseServiceImpl<TaskComplete, String> implements TaskCompleteService{
	@Resource
	public void setBaseDao(TaskCompleteDao taskCompleteDao) {
		super.setBaseDao(taskCompleteDao);
	}
	@Resource private TaskCompleteDao taskCompleteDao;
	
}
