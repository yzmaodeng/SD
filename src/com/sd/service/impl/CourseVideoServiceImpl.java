package com.sd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.CourseVideoDao;
import com.sd.service.CourseVideoService;
import com.sd.vo.CourseVideo;

@Service
public class CourseVideoServiceImpl extends BaseServiceImpl<CourseVideo, String> implements CourseVideoService{
	@Resource
	public void setBaseDao(CourseVideoDao courseVideoDao) {
		super.setBaseDao(courseVideoDao);
	}
	@Resource private CourseVideoDao courseVideoDao;
	@Override
	public List<CourseVideo> executeSql(String sql, String keyword,String type) {
		return courseVideoDao.executeSql(sql, keyword,type);
	}
	@Override
	public List<CourseVideo> getVoListBySql(String hql2) {
		// TODO Auto-generated method stub
		return courseVideoDao.getVoListBySql(hql2);
	}
	@Override
	public void updateWatchNumber(String videoGid) {
		String sql = "update courseVideo set watchNumber = watchNumber+1 where gid='"+videoGid+"'";
		courseVideoDao.executeUpdateSql(sql);
	}
	
}
