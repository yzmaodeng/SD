package com.sd.dao;

import java.util.List;

import com.sd.vo.Posts;

public interface PostsDao extends BaseDao<Posts, String> {

	/**
	 * 查询频道帖子
	 * @param chGid
	 * @param dateBegin
	 * @param count
	 * @param isFav
	 * @param userId
	 * @param orderNum 
	 * @return
	 */
	List<Posts> getPostsList(String chGid,String mark, String dateBegin, String count,
			String isFav, String userId, String orderNum);

		/**
	 * 查询用户的帖子
	 * @param userId
	 * @return
	 */
	List<Posts> getPostsList(String gid);

		int todayNum(String s);

}
