package com.sd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.AdminDao;
import com.sd.dao.CourseExpertDao;
import com.sd.service.AdminService;
import com.sd.service.CourseExpertService;
import com.sd.vo.Admin;
import com.sd.vo.CourseExpert;

@Service
public class CourseExpertServiceImpl extends BaseServiceImpl<CourseExpert, String> implements CourseExpertService{
	@Resource
	public void setBaseDao(CourseExpertDao courseExpertDao) {
		super.setBaseDao(courseExpertDao);
	}
	@Resource private CourseExpertDao courseExpertDao;
	@Override
	public List<CourseExpert> getfourExperts() {
		String s="select  * from  courseExpert    order by createTime  asc ";
		return courseExpertDao.getVoListBySql(s);
	}
	
}
