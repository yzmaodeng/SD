package com.sd.service;

import java.util.List;

import com.sd.vo.Posts;

public interface PostsService extends BaseService<Posts, String> {

	/**
	 * 查询频道帖子
	 * 
	 * @param chGid
	 * @param dateBegin
	 * @param count
	 * @param isFav
	 * @param userId
	 * @param orderNum
	 * @return
	 */
	List<Posts> getPostsList(String chGid, String mark, String dateBegin,
			String count, String isFav, String userId, String orderNum);

	/**
	 * 查询用户的帖子
	 * 
	 * @param userId
	 * @return
	 */
	List<Posts> getPostsList(String gid);

	/**
	 * 查询频道帖子数
	 * 
	 * @param gid
	 * @param type
	 * @return
	 */
	String getCountNum(String gid, String type);

	/**
	 * 刷新帖子的点赞数
	 * 
	 * @param postid
	 */
	void updateFavCount(String postid);

	/**
	 * 刷新帖子的回复数
	 * 
	 * @param postid
	 */
	void updateReplyCount(String postid);

	List<Posts> getPostsListByPage(String userId, String pageNum,
			String pageSize);

	int countNumBySql(String s);

	List<Posts> queryHomePosts();
}
