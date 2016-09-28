package com.hp.titan.common.util;

import java.util.Iterator;
import java.util.Set;

import com.hp.app.config.SpringUtil;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.auth.model.Role;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.service.UserService;
import com.hp.titan.common.constants.TitanContent;

public class UserInfoUtil {
	
	public static User getUser(String userCode) {
		User u = null;
		try {
			u= getUserService().getUserByCode(userCode);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return u;
	}
	
	//判断当前用户是否是管理员
	public static boolean isAdministratorAndDesigner(User user){
		boolean isAdmin = false;
		/*Set<Role> roles = user.getUserRoles();
		Iterator<Role> itr=roles.iterator();
		while(itr.hasNext()){
			Role role = itr.next();
			if(role.getRoleName().equals(TitanContent.SYSTEM_ROLE) || role.getRoleName().equals(TitanContent.ADMIN_ROLE)
					|| role.getRoleName().equals(TitanContent.DESINGER_ROLE)){
				isAdmin = true;
				break;
			}
		}*/
		return isAdmin;
	}
	
	public static boolean isAdministratorAndDesigner(String userCode){
		return isAdministratorAndDesigner(getUser(userCode));
	}

	//判断当前用户是否是管理员
	public static boolean isAdministrator(User user){
		boolean isAdmin = false;
	/*	Set<Role> roles = user.getUserRoles();
		Iterator<Role> itr=roles.iterator();
		while(itr.hasNext()){
			Role role = itr.next();
			if(role.getRoleName().equals(TitanContent.SYSTEM_ROLE) || role.getRoleName().equals(TitanContent.ADMIN_ROLE)){
				isAdmin = true;
				break;
			}
		}*/
		return isAdmin;
	}
	
	public static boolean isAdministrator(String userCode){
		return isAdministrator(getUser(userCode));
	}

	//判断当前用户是否是设计师
	public static boolean isDesigner(User user){
		boolean isDesigner = false;
/*		Set<Role> roles = user.getUserRoles();
		Iterator<Role> itr=roles.iterator();
		while(itr.hasNext()){
			Role role = itr.next();
			if(role.getRoleName().equals(TitanContent.DESINGER_ROLE)){
				isDesigner = true;
				break;
			}
		}*/
		return isDesigner;
	}

	public static boolean isDesigner(String userCode){
		return isDesigner(getUser(userCode));
	}
	
	//判断当前用户是否是普通用户
	public static boolean isCommonUser(User user){
		boolean isCommonUser = false;
/*		Set<Role> roles = user.getUserRoles();
		Iterator<Role> itr=roles.iterator();
		while(itr.hasNext()){
			Role role = itr.next();
			if(role.getRoleName().equals(TitanContent.COMMON_ROLE)){
				isCommonUser = true;
				break;
			}
		}*/
		return isCommonUser;
	}

	public static boolean isCommonUser(String userCode){
		return isCommonUser(getUser(userCode));
	}

	private static UserService getUserService() {
		return (UserService)SpringUtil.getBean("userService");
	}
}
