package com.hp.titan.auth.service;

import java.util.List;

import com.hp.app.exception.BaseDaoException;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.model.ViewUserInfo;
import com.hp.titan.auth.vo.UserVo;

 

public interface ViewUserInfoService {


	/**
	 * 获得所有用户视图
	 * 
	 * @return
	 */
	public List<User> getAllUserViewList(UserVo userVo) throws BaseDaoException ;
	
 
	public ViewUserInfo getUserByUserCode(String userCode) throws BaseDaoException ;

}
