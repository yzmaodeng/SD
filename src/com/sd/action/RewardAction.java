package com.sd.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipayCore;
import com.alipay.util.AlipayNotify;
import com.sd.service.AcademyService;
import com.sd.service.CouponService;
import com.sd.service.OrderInfoService;
import com.sd.util.CustomException;
import com.sd.util.SmsUtil;
import com.sd.vo.OrderInfo;
import com.sd.vo.User;
import com.wxpay.config.WXPayConfig;
import com.wxpay.util.CommonUtil;
import com.wxpay.util.PayCommonUtil;
import com.wxpay.util.TenpayUtil;
import com.wxpay.util.XMLUtil;

public class RewardAction extends BaseAction {

	private Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private OrderInfoService orderInfoService;
	@Resource
	private CouponService couponService;
	@Resource
	private AcademyService academyService;
	private String price; // 打赏金额
	private String expertGid; // 专家GID
	private String gid;
	private String orderNo;
	private String type;// "1"打赏“2”购票

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	/**
	 * 支付宝订单信息
	 * 
	 * @return
	 */
	public void getAliOrderInfo() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userId");
			boolean logined = userTokenService.checkToken(ServletActionContext
					.getRequest());
			if (!logined) {
				throw new CustomException("沒有登錄");
			}
			if (StringUtils.isBlank(price))
				throw new CustomException("请填写金额");

			String out_trade_no = generateOrderNo();
			String totalprice = price;
			if ("1".equals(type)) {
				orderInfoService.createRewardOrder(userId);
			} else {
				out_trade_no = orderNo;
				if (!StringUtils.isNotEmpty(orderNo)) {
					throw new CustomException("当type类型为2的时候orderNo传值错误！！！");
				}
				List<OrderInfo> list = orderInfoService.getPrice(orderNo,
						userId);
				if (list.size() == 0) {
					throw new CustomException(orderNo + "订单不存在");
				}
				OrderInfo orderInfo = list.get(0);
				totalprice = orderInfo.getTotalprice();
			}

			String orderInfo = AlipayCore.getOrderInfo("骨今中外", "订单号："
					+ out_trade_no, totalprice, out_trade_no);
			String sign = AlipayCore.sign(orderInfo, AlipayConfig.private_key);
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");

			String orderString = orderInfo + "&sign=\"" + sign + "\"&"
					+ AlipayCore.getSignType();

			Map<String, String> infoMap = new HashMap<String, String>();
			infoMap.put("orderInfo", orderString);
			infoMap.put("orderNo", out_trade_no);

			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", infoMap);
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

	// 网页调取支付宝
	public static String APP_ID = "2016041801309223";
	public static String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
	public static String APP_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL+dugJ3GFXyLu59Z/Z70lMZn4j/7XTqbq/vBn+/Kqx6DKSp+UBaeHOfoQxD70IVP0XyiRcSmNA5I9iOnZswMfE8kXb3fRXqVcb3FgGrJc5NTS95KJ/w373CkNlDWRnOrrVz1LRT7hzYiqMf2QT3LZ8dqYc4VDg+qYpl75CJIm+zAgMBAAECgYActimVDPtchXl6yte1G0Ccqw44nQCbsDT9r7ctlRtz1KXoVn++oM/Do3uiNtu27zAX7wuTpXm4WZeBb302L7aLyCVIX/RXnIPQzhscH201u/BUGnjhu1JlPa8Udz2dDh5LTrE8dY6gqQgXZpDlV1tQrERS2rhdohYK6A6LlP8VIQJBAP3gRzmUrBxJCziK9IivdqUpMxAv0F+/t9ZDBhWOf2jWz4UyiFoCfKwaMnlOZqo/I3FgOBiSxQAfcWzQF4TqOpUCQQDBOBt61s1DRaJa20lXMRJ9wBZoAftboK+vFcWqqToNiac8aPIiTKZVtptwaftpzJehT7FgvZUst26iU57hh7cnAkBPaJ5+qT0oX8SNvBD+y/tNb9SUBJCl0l7bOv2lMnwxu7cPT54MoWiDoHIXNWmxaKxaYyFItme+QReGVJR2s5j9AkAs+nqrJcWym0soC1QPUAUV8NlGbO+ubMF46ICTMcGp1RlxHpz/DwjJezDEAmfcQRwrGPoZowhO2ISQlRavOYCRAkEAxd6yDBJjnpE3UCGXoh/e3nQmnpQ4homaVHqhr+3T3DePebIB7KzcWrVLpBUZn5DHE6vxAfyyrssRFZpJCrFZbA==";

