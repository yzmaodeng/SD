package com.sd.service;

import java.util.List;

import com.sd.vo.Academy;
import com.sd.vo.AcademyVip;
import com.sd.vo.Admin;
import com.sd.vo.NeedsChange;
import com.sd.vo.Notification;

public interface AcademyService extends BaseService<Academy, String> {	
	List<Academy> getHomeAcademy();
	List<Academy> getListBySql(int firstNUm, String pageCount);
	AcademyVip getVipInfo(String gid);
	void deleteVip(String academyGid, int num);
	List<NeedsChange> needsChange();
}
