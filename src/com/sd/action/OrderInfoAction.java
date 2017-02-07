package com.sd.action;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.sd.service.AcademyService;
import com.sd.service.AcademyUserService;
import com.sd.service.CouponService;
import com.sd.service.UserService;
import com.sd.util.ComUtil;
import com.sd.util.CustomException;
import com.sd.vo.Academy;
import com.sd.vo.AcademyUser;
import com.sd.vo.AcademyVip;
import com.sd.vo.Coupon;
import com.sd.vo.Course;
import com.sd.vo.CourseExpert;
import com.sd.vo.CourseVideo;
import com.sd.vo.Invoice;
import com.sd.vo.OrderInfo;
import com.wxpay.util.TenpayUtil;

public class OrderInfoAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	protected static final String NO_ACADEMY = "票据信息不完整";

	private Logger logger = Logger.getLogger(this.getClass());
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Resource
	private AcademyService academyService;
	@Resource
	private AcademyUserService academyUserService;
	@Resource
	private CouponService couponService;
	@Resource
	protected UserService userService;
	private String orderType;// 类型 1.打赏 2.购票
	private String gid;// 订单gid
	private Integer type;// 类型 1.打赏 2.购票
	private String type1;// 类型 1.打赏 2.购票
	private  String courseGid;

	public String getType1() {
		return type1;
	}

	public void setType1(String type1) {
		this.type1 = type1;
	}

	public String getCourseGid() {
		return courseGid;
	}

	public void setCourseGid(String courseGid) {
		this.courseGid = courseGid;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	private String academyGid;// 票据Gid
	private String contactGids;// 购票人Gid
	private String couponGid;// 优惠券的gid
	private String invoiceGid;// 发票的gid
	private String remark;// 备注

	public String getInvoiceGid() {
		return invoiceGid;
	}

	public void setInvoiceGid(String invoiceGid) {
		this.invoiceGid = invoiceGid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCouponGid() {
		return couponGid;
	}

	public void setCouponGid(String couponGid) {
		this.couponGid = couponGid;
	}
	/**
	 * 查询用户订单列表
	 * 
	 * @param userId
	 * @param token
	 * @param type
	 */
	public void queryOrderInfo() throws Exception {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
		String userId = request.getHeader("userId");
		boolean logined = userTokenService.checkToken(ServletActionContext
				.getRequest());
		if (!logined) { throw new CustomException("沒有登录"); }
		 
		if (StringUtils.isBlank(userId)) {
			throw new CustomException("userId为空");
		}
		if (type == 0) {
			throw new CustomException("type错误");
		}
//		String userId="b5b95490-5cd7-48c7-bbf1-54cf385314b4";
//		int  type=2;
	
			JSONArray reslist = new JSONArray();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			List<OrderInfo> list= orderInfoService.getOrdersByUserGidandType(userId,type);
			if(list.size()!=0){
				JSONArray conlist = new JSONArray();
				for (OrderInfo orderInfo : list) {
					String invoiceGid = orderInfo.getInvoiceId();
					String couponGid = orderInfo.getCouponId();
					String orderType2 = orderInfo.getOrderType();
					JSONObject res = new JSONObject();
					String conGids = orderInfo.getContactGids();
					String[] strArray = conGids.split(",");
					if(type!=4){
						Academy academy = academyService.get(orderInfo.getAcademyGid());
						res.put("preprice", academy.getPreprice());
						res.put("begindate", sdf.format(academy.getBeginDate()));
						res.put("enddate", sdf.format(academy.getEndDate()));
						res.put("price", academy.getPrice());
						res.put("local", academy.getLocal());
						res.put("title", academy.getTitle());
						res.put("picGid", academy.getPic());
						for (String str : strArray) {
							JSONObject ress = new JSONObject();
							List<AcademyUser> aclist = academyUserService.getByGid(str);
							if (aclist.size()==0) {
								throw new CustomException("gid为"+str+"的用户为空！！");
							}
							AcademyUser academyUser = aclist.get(0);
							ress.put("name", academyUser.getName());
							ress.put("gid", academyUser.getGid());
							ress.put("telephoneNum", academyUser.getTelephoneNum());
							conlist.add(ress);
						}
						res.put("contacts", conlist);
						
					}else{
						Course course = courseService.get(orderInfo.getAcademyGid());
						if(course==null)
							throw new CustomException("慕课为空！！");
						String expertGid = course.getExpertGid();
						if(expertGid==null)
							throw new CustomException("慕课的专家为空！！");
						String[] gids = expertGid.split(",");
						JSONArray jsonArrayEx = new JSONArray();
						
						for (String string : gids) {
							JSONObject re = new JSONObject();
							CourseExpert courseExpert = courseExpertService.get(string);
							
							if(courseExpert==null){
								re.put("expertName","骨今小助手");
							}else{
								re.put("expertName",courseExpert.getName() );
							}
							jsonArrayEx.add(re);
						}
						
						
						res.put("orderType", orderType2);
						res.put("expertNames",jsonArrayEx);
						res.put("courseTitle",course.getTitle());
						res.put("courseGid",course.getGid());
						if ("2".equals(orderType2)) {
							String string = strArray[0];
							CourseVideo courseVideo = courseVideoService.get(string);
							res.put("videoTitle",courseVideo.getTitle());
							res.put("videoGid",courseVideo.getGid());
							res.put("videoAvatar",courseVideo.getAvatar());
						}else{
							res.put("videoTitle","");
							res.put("videoGid","");
							res.put("videoAvatar",course.getAvatar());
						}
						
						
						res.put("preprice","");
						res.put("begindate", "");
						res.put("enddate", "");
						res.put("price", "");
						res.put("local", "");
						res.put("picGid","");
					}
					
					res.put("invoiceGid", invoiceGid);
					res.put("couponGid", couponGid);
					res.put("gid", orderInfo.getGid());
					res.put("id", orderInfo.getId());
					res.put("serviceTel", orderInfo.getHotline());
					res.put("orderNum", orderInfo.getOrderNum());
					res.put("status", orderInfo.getStatus());
					res.put("type", orderInfo.getType());
					res.put("userGid", orderInfo.getUserGid());
					res.put("totalPrice", orderInfo.getTotalprice());
					res.put("createTime", sdf.format(orderInfo.getCreateTime()));
					reslist.add(res);
				}
			
				
				
			}
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", reslist);
		} catch (CustomException e) {
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONObject());
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}

	/**
	 * 
	 * 创建订单
	 * 
	 * @param userId
	 * @param token
	 * @param type
	 * @param academyGid
	 * @param contactGids
	 * @param couponGid
	 */
	public void createOrder() {
		JSONObject ret = new JSONObject();
		try {
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = request.getHeader("userId");
		boolean logined = userTokenService.checkToken(request);
		if (!logined) {
			throw new CustomException("沒有登录");
		}
		if (!"1".equals(orderType) && !"2".equals(orderType)
				&& !"3".equals(orderType)) {
			throw new CustomException("orderType传值错误！！！");
		}
		
			String out_trade_no = generateOrderNo();
			String OrderInfoGid = ComUtil.getuuid();
			if (type == 2||type == 3) {
				if (!StringUtils.isNotEmpty(contactGids)
						&& !StringUtils.isNotEmpty(academyGid)) {
					throw new CustomException(NO_ACADEMY);
				} else {
					Map<String, Object> res = orderInfoService.createOrderInfo(
							remark, out_trade_no, userId, type, academyGid,
							contactGids, OrderInfoGid, couponGid, invoiceGid,
							orderType);
					ret.put("code", "1");
					ret.put("message", SUCCESS_INFO);
					ret.put("result_data", res);
				}
			} else if (type == 1) {
				Map<String, Object> res = orderInfoService.createOrderInfo(
						remark, out_trade_no, userId, type, academyGid,
						contactGids, OrderInfoGid, couponGid, invoiceGid,
						orderType);
				ret.put("code", "1");
				ret.put("message", SUCCESS_INFO);
				ret.put("result_data", res);
			}else if (type == 4){
				Map<String, Object> res = orderInfoService.createOrderInfo(
						remark, out_trade_no, userId, type, academyGid,
						contactGids, OrderInfoGid, couponGid, invoiceGid,
						orderType);
				ret.put("code", "1");
				ret.put("message", SUCCESS_INFO);
				ret.put("result_data", res);
				
			}else if(type == 5){
				Map<String, Object> res = orderInfoService.createOrderInfo(
						remark, out_trade_no, userId, type, academyGid,
						contactGids, OrderInfoGid, couponGid, invoiceGid,
						orderType);
				ret.put("code", "1");
				ret.put("message", SUCCESS_INFO);
				ret.put("result_data", res);
				
			}else if (type == 6) {
				Map<String, Object> res = orderInfoService.createOrderInfo(
						remark, out_trade_no, userId, type, academyGid,
						contactGids, OrderInfoGid, couponGid, invoiceGid,"1"
						);
				ret.put("code", "1");
				ret.put("message", SUCCESS_INFO);
				ret.put("result_data", res);
			}
		}catch(CustomException e){
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONObject());
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}

	/**
	 * 订单详情
	 * 
	 * @param userId
	 * @param token
	 * @param gid
	 */
	public void orderDetail()  {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = request.getHeader("userId");
		JSONObject rets = new JSONObject();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		
		
		try {
		boolean logined = userTokenService.checkToken(ServletActionContext
				.getRequest());
		
		  if (!logined) { throw new CustomException("沒有登录"); }
		 
		if (StringUtils.isBlank(userId)) {
			throw new CustomException("userId为空");
		}
		
		

		
		
	
		
		if("4".equals(type1))
		{
			
			JSONObject ret = new JSONObject();
			List<OrderInfo> list = orderInfoService.getOrderDetailByGid(gid);
			if(list.size()==0)
				throw new CustomException("订单不存在");
			OrderInfo orderinfo = list.get(0);
			String orderType2 = orderinfo.getOrderType();
			String academyGid2 = orderinfo.getAcademyGid();
			ret.put("orderType", orderType2);
			JSONArray jsonArray = new JSONArray();
			if ("1".equals(orderType2)) {
				JSONObject resCVideo = new JSONObject();
				resCVideo.put("title",  courseService.get(academyGid2).getTitle());
				jsonArray.add(resCVideo);
			} else {
				
				String contactGids2 = orderinfo.getContactGids();
				
				if(!StringUtils.isNotEmpty(contactGids2))
					throw new CustomException("ContactGids不能为空");
				String[] split = contactGids2.split(",");
				for(String c : split){
					CourseVideo courseVideo = courseVideoService.get(c);
					if(courseVideo==null)
						throw new CustomException("该视频已经不存在！！");
					JSONObject resVideo = new JSONObject();
					resVideo.put("title", courseVideo.getTitle());
					jsonArray.add(resVideo);
					 
				}
				
				

			}
			ret.put("title",jsonArray );
			
			
			ret.put("type",orderinfo.getType());
			ret.put("status", orderinfo.getStatus());
			ret.put("orderNum", orderinfo.getOrderNum());
			ret.put("totalprice", orderinfo.getTotalprice());
			JSONObject resInvoice = new JSONObject();
			if (StringUtils.isNotEmpty(orderinfo.getInvoiceId())) {
				List<Invoice> invoiceList = invoiceService.getInvoiceByGid(orderinfo.getInvoiceId());
				Invoice invoice = invoiceList.get(0);
				String type2 = invoice.getType();
				String invoiceType="";
				if ("1".equals(type2)) {
					invoiceType="会务费";
				} else {
					invoiceType="会议费";
				}
				resInvoice.put("name", invoice.getName());
				resInvoice.put("content",invoiceType );
				resInvoice.put("name", invoice.getName());
				resInvoice.put("detail", invoice.getDetail());
				
	        }else{
	        	resInvoice.put("name", "");
				resInvoice.put("detail", "");
	        }
			ret.put("invoice", resInvoice);
			
			
			
			JSONObject resCoupon = new JSONObject();
			if (StringUtils.isNotEmpty(orderinfo.getCouponId())) {
				List<Coupon> couponList = couponService.getByGid(orderinfo
						.getCouponId());
				Coupon coupon = couponList.get(0);
				resCoupon.put("couponGid", coupon.getGid());
				resCoupon.put("discount", coupon.getDiscount());
			} else {
				resCoupon.put("couponGid", "");
				resCoupon.put("discount", "");
			}
			ret.put("resCoupon", resCoupon);
			rets.put("code", "1");
			rets.put("message", SUCCESS);
			rets.put("result_data", ret);
		}else{
			
			JSONObject ret = new JSONObject();
			JSONArray reslist = new JSONArray();
			List<OrderInfo> list = orderInfoService.getOrderDetailByGid(gid);
			OrderInfo orderinfo = list.get(0);
			Academy academy = academyService.get(orderinfo.getAcademyGid());
			String conGids = orderinfo.getContactGids();
			String[] strArray = conGids.split(",");
			// 计算优惠总额
			int num = strArray.length;
			int discount = 0;
			if (StringUtils.isNotEmpty(orderinfo.getCouponId())) {
				List<Coupon> couponList = couponService.getByGid(orderinfo
						.getCouponId());
				Coupon coupon = couponList.get(0);
				discount = Integer.parseInt(coupon.getDiscount());
				ret.put("couponGid", coupon.getGid());
				ret.put("discount", coupon.getDiscount());
			} else {
				ret.put("couponGid", "");
				ret.put("discount", "");
			}
			JSONObject resInvoice = new JSONObject();
			if (StringUtils.isNotEmpty(orderinfo.getInvoiceId())) {
				List<Invoice> invoiceList = invoiceService.getInvoiceByGid(orderinfo.getInvoiceId());
				Invoice invoice = invoiceList.get(0);
				String type2 = invoice.getType();
				String invoiceType="";
				if ("1".equals(type2)) {
					invoiceType="会务费";
				} else {
					invoiceType="会议费";
				}
				resInvoice.put("name", invoice.getName());
				resInvoice.put("content",invoiceType );
				resInvoice.put("detail", invoice.getDetail());
				ret.put("invoice", resInvoice);
	        }else{
	        	resInvoice.put("name", "");
				resInvoice.put("detail", "");
				ret.put("invoice", resInvoice);
	        }
	        Integer totleNum = academy.getTotleNum()+strArray.length;
	        rets.put("code", "1");
	        rets.put("message", SUCCESS_INFO);
	        ret.put("ticketNum", String.valueOf(strArray.length));
	        ret.put("orderNum", orderinfo.getOrderNum());
	        ret.put("totleNum", totleNum);
	        ret.put("createTime", sdf.format(orderinfo.getCreateTime()));
	        ret.put("status", orderinfo.getStatus());
	        ret.put("realMoney",orderinfo.getTotalprice());
	        ret.put("type",orderinfo.getType());
	        ret.put("markInfor",orderinfo.getRemark());
	        ret.put("serviceTel",orderinfo.getHotline());
	        ret.put("totalprice",orderinfo.getTotalprice());
	        ret.put("orderType",orderinfo.getOrderType());
	        if (orderinfo.getType()==3) {
	        	 AcademyVip vipInfo = academyService.getVipInfo(academy.getGid());
	        	 ret.put("vipPrice",vipInfo.getPrice());
	        	 
			}else{
				 ret.put("vipPrice","");
			}
	        String orderType3 = orderinfo.getOrderType();
	        String saleMessage="";
	        if ("1".equals(orderType3)) {
				saleMessage="3人拼团：3人以上限时优惠"+academy.getPriceI()+"元";
			} else {
				saleMessage="折扣规则：3人以上9折优惠";
			}
	        ret.put("saleMessage",saleMessage);
	        JSONObject resAcademy = new JSONObject();
	        resAcademy.put("price", academy.getPrice());
	        resAcademy.put("priceRes", academy.getPrice());
			resAcademy.put("priceI", academy.getPriceI());
	        resAcademy.put("preprice", academy.getPreprice());
	        resAcademy.put("begindate",sdf.format(academy.getBeginDate()));
	        resAcademy.put("enddate",sdf.format(academy.getEndDate()));
	        resAcademy.put("local",academy.getLocal());
	        resAcademy.put("title",academy.getTitle());
	        resAcademy.put("pic",academy.getPic());
	        for(String str:strArray){
				JSONObject res = new JSONObject();
				List<AcademyUser> aclist = academyUserService.getByGid(str);
				AcademyUser academyUser = aclist.get(0);
				res.put("name", academyUser.getName());
				res.put("gid", academyUser.getGid());
				res.put("telephoneNum", academyUser.getTelephoneNum());
				reslist.add(res);
			}
	        
	        
	        DecimalFormat df = new DecimalFormat("#0.00"); 
	        Double d=(double) 0;
	        if ("2".equals(orderType3)) {
				resAcademy.put("priceRes", academy.getPriceII());
				resAcademy.put("price", academy.getPriceII());
			} else if("3".equals(orderType3)) {
				resAcademy.put("priceRes", academy.getPriceIII());
				resAcademy.put("price", academy.getPriceIII());
			}
					
			
	        if (num>=3) {
				//计算则扣
				if ("2".equals(orderType3)) {
					resAcademy.put("priceRes", df.format(Double.parseDouble(academy.getPriceII())*0.9));
					d=Double.parseDouble(academy.getPriceII())*0.1*num;
				} else if("3".equals(orderType3)) {
					resAcademy.put("priceRes", df.format(Double.parseDouble(academy.getPriceIII())*0.9));
					 d=Double.parseDouble(academy.getPriceIII())*0.1*num;
				}else{
					resAcademy.put("priceRes", academy.getPriceI());
				}
				
			}
	        
	        if(d==0){
	        	ret.put("discountNum", "0.0");
	        }else{
	        	
	        	ret.put("discountNum", df.format(d));
	        }
	        ret.put("contacts", reslist);
	        ret.put("academy", resAcademy);
			rets.put("code", "1");
			rets.put("message", SUCCESS);
			rets.put("result_data", ret);
			
		}
		} catch (Exception e) {
			logger.error("", e);
			rets.put("code", "0");
			rets.put("message", ERROR_INFO);
			rets.put("result_data", new JSONObject());
		}
		putDataOut(rets.toString());
	}

	/**
	 * 更新支付状态 取消订单
	 * 
	 * @param userId
	 * @param token
	 * @param gid
	 */
	public void OrderCancel() {

		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userId");
			boolean logined = userTokenService.checkToken(ServletActionContext
					.getRequest());
			if (!logined) {
				throw new CustomException("沒有登录");
			}

			orderInfoService.deleteCouIvoice(gid);
			orderInfoService.deleteByGid(gid);


				ret.put("code", "1");
				ret.put("message", SUCCESS_INFO);
				ret.put("result_data", new JSONObject());

		} catch (CustomException e) {
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONObject());
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());


    	}

	/**
	 * 生成用于支付的订单号
	 */
	private String generateOrderNo() {
		// 当前时间 yyyyMMddHHmmss
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		// 10位序列号,可以自行调整。
		String strReq = strTime + strRandom;
		// 订单号，此处用时间加随机数生成，商户根据自己情况调整，只要保持全局唯一就行

		return strReq;
	}

	public String getGid() {
		return gid;
	}

	public String getAcademyGid() {
		return academyGid;
	}

	public void setAcademyGid(String academyGid) {
		this.academyGid = academyGid;
	}

	public String getContactGids() {
		return contactGids;
	}

	public void setContactGids(String contactGids) {
		this.contactGids = contactGids;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	
}