	public void getAliPayInfo() throws IOException, AlipayApiException {

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		AlipayClient alipayClient = new DefaultAlipayClient(
				"https://openapi.alipay.com/gateway.do", APP_ID,
				APP_PRIVATE_KEY, "json", "UTF-8", ALIPAY_PUBLIC_KEY);

		AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();// 创建API对应的request

		alipayRequest.setReturnUrl("http://domain.com/CallBack/return_url.jsp");
		alipayRequest.setNotifyUrl("http://domain.com/CallBack/notify_url.jsp");// 在公共参数中设置回跳和通知地址
		alipayRequest.setBizContent("{"
				+ "    \"out_trade_no\":\"20150320010101002\","
				+ "    \"total_amount\":88.88,"
				+ "    \"subject\":\"Iphone6 16G\","
				+ "    \"seller_id\":\"2088123456789012\","
				+ "    \"product_code\":\"QUICK_WAP_PAY\"" + "  }");// 填充业务参数
		String form = alipayClient.pageExecute(alipayRequest).getBody(); // 调用SDK生成表单
		response.setContentType("text/html;charset=" + AlipayConstants.CHARSET);
		response.getWriter().write(form);// 直接将完整的表单html输出到页面
		response.getWriter().flush();

	}

	// 网页调取支付宝测试方法
	public void getAlipayInfos() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userId");
			boolean logined = userTokenService.checkToken(ServletActionContext
					.getRequest());
			if (!logined) {
				throw new CustomException("沒有登錄");
			}
			if (StringUtils.isBlank(price))
				throw new CustomException("请填写金额");

			String out_trade_no = generateOrderNo();
			String totalprice = price;
			if ("1".equals(type)) {
				orderInfoService.createRewardOrder(userId);
			} else {
				out_trade_no = orderNo;
				if (!StringUtils.isNotEmpty(orderNo)) {
					throw new CustomException("当type类型为2的时候orderNo传值错误！！！");
				}
				List<OrderInfo> list = orderInfoService.getPrice(orderNo,
						userId);
				if (list.size() == 0) {
					throw new CustomException(orderNo + "订单不存在");
				}
				OrderInfo orderInfo = list.get(0);
				totalprice = orderInfo.getTotalprice();
			}

			String orderInfo = AlipayCore.getOrderInfo("骨今中外", "订单号："
					+ out_trade_no, totalprice, out_trade_no);
			String sign = AlipayCore.sign(orderInfo, AlipayConfig.private_key);
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");

			String orderString = orderInfo + "&sign=\"" + sign + "\"&"
					+ AlipayCore.getSignType();

