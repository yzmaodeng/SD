package com.sd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.AcademyUserDao;
import com.sd.service.AcademyUserService;
import com.sd.vo.AcademyUser;

@Service
public class AcademyUserServiceImpl extends BaseServiceImpl<AcademyUser, String>
		implements AcademyUserService {
	
	@Resource
	public void setBaseDao(AcademyUserDao academyUserDao) {
		super.setBaseDao(academyUserDao);
	}
	@Resource private AcademyUserDao academyUserDao;



	@Override
	public List<AcademyUser> getByOrganiser(String userId) {
		String sqlString="select  * from  academyUser  where  organiserId='"+userId+"'";
		return academyUserDao.getVoListBySql(sqlString);
	}
	
	@Override
	public List<AcademyUser> getByGid(String gid) {
		String sqlString="select  * from  academyUser  where  gid='"+gid+"'";
		return academyUserDao.getVoListBySql(sqlString);
	}

}
