package com.sd.service;

import java.util.List;

import com.sd.vo.Advertisement;

public interface AdvertisementService extends BaseService<Advertisement , String> {

	List<Advertisement> getDailyRec();


	
}