			Map<String, String> infoMap = new HashMap<String, String>();
			infoMap.put("orderInfo", orderString);
			infoMap.put("orderNo", out_trade_no);

			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", infoMap);
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
	 * 微信支付订单信息
	 * 
	 * @return
	 */
	public void getWXPayReq() {
		JSONObject resInfo = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userId");
			boolean logined = userTokenService.checkToken(ServletActionContext
					.getRequest());
			if (!logined) {
				throw new CustomException("沒有登錄");
			}

			if (StringUtils.isBlank(price))
				throw new CustomException("请填写金额");
			String out_trade_no = generateOrderNo();
			String totalprice = price;
			if ("1".equals(type)) {
				orderInfoService.createRewardOrder(userId);
			} else {
				if (!StringUtils.isNotEmpty(orderNo)) {
					throw new CustomException("当type类型为2的时候orderNo传值错误！！！");
				}
				out_trade_no = orderNo;
				List<OrderInfo> list = orderInfoService.getPrice(orderNo,
						userId);
				if (list.size() == 0) {
					throw new CustomException(orderNo + "订单不存在");
				}
				OrderInfo orderInfo = list.get(0);
				totalprice = orderInfo.getTotalprice();
				double parseDouble = Double.parseDouble(totalprice);
				int i = (int) (parseDouble * 100);
				totalprice = i + "";
				
			}

			String noncestr = PayCommonUtil.CreateNoncestr();
			String timestamp = String
					.valueOf(System.currentTimeMillis() / 1000);

			String retmsg = "";

			SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
			parameters.put("appid", WXPayConfig.APPID);
			parameters.put("mch_id", WXPayConfig.MCH_ID);
			parameters.put("nonce_str", noncestr);
			parameters.put("body", "骨今中外");
			parameters.put("out_trade_no", out_trade_no);
			parameters.put("total_fee", totalprice);
			parameters.put("spbill_create_ip", InetAddress.getLocalHost()
					.getHostAddress());
			parameters.put("notify_url", WXPayConfig.NOTIFY_URL);
			parameters.put("trade_type", "APP");
			String sign = PayCommonUtil.createSign("UTF-8", parameters);
			parameters.put("sign", sign);
			String requestXML = PayCommonUtil.getRequestXml(parameters);
			// 以POST方式调用微信统一支付接口 取得预支付id
			String result = CommonUtil.httpsRequest(
					WXPayConfig.UNIFIED_ORDER_URL, "POST", requestXML);
			// 解析微信返回的信息，以Map形式存储便于取值
			Map<String, String> map = XMLUtil.doXMLParse(result);

			// 获取prepayId
			String prepayid = map.get("prepay_id");
			logger.info("获取prepayid------值 " + prepayid);

			// 吐回给客户端的参数
			if (null != prepayid && !"".equals(prepayid)) {
				SortedMap<Object, Object> params = new TreeMap<Object, Object>();
				params.put("appid", WXPayConfig.APPID);
				params.put("partnerid", WXPayConfig.MCH_ID);
				params.put("prepayid", prepayid);
				params.put("timestamp", timestamp);
				params.put("noncestr", noncestr);
				params.put("package", "Sign=WXPay");
				params.put("sign", PayCommonUtil.createSign("UTF-8", params));

				retmsg = "成功";
				resInfo.put("code", 1);
				resInfo.put("message", retmsg);
				resInfo.put("result_data", JSONObject.fromObject(params));
			} else {
				retmsg = "错误：获取prepayId失败";
				resInfo.put("code", 0);
				resInfo.put("message", retmsg);
			}
		} catch (CustomException e) {
			resInfo.put("code", 0);
			resInfo.put("message", e.getMessage());
		} catch (Exception e) {
			logger.error("", e);
			resInfo.put("code", 0);
			resInfo.put("message", e.getMessage());
		}
		putDataOut(resInfo.toString());
	}
	
	/**
	 * 微信支付订单信息
	 * 
	 * @return
	 */
	public void getWXJsPayReq() {
		JSONObject resInfo = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userId");
			boolean logined = userTokenService.checkToken(ServletActionContext
					.getRequest());
			if (!logined) {
				throw new CustomException("沒有登錄");
			}

			if (StringUtils.isBlank(price))
				throw new CustomException("请填写金额");
			String out_trade_no = generateOrderNo();
			String totalprice = price;
			if ("2".equals(type)) {
				if (!StringUtils.isNotEmpty(orderNo)) {
					throw new CustomException("当type类型为2的时候orderNo传值错误！！！");
				}
				out_trade_no = orderNo;
				List<OrderInfo> list = orderInfoService.getPrice(orderNo,
						userId);
				if (list.size() == 0) {
					throw new CustomException(orderNo + "订单不存在");
				}
				
				OrderInfo orderInfo = list.get(0);
				totalprice = orderInfo.getTotalprice();
				double parseDouble = Double.parseDouble(totalprice);
				int i = (int) (parseDouble * 100);
				totalprice = i + "";
				
			}

			String noncestr = PayCommonUtil.CreateNoncestr();
			String timestamp = String
					.valueOf(System.currentTimeMillis() / 1000);

			String retmsg = "";
			String openid = request.getParameter("openid");
			SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
			parameters.put("appid", WXPayConfig.APPJSID);
			parameters.put("mch_id", WXPayConfig.JSMCH_ID);
			parameters.put("nonce_str", noncestr);
			parameters.put("body", "骨今中外");
			parameters.put("out_trade_no", out_trade_no);
			parameters.put("total_fee", totalprice);
			parameters.put("spbill_create_ip", InetAddress.getLocalHost()
					.getHostAddress());
			parameters.put("notify_url", WXPayConfig.JSNOTIFY_URL);
			parameters.put("trade_type", "JSAPI");
			parameters.put("openid", openid);
			String sign = PayCommonUtil.createJSSign("UTF-8", parameters);
			parameters.put("sign", sign);
			String requestXML = PayCommonUtil.getRequestXml(parameters);
			// 以POST方式调用微信统一支付接口 取得预支付id
			String result = CommonUtil.httpsRequest(
					WXPayConfig.UNIFIED_ORDER_URL, "POST", requestXML);
			// 解析微信返回的信息，以Map形式存储便于取值
			Map<String, String> map = XMLUtil.doXMLParse(result);

			// 获取prepayId
			String prepayid = map.get("prepay_id");
			logger.info("获取prepayid------值 " + prepayid);

			// 吐回给客户端的参数
			if (null != prepayid && !"".equals(prepayid)) {
				SortedMap<Object, Object> params = new TreeMap<Object, Object>();
				params.put("appId", WXPayConfig.APPJSID);
				params.put("nonceStr", noncestr);
				params.put("package","prepay_id="+prepayid);
				params.put("signType","MD5");
				params.put("timeStamp", timestamp);
				params.put("key", WXPayConfig.JSAPI_KEY);
				params.put("sign", PayCommonUtil.createJSSign("UTF-8", params));

				retmsg = "成功";
				resInfo.put("code", 1);
				resInfo.put("message", retmsg);
				resInfo.put("result_data", JSONObject.fromObject(params));
			} else {
				retmsg = "错误：获取prepayId失败";
				resInfo.put("code", 0);
				resInfo.put("message", retmsg);
			}
		} catch (CustomException e) {
			resInfo.put("code", 0);
			resInfo.put("message", e.getMessage());
		} catch (Exception e) {
			logger.error("", e);
			resInfo.put("code", 0);
			resInfo.put("message", e.getMessage());
		}
		putDataOut(resInfo.toString());
	}

	/**
	 * 微信支付成功回调
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void wxPayCallBack() {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			InputStream inStream = request.getInputStream();
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			outSteam.close();
			inStream.close();
			String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
			Map<Object, Object> map = XMLUtil.doXMLParse(result);
			for (Object keyValue : map.keySet()) {
				System.out.println(keyValue + "=" + map.get(keyValue));
			}

			if (map.get("result_code").toString().equalsIgnoreCase("SUCCESS")) {
				// TODO 对数据库的操作
				int code = orderInfoService.updateOrderStatus(
						map.get("out_trade_no").toString(), 2);

				if (code == 1) {
					// 告诉微信服务器，我收到信息了，不要在调用回调action了 
					List<OrderInfo> byOrderNu = orderInfoService.getByOrderNu(map.get("out_trade_no").toString());
					Integer type2 = byOrderNu.get(0).getType();
					if (type2==5) {
						updateUserYearCoupon(byOrderNu.get(0));
					}
					msmTool(map.get("out_trade_no").toString());
					response.getWriter().write("success");

				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	private void updateUserYearCoupon(OrderInfo orderInfo) {
		
		String userGid = orderInfo.getUserGid();
		Date createTime = orderInfo.getCreateTime();
		User user = userService.get(userGid);
		Date annualmembership = user.getAnnualmembership();
		Date date2 = new Date();
		
		long i = date2.getTime()-annualmembership.getTime();
		 Calendar calendar = Calendar.getInstance();
		if (i>=0) {
			calendar.setTime(new Date());
			calendar.add(Calendar.YEAR, 1);
			user.setAnnualmembership(calendar.getTime());
			
		} else {
			 calendar.setTime(annualmembership);
				calendar.add(Calendar.YEAR, 1);
				user.setAnnualmembership(calendar.getTime());
		}
		userService.update(user);
	}

	private void msmTool(String orderNum) {
		try {
			List<OrderInfo> list = orderInfoService.getByOrderNu(orderNum);
			if (list.size() == 0) {
				throw new CustomException(orderNum + "订单号不存在");
			}
			OrderInfo orderInfo = list.get(0);
			String totalprice = orderInfo.getTotalprice();
			String academyGid = orderInfo.getAcademyGid();
			Integer type2 = orderInfo.getType();
			String userGid = orderInfo.getUserGid();
			User user = userService.get(userGid);
			String phone2 = user.getPhone();
			Date annualmembership = user.getAnnualmembership();
			Calendar c = Calendar.getInstance();
			 c.setTime(annualmembership);
			if (user == null) {
				throw new CustomException("用户不存在");
			}
			String name = user.getName();
			String realName = user.getRealName();
			if (!StringUtils.isNotBlank(realName)) {
				realName = name;
			}
			String phone = user.getPhone();
			if (type2==4||type2==2) {
				String perTital = (type2 == 4 ? courseService.get(academyGid)
						.getTitle() : academyService.get(academyGid).getTitle());
				logger.info("【骨今中外】医生" + realName + "(" + phone + ")购买" + perTital
						+ "，消费" + totalprice + "。");
				//13701336571
				if (!SmsUtil.sendSms("【骨今中外】医生" + realName + "(" + phone + ")购买"+ perTital + "，消费" + totalprice + "。", "13552997031")||!SmsUtil.sendSms("【骨今中外】医生" + realName + "(" + phone + ")购买"+ perTital + "，消费" + totalprice + "。", "13811943437"))
					throw new CustomException("赵哥到账通知发送失败");
			}else if (type2==5) {
				String string="【骨今中外】您已成功开通骨今学院超级年会员，有效期至"+c.get(Calendar.YEAR)+"年"+(c.get(Calendar.MONTH)+1)+"月"+c.get(Calendar.DATE)+"日，专属客服电话（微信号）：13699187603。请尽快登录骨今中外APP完善资料，确保正常使用";
//				if (!SmsUtil.sendSms("【骨今中外】医生" + realName + "(" + phone + ")购买"+ "超级年会员" + "，消费" + totalprice + "。", "13552997031")||!SmsUtil.sendSms("【骨今中外】医生" + realName + "(" + phone + ")购买"+ "超级年会员" + "，消费" + totalprice + "。", "13811943437")||!SmsUtil.sendSms("【骨今中外】医生" + realName + "(" + phone + ")购买"+ "超级年会员" + "，消费" + totalprice + "。", "13701336571")||!SmsUtil.sendSms(string, phone2))
//					throw new CustomException("赵哥到账通知发送失败");
				if (!SmsUtil.sendSms("【骨今中外】医生" + realName + "(" + phone + ")购买"+ "超级年会员" + "，消费" + totalprice + "。", "13552997031")||!SmsUtil.sendSms(string, phone2))
					throw new CustomException("赵哥到账通知发送失败");
			
			}else if (type2==6) {
				String perTital =columnsService.get(academyGid)
						.getTitle(); 
				perTital=perTital+"专栏";
				String string="【骨今中外】您已成功购买"+perTital+"，请尽快登录骨今中外APP查看，确保正常使用。";
//				if (!SmsUtil.sendSms("【骨今中外】医生" + realName + "(" + phone + ")购买"+ "超级年会员" + "，消费" + totalprice + "。", "13552997031")||!SmsUtil.sendSms("【骨今中外】医生" + realName + "(" + phone + ")购买"+ "超级年会员" + "，消费" + totalprice + "。", "13811943437")||!SmsUtil.sendSms("【骨今中外】医生" + realName + "(" + phone + ")购买"+ "超级年会员" + "，消费" + totalprice + "。", "13701336571")||!SmsUtil.sendSms(string, phone2))
//					throw new CustomException("赵哥到账通知发送失败");
				if (!SmsUtil.sendSms("【骨今中外】医生" + realName + "(" + phone + ")购买"+ perTital + "，消费" + totalprice + "。", "13552997031")||!SmsUtil.sendSms(string, phone2))
					throw new CustomException("赵哥到账通知发送失败");
			
			}
		} catch (Exception e) {
			logger.error("", e);
			e.printStackTrace();
		}

	}

	/**
	 * 支付宝支付成功回调
	 */
	public void aliPayCallBack() {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			// 获取支付宝POST过来反馈信息
			Map<String, String> params = new HashMap<String, String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter
					.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				// valueStr = new String(valueStr.getBytes("ISO-8859-1"),
				// "gbk");
				params.put(name, valueStr);
			}

			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			String order_no = request.getParameter("out_trade_no"); // 获取订单号
			String trade_no = request.getParameter("trade_no"); // 支付宝交易号
			String total_fee = request.getParameter("total_fee"); // 获取总金额
			String subject = new String(request.getParameter("subject")
					.getBytes("ISO-8859-1"), "gbk");// 商品名称、订单名称
			String body = "";
			if (request.getParameter("body") != null) {
				body = new String(request.getParameter("body").getBytes(
						"ISO-8859-1"), "gbk");// 商品描述、订单备注、描述
			}
			String buyer_email = request.getParameter("buyer_email"); // 买家支付宝账号
			String trade_status = request.getParameter("trade_status"); // 交易状态
			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

			if (AlipayNotify.verify(params)) {// 验证成功
				// TODO 对数据库的操作
				if (trade_status.equals("TRADE_FINISHED")
						|| trade_status.equals("TRADE_SUCCESS")) {
					int code = orderInfoService.updateOrderStatus(order_no, 2);
					if (code == 1) {

						// orderInfoService.changeVipNum(trade_no);
						List<OrderInfo> byOrderNu = orderInfoService.getByOrderNu(order_no);
						Integer type2 = byOrderNu.get(0).getType();
						if (type2==5) {
							updateUserYearCoupon(byOrderNu.get(0));
						}
						msmTool(order_no);
						response.getWriter().write("success");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getExpertGid() {
		return expertGid;
	}

	public void setExpertGid(String expertGid) {
		this.expertGid = expertGid;
	}

}
