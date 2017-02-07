package com.sd.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.sd.dao.ChinaAreaDao;
import com.sd.service.ChinaAreaService;
import com.sd.vo.ChinaArea;

@Service
public class ChinaAreaServiceImpl extends BaseServiceImpl<ChinaArea, String> implements ChinaAreaService{
	@Resource
	public void setBaseDao(ChinaAreaDao chinaAreaDao) {
		super.setBaseDao(chinaAreaDao);
	}
	@Resource private ChinaAreaDao chinaAreaDao;
	
	
}
