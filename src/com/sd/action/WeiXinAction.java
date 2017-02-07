package com.sd.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jboss.logging.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wxpay.config.WXPayConfig;

@Controller
@RequestMapping("/weixin")
public class WeiXinAction extends BaseAction{
	
	@RequestMapping(value = "/entrance", method = { RequestMethod.GET, RequestMethod.POST })
	public void postDetailSelect(HttpServletRequest request,HttpServletResponse response,@Param String signature,@Param String timestamp,@Param String nonce,@Param String echostr){
		try {
			boolean isGet = request.getMethod().toLowerCase().equals("get");  
			if(isGet){
				String[] str = { "meng", timestamp, nonce };
				Arrays.sort(str); // 字典序排序
				String bigStr = str[0] + str[1] + str[2];
				String digest = DigestUtils.sha1Hex(bigStr);
				if (digest.equals(signature)) {
					//最好此处将echostr存起来，以后每次校验消息来源都需要用到
					PrintWriter out = response.getWriter();
					out.print(echostr);
				}
			}else{
				// 处理接收消息  
				Map<String, String> map = this.parseXml(request);
				
				StringBuffer str = new StringBuffer();  
	            str.append("<xml>");  
	            str.append("<ToUserName><![CDATA[" + map.get("FromUserName") + "]]></ToUserName>");  
	            str.append("<FromUserName><![CDATA[" + map.get("ToUserName") + "]]></FromUserName>");  
	            str.append("<CreateTime>" + new Date().getTime() + "</CreateTime>");  
	            str.append("<MsgType><![CDATA[" + "news" + "]]></MsgType>");  //图文
	            str.append("<ArticleCount>" + "1" + "</ArticleCount>");
	            str.append("<Articles>");
	            str.append("<item>");
	            str.append("<Title><![CDATA[" + "致13万骨科医生，最高端的骨科专业培训课程你愿意参加吗？"+ "]]></Title>");
	            str.append("<Description><![CDATA[" + "中国医生最头疼的问题 不在大医院，该如何提高临床水平？基层医院，技术水平不高，没有大牛带路，如何开展新的手术"+ "]]></Description>");
	            str.append("<PicUrl><![CDATA[" + "http://academy.ufile.ucloud.com.cn/invitation.jpeg"+ "]]></PicUrl>");
	            str.append("<Url><![CDATA[" + "http://mp.weixin.qq.com/s?__biz=MzI4MzQ0NzU2Ng==&mid=100000010&idx=1&sn=2f752a20b9a6aedaa86f28b63c633bce"+ "]]></Url>");
	            str.append("</item>");
	            str.append("</Articles>");
	            str.append("</xml>");  
	            response.setCharacterEncoding("UTF-8");
	            response.setContentType("text/plain");
	            response.getWriter().write(str.toString());  
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public Map parseXml(HttpServletRequest request) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        // 从request中取得输入流
        StringBuffer sb = new StringBuffer();
        InputStream is = request.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String s = "";
        while ((s = br.readLine()) != null) {
            sb.append(s);
        }
        String xml = sb.toString();
 
        // 读取输入流
        Document document = null;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (DocumentException e1) {
            e1.printStackTrace();
        }
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();
        // 遍历所有子节点
        for (Element e : elementList) {
            // 对于CDATA区域内的内容，XML解析程序不会处理，而是直接原封不动的输出。
            map.put(e.getName(), e.getText());
        }
        return map;
    }
	
	public String getAccess_token() {
			if(token==""||new Date().getTime()-tokenDate.getTime()>3600*1000){
				String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
						+WXPayConfig.APPJSID+ "&secret=" + WXPayConfig.APP_JSSECRECT;
				String accessToken = null;
				try {
					URL urlGet = new URL(url);
					HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
					http.setRequestMethod("GET"); // 必须是get方式请求
					http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
					http.setDoOutput(true);
					http.setDoInput(true);
					System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
					System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
					http.connect();
					InputStream is = http.getInputStream();
					int size = is.available();
					byte[] jsonBytes = new byte[size];
					is.read(jsonBytes);
					String message = new String(jsonBytes, "UTF-8");
					JSONObject demoJson = JSONObject.fromObject(message);
					accessToken = demoJson.getString("access_token");
					token=accessToken;
					tokenDate=new Date();
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println(token);
			return token;
	   }
	public String getTicket() {
		if(jsapi_ticket==""||new Date().getTime()-ticketDate.getTime()>3600*1000){
			String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+getAccess_token()+"&type=jsapi";
			String ticket = null;
			try {
				URL urlGet = new URL(url);
				HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
				http.setRequestMethod("GET"); // 必须是get方式请求
				http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
				http.setDoOutput(true);
				http.setDoInput(true);
				System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
				System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
				http.connect();
				InputStream is = http.getInputStream();
				int size = is.available();
				byte[] jsonBytes = new byte[size];
				is.read(jsonBytes);
				String message = new String(jsonBytes, "UTF-8");
				JSONObject demoJson = JSONObject.fromObject(message);
				ticket = demoJson.getString("ticket");
				jsapi_ticket=ticket;
				ticketDate=new Date();
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println(jsapi_ticket);
		return jsapi_ticket;
   }
	
	@RequestMapping("/menu.do")
	public String createMenu() {
			JSONObject jSONObject=new JSONObject();
			JSONArray jSONArray=new JSONArray();
			JSONObject jSONObject2=new JSONObject();
			jSONObject2.put("type", "view");
			jSONObject2.put("name", "骨今中外");
			jSONObject2.put("url","http://www.autohome.com.cn/");
			jSONArray.add(jSONObject2);
			jSONObject.put("button", jSONArray);
	        //此处改为自己想要的结构体，替换即可
	        String access_token= this.getAccess_token();
	        String action = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+access_token;
	        try {
	           URL url = new URL(action);
	           HttpURLConnection http =   (HttpURLConnection) url.openConnection();    

	           http.setRequestMethod("POST");        
	           http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");    
	           http.setDoOutput(true);        
	           http.setDoInput(true);
	           System.setProperty("sun.net.client.defaultConnectTimeout", "30000");//连接超时30秒
	           System.setProperty("sun.net.client.defaultReadTimeout", "30000"); //读取超时30秒
	           http.connect();
	           OutputStreamWriter out = new OutputStreamWriter(http.getOutputStream());
               out.write(jSONObject.toString());
               out.flush();
               out.close();

	           InputStream is =http.getInputStream();
	           int size =is.available();
	           byte[] jsonBytes =new byte[size];
	           is.read(jsonBytes);
	           String message=new String(jsonBytes,"UTF-8");
	           return "返回信息"+message;
	           } catch (MalformedURLException e) {
	               e.printStackTrace();
	           } catch (IOException e) {
	               e.printStackTrace();
	           }    
	        return "createMenu 失败";
	   }
	@RequestMapping("/query.do")
	public void query(HttpServletResponse response,@Param String url){
		String noncestr=UUID.randomUUID().toString();
		String jsapi_ticket=getTicket();
		String timestamp =Long.toString(System.currentTimeMillis() / 1000);
		String ss="jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + noncestr +
                "&timestamp=" + timestamp +
                "&url=" + url;
		String signature=DigestUtils.sha1Hex(ss);
		JSONObject jSONObject=new JSONObject();
		jSONObject.put("jsapi_ticket", jsapi_ticket);
		jSONObject.put("noncestr", noncestr);
		jSONObject.put("timestamp", timestamp);
		jSONObject.put("signature", signature);
		jSONObject.put("ss", ss);
		response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
        try {
			response.getWriter().write(jSONObject.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
}
