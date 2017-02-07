package com.sd.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.MotionDao;
import com.sd.dao.TargetDao;
import com.sd.service.MotionService;
import com.sd.vo.Motion;
import com.sd.vo.MotionTmpl;
import com.sd.vo.SchemeTmpl;
import com.sd.vo.Target;
import com.sd.vo.TargetTmpl;

@Service
public class MotionServiceImpl extends BaseServiceImpl<Motion, String> implements MotionService{
	@Resource
	public void setBaseDao(MotionDao motionDao) {
		super.setBaseDao(motionDao);
	}
	@Resource private MotionDao motionDao;
	@Resource private TargetDao targetDao;
	
	/**
	 * 复制模版动作，目标成为患者的动作，目标
	 * @param newScheme	方案模版
	 * @param newSgid	新方案id
	 */
	public void saveMotionByCopy(SchemeTmpl newScheme, String newSgid) {
		Date now = new Date();
		for (MotionTmpl mtmpl : newScheme.getMotionSet()){
			Motion motion = new Motion();
			motion.setCreateTime(now);
			motion.setUpdateTime(now);
			motion.setIsdel("1");
			motion.setStMotionId(mtmpl.getStMotionId());
			motion.setOrdering(mtmpl.getOrdering());
			motion.setSgid(newSgid);
			String mgid = motionDao.save(motion);
			
			for (TargetTmpl ttmpl : mtmpl.getTargetSet()){
				Target target = new Target();
				target.setCreateTime(now);
				target.setUpdateTime(now);
				target.setIsdel("0");
				target.setBdays(ttmpl.getBdays());
				target.setEdays(ttmpl.getEdays());
				target.setGroup(ttmpl.getGroup());
				target.setMgid(mgid);
				target.setTarget(ttmpl.getTarget());
				target.setNum(ttmpl.getNum());
				targetDao.save(target);
			}
		}
		
	}
	
}