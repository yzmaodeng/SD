package com.sd.dao;

import java.util.List;

import com.sd.vo.OrderInfo;

public interface OrderInfoDao extends BaseDao<OrderInfo,String>{
	public List<OrderInfo> getOrderListByQbc(String userGid,int type);
	public int updateOrderStatus(String gid,int status);
	public int totleNum(String string);
	public List<OrderInfo> getOrderCourse(String userGid, int type,
			String orderType, String gid);
	public List<OrderInfo> getOrderCourseOrNum(String userGid, int type,
			String orderType, String gid);
}
