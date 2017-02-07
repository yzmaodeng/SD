package com.sd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.ColumnContentDao;
import com.sd.dao.ColumnsDao;
import com.sd.service.ColumnContentService;
import com.sd.vo.ColumnContent;

@Service
public class ColumnContentServiceImpl extends BaseServiceImpl<ColumnContent, String> implements ColumnContentService{
	@Resource
	public void setBaseDao(ColumnContentDao columnContentDao) {
		super.setBaseDao(columnContentDao);
	}
	@Resource private ColumnContentDao columnContentDao;
	@Override
	public List<ColumnContent> getByParentID(String gid) {
		String string="SELECT * FROM columnContent WHERE parentgid='"+gid+"'ORDER BY createtime DESC LIMIT 0,10;";
		return columnContentDao.getVoListBySql(string);
	}
	
	
}
