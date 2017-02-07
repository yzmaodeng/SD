package com.sd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.AdminDao;
import com.sd.dao.TaskDao;
import com.sd.service.AdminService;
import com.sd.service.TaskService;
import com.sd.vo.Admin;
import com.sd.vo.Task;

@Service
public class TaskServiceImpl extends BaseServiceImpl<Task, String> implements TaskService{
	@Resource
	public void setBaseDao(TaskDao taskDao) {
		super.setBaseDao(taskDao);
	}
	@Resource private TaskDao taskDao;
	public List<Task> getBaseList(String userId){
		return taskDao.getBaseList(userId);
	}
}
