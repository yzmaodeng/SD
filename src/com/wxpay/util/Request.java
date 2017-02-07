package com.wxpay.util;

import java.util.HashMap;
import java.util.Map;

public class Request
{
	private String url = null;
	private String parameter = null;
	private Map<String, String> header = null;
	
	private String sendEncoding = "UTF-8";
	private String receiveEncoding = "UTF-8";
	
	public Request(String url, String parameter)
	{
		this.url = url;
		this.parameter = parameter;
		this.header = new HashMap<String, String>();
		this.header.put("Connection", "Keep-Alive");
	}
	
	public void addHeader(String key, String value)
	{
		this.header.put(key, value);
	}
	
	public Map<String, String> getHeader()
	{
		return this.header;
	}

	public String getUrl()
	{
		return url;
	}

	public String getParameter()
	{
		return parameter;
	}

	public String getSendEncoding()
	{
		return sendEncoding;
	}

	public void setSendEncoding(String sendEncoding)
	{
		this.sendEncoding = sendEncoding;
	}

	public String getReceiveEncoding()
	{
		return receiveEncoding;
	}

	public void setReceiveEncoding(String receiveEncoding)
	{
		this.receiveEncoding = receiveEncoding;
	}
}