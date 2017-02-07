package com.wxpay.util;

import antlr.StringUtils;
import freemarker.template.utility.StringUtil;

public class Response
{
	private String responseText = null;
	private int responseCode = 0;
	private Exception e = null;
	
	/**
	 * 判断请求是否发送成功(验证响应为200并且没有异常)
	 * @return
	 */
	public boolean requestSucceed1()
	{
		return (this.responseCode == 200 && this.e == null);
	}
	
	/**
	 * 判断请求是否发送成功(验证响应为200、没有异常并且有响应内容)
	 * @return
	 */
	public boolean requestSucceed2()
	{
		return (this.responseCode == 200 && this.e == null);
	}
	
	public String getResponseText()
	{
		return responseText;
	}
	public void setResponseText(String responseText)
	{
		this.responseText = responseText;
	}
	public int getResponseCode()
	{
		return responseCode;
	}
	public void setResponseCode(int responseCode)
	{
		this.responseCode = responseCode;
	}
	public Exception getE()
	{
		return e;
	}
	public void setE(Exception e)
	{
		this.e = e;
	}
}