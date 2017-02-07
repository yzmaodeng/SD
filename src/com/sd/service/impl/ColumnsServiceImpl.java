package com.sd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.ChannelDao;
import com.sd.dao.ColumnsDao;
import com.sd.service.ColumnsService;
import com.sd.vo.Columns;

@Service
public class ColumnsServiceImpl extends BaseServiceImpl<Columns, String> implements ColumnsService{
	@Resource
	public void setBaseDao(ColumnsDao columnsDao) {
		super.setBaseDao(columnsDao);
	}
	@Resource private ColumnsDao columnsDao;
	@Override
	public List<Columns> getHomeColumn() {
		String string="SELECT * FROM `columns` WHERE recommend='1'";
		return columnsDao.getVoListBySql(string);
	}
	@Override
	public Columns getById(String gid) {
		Columns columns = columnsDao.get(gid);
		
		return columns;
	}
	
	
}
