package com.sd.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.ConfDao;
import com.sd.dao.CourseReplyDao;
import com.sd.service.CourseReplyService;
import com.sd.util.Conditions;
import com.sd.util.MyPage;
import com.sd.vo.CourseReply;

@Service
public class CourseReplyServiceImpl extends
		BaseServiceImpl<CourseReply, String> implements CourseReplyService {
	@Resource
	private CourseReplyDao courseReplyDao;

	@Resource
	public void setBaseDao(CourseReplyDao courseReplyDao) {
		super.setBaseDao(courseReplyDao);
	}

}
