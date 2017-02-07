package com.sd.service;

import java.util.List;

import com.sd.vo.Notification;

public interface NotificationService extends BaseService<Notification, String> {

	List<Notification> getListBySql(int firstNUm, String pageCount);

	List<Notification> getList(String s);

	
}
