package com.sd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.ChannelDao;
import com.sd.service.ChannelService;
import com.sd.vo.Channel;
import com.sd.vo.User;

@Service
public class ChannelServiceImpl extends BaseServiceImpl<Channel, String> implements ChannelService{
	@Resource
	public void setBaseDao(ChannelDao channelDao) {
		super.setBaseDao(channelDao);
	}
	@Resource private ChannelDao channelDao;
	
	/**
	 * 查询频道中用户列表
	 * @param gid
	 * @return
	 */
	public List<User> getUserList(String gid) {
		return channelDao.getUserList(gid);
	}
	/**
	 * 查询用户所属的频道列表
	 * @param gid
	 * @return
	 */
	public List<Channel> getChannelList(String gid) {
		return channelDao.getChannelList(gid);
	}
	/**
	 * 查询频道列表
	 * @param pageNum
	 * @param count
	 * @return
	 */
	public List<Channel> getAllList(String pageNum, String count) {
		return channelDao.getAllList(pageNum, count);
	}
	/**
	 * 查询用户的频道权限
	 * @param channel
	 * @param userId
	 * @return 1有权限0无权限
	 */
	public String isAllowed(Channel channel, User loginUser) {
		if (channel == null)
			return "0";
		if (loginUser == null)
			return "0";
		
		if ("1".equals(loginUser.getChAu()))
			// 全频道管理员
			return "1";
		// 非全频道管理员是时，判断是否为单一频道管理员
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*),'' ")
			.append(" from tchanneluser t")
			.append(" where t.cu_cgid = '")
			.append(channel.getGid()).append("' ")
			.append(" and t.cu_ugid = '")
			.append(loginUser.getGid()).append("' ")
			.append(" and t.cu_isadmin = '1'");
		
		String ret = channelDao.getSingleBysql(sql.toString(), 0);
		if ("0".equals(ret))
			return "0";
		return "1";
	}

}
