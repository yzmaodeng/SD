package com.sd.service;

import java.util.Date;
import java.util.List;

import com.sd.vo.Conf;
import com.sd.vo.Topic;

public interface ConfService extends BaseService<Conf, String> {
	/**
	 * 查询会议列表
	 * @param dateBegin
	 * @param selectType
	 * @param count
	 * @param isFav
	 * @param userId 
	 * @return
	 */
	List<Conf> getConfList(String dateBegin, String selectType, String count,
			String isFav, String userId);
	/**
	 * 议程查询
	 * @param gid
	 * @param string
	 * @return
	 */
	List<Topic> getTopicList(String gid, String level);
	List<Conf> getByMonth(Date time);
	List<Conf> getConfListFactor(String userId, String count, String isFav,
			String status, String city, String date);
}
