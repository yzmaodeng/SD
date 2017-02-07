package com.alipay.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088122694938433";
	// 商户的私钥,使用支付宝自带的openssl工具生成。
	public static String private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL+dugJ3GFXyLu59Z/Z70lMZn4j/7XTqbq/vBn+/Kqx6DKSp+UBaeHOfoQxD70IVP0XyiRcSmNA5I9iOnZswMfE8kXb3fRXqVcb3FgGrJc5NTS95KJ/w373CkNlDWRnOrrVz1LRT7hzYiqMf2QT3LZ8dqYc4VDg+qYpl75CJIm+zAgMBAAECgYActimVDPtchXl6yte1G0Ccqw44nQCbsDT9r7ctlRtz1KXoVn++oM/Do3uiNtu27zAX7wuTpXm4WZeBb302L7aLyCVIX/RXnIPQzhscH201u/BUGnjhu1JlPa8Udz2dDh5LTrE8dY6gqQgXZpDlV1tQrERS2rhdohYK6A6LlP8VIQJBAP3gRzmUrBxJCziK9IivdqUpMxAv0F+/t9ZDBhWOf2jWz4UyiFoCfKwaMnlOZqo/I3FgOBiSxQAfcWzQF4TqOpUCQQDBOBt61s1DRaJa20lXMRJ9wBZoAftboK+vFcWqqToNiac8aPIiTKZVtptwaftpzJehT7FgvZUst26iU57hh7cnAkBPaJ5+qT0oX8SNvBD+y/tNb9SUBJCl0l7bOv2lMnwxu7cPT54MoWiDoHIXNWmxaKxaYyFItme+QReGVJR2s5j9AkAs+nqrJcWym0soC1QPUAUV8NlGbO+ubMF46ICTMcGp1RlxHpz/DwjJezDEAmfcQRwrGPoZowhO2ISQlRavOYCRAkEAxd6yDBJjnpE3UCGXoh/e3nQmnpQ4homaVHqhr+3T3DePebIB7KzcWrVLpBUZn5DHE6vxAfyyrssRFZpJCrFZbA==";
	
	// 支付宝的公钥，无需修改该值
	public static String ali_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";
	
	public static String seller = "webmaster@medkr.com";

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "UTF-8";
	
	// 签名方式 不需修改
	public static String sign_type = "RSA";

}
