package com.sd.service.impl;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Service;

import com.sd.dao.OrderInfoDao;
import com.sd.service.AcademyService;
import com.sd.service.AcademyUserService;
import com.sd.service.ColumnContentService;
import com.sd.service.ColumnsService;
import com.sd.service.CouponService;
import com.sd.service.CourseService;
import com.sd.service.CourseVideoService;
import com.sd.service.InvoiceService;
import com.sd.service.OrderInfoService;
import com.sd.util.ComUtil;
import com.sd.util.CustomException;
import com.sd.util.SmsUtil;
import com.sd.vo.Academy;
import com.sd.vo.AcademyUser;
import com.sd.vo.AcademyVip;
import com.sd.vo.Columns;
import com.sd.vo.Coupon;
import com.sd.vo.Course;
import com.sd.vo.CourseVideo;
import com.sd.vo.Invoice;
import com.sd.vo.OrderInfo;

@Service
public class OrderInfoServiceImpl extends BaseServiceImpl<OrderInfo, String>
		implements OrderInfoService {
	@Resource
	public void setBaseDao(OrderInfoDao orderInfoDao) {
		super.setBaseDao(orderInfoDao);
	}

	@Resource
	private OrderInfoDao orderinfoDao;
	@Resource protected ColumnsService columnsService;
	@Resource protected ColumnContentService columnContentService;
	
	@Resource
	private AcademyService academyService;
	@Resource
	private CourseService courseService;
	@Resource
	private CourseVideoService courseVideoService;
	@Resource
	private AcademyUserService academyUserService;
	@Resource
	private CouponService couponService;
	@Resource
	private InvoiceService invoiceService;

	@Override
	public List<OrderInfo> getOrdersByUserGidandType(String userGid, int type) {

		return orderinfoDao.getOrderListByQbc(userGid, type);
	}
	@Override
	public List<OrderInfo> getOrderCourse(String userGid,int type,String orderType,String gid) {

		return orderinfoDao.getOrderCourse(userGid, type,orderType,gid);
	}
	

	public BigInteger getCountByUserGidandType(String userGid, int type) {
		String sqlString = "select count(*) from orderInfo where userGid = ? and type = ?";
		Query query = orderinfoDao.getSession().createSQLQuery(sqlString);
		query.setString(0, userGid);
		query.setInteger(1, type);
		return (BigInteger) query.uniqueResult();
	}

	@Override
	public Map<String, Object> createOrderInfo(String remark, String orderNum,
			String userGid, int type, String academyGid, String contactGids,
			String gid, String couponGid, String invoiceGid, String orderType) throws CustomException {
		Map<String, Object> resMap = new HashMap<String, Object>();
		JSONObject resCourse = new JSONObject();
		DecimalFormat df = new DecimalFormat("#0.00");
		String[] strArray = new String[30];
		strArray = contactGids.split(",");
		Date now = new Date();
		// 计算优惠总额
		int num = strArray.length;
		//type decide the course and orderType decide the real type of get,academyGid==courseGid,contactGids=videoGids
		if(type==4){
			int discount=0;
			OrderInfo order = null;
			if (StringUtils.isNotEmpty(couponGid)) {
				List<Coupon> couponList = couponService.getByGid(couponGid);
				Coupon coupon = couponList.get(0);
				discount = Integer.parseInt(coupon.getDiscount());
				couponService.updateCouponStatus(couponGid);
				 
			}
			
			
			if("1".equals(orderType))
			{
				Course course = courseService.get(academyGid);
				if(course==null)
					throw new CustomException("该慕课已经不存在！！");
				resCourse.put("title", course.getTitle());
				resCourse.put("orderType", "1");
				 order = new OrderInfo();
				order.setOrderNum(orderNum);
				order.setUserGid(userGid);
				order.setType(4);
				//academyGid就存courseGid
				order.setAcademyGid(academyGid);
				order.setContactGids("");
				order.setCreateTime(now);
				order.setHotline("13699187603");
				order.setStatus(1);
				order.setGid(gid);
				if (!StringUtils.isNotEmpty(couponGid))
					couponGid = "";
				order.setCouponId(couponGid);
				if (!StringUtils.isNotEmpty(invoiceGid))
					invoiceGid = "";
				order.setInvoiceId(invoiceGid);
				
				String totalprice = df.format(course.getPrice()-discount);
				
				
				
				order.setTotalprice(totalprice);
				resMap.put("totalPrice", totalprice);
				order.setRemark(remark);
				order.setHotline("13699187603");
				order.setOrderType("1");
				orderinfoDao.save(order);
				
				
			}else
			{  
				JSONArray jsonArray = new JSONArray();
				float d=0;
				if(num==0)
					throw new CustomException("请选择相关视频");
				
				resCourse.put("orderType", "2");
				for(String c : strArray){
					CourseVideo courseVideo = courseVideoService.get(c);
					if(courseVideo==null)
						throw new CustomException("该视频已经不存在！！");
					JSONObject resVideo = new JSONObject();
					resVideo.put("title", courseVideo.getTitle());
					jsonArray.add(resVideo);
					  float price = courseVideo.getPrice();
					  
					 d+=price;
				}
				resCourse.put("videoTitals", jsonArray);
				d=d-discount;
				 order = new OrderInfo();
				order.setOrderNum(orderNum);
				order.setUserGid(userGid);
				order.setType(4);
				order.setHotline("13699187603");
				order.setAcademyGid(academyGid);
				order.setContactGids(contactGids);
				order.setCreateTime(now);
				order.setStatus(1);
				order.setGid(gid);
				if (!StringUtils.isNotEmpty(couponGid))
					couponGid = "";
				order.setCouponId(couponGid);
				if (!StringUtils.isNotEmpty(invoiceGid))
					invoiceGid = "";
				order.setInvoiceId(invoiceGid);
				order.setTotalprice(df.format(d));
				resMap.put("totalPrice", df.format(d));
				order.setRemark(remark);
				order.setHotline("13699187603");
				order.setOrderType("2");
				orderinfoDao.save(order);
					
			}
			resMap.put("resCourse", resCourse);
			//返回发票的信息
			// 组织返回信息
			// 发票返回信息
			JSONObject resInvoice = new JSONObject();
			if (!invoiceGid.isEmpty()) {
				List<Invoice> invoiceList = invoiceService
						.getInvoiceByGid(invoiceGid);
				Invoice invoice = invoiceList.get(0);
				String type2 = invoice.getType();
				String invoiceType = "";
				if ("1".equals(type2)) {
					invoiceType = "会务费";
				} else {
					invoiceType = "会议费";
				}
				resInvoice.put("name", invoice.getName());
				resInvoice.put("content", invoiceType);
				resInvoice.put("detail", invoice.getDetail());
				resMap.put("invoice", resInvoice);
			} else {
				resInvoice.put("name", "");
				resInvoice.put("detail", "");
				resMap.put("invoice", resInvoice);
			}
			
			
			
			resMap.put("orderGid", gid);
			resMap.put("orderNum", orderNum);
			resMap.put("markInfor", remark == null ? "" : remark);
			resMap.put("serviceTel", order.getHotline());
			resMap.put("status", order.getStatus());
			
			
			
			
		}else if (type==5) {
			
			if (StringUtils.isNotEmpty(couponGid)) {
				List<Coupon> couponList = couponService.getByGid(couponGid);
				Coupon coupon = couponList.get(0);
				 int parseInt = Integer.parseInt(coupon.getDiscount());
				resMap.put("couponGid", coupon.getGid());
				resMap.put("discount", coupon.getDiscount());
				couponService.updateCouponStatus(couponGid);
			} else {
				resMap.put("couponGid", "");
				resMap.put("discount", "");
			}
			
			JSONObject resInvoice = new JSONObject();
			if (!invoiceGid.isEmpty()) {
				List<Invoice> invoiceList = invoiceService
						.getInvoiceByGid(invoiceGid);
				Invoice invoice = invoiceList.get(0);
				String type2 = invoice.getType();
				String invoiceType = "";
				if ("1".equals(type2)) {
					invoiceType = "会务费";
				} else {
					invoiceType = "会议费";
				}
				resInvoice.put("name", invoice.getName());
				resInvoice.put("content", invoiceType);
				resInvoice.put("detail", invoice.getDetail());
				resMap.put("invoice", resInvoice);
			} else {
				resInvoice.put("name", "");
				resInvoice.put("detail", "");
				resMap.put("invoice", resInvoice);
			}
			
			
			OrderInfo order = new OrderInfo();
			order.setOrderNum(orderNum);
			order.setUserGid(userGid);
			order.setType(type);
			order.setAcademyGid(academyGid);
			order.setContactGids(contactGids);
			order.setCreateTime(now);
			order.setStatus(1);
			order.setGid(gid);
			if (!StringUtils.isNotEmpty(couponGid))
				couponGid = "";
			order.setCouponId(couponGid);
			if (!StringUtils.isNotEmpty(invoiceGid))
				invoiceGid = "";
			order.setInvoiceId(invoiceGid);
			order.setTotalprice("3998");
			order.setRemark(remark);
			order.setHotline("13699187603");
			order.setOrderType(orderType);
			orderinfoDao.save(order);
			resMap.put("orderGid", gid);
			resMap.put("totalPrice", "3998");
			resMap.put("orderNum", orderNum);
			resMap.put("markInfor", remark == null ? "" : remark);
			resMap.put("serviceTel", order.getHotline());
			resMap.put("status", "1");
		}else if (type==6) {
			if (StringUtils.isNotEmpty(couponGid)) {
				List<Coupon> couponList = couponService.getByGid(couponGid);
				Coupon coupon = couponList.get(0);
				 int parseInt = Integer.parseInt(coupon.getDiscount());
				resMap.put("couponGid", coupon.getGid());
				resMap.put("discount", coupon.getDiscount());
				couponService.updateCouponStatus(couponGid);
			} else {
				resMap.put("couponGid", "");
				resMap.put("discount", "");
			}
			
			JSONObject resInvoice = new JSONObject();
			if (!invoiceGid.isEmpty()) {
				List<Invoice> invoiceList = invoiceService
						.getInvoiceByGid(invoiceGid);
				Invoice invoice = invoiceList.get(0);
				String type2 = invoice.getType();
				String invoiceType = "";
				if ("1".equals(type2)) {
					invoiceType = "会务费";
				} else {
					invoiceType = "会议费";
				}
				resInvoice.put("name", invoice.getName());
				resInvoice.put("content", invoiceType);
				resInvoice.put("detail", invoice.getDetail());
				resMap.put("invoice", resInvoice);
			} else {
				resInvoice.put("name", "");
				resInvoice.put("detail", "");
				resMap.put("invoice", resInvoice);
			}
			
			
			OrderInfo order = new OrderInfo();
			order.setOrderNum(orderNum);
			order.setUserGid(userGid);
			order.setType(type);
			order.setAcademyGid(academyGid);
			order.setContactGids(contactGids);
			order.setCreateTime(now);
			order.setStatus(1);
			order.setGid(gid);
			if (!StringUtils.isNotEmpty(couponGid))
				couponGid = "";
			order.setCouponId(couponGid);
			if (!StringUtils.isNotEmpty(invoiceGid))
				invoiceGid = "";
			order.setInvoiceId(invoiceGid);
			 Columns columns = columnsService.get(academyGid);
				if (columns==null) {
					throw new CustomException("专栏不存在");
				}
				String price = columns.getPrice();
			order.setTotalprice(price);
			order.setRemark(remark);
			order.setHotline("13699187603");
			order.setOrderType(orderType);
			orderinfoDao.save(order);
			resMap.put("orderGid", gid);
			resMap.put("totalPrice", price);
			resMap.put("orderNum", orderNum);
			resMap.put("markInfor", remark == null ? "" : remark);
			resMap.put("serviceTel", order.getHotline());
			resMap.put("status", "1");
			
		}else{
			Academy academy = academyService.get(academyGid);
			AcademyVip vipInfo =null;
			if (type==3) {
				vipInfo=academyService.getVipInfo(academyGid);
				if (vipInfo.getResNum()<=0||vipInfo.getResNum()<num) {
					throw new CustomException("vip票仅剩"+vipInfo.getResNum()+"张，当前订票人数为"+num+"人");
				}
			}
			String vipPrice="";
			Integer totleNum = academy.getTotleNum() + strArray.length;
			academy.setTotleNum(totleNum);
			academyService.update(academy);
			String price = academy.getPrice();
			String priceI = academy.getPriceI();
			int discount = 0;
			double totalprice;
			if (StringUtils.isNotEmpty(couponGid)) {
				List<Coupon> couponList = couponService.getByGid(couponGid);
				Coupon coupon = couponList.get(0);
				discount = Integer.parseInt(coupon.getDiscount());
				resMap.put("couponGid", coupon.getGid());
				resMap.put("discount", coupon.getDiscount());
				couponService.updateCouponStatus(couponGid);
			} else {
				resMap.put("couponGid", "");
				resMap.put("discount", "");
			}
			Double d = (double) 0;
			 resMap.put("vipPrice", "");
			if (type == 3&&vipInfo!=null) {
			    vipPrice = vipInfo.getPrice();
			    resMap.put("vipPrice", vipPrice);
				totalprice = Double.parseDouble(vipPrice) * num - discount;
				 academyService.deleteVip(academyGid,num);

			} else {

				if (num >= 3) {
					if ("1".equals(orderType)) {
						totalprice = Double.parseDouble(priceI) * num - discount;
					} else if ("2".equals(orderType)) {
						d = Double.parseDouble(academy.getPriceII()) * 0.1 * num;
						totalprice = Double.parseDouble(academy.getPriceII()) * num
								* 0.9 - discount;
					} else {
						totalprice = Double.parseDouble(academy.getPriceIII())
								* num * 0.9 - discount;
						d = Double.parseDouble(academy.getPriceIII()) * 0.1 * num;
					}
				} else {

					if ("1".equals(orderType)) {
						totalprice = Double.parseDouble(price) * num - discount;
					} else if ("2".equals(orderType)) {
						totalprice = Double.parseDouble(academy.getPriceII()) * num
								- discount;
					} else {
						totalprice = Double.parseDouble(academy.getPriceIII())
								* num - discount;
					}
				}

			}

			// 保存OrderInfo信息
			OrderInfo order = new OrderInfo();
			order.setOrderNum(orderNum);
			order.setUserGid(userGid);
			order.setType(type);
			order.setAcademyGid(academyGid);
			order.setContactGids(contactGids);
			order.setCreateTime(now);
			order.setStatus(1);
			order.setGid(gid);
			if (!StringUtils.isNotEmpty(couponGid))
				couponGid = "";
			order.setCouponId(couponGid);
			if (!StringUtils.isNotEmpty(invoiceGid))
				invoiceGid = "";
			order.setInvoiceId(invoiceGid);
			order.setTotalprice(df.format(totalprice));
			order.setRemark(remark);
			order.setHotline("13699187603");
			order.setOrderType(orderType);
			orderinfoDao.save(order);
			// 组织返回信息
			// 发票返回信息
			JSONObject resInvoice = new JSONObject();
			if (!invoiceGid.isEmpty()) {
				List<Invoice> invoiceList = invoiceService
						.getInvoiceByGid(invoiceGid);
				Invoice invoice = invoiceList.get(0);
				String type2 = invoice.getType();
				String invoiceType = "";
				if ("1".equals(type2)) {
					invoiceType = "会务费";
				} else {
					invoiceType = "会议费";
				}
				resInvoice.put("name", invoice.getName());
				resInvoice.put("content", invoiceType);
				resInvoice.put("detail", invoice.getDetail());
				resMap.put("invoice", resInvoice);
			} else {
				resInvoice.put("name", "");
				resInvoice.put("detail", "");
				resMap.put("invoice", resInvoice);
			}
			
			JSONObject resAcademy = new JSONObject();
			resAcademy.put("price", academy.getPrice());
			resAcademy.put("priceRes", academy.getPrice());
			resAcademy.put("priceI", academy.getPriceI());
			if ("2".equals(orderType)) {
				resAcademy.put("price", academy.getPriceII());
				resAcademy.put("priceRes", academy.getPriceII());
				if (num >= 3)
					resAcademy.put("priceRes", df.format(Double.parseDouble(academy
							.getPriceII()) * 0.9));
			} else if ("3".equals(orderType)) {
				resAcademy.put("price", academy.getPriceIII());
				resAcademy.put("priceRes", academy.getPriceIII());
				if (num >= 3)
					resAcademy.put("priceRes", df.format(Double.parseDouble(academy
							.getPriceIII()) * 0.9));
			} else {
				if (num >= 3)
					resAcademy.put("priceRes", academy.getPriceI());
			}
			resAcademy.put("preprice", academy.getPreprice());
			resAcademy.put("begindate", sdf.format(academy.getBeginDate()));
			resAcademy.put("enddate", sdf.format(academy.getEndDate()));
			resAcademy.put("local", academy.getLocal());
			resAcademy.put("title", academy.getTitle());
			resAcademy.put("pic", academy.getPic());

			JSONArray ret = new JSONArray();
			for (String str : strArray) {
				JSONObject res = new JSONObject();
				List<AcademyUser> aclist = academyUserService.getByGid(str);
				AcademyUser academyUser = aclist.get(0);
				res.put("name", academyUser.getName());
				res.put("gid", academyUser.getGid());
				res.put("telephoneNum", academyUser.getTelephoneNum());
				ret.add(res);
			}

			String saleMessage = "";
			if ("1".equals(orderType)) {
				saleMessage = "3人拼团：3人以上限时优惠" + academy.getPriceI() + "元";
			} else {
				saleMessage = "折扣规则：3人以上9折优惠";
			}
			resMap.put("orderType", orderType);
			resMap.put("saleMessage", saleMessage);

			if (d == 0) {
				resMap.put("discountNum", "0.0");
			} else {

				resMap.put("discountNum", df.format(d));
			}

			resMap.put("orderGid", gid);
			resMap.put("academy", resAcademy);
			resMap.put("totalPrice", df.format(totalprice));
			resMap.put("orderNum", orderNum);
			resMap.put("ticketNum", String.valueOf(strArray.length));
			resMap.put("totleNum", totleNum);
			resMap.put("contents", ret);
			resMap.put("markInfor", remark == null ? "" : remark);
			resMap.put("serviceTel", order.getHotline());
			resMap.put("status", order.getStatus());
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		return resMap;
	}

	@Override
	public int updateOrderStatus(String gid, int status) {

		int i = orderinfoDao.updateOrderStatus(gid, status);


		
		
		
		return i;
	}

	@Override
	public List<OrderInfo> getOrderDetailByGid(String gid) {
		String sqlString = "select  * from  orderInfo  where  gid='" + gid
				+ "'";
		return orderinfoDao.getVoListBySql(sqlString);
	}

	@Override
	public void deleteCouIvoice(String gid) throws CustomException {
		List<OrderInfo> orderDetailByGid = this.getOrderDetailByGid(gid);
		OrderInfo orderInfo = orderDetailByGid.get(0);
		if (orderInfo == null) {
			throw new CustomException("订单不存在");
		}
		Integer type = orderInfo.getType();
         if (type==3) {
        	 String contactGids = orderInfo.getContactGids();
             int i=contactGids.split(",").length;
             academyService.deleteVip(orderInfo.getAcademyGid(), -i);
		}		
		
		Coupon coupon = orderInfo.getCoupon();
		Invoice invoice = orderInfo.getInvoice();
		if (coupon != null && "0".equals(coupon.getInuse())) {
			coupon.setInuse("-1");
			couponService.update(coupon);
		}
		if (invoice != null) {

			invoiceService.delete(invoice);
		}
	}

	@Override
	public void createRewardOrder(String userId) {
		Date now = new Date();
		OrderInfo order = new OrderInfo();
		order.setUserGid(userId);
		order.setType(1);
		order.setCreateTime(now);
		order.setStatus(1);
		order.setGid(ComUtil.getuuid());
		orderinfoDao.save(order);

	}

	@Override
	public int getOrderNum(String userId) {
		String string = "select count(*) from orderInfo where userGid='"
				+ userId + "'and status <> 0";
		return orderinfoDao.totleNum(string);
	}

	@Override
	public List<OrderInfo> getPrice(String orderNo, String userId) {
		String string = "select * from orderInfo where userGid='" + userId
				+ "' and orderNum='" + orderNo + "'";
		return orderinfoDao.getVoListBySql(string);
	}

	@Override
	public void changeVipNum(String trade_no) {
		List<OrderInfo> orderDetailByGid = getOrderDetailByGid(trade_no);
		OrderInfo orderInfo = orderDetailByGid.get(0);
		String academyGid = orderInfo.getAcademyGid();
		Integer i = orderInfo.getContactGids().split(",").length;
		academyService.deleteVip(academyGid, i);
	}

	@Override
	public void deleteByGid(String gid) {
		String string = "DELETE from orderInfo where gid='"+gid+"'";
		orderinfoDao.executeUpdateSql(string);
	}
	@Override
	public List<OrderInfo> getOrderCourseOrNum(String userId, int i,
			String string, String gid) {
		// TODO Auto-generated method stub
		return orderinfoDao.getOrderCourseOrNum(userId, i, string , gid);
	}
	@Override
	public List<OrderInfo> getByOrderNu(String orderNum) {
	String sqlString="SELECT * FROM orderInfo WHERE orderNum='"+orderNum+"'";
		return orderinfoDao.getVoListBySql(sqlString);
	}
	@Override
	public List<OrderInfo> getOrders(String gid, String academyGid) {
		String stringUtils="SELECT * FROM orderInfo WHERE  `status`=2 AND userGid='"+gid+"'  AND academyGid='"+academyGid+"'";
		return orderinfoDao.getVoListBySql(stringUtils);
	}
	@Override
	public List<OrderInfo> getByUserIdAcademyId(String userId, String gid) {
		return orderinfoDao.getOrderCourse(userId, 6, "1", gid);
	}

}
