package com.sd.service;

import java.util.List;

import com.sd.vo.Admin;
import com.sd.vo.Course;

public interface CourseService extends BaseService<Course, String> {


	List<Course> executeSql(String sql, String keyword, String type);

	List<Course> getVoListBySql(String hql);

	
	
	
}
