package com.sd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.NeedsChangeDao;
import com.sd.service.NeedsChangeService;
import com.sd.vo.NeedsChange;

@Service
public class NeedsChangeServiceImpl extends BaseServiceImpl<NeedsChange, String> implements NeedsChangeService
{
	@Resource
	public void setBaseDao(NeedsChangeDao needsChangeDao) {
		super.setBaseDao(needsChangeDao);
	}
	@Resource private NeedsChangeDao needsChangeDao;
	

	@Override
	public boolean checkByCodeToken(String codeToken,String mobile) {
		String string="select * from needChange where mobile="+mobile+" order by createTime desc limit 1 ";
		List<NeedsChange> voListBySql = needsChangeDao.getVoListBySql(string);
		if (voListBySql.size()==0) {
			return false;
		} else {
			NeedsChange needsChange = voListBySql.get(0);
            if (needsChange.getToken().equals(codeToken)) {
				return true;
			} else {
                 return false;
			}
		}
	}






	
}
