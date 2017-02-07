package com.sd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.AdvertisementDao;
import com.sd.service.AdvertisementService;
import com.sd.vo.Advertisement;

@Service
public class AdvertisementServiceImpl extends BaseServiceImpl<Advertisement, String>
		implements AdvertisementService {
	
	@Resource
	public void setBaseDao(AdvertisementDao advertisementDao) {
		super.setBaseDao(advertisementDao);
	}
	@Resource private AdvertisementDao advertisementDao;
	@Override
	public List<Advertisement> getDailyRec() {
		String s="SELECT * FROM advertisement WHERE iosUrl='1'";		
		return advertisementDao.getVoListBySql(s);
	}



	
}
