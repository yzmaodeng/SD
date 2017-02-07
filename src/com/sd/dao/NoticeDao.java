package com.sd.dao;

import java.util.List;

import com.sd.vo.Notice;

public interface NoticeDao extends BaseDao<Notice, String> {
	/**
	 * 根据用户和帖子信息获得未读消息
	 * @param user
	 * @param gid
	 * @return
	 */
	Notice getByUserPost(String user, String gid);
	/**
	 * 获取消息列表
	 * @param userId
	 * @param dateBegin
	 * @param count
	 * @param type
	 * @return
	 */
	List<Notice> getNoticeList(String userId, String dateBegin, String count,
			String type);
}
