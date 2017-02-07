package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.FeedbackDao;
import com.sd.service.FeedbackService;
import com.sd.vo.Feedback;
@Service
public class FeedbackServiceImpl extends BaseServiceImpl<Feedback, String> implements FeedbackService {
	@Resource
	public void setBaseDao(FeedbackDao feedbackDao) {
		super.setBaseDao(feedbackDao);
	}
	@Resource private FeedbackDao feedbackDao;
	
}
