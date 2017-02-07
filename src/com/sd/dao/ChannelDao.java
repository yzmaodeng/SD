package com.sd.dao;

import java.util.List;

import com.sd.vo.Channel;
import com.sd.vo.User;

public interface ChannelDao extends BaseDao<Channel, String> {
	/**
	 * 查询频道中用户列表
	 * @param gid
	 * @return
	 */
	List<User> getUserList(String gid);
	/**
	 * 查询用户所属的频道列表
	 * @param gid
	 * @return
	 */
	List<Channel> getChannelList(String gid);
	/**
	 * 查询频道列表
	 * @param pageNum
	 * @param count
	 * @return
	 */
	List<Channel> getAllList(String pageNum, String count);

}
