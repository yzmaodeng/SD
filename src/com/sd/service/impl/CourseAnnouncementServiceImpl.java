package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.CourseAnnouncementDao;
import com.sd.service.CourseAnnouncementService;
import com.sd.vo.CourseAnnocement;

@Service
public class CourseAnnouncementServiceImpl extends BaseServiceImpl<CourseAnnocement, String> implements CourseAnnouncementService {
	@Resource
	private CourseAnnouncementDao courseAnnouncementDao;

	@Resource
	public void setBaseDao(CourseAnnouncementDao courseAnnouncementDao) {
		super.setBaseDao(courseAnnouncementDao);
	}
}
