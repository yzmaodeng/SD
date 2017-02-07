package com.wxpay.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.commons.io.IOUtils;

public class NetWork
{
	private final static int CON_TIMEOUT = 20000; //超时时间，单位：ms
	private final static int READ_TIMEOUT = 20000; //超时时间，单位：ms
	
	/**
	 * 获取URLConnection
	 * @param request
	 * @return
	 */
	private static URLConnection getConnection(Request request)
		throws Exception
	{
		String url = request.getUrl();
		HttpURLConnection con = null;
		try
		{
			URL address = new URL(url);
			con = (HttpURLConnection)address.openConnection();
			con.setConnectTimeout(NetWork.CON_TIMEOUT);
			con.setReadTimeout(NetWork.READ_TIMEOUT);
			con.setDefaultUseCaches(false);
			Map<String, String> header = request.getHeader();
			for(String key : header.keySet())
			{
				con.addRequestProperty(key, header.get(key));
			}
			con.setRequestMethod("POST");
			if(url.indexOf("https://") != -1)
			{
				HttpsURLConnection https = (HttpsURLConnection)con;
				SSLContext ssl = SSLContext.getInstance("SSL");
				ssl.init(null, new TrustManager[] {new MyX509TrustManager()}, new SecureRandom());
				https.setSSLSocketFactory(ssl.getSocketFactory());
				https.setHostnameVerifier(new MyHostnameVerifier());
			}
			con.setDoInput(true);
			if(request.getParameter()!=null&&request.getParameter()!="")
			{
				con.setDoOutput(true);
				OutputStream output = con.getOutputStream();
				output.write(request.getParameter().getBytes(request.getSendEncoding()));
				output.flush();
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		return con;
	}

	/**
	 * 发送HTTP请求并接收返回值，返回值在任何时候都不会为null.
	 * @param request
	 * @return
	 */
	public static Response getDataByHttp(Request request)
	{
		Response response = new Response();
		HttpURLConnection con = null;
		InputStream input = null;
		try
		{
			con = (HttpURLConnection)NetWork.getConnection(request);
			response.setResponseCode(con.getResponseCode());
			input = con.getInputStream();
			response.setResponseText(IOUtils.toString(input, request.getReceiveEncoding()));
		}
		catch (Exception e)
		{
			response.setE(e);
		}
		finally
		{
			if(input != null)
			{
				try
				{
					input.close();
				}
				catch (IOException e)
				{
					
				}
			}
			if(con != null) con.disconnect();
		}
		return response;
	}
	
	
}