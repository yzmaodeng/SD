package com.sd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.AdminDao;
import com.sd.dao.CourseDao;
import com.sd.service.AdminService;
import com.sd.service.CourseService;
import com.sd.vo.Admin;
import com.sd.vo.Course;

@Service
public class CourseServiceImpl extends BaseServiceImpl<Course, String> implements CourseService{
	@Resource
	public void setBaseDao(CourseDao courseDao) {
		super.setBaseDao(courseDao);
	}
	@Resource private CourseDao courseDao;
	@Override
	public List<Course> executeSql(String sql, String keyword,String type) {
		return courseDao.executeSql(sql,keyword,type);
	}
	@Override
	public List<Course> getVoListBySql(String hql) {
		// TODO Auto-generated method stub
		return courseDao.getVoListBySql(hql);
	}


	
}
