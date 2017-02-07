package com.sd.util;

import cn.b2m.eucp.sdkhttp.SDKServiceBindingStub;
import cn.b2m.eucp.sdkhttp.SDKServiceLocator;


public class OldSmsUtil{

    //<add key="SMSPassWord" value="159283"/>
	//<add key="SMSCharset" value="GBK"/>
	private static final String userkey = "6SDK-EMY-6688-KDWTN";
    private static final String password = "159283";
    private static final String charset = "GBK";
    //验证码业务
    private static final String validTemplate = "【X-APP】您申请注册X-APP账号的短信验证码为{0}，请在30分钟内完成验证。退订回复TD";
    //提醒锻炼业务
    private static final String exerciseRemindTemplate = "【X-APP】亲爱的患者！您的主治医生已经连续三天未收到您的锻炼信息了，请您及时登录手机应用补充锻炼信息，方便医生指导您的康复。退订回复TD";
    //提醒复查业务
    private static final String recheckRemindTemplate="【X-APP】亲爱的患者！今天距离您的手术时间快一个月了，为了您更快更好地恢复，建议您前往手术医院进行复查。退订回复TD";
    /// <summary>
    /// 发送验证码
    /// </summary>
    /// <param name="phone"></param>
    /// <param name="validCode"></param>
    /// <returns></returns>
    public static Boolean SendValidCode(String phone,String validCode)
    {
        return SendSms(phone,String.format(validTemplate,validCode));
    }
    /// <summary>
    /// 发送锻炼提醒
    /// </summary>
    /// <param name="phone"></param>
    /// <returns></returns>
    public static Boolean SendExerciseRemind(String phone)
    {
        return SendSms(phone, exerciseRemindTemplate);
    }

    public static Boolean SendRecheckRemind(String phone)
    {
        return SendSms(phone, recheckRemindTemplate);
    }



    /// <summary>
    /// 发送单条短信
    /// </summary>
    /// <param name="mobile"></param>
    /// <param name="content"></param>
    /// <returns></returns>
    public static Boolean SendSms(String mobile, String content)
    {
        return SendSms(mobile.split(","), content);
    }
    /// <summary>
    /// 群发短信
    /// </summary>
    /// <param name="mobileArray"></param>
    /// <param name="content"></param>
    /// <returns></returns>
    public static Boolean SendSms(String[] mobileArray, String content)
    {
        int res = 0;
		try {
			SDKServiceLocator locator = new SDKServiceLocator();
			SDKServiceBindingStub stub = (SDKServiceBindingStub) locator.getSDKService();
			res = stub.sendSMS(userkey, password, null, mobileArray, content, null, charset, 1, 9988);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return res == 0 ? true : false;
      
    }



}
