package com.hp.titan.login;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter{
	String LOGIN_PAGE="/index.jsp";
	protected FilterConfig filterConfig;
	
	public void init(FilterConfig config) throws ServletException {
		  this.filterConfig = config;       
	}

	public void doFilter(ServletRequest request, ServletResponse response,
	    FilterChain chain) throws IOException, ServletException {
		  HttpServletRequest req = (HttpServletRequest) request;
		  
		  HttpServletResponse res=(HttpServletResponse)response;
		  req.getSession().getAttribute("loginUser");
		  System.out.println("Session loginUser : " + req.getSession().getAttribute("loginUser"));
		  req.getSession().getAttribute("point");
		  System.out.println("Session point : " + req.getSession().getAttribute("point"));
		  String path = req.getServletPath(); 
		  String requestURI = req.getRequestURI();
		  String contextPath = req.getContextPath();
		  requestURI = requestURI.substring(contextPath.length());
		  System.out.println(requestURI);
		  if(requestURI.equals("/") || requestURI.equals("/index.jsp") ||
		  requestURI.equals("/login.jsp") || 
		   requestURI.equals("/login.do") ||
		   requestURI.equals("/auth/userlist.do")){
			  chain.doFilter(request, response);
		  } else {
		   // String islogin = (String) session.getAttribute("passed");
			   if (req.getSession().getAttribute("loginUser") != null) {
			    chain.doFilter(request, response);
			   } else {
				    req.getSession().invalidate();
				    System.out.print(req.getContextPath()+LOGIN_PAGE);
				    res.sendRedirect(req.getContextPath()+LOGIN_PAGE);
			   }
		  }

		  
	  System.out.println("~~~~~~~~~~loginFilter start~~~~~~~~~~~!");
	  /** HttpServletRequest req=(HttpServletRequest)request;
	   HttpServletResponse res=(HttpServletResponse)response;
	   HttpSession session=req.getSession();
	   String currentURL = req.getRequestURI();
	   String targetURL = currentURL.substring(currentURL.indexOf("/", 1));
	   System.out.println("targetURL"+targetURL);
	   if(session.getAttribute("loginUser")!=null){
		   chain.doFilter(request, response);
	   } 
	   if(session == null || session.getAttribute("loginUser")==null){
				res.sendRedirect(req.getContextPath()+LOGIN_PAGE);   //"http://"+req.getHeader("Host")+
			  // request.getRequestDispatcher("index.jsp").forward(request, response);
			  // System.out.println("http://"+req.getHeader("Host")+req.getContextPath()+"/index.jsp");
			   System.out.println("~~~~~~~~~~loginFilter success~~~~~~~~~~~!");
	   }
		  // if ((!"/index.jsp".equals(targetURL) )){}
	   
	   System.out.println("~~~~~~~~~~loginFilter end~~~~~~~~~~~!");
	   **/
	}

	public void setFilterConfig(final FilterConfig filterConfig)
	{
		this.filterConfig=filterConfig;
	}
	 
	public void destroy() {
		this.filterConfig=null;
	}
	
	}