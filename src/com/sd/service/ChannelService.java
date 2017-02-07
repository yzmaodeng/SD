package com.sd.service;

import java.util.List;

import com.sd.vo.Channel;
import com.sd.vo.User;

public interface ChannelService extends BaseService<Channel, String> {
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
	/**
	 * 查询用户的频道权限
	 * @param channel
	 * @param loginUser
	 * @return 1有权限0无权限
	 */
	String isAllowed(Channel channel, User loginUser);

}
