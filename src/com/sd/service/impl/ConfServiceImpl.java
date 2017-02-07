package com.sd.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.ConfDao;
import com.sd.service.ConfService;
import com.sd.vo.Conf;
import com.sd.vo.Topic;

@Service
public class ConfServiceImpl extends BaseServiceImpl<Conf, String> implements ConfService{
	@Resource
	public void setBaseDao(ConfDao confDao) {
		super.setBaseDao(confDao);
	}
	@Resource private ConfDao confDao;
	
	/**
	 * 查询会议列表
	 * @param dateBegin
	 * @param selectType
	 * @param count
	 * @param isFav
	 * @param userId
	 * @return
	 */
	public List<Conf> getConfList(String dateBegin, String selectType,
			String count, String isFav, String userId) {
		return confDao.getConfList(dateBegin, selectType, count, isFav, userId);
	}
	/**
	 * 议程查询
	 * @param gid
	 * @param string
	 * @return
	 */
	public List<Topic> getTopicList(String gid, String level) {
		return confDao.getTopicList(gid, level);
	}
	public List<Conf> getByMonth(Date time){
		return confDao.getByMonth(time);
	}
	
	public List<Conf> getConfListFactor(String userId, String count,
			String isFav, String status, String city,String date) {
		return confDao.getConfListFactor( userId,  count,
				 isFav,  status,  city,date);
	}
}
