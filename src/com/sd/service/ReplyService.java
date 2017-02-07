package com.sd.service;

import java.util.List;

import com.sd.vo.Reply;

public interface ReplyService extends BaseService<Reply, String> {
	/**
	 * 根据帖子id查询回复列表
	 * @param gid
	 * @return
	 */
	List<Reply> getReplyClient(String gid);

}
