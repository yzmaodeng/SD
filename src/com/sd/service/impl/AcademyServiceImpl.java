package com.sd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.AcademyDao;
import com.sd.dao.AcademyVipDao;
import com.sd.dao.NeedsChangeDao;
import com.sd.service.AcademyService;
import com.sd.vo.Academy;
import com.sd.vo.AcademyVip;
import com.sd.vo.NeedsChange;

@Service
public class AcademyServiceImpl extends BaseServiceImpl<Academy, String>
		implements AcademyService {
	@Resource
	public void setBaseDao(AcademyDao academyDao) {
		super.setBaseDao(academyDao);
	}

	@Resource
	private AcademyDao academyDao;
	
	


	@Resource
	private AcademyVipDao academyVipDao;
	
	@Resource
	private  NeedsChangeDao needsChangeDao;

	
	
	

	@Override
	public List<Academy> getHomeAcademy() {
		String SqlString = "SELECT * from academy where inuse='1' and commend='1'  order by createTime desc limit 3";
		return academyDao.getVoListBySql(SqlString);

	}

	@Override
	public List<Academy> getListBySql(int firstNUm, String pageCount) {
		String SqlString="SELECT * from academy where  inuse ='1' and stageTime<now() order by createTime desc limit "+firstNUm+","+pageCount;
		return academyDao.getVoListBySql(SqlString);
	}

	@Override
	public AcademyVip getVipInfo(String gid) {
		
		 String sqlStr="select 	* from 	academyVip where academyGid='"+gid+"'";
		 List<AcademyVip> voListBySql = academyVipDao.getVoListBySql(sqlStr);
		 if (voListBySql.size()==0) {
			return null;
		} else {
			return  voListBySql.get(0);
		}
		
	}

	@Override
	public void deleteVip(String academyGid, int num) {

		
		String   sqlString="update 	academyVip  set   resNum=resNum-"+num+" where academyGid='"+academyGid+"'";
		academyVipDao.executeUpdateSql(sqlString);
	}

	@Override
	public List<NeedsChange> needsChange() {
		
		String string="select * from needChange where id=1";
		return needsChangeDao.getVoListBySql(string);
	}
	
	            
	
	
	
	

}
