package com.hp.titan.notify;

import java.util.List;

import javax.mail.SendFailedException;

import com.hp.app.action.DefaultBaseAction;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.service.UserService;
import com.hp.titan.auth.vo.UserVo;


public class EmailManageAction extends DefaultBaseAction {
	
	public String sendEmail(String title, String context, String address){
		Mail m = new Mail();
		m.doSendHtmlEmail(title, context, address);;
		return SUCCESS;
	}
}
