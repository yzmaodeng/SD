package com.sd.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.sd.util.CustomException;
import com.sd.vo.OrderInfo;

public interface OrderInfoService extends BaseService<OrderInfo,String>{
	
	public List<OrderInfo> getOrdersByUserGidandType(String userGid, int type);
	public BigInteger getCountByUserGidandType(String userGid, int type);
	public Map<String,Object> createOrderInfo(String remark,String orderNum,String userGid,int type,String academyGid,String contactGids,String gid, String couponGid, String invoiceGid, String type1) throws CustomException;
	public int updateOrderStatus(String gid,int status);
	public List<OrderInfo> getOrderDetailByGid(String gid);
	public void deleteCouIvoice(String gid) throws CustomException;
	public void createRewardOrder(String userId);
	public int getOrderNum(String userId);
	public List<OrderInfo> getPrice(String orderNo, String userId);
	public void changeVipNum(String trade_no);
	public void deleteByGid(String gid);
	List<OrderInfo> getOrderCourse(String userGid, int type, String orderType, String string);
	public List<OrderInfo> getOrderCourseOrNum(String userId, int i,
			String string, String gid);
	public List<OrderInfo> getByOrderNu(String orderNum);
	public List<OrderInfo> getOrders(String gid, String phone);
	public List<OrderInfo> getByUserIdAcademyId(String userId, String gid);
	
}
