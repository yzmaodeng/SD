package com.sd.service;

import java.util.List;

import com.sd.vo.Admin;
import com.sd.vo.Task;

public interface TaskService extends BaseService<Task, String> {
	List<Task> getBaseList(String userId);
}
