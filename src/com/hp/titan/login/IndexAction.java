package com.hp.titan.login;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.hp.app.action.DefaultBaseAction;
import com.hp.app.common.util.Dom4jUtil;
import com.hp.app.config.SysConfiger;
import com.hp.titan.auth.model.User;


public class IndexAction extends DefaultBaseAction {
	

	private List<User> userList;


	public String goIndex() throws DocumentException, IOException {
		
	 
 
		
		return SUCCESS;
	}


}
