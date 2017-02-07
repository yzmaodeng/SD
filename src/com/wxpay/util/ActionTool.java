package com.wxpay.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sd.vo.WxUser;

public class ActionTool {
	
	public static final String USER_KEY = "_session_user";
	private static final String WX_USER = "_session_wxuser";

	
	public static WxUser getCurrentWxUser(HttpSession session) {
		return (WxUser)session.getAttribute(ActionTool.WX_USER);
		//return new WxUser("USER_14597675083852598", "oWqztw4nyQtvpeNWWV8kCW89PMDs");
	}
	
	public static void addWxUser(String userId, String accessToken, String refreshToken, int expiresIn, String openId, HttpSession session) {
		WxUser user = new WxUser(userId, accessToken, refreshToken, expiresIn, openId);
		session.setAttribute(ActionTool.WX_USER, user);
	}
	
	/**
	 * 检测微信用户是否已经登录
	 * @param session
	 * @return
	 */
	public static boolean isUserLogined(HttpSession session) {
		return (ActionTool.getCurrentWxUser(session) != null);
	}
	
	/**
	 * 获取项目根路径
	 * @param request
	 * @return
	 */
	public static String getBasepath(HttpServletRequest request) {
		StringBuilder builder = new StringBuilder();
		builder.append(request.getScheme());
		builder.append("://");
		builder.append(request.getServerName());
		builder.append(":");
		builder.append(request.getServerPort());
		builder.append(request.getContextPath());
		builder.append("/");
		return builder.toString();
	}
	
	/**
	 * 获取IP地址
	 * @param request
	 * @return
	 */
	public static String getRealIP(HttpServletRequest request) {
	    String ip = request.getHeader("x-forwarded-for");  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) 
	    {  
	        ip = request.getHeader("PRoxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	    {  
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) 
	    {  
	        ip = request.getRemoteAddr();  
	    }
	    if("0:0:0:0:0:0:0:1".equals(ip))
	    {
	    	ip = "127.0.0.1";
	    }
	    return ip;  
	}
}