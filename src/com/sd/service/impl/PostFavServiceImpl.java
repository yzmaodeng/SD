package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.PostFavDao;
import com.sd.service.PostFavService;
import com.sd.vo.PostFav;

@Service
public class PostFavServiceImpl extends BaseServiceImpl<PostFav, String> implements PostFavService{
	@Resource
	public void setBaseDao(PostFavDao postFavDao) {
		super.setBaseDao(postFavDao);
	}
	@Resource private PostFavDao postFavDao;
	
	/**
	 * 获取帖子是否点赞
	 * @param gid
	 * @param userId
	 * @return
	 */
	public String isPraised(String gid, String userId) {
		return postFavDao.isPraised(gid, userId);
	}

}
