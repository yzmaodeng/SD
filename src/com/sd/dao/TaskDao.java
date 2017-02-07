package com.sd.dao;

import java.util.List;

import com.sd.vo.Task;
import com.sd.vo.Team;

public interface TaskDao extends BaseDao<Task, String> {
	List<Task> getBaseList(String userId);
}
