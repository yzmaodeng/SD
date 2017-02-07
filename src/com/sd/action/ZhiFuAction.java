package com.sd.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.sd.util.CustomException;
import com.wxpay.config.WXPayConfig;
import com.wxpay.util.ActionTool;
import com.wxpay.util.NetWork;
import com.wxpay.util.Request;
import com.wxpay.util.Response;
import com.wxpay.util.TenpayUtil;

import net.sf.json.JSONObject;

public class ZhiFuAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());
	
	private String time ;
	private String openid ;
	private String userid ;
	private String useridd ;
	private String ogid ;
	private String pname ;
	private String hname;
	private String dname;
	private String sdname;
	private String atime;
	private String grade;
	private String service;
	private String charge;
	private String ch;
	private static String  finalsigns  ;
	private static String  prepayids  ;
	private static String  timestamps  ;
	private static String  packagess  ;
	private static String  noncestrs  ;
	private  String  finalsigns1  ;
	private  String  timestamps1  ;
	private  String  packagess1  ;
	private  String  noncestrs1  ;
	
		private static String partner = "1273";
		//这个参数partnerkey是在商户后台配置的一个32位的key,微信商户平台-账户设置-安全设置-api安全
		private static String partnerkey = "ZXCV";
		//openId 是微信用户针对公众号的标识，授权的部分这里不解释
		//微信支付成功后通知地址 必须要求80端口并且地址不能带参数
		private static String notifyurl = "http://ntjkxgj.naton.cn/JKS/";// Key
		
		
		public void shouQuan(){

			List<String> urls = new ArrayList<String>();
			HttpServletRequest request = ServletActionContext.getRequest();
			String code = request.getParameter("code");
			if(code == null || code.length() == 0){
				try {
					throw new CustomException("code值为空！");
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
			
			urls.add("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" +WXPayConfig.APPJSID+
					"&secret=" +WXPayConfig.APP_JSSECRECT+
					"&code=" +code+
					"&grant_type=authorization_code");
			String access_token = null;
			String openid = null;
			String nickname = null;
			String unionid = null;
			for(Integer i = 0;i<2;i++){
				Request re = new Request(urls.get(i), null);
				Response res = NetWork.getDataByHttp(re);
				JSONObject json = JSONObject.fromObject(res.getResponseText());
				openid = json.getString("openid");
				if(i == 0){
					access_token = json.getString("access_token");
					urls.add("https://api.weixin.qq.com/sns/userinfo?access_token=" +access_token+
				    		"&openid="+openid+
				    		"&lang=zh_CN");
				}else if(i == 1){
					nickname = json.getString("nickname");
					unionid = json.getString("unionid");
				}
			}
			JSONObject result = new JSONObject();
			result.put("nickname", nickname);
			result.put("openid", openid);
			result.put("unionid", unionid);
			putDataOut(result.toString());
		}
		 	

		/**
		 * 获取随机字符串
		 * @return
		 */
		public static String getNonceStr() {
			// 随机数
			String currTime = TenpayUtil.getCurrTime();
			// 8位日期
			String strTime = currTime.substring(8, currTime.length());
			// 四位随机数
			String strRandom = TenpayUtil.buildRandom(4) + "";
			// 10位序列号,可以自行调整。
			return strTime + strRandom;
		}

		/**
		 * 元转换成分
		 * @param money
		 * @return
		 */
		public static String getMoney(String amount) {
			if(amount==null){
				return "";
			}
			// 金额转化为分为单位
			String currency =  amount.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额  
	        int index = currency.indexOf(".");  
	        int length = currency.length();  
	        Long amLong = 0l;  
	        if(index == -1){  
	            amLong = Long.valueOf(currency+"00");  
	        }else if(length - index >= 3){  
	            amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));  
	        }else if(length - index == 2){  
	            amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);  
	        }else{  
	            amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");  
	        }  
	        return amLong.toString(); 
		}

		public String getUserid() {
			return userid;
		}

		public String getOpenid() {
			return openid;
		}

		public void setOpenid(String openid) {
			this.openid = openid;
		}

		public void setUserid(String userid) {
			this.userid = userid;
		}

		public String getFinalsigns() {
			return finalsigns;
		}

		public void setFinalsigns(String finalsigns) {
			this.finalsigns = finalsigns;
		}

		public String getPrepayids() {
			return prepayids;
		}

		public void setPrepayids(String prepayids) {
			this.prepayids = prepayids;
		}

		public static String getTimestamps() {
			return timestamps;
		}

		public static void setTimestamps(String timestamps) {
			ZhiFuAction.timestamps = timestamps;
		}

		public static String getPackagess() {
			return packagess;
		}

		public static void setPackagess(String packagess) {
			ZhiFuAction.packagess = packagess;
		}

		public static String getNoncestrs() {
			return noncestrs;
		}

		public String getOgid() {
			return ogid;
		}

		public void setOgid(String ogid) {
			this.ogid = ogid;
		}

		public static void setNoncestrs(String noncestrs) {
			ZhiFuAction.noncestrs = noncestrs;
		}

		public String getFinalsigns1() {
			return finalsigns1;
		}

		public void setFinalsigns1(String finalsigns1) {
			this.finalsigns1 = finalsigns1;
		}


		public String getTimestamps1() {
			return timestamps1;
		}

		public void setTimestamps1(String timestamps1) {
			this.timestamps1 = timestamps1;
		}

		public String getPackagess1() {
			return packagess1;
		}

		public void setPackagess1(String packagess1) {
			this.packagess1 = packagess1;
		}

		public String getNoncestrs1() {
			return noncestrs1;
		}

		public void setNoncestrs1(String noncestrs1) {
			this.noncestrs1 = noncestrs1;
		}

		public String getUseridd() {
			return useridd;
		}

		public void setUseridd(String useridd) {
			this.useridd = useridd;
		}

		public String getPname() {
			return pname;
		}

		public void setPname(String pname) {
			this.pname = pname;
		}

		public String getHname() {
			return hname;
		}

		public void setHname(String hname) {
			this.hname = hname;
		}

		public String getDname() {
			return dname;
		}

		public void setDname(String dname) {
			this.dname = dname;
		}

		public String getSdname() {
			return sdname;
		}

		public String getAtime() {
			return atime;
		}

		public void setAtime(String atime) {
			this.atime = atime;
		}

		public String getGrade() {
			return grade;
		}

		public void setGrade(String grade) {
			this.grade = grade;
		}

		public String getService() {
			return service;
		}

		public void setService(String service) {
			this.service = service;
		}

		public void setSdname(String sdname) {
			this.sdname = sdname;
		}

		public String getCharge() {
			return charge;
		}

		public void setCharge(String charge) {
			this.charge = charge;
		}

		public String getCh() {
			return ch;
		}

		public void setCh(String ch) {
			this.ch = ch;
		}


}
