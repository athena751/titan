package com.hp.titan.login;
import java.util.Map;  

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.hp.titan.auth.model.User;
import com.hp.titan.common.util.UserSessionUtil;
import com.opensymphony.xwork2.Action;  
import com.opensymphony.xwork2.ActionContext;  
import com.opensymphony.xwork2.ActionInvocation;  
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;  
  
public class LoginInterceptor extends AbstractInterceptor {  
  
    @Override  
    public String intercept(ActionInvocation invocation) throws Exception {  
  
    	HttpServletRequest request = ServletActionContext.getRequest();		
    	 String requestURI = request.getRequestURI();
		  String contextPath = request.getContextPath();
		  requestURI = requestURI.substring(contextPath.length());
		  if(requestURI.equals("/") || requestURI.equals("/index.jsp") ||
				  requestURI.equals("/login.jsp") || requestURI.equals("/doLogin.do") || requestURI.equals("/doFindPwd.do")	 ||
				  requestURI.equals("/login.do") || requestURI.equals("/register.do") || requestURI.equals("/doRegister.do") ||
				  requestURI.equals("/goFindPwd.do") ||  requestURI.equals("/doCheckLogin.do") ||  requestURI.equals("/auth/checkUserCode.do") || requestURI.equals("/auth/checkUserMail.do") ||
				  requestURI.equals("/portal/login.do") || requestURI.equals("/portal/doLogin.do") || requestURI.equals("/test/doRunOnCommand.do")){
			  return invocation.invoke();  
		  } else {
			  	if(request.getSession(false)==null){
				   System.out.println("Session has been invalidated!");
				   UserSessionUtil.clearSession();
				   return Action.LOGIN; 
			  	}else{
				   System.out.println("Session is active!");
				  	User user =UserSessionUtil.getUser();
			        // 如果没有登陆，返回重新登陆  
			  
			        if (user != null) {  
			            System.out.println("test success");  
			            return invocation.invoke();  
			        }else{
			        	System.out.println("no login");  
			        }  
			  	}
		  }
        
        
       
        return Action.LOGIN;  
  
    }  
  
}  
 