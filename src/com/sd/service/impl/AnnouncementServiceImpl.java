package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.AdminDao;
import com.sd.dao.AnnouncementDao;
import com.sd.service.AdminService;
import com.sd.service.AnnouncementService;
import com.sd.vo.Admin;
import com.sd.vo.Announcement;

@Service
public class AnnouncementServiceImpl extends BaseServiceImpl<Announcement, String> implements AnnouncementService{
	@Resource private AnnouncementDao announcementDao;
	@Resource
	public void setBaseDao(AnnouncementDao announcementDao) {
		super.setBaseDao(announcementDao);
	}
}
