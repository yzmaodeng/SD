package com.sd.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

public class MyFilter extends StrutsPrepareAndExecuteFilter {
	@Override
	public void doFilter(ServletRequest req,ServletResponse res,FilterChain chain)throws IOException, ServletException{
		HttpServletRequest request = (HttpServletRequest) req;
        //不过滤的url,可以不断添加,如fck可用/fckeditor/editor/filemanager/connectors/fileupload,下面的是flex
        if (request.getRequestURI().contains(".do")||request.getRequestURI().contains(".jsp")) {
            //System.out.println("使用自定义的过滤器");
            HttpServletResponse response=(HttpServletResponse)res;
            HttpSession session=request.getSession();
//            if(session.getAttribute("userGid")==null&&!request.getRequestURI().contains("login.jsp")&&!request.getRequestURI().contains("login.do")){
//            	response.sendRedirect(request.getContextPath()+"/jsp/login/login.jsp");
//            }else{
            	chain.doFilter(req, res);
//            }
        }else{
            //System.out.println("使用默认的过滤器");
            super.doFilter(req, res, chain);
        }
	}
}
