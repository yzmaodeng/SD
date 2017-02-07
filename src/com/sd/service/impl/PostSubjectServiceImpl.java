package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.ChannelDao;
import com.sd.dao.PostSubjectDao;
import com.sd.service.PostSubjectService;
import com.sd.vo.PostSubject;

@Service
public class PostSubjectServiceImpl extends
		BaseServiceImpl<PostSubject, String> implements PostSubjectService {
	@Resource
	public void setBaseDao(PostSubjectDao postSubjectDao ) {
		super.setBaseDao(postSubjectDao);
	}

	@Resource
	private PostSubjectDao postSubjectDao;

}
