package com.sd.service;

import com.sd.vo.PostFav;

public interface PostFavService extends BaseService<PostFav, String> {
	/**
	 * 获取帖子是否点赞
	 * @param gid
	 * @param userId
	 * @return
	 */
	String isPraised(String gid, String userId);

}
