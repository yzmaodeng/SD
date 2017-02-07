package com.sd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.sd.dao.PostsDao;
import com.sd.service.PostsService;
import com.sd.vo.Posts;

@Service
public class PostsServiceImpl extends BaseServiceImpl<Posts, String> implements PostsService{
	@Resource
	public void setBaseDao(PostsDao postsDao) {
		super.setBaseDao(postsDao);
	}
	@Resource private PostsDao postsDao;
	
	/**
	 * 查询频道帖子
	 * @param chGid
	 * @param dateBegin
	 * @param count
	 * @param isFav
	 * @param userId
	 * @return
	 */
	public List<Posts> getPostsList(String chGid,String mark, String dateBegin,
			String count, String isFav, String userId,String orderNum) {
		return postsDao.getPostsList(chGid,mark, dateBegin, count, isFav, userId,orderNum);
	}

	/**
	 * 查询用户的帖子
	 * @param userId
	 * @return
	 */
	public List<Posts> getPostsList(String gid) {
		return postsDao.getPostsList(gid);
	}

	/**
	 * 查询帖子数
	 * @param gid
	 * @param type
	 * @return
	 */
	public String getCountNum(String gid, String type) {
		StringBuffer sql = new StringBuffer();
		if ("new".equals(type))
			sql.append(" select count(*),'' from tpost t ")
				.append(" where t.pst_isdel='1' ")
				.append(" and to_days(t.pst_CreateTime) = to_days(now()) ")
				.append(" and t.pst_chGid = '")
				.append(gid).append("' ");
		else
			sql.append(" select count(*),'' from tpost t ")
				.append(" where t.pst_isdel='1' ")
				.append(" and t.pst_chGid = '")
				.append(gid).append("' ");
		
		String ret = postsDao.getSingleBysql(sql.toString(), 0);
		if (StringUtils.isBlank(ret))
			return "0";
		else
			return ret;
	}
	/**
	 * 刷新帖子的点赞数
	 * @param postid
	 */
	public void updateFavCount(String postid) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*),'' from tpostfav t ")
			.append(" where t.pf_isdel='1' ")
			.append(" and t.pf_pstGid = '")
			.append(postid).append("' ");
		String ret = postsDao.getSingleBysql(sql.toString(), 0);
		Posts post = postsDao.get(postid);
		post.setFavCount(ret);
		postsDao.update(post);
	}
	/**
	 * 刷新帖子的回复数
	 * @param postid
	 */
	public void updateReplyCount(String postid) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*),'' from treply t ")
			.append(" where t.rpy_isdel='1' ")
			.append(" and t.rpy_pstGid = '")
			.append(postid).append("' ");
		 String singleBysql = postsDao.getSingleBysql(sql.toString(), 0);
		Posts post = postsDao.get(postid);
		post.setReplyCount(Integer.parseInt(singleBysql));
		postsDao.update(post);
	}

	@Override
	public List<Posts> getPostsListByPage(String userId, String pageNum,
			String pageSize) {
		int num = Integer.parseInt(pageNum);
		int size = Integer.parseInt(pageSize);
		int firstNUm = (Integer.parseInt(pageNum) - 1)* Integer.parseInt(pageSize);
		String SqlString = "select * from tpost where pst_isdel='1'and pst_userGid='"+userId+"' order by pst_CreateTime desc  limit "+firstNUm+","+size;
		List<Posts> voListBySql = postsDao.getVoListBySql(SqlString);
		return voListBySql;
	}

	@Override
	public int countNumBySql(String s) {
		return postsDao.todayNum(s);
	}

	@Override
	public List<Posts> queryHomePosts() {
		String string="SELECT * FROM tpost WHERE top=1";
		return postsDao.getVoListBySql(string);
	}

}
