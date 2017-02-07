package com.sd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.NotificationDao;
import com.sd.service.NotificationService;
import com.sd.vo.Notification;

@Service
public class NotificationServiceImpl extends BaseServiceImpl<Notification, String> implements NotificationService{
	@Resource
	public void setBaseDao(NotificationDao notificationDao) {
		super.setBaseDao(notificationDao);
	}
	@Resource private NotificationDao notificationDao;

	@Override
	public List<Notification> getListBySql(int firstNUm, String pageCount) {
		String SqlString="SELECT * from notification order by createtime desc limit "+firstNUm+","+pageCount;
		return notificationDao.getVoListBySql(SqlString);
	}

	@Override
	public List<Notification> getList(String s) {
	
		return notificationDao.getVoListBySql(s);
	}

	


	
}
