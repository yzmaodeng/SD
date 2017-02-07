package com.sd.dao;

import com.sd.vo.PostFav;

public interface PostFavDao extends BaseDao<PostFav, String> {

	/**
	 * 获取帖子是否点赞
	 * @param gid
	 * @param userId
	 * @return
	 */
	String isPraised(String gid, String userId);
	
}
