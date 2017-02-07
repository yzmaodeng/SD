package com.sd.service.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.PostAnswerDao;
import com.sd.dao.PostSubjectDao;
import com.sd.service.PostAnswerService;
import com.sd.vo.PostAnswer;

@Service
public class PostAnswerServiceImpl extends BaseServiceImpl<PostAnswer, String>
		implements PostAnswerService {
	@Resource
	public void setBaseDao(PostAnswerDao postAnswerDao) {
		super.setBaseDao(postAnswerDao);
	}

	@Resource
	private PostAnswerDao postAnswerDao;

	@Override
	public String getStatus(String userId, String subjectgid) {
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("userId", userId);
		condition.put("postsubjectGid", subjectgid);
		return postAnswerDao.getConditonCount(condition);
	}

}
