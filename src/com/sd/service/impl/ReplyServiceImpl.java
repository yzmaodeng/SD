package com.sd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.ReplyDao;
import com.sd.service.ReplyService;
import com.sd.vo.Reply;

@Service
public class ReplyServiceImpl extends BaseServiceImpl<Reply, String> implements ReplyService{
	@Resource
	public void setBaseDao(ReplyDao replyDao) {
		super.setBaseDao(replyDao);
	}
	@Resource private ReplyDao replyDao;
	
	/**
	 * 根据帖子id查询回复列表
	 * @param gid
	 * @return
	 */
	public List<Reply> getReplyClient(String gid) {
		return replyDao.getReplyClient(gid);
	}

}
