package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.NoticeDetailDao;
import com.sd.service.NoticeDetailService;
import com.sd.vo.NoticeDetail;

@Service
public class NoticeDetailServiceImpl extends BaseServiceImpl<NoticeDetail, String> implements NoticeDetailService{
	@Resource
	public void setBaseDao(NoticeDetailDao noticeDetailDao) {
		super.setBaseDao(noticeDetailDao);
	}
	@Resource private NoticeDetailDao noticeDetailDao;
	
}