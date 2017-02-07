package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.TopicDao;
import com.sd.service.TopicService;
import com.sd.vo.Topic;
@Service
public class TopicServiceImpl extends BaseServiceImpl<Topic, String> implements TopicService {
	@Resource
	public void setBaseDao(TopicDao topicDao) {
		super.setBaseDao(topicDao);
	}
	@Resource private TopicDao topicDao;
	
}
