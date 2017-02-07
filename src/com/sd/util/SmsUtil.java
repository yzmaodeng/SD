package com.sd.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
/**
 * 短信http接口的java代码调用示例
 * 基于Apache HttpClient 4.3
 *
 */
public class SmsUtil {
	private static Logger logger = Logger.getLogger(new SmsUtil().getClass());
	
    // 查账户信息的http地址
    private static String URI_GET_USER_INFO = "http://yunpian.com/v1/user/get.json";

    //通用发送接口的http地址
    private static String URI_SEND_SMS = "http://yunpian.com/v1/sms/send.json";

    // 模板发送接口的http地址
    private static String URI_TPL_SEND_SMS = "http://yunpian.com/v1/sms/tpl_send.json";

    // 发送语音验证码接口的http地址
    private static String URI_SEND_VOICE = "http://yunpian.com/v1/voice/send.json";

    //编码格式。发送编码格式统一用UTF-8
    private static String ENCODING = "UTF-8";
    
    private static String apikey = "b4be82c2bf2d9d1f47c58a609d8b8384";

    public static void main(String[] args) throws IOException, URISyntaxException {
        //修改为您的apikey.apikey可在官网（http://www.yuanpian.com)登录后用户中心首页看到
        String apikey = "b4be82c2bf2d9d1f47c58a609d8b8384";
        //修改为您要发送的手机号
        String mobile = "13683520110";

        /**************** 查账户信息调用示例 *****************/
        System.out.println(SmsUtil.getUserInfo(apikey));

        /**************** 使用通用接口发短信(推荐) *****************/
        //设置您要发送的内容(内容必须和某个模板匹配。以下例子匹配的是系统提供的1号模板）
        String text = "【云片网】您的验证码是1234";
        //发短信调用示例
        System.out.println(SmsUtil.sendSms(apikey, text, mobile));

        /**************** 使用模板接口发短信(不推荐，建议使用通用接口) *****************/
        //设置模板ID，如使用1号模板:【#company#】您的验证码是#code#
        long tpl_id = 1;
        //设置对应的模板变量值
        //如果变量名或者变量值中带有#&=%中的任意一个特殊符号，需要先分别进行urlencode编码
        //如code值是#1234#,需作如下编码转换
        String codeValue = URLEncoder.encode("#1234#", ENCODING);
        String tpl_value = "#code#=" + codeValue + "&#company#=云片网";
        //模板发送的调用示例
        System.out.println(SmsUtil.tplSendSms(tpl_id, tpl_value, mobile));

        /**************** 使用接口发语音验证码 *****************/
        String code = "1234";
        System.out.println(SmsUtil.sendVoice(apikey, mobile ,code));
    }
    
    
    // 黑名单
    private static Map<String,String> blackMap = new ConcurrentHashMap<String, String>();
    
    /*
     *  访问权限
     *  -1 黑名单 0 今日受限 1 正常
     */
    public synchronized static int getVisitAuthority(String ip,String phone, Date newestIPDate, int todayIPCount){
    	
    	if(blackMap.get(ip)!=null){
    		return -1;
    	}
    	Date now = new Date();
    	// 1秒内连续访问，加入黑名单
		if (newestIPDate != null && now.getTime() - newestIPDate.getTime() < 2*1000){
			blackMap.put(ip, phone);
			putKeyValueToDB(ip, phone);
			return -1;
		}else{
			if (todayIPCount >= 5){
				return 0;
			}else{
				return 1;
			}
		}
    } 
    public static void putKeyValueToDB(String ip ,String phone) {
    	Properties prop = new Properties(); 
    	FileOutputStream oFile=null;
			try {
				oFile= new FileOutputStream("/usr/tomcat/BlackList.properties", true);//true表示追加打开
				             prop.setProperty(ip, phone);
				             prop.store(oFile, "***********************");
				             oFile.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if (oFile!=null) {
					try {
						oFile.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

    }
    public static boolean  checkPhone(String phone) {
    	Properties prop = new Properties(); 
    	FileInputStream in=null;
    	try {
			     		 in = new FileInputStream("/usr/tomcat/PhoneList.properties");
			     		prop.load(in);
			     		return prop.containsValue(phone);
			     		
			         
			             
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in!=null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
    }
    
    
    public static boolean  checkBlack(String ip ,String phone) {
    	Properties prop = new Properties(); 
    	FileInputStream in=null;
    	try {
			     		 in = new FileInputStream("/usr/tomcat/BlackList.properties");
			     		prop.load(in);
			     		boolean containsKey = prop.containsKey(ip);
			     		boolean containsValue = prop.containsValue(phone);
			     		if (containsKey || containsValue) {
							return  true;
						} else{
							return false;
						}
			         
			             
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in!=null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
    }
    
    /**
     * 取账户信息
     *
     * @return json格式字符串
     * @throws java.io.IOException
     */
    public static String getUserInfo(String apikey) throws IOException, URISyntaxException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", apikey);
        return post(URI_GET_USER_INFO, params);
    }

    /**
     * 通用接口发短信
     *
     * @param apikey apikey
     * @param text   　短信内容
     * @param mobile 　接受的手机号
     * @return json格式字符串
     * @throws IOException
     */
    public static String sendSms(String apikey, String text, String mobile) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", apikey);
        params.put("text", text);
        params.put("mobile", mobile);
        return post(URI_SEND_SMS, params);
    }
    @SuppressWarnings("static-access")
	public static boolean sendSms(String text, String mobile) throws IOException {
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("apikey", apikey);
    	params.put("text", text);
    	params.put("mobile", mobile);
    	String ret = post(URI_SEND_SMS, params);
    	JSONObject obj = new JSONObject().fromObject(ret);
    	if (!"0".equals(obj.get("code")+"")){
    		logger.error("#############################################################");
    		logger.error(ret);
    		logger.error("#############################################################");
    		return false;
    	}
    	return true;
    }

    /**
     * 通过模板发送短信(不推荐)
     *
     * @param apikey    apikey
     * @param tpl_id    　模板id
     * @param tpl_value 　模板变量值
     * @param mobile    　接受的手机号
     * @return json格式字符串
     * @throws IOException
     */
    public static String tplSendSms(long tpl_id, String tpl_value, String mobile) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", apikey);
        params.put("tpl_id", String.valueOf(tpl_id));
        params.put("tpl_value", tpl_value);
        params.put("mobile", mobile);
        return post(URI_TPL_SEND_SMS, params);
    }

    /**
    * 通过接口发送语音验证码
    * @param apikey apikey
    * @param mobile 接收的手机号
    * @param code   验证码
    * @return
    */
    public static String sendVoice(String apikey, String mobile, String code) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", apikey);
        params.put("mobile", mobile);
        params.put("code", code);
        return post(URI_SEND_VOICE, params);
    }

    /**
     * 基于HttpClient 4.3的通用POST方法
     *
     * @param url       提交的URL
     * @param paramsMap 提交<参数，值>Map
     * @return 提交响应
     */
    public static String post(String url, Map<String, String> paramsMap) {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            if (paramsMap != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
                    paramList.add(pair);
                }
                method.setEntity(new UrlEncodedFormEntity(paramList, ENCODING));
            }
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseText;
    }
}
