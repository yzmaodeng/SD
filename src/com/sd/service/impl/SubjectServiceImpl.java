package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.SubjectDao;
import com.sd.service.SubjectService;
import com.sd.vo.Subject;

@Service
public class SubjectServiceImpl extends BaseServiceImpl<Subject, String> implements SubjectService{
	@Resource
	public void setBaseDao(SubjectDao subjectDao) {
		super.setBaseDao(subjectDao);
	}
	@Resource private SubjectDao subjectDao;
	
}
