package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.AcademyDao;
import com.sd.dao.AcademyFileDao;
import com.sd.dao.AdminDao;
import com.sd.service.AcademyFileService;
import com.sd.service.AcademyService;
import com.sd.service.AdminService;
import com.sd.vo.Academy;
import com.sd.vo.AcademyFile;
import com.sd.vo.Admin;

@Service
public class AcademyFileServiceImpl extends BaseServiceImpl<AcademyFile, String> implements AcademyFileService{
	@Resource
	public void setBaseDao(AcademyFileDao academyFileDao) {
		super.setBaseDao(academyFileDao);
	}
	@Resource private AcademyFileDao academyFileDao;
	
}
