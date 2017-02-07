package com.sd.util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ComUtil {
	public static String getuuid(){
		return UUID.randomUUID().toString();
	}
	
	public static String date2Str(Date date){
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		if (date == null)
			return "";
		return formatDate.format(date);
	}
	public static String time2Str(Date date){
		SimpleDateFormat formatDate = new SimpleDateFormat("HH:mm:ss");
		if (date == null)
			return "";
		return formatDate.format(date);
	}
	public static String dateTime2Str(Date date){
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (date == null)
			return "";
		return formatDate.format(date);
	}
	
	public static Date str2Date(String strDate) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date time = null;
        try {
            time = formatDate.parse(strDate);
        } catch (Exception e) {
            return null;
        }
        return time;
    }
	public static Date str2Time(String strDate) {
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date time = null;
		try {
			time = formatDate.parse(strDate);
		} catch (Exception e) {
			return null;
		}
		return time;
	}
	public static long daysBetweenDate(Date begin, Date end){
		if (begin == null)
			return 0;
		if (end == null)
			return 0;
		
		return (end.getTime() - begin.getTime()) / (24 * 60 * 60 * 1000);
	}
	public static String MD5(String pwd) {
		char hexDigits[] = { '0', '1', '2', '3', '4','5', '6', '7', '8', '9','A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = pwd.getBytes();
			//获得MD5摘要算法的 MessageDigest 对象   
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			//使用指定的字节更新摘要   
			mdInst.update(btInput);
			//获得密文   
			byte[] md = mdInst.digest();
			//把密文转换成十六进制的字符串形式   
		    int j = md.length;
		    char str[] = new char[j * 2];
		    int k = 0;
		    for (int i = 0; i < j; i++) {
		        byte byte0 = md[i];
		        str[k++] = hexDigits[byte0 >>> 4 & 0xf];
		        str[k++] = hexDigits[byte0 & 0xf];
		    }
		    return new String(str);
		} catch (Exception e) {
		    e.printStackTrace();  
		    return "";  
		}
	}
	public static String randomPic(String id){
		String s = id.substring(id.length()-1);
		switch(s){
			case "0" : return "2e930145-b625-4b50-b2a1-960009cb1a54";
			case "1" : return "2e930145-b625-4b50-b2a1-960009cb1a54";
			case "2" : return "7325ef9b-02d4-40e3-8429-c116c418a4c7";
			case "3" : return "7325ef9b-02d4-40e3-8429-c116c418a4c7";
			case "4" : return "8cc0e507-403d-4965-87db-c3a4ceee856e";
			case "5" : return "8cc0e507-403d-4965-87db-c3a4ceee856e";
			case "6" : return "a242d0a2-a69c-451f-8a41-9e3148ec3781";
			case "7" : return "a242d0a2-a69c-451f-8a41-9e3148ec3781";
			case "8" : return "935f62b2-fd16-4ffd-b96c-7ecdb8f92264";
			case "9" : return "935f62b2-fd16-4ffd-b96c-7ecdb8f92264";
			case "a" : return "eedf31a9-e6d1-45c7-9146-3e0479d3cc2a";
			case "b" : return "eedf31a9-e6d1-45c7-9146-3e0479d3cc2a";
			case "c" : return "cba2d99e-ef85-4368-90e1-ec8447a8f2f3";
			case "d" : return "cba2d99e-ef85-4368-90e1-ec8447a8f2f3";
			case "e" : return "9a737c61-36b3-40c1-ad3b-2cb7e1dc4f39";
			case "f" : return "9a737c61-36b3-40c1-ad3b-2cb7e1dc4f39";
		}
		return null;
	}
}
