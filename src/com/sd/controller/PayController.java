package com.sd.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipayCore;
import com.alipay.util.AlipayNotify;
import com.sd.service.OrderInfoService;
import com.sd.util.CustomException;
import com.wxpay.config.WXPayConfig;
import com.wxpay.util.CommonUtil;
import com.wxpay.util.PayCommonUtil;
import com.wxpay.util.TenpayUtil;
import com.wxpay.util.XMLUtil;

/**
 * 微信支付服务端简单示例
 * 
 * @author seven_cm
 * @dateTime 2014-11-29
 */
@Controller
@RequestMapping("/pay")
public class PayController {
	 @Autowired  
     private HttpServletRequest request;  

	protected static final String NO_ACADEMY = "票据信息不完整";
	private Logger log = Logger.getLogger(PayController.class);
	@Resource 
	private OrderInfoService orderInfoService;
	/**
	 * 支付宝订单信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getOrderInfo.do")
	public Map<String, Object> getOrderInfo(
			@RequestParam(required = true) String price,
			@RequestParam(required = false) String academyGid,
			@RequestParam(required = false) String contactGids,
			@RequestParam(required = false) int type,
			@RequestParam(required =false) String userGid,
			@RequestParam(required = true) String gid) {
		
		Map<String, Object> resInfo = new TreeMap<String, Object>();
		// ---------------生成订单号 开始------------------------
		// 当前时间 yyyyMMddHHmmss
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		// 10位序列号,可以自行调整。
		String strReq = strTime + strRandom;
		// 订单号，此处用时间加随机数生成，商户根据自己情况调整，只要保持全局唯一就行
		String out_trade_no = strReq;
		// ---------------生成订单号 结束------------------------

		try {
			String orderInfo = AlipayCore.getOrderInfo("支付宝支付",
					"用于测试支付宝快捷支付测试！", price, out_trade_no);
			String sign = AlipayCore.sign(orderInfo, AlipayConfig.private_key);
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");

			String orderString = orderInfo + "&sign=\"" + sign + "\"&"
					+ AlipayCore.getSignType();
			//此处调取创建订单方法
			if(type==2){
				if(academyGid==null||contactGids==null||academyGid==""||contactGids==""){
					throw new CustomException(NO_ACADEMY);
				}else{
					/*String[] contactArr = contactGids.split(",");
					int a = contactArr.length;
					for(int x=0;x<contactArr.length;x++){
						}
					}*/
//					orderInfoService.createOrderInfo(out_trade_no, request.getHeader("userId"),2,academyGid,contactGids,gid);
				}
			}else if(type==1){
//				orderInfoService.createOrderInfo(out_trade_no,userGid,1,academyGid,contactGids,gid);
			}
			resInfo.put("result", orderString);
		} catch (Exception e) {
			log.error("", e);
			resInfo.put("result", "failure");
		}
		return resInfo;
	}
	
	/**
	 * 微信支付订单信息
	 * 
	 * @return
	 */

	@RequestMapping("/getWXPayReq.do")
	public void getWXPayReq(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = true) String price,
			@RequestParam(required = false) String academyGid,
			@RequestParam(required = false) String contactGids,
			@RequestParam(required = false) int type,
			@RequestParam(required = true) String userGid,
			@RequestParam(required = true) String gid) throws Exception {
		// ---------------生成订单号 开始------------------------
		// 当前时间 yyyyMMddHHmmss
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		// 10位序列号,可以自行调整。
		String strReq = strTime + strRandom;
		// 订单号，此处用时间加随机数生成，商户根据自己情况调整，只要保持全局唯一就行
		String out_trade_no = strReq;
		// ---------------生成订单号 结束------------------------

		String noncestr = PayCommonUtil.CreateNoncestr();
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);

		JSONObject resInfo = new JSONObject();
		String retmsg = "";

		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
		parameters.put("appid", WXPayConfig.APPID);
		parameters.put("mch_id", WXPayConfig.MCH_ID);
		parameters.put("nonce_str", noncestr);
		parameters.put("body", "test");
		parameters.put("out_trade_no", out_trade_no);
		parameters.put("total_fee", "1");
		parameters.put("spbill_create_ip", "192.168.199.230");
		parameters.put("notify_url", WXPayConfig.NOTIFY_URL);
		parameters.put("trade_type", "APP");
		String sign = PayCommonUtil.createSign("UTF-8", parameters);
		parameters.put("sign", sign);
		String requestXML = PayCommonUtil.getRequestXml(parameters);
		// 以POST方式调用微信统一支付接口 取得预支付id
		String result = CommonUtil.httpsRequest(WXPayConfig.UNIFIED_ORDER_URL,
				"POST", requestXML);
		// 解析微信返回的信息，以Map形式存储便于取值
		Map<String, String> map = XMLUtil.doXMLParse(result);

		// 获取prepayId
		String prepayid = map.get("prepay_id");
		log.info("获取prepayid------值 " + prepayid);

		// 吐回给客户端的参数
		if (null != prepayid && !"".equals(prepayid)) {
			//此处调取创建订单方法
			if(type==2){
				if(academyGid==null||contactGids==null||academyGid==""||contactGids==""){
					throw new CustomException(NO_ACADEMY);
				}else{
					/*String[] contactArr = contactGids.split(",");
					int a = contactArr.length;
					for(int x=0;x<contactArr.length;x++){
						}
					}*/
//					orderInfoService.createOrderInfo(out_trade_no,userGid,2,academyGid,contactGids,gid);
				}
			}else if(type==1){
//				orderInfoService.createOrderInfo(out_trade_no,userGid,1,academyGid,contactGids,gid);
			}
			SortedMap<Object, Object> params = new TreeMap<Object, Object>();
			params.put("appid", WXPayConfig.APPID);
			params.put("partnerid", WXPayConfig.MCH_ID);
			params.put("prepayid", prepayid);
			params.put("timestamp", timestamp);
			params.put("noncestr", noncestr);
			params.put("package", "Sign=WXPay");
			params.put("sign", PayCommonUtil.createSign("UTF-8", params));
			String json = JSONObject.fromObject(params).toString();

			retmsg = "成功";
			resInfo.put("code", 1);
			resInfo.put("message", retmsg);
			resInfo.put("result_data", json);
		} else {
			retmsg = "错误：获取prepayId失败";
			resInfo.put("code", 0);
			resInfo.put("message", retmsg);
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		response.getWriter().write(JSONObject.fromObject(resInfo).toString());
	}
	/**
	 * 微信支付成功回调
	 * 
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/wxPayCallBack")
	private void wxPayCallBack(HttpServletRequest request,
			HttpServletResponse response) {
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
				int code = orderInfoService.updateOrderStatus(map.get("out_trade_no")
						.toString(),2);
				if (code == 1) {
					// 告诉微信服务器，我收到信息了，不要在调用回调action了
					response.getWriter().write("success");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 支付宝支付成功回调
	 */
	@ResponseBody
	@RequestMapping(value = "/aliPayCallBack")
	private void aliPayCallBack(HttpServletRequest request,
			HttpServletResponse response) {
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
					int code = orderInfoService.updateOrderStatus(order_no,2);
					if (code == 1) {
						response.getWriter().write("success");
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
