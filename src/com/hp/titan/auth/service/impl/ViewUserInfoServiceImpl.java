package com.hp.titan.auth.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseServiceException;
import com.hp.titan.auth.dao.ViewUserInfoDao;
import com.hp.titan.auth.model.Group;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.model.ViewUserInfo;
import com.hp.titan.auth.service.ViewUserInfoService;
import com.hp.titan.auth.vo.UserVo;

 

/**
 * @author kevin
 *
 */
public class ViewUserInfoServiceImpl implements ViewUserInfoService {

	private ViewUserInfoDao viewUserInfoDao;

	public List<User> getAllUserViewList(UserVo userVo) throws BaseDaoException {
		// TODO Auto-generated method stub
		List<User> users = new ArrayList<User>();
		 List<ViewUserInfo> viewUsers = viewUserInfoDao.getAllUserViewList(userVo);
		 Set<Group> userGroups;
		 for(int i =0 ; i<viewUsers.size();i++){
			 ViewUserInfo  viewUsser =  viewUsers.get(i);
			 //System.out.println("@@@@@@@@@@@viewUsser  "+viewUsser.getUserId()+"~~~"+viewUsser.getUserCode()+viewUsser.getUserName()+"groupId groupName"+viewUsser.getGroupId()+viewUsser.getGroupName());
			 User user =isUserEquals(users,viewUsser.getUserCode());
			 if(user!=null){ //存在
				  userGroups = user.getUserGroups();
				 //System.out.println("~~~~~~~~~user"+user.getUserCode()+user.getUserName()+"groupId groupName"+viewUsser.getGroupId());
				 Group group =isGroupEquals(userGroups,viewUsser.getGroupId());
				if(group==null){
					 group = new Group();
					 group.setGroupId(viewUsser.getGroupId());
					 group.setGroupName(viewUsser.getGroupName());
					 //System.out.println("~~~~~~~~~groupId "+group.getGroupId()+"groupName "+group.getGroupName());
					 userGroups.add(group);
					 user.setUserGroups(userGroups);
				}
			 }else{											//不存在
				 user = new User();
				 user.setUserId(viewUsser.getUserId());
				 user.setUserCode(viewUsser.getUserCode());
				 user.setMail(viewUsser.getMail());
				 user.setPassword(viewUsser.getPassword());
				 user.setRemark(viewUsser.getRemark());
				   userGroups = new HashSet<Group>();
				 Group group = new Group();
				 group.setGroupId(viewUsser.getGroupId());
				 group.setGroupName(viewUsser.getGroupName());
				 userGroups.add(group);
				 user.setUserGroups(userGroups);
				 users.add(user);
			 }
			// System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			 
		 }
		return users;
	}
	public   User isUserEquals(List<User> list,String str) throws BaseDaoException {
		  for (int i = 0; i < list.size(); i++) {
		   if (str.equals(list.get(i).getUserCode().toString())) {
		    return (User)list.get(i);
		   }
		  }
		  return null;
	}
	public Group isGroupEquals(Set<Group> userGroups,int str) throws BaseDaoException  {
		Iterator<Group> iter = userGroups.iterator();
		while(iter.hasNext()){
			Group group =(Group)iter.next();
			//System.out.println("!!!!!!groupId "+group.getGroupId()+"groupName "+group.getGroupName());
			//System.out.println("str "+str);
			if(group.getGroupId()==str){
				return group;
			}
		}
		return null;
	}
	public ViewUserInfo getUserByUserCode(String userCode) throws BaseDaoException {
		// TODO Auto-generated method stub
		return null;
	}

	 
	public ViewUserInfoDao getViewUserInfoDao() {
		return viewUserInfoDao;
	}

	public void setViewUserInfoDao(ViewUserInfoDao viewUserInfoDao) {
		this.viewUserInfoDao = viewUserInfoDao;
	}


}
