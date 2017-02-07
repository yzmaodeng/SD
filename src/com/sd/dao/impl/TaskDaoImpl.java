package com.sd.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sd.dao.AdminDao;
import com.sd.dao.TaskDao;
import com.sd.vo.Admin;
import com.sd.vo.Task;
import com.sd.vo.User;

@Repository
public class TaskDaoImpl extends BaseDaoImpl<Task, String> implements TaskDao{
	public List<Task> getBaseList(String userId){
		Criteria criteria = getSession().createCriteria(Task.class);
		criteria.add(Restrictions.sqlRestriction("type='1' and gid not in (select taskId from taskComplete where finish='1' and uid='"+userId+"' )"));
		return criteria.list();
	}
}
