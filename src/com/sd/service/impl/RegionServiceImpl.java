package com.sd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.RegionDao;
import com.sd.service.RegionService;
import com.sd.vo.Area;
@Service
public class RegionServiceImpl implements RegionService {
	@Resource private RegionDao regionDao;
	
	public List<Area> queryRegion(Area tarea){
		return regionDao.queryRegion(tarea);
	}
}
