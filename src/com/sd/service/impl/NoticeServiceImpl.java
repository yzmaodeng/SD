package com.sd.service.impl;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.NoticeDao;
import com.sd.dao.NoticeDetailDao;
import com.sd.service.NoticeService;
import com.sd.vo.Notice;
import com.sd.vo.NoticeDetail;

@Service
public class NoticeServiceImpl extends BaseServiceImpl<Notice, String> implements NoticeService{
	@Resource
	public void setBaseDao(NoticeDao noticeDao) {
		super.setBaseDao(noticeDao);
	}
	@Resource private NoticeDao noticeDao;
	@Resource private NoticeDetailDao noticeDetailDao;
	
	/**
	 * 根据用户和帖子信息获得未读消息
	 * @param user
	 * @param gid
	 * @return
	 */
	public Notice getByUserPost(String user, String gid) {
		return noticeDao.getByUserPost(user, gid);
	}
	/**
	 * 获取消息列表
	 * @param userId
	 * @param dateBegin
	 * @param count
	 * @param type
	 * @return
	 */
	public List<Notice> getNoticeList(String userId, String dateBegin,
			String count, String type) {
		return noticeDao.getNoticeList(userId, dateBegin, count, type);
	}
	/**
	 * 删除参与者明细，并刷新表头
	 * @param notice
	 * @param userId
	 */
	public void refreshOneNotice(Notice notice, String userId) {
		Set<NoticeDetail> detailSet = notice.getDetailSet();
		if (detailSet.size() > 1) {
			// 只删明细
			for (NoticeDetail dedatil : detailSet)
				if (userId.equals(dedatil.getParticipate()))
					noticeDetailDao.delete(dedatil);
		} else {
			// 删明细及表头
			for (NoticeDetail dedatil : detailSet)
				noticeDetailDao.delete(dedatil);
			noticeDao.delete(notice);
		}
	}
}
