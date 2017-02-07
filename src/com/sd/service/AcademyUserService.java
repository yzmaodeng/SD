package com.sd.service;

import java.util.List;

import com.sd.vo.AcademyUser;

public interface AcademyUserService extends BaseService<AcademyUser, String> {

	List<AcademyUser> getByOrganiser(String userId);
	List<AcademyUser> getByGid(String gid);
	
}
