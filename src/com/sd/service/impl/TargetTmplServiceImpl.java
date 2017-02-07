package com.sd.service.impl;

import java.util.Date;
import java.util.Iterator;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.sd.dao.TargetTmplDao;
import com.sd.service.TargetTmplService;
import com.sd.vo.TargetTmpl;

@Service
public class TargetTmplServiceImpl extends BaseServiceImpl<TargetTmpl, String> implements TargetTmplService{
	@Resource
	public void setBaseDao(TargetTmplDao targetTmplDao) {
		super.setBaseDao(targetTmplDao);
	}
	@Resource private TargetTmplDao targetTmplDao;
	
	/**
	 * 根据json串，保存target
	 * @param mgid
	 * @param jsonTarget 
	 */
	@SuppressWarnings({ "static-access", "rawtypes" })
	public void saveJsonTarget(String mgid, Object jsonTarget) {
		if (jsonTarget != null){
			Date now = new Date();
			JSONArray json = new JSONArray().fromObject(jsonTarget);
			Iterator iter = json.iterator();
			while (iter.hasNext()) {
				JSONObject obj = new JSONObject().fromObject(iter.next());
				String beginDay = obj.get("beginDay") + "";
				String endDay = obj.get("endDay") + "";
				String gCount = obj.get("gCount") + "";
				String eCount = obj.get("eCount") + "";
				
				TargetTmpl target = new TargetTmpl();
				target.setBdays(beginDay);
				target.setEdays(endDay);
				target.setGroup(gCount);
				target.setNum(eCount);
				target.setTarget(obj.get("target")+"");
				target.setUpdateTime(now);
				target.setCreateTime(now);
				target.setIsdel("0");
				target.setMgid(mgid);
				targetTmplDao.save(target);
			}
		}
	}	
}
