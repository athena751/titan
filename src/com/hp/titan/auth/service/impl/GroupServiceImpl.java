package com.hp.titan.auth.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.hp.app.common.util.DateUtils;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.auth.dao.GroupDao;
import com.hp.titan.auth.dao.GroupUserDao;
import com.hp.titan.auth.dao.UserDao;
import com.hp.titan.auth.model.Auth;
import com.hp.titan.auth.model.Group;
import com.hp.titan.auth.model.Role;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.model.UserGroup;
import com.hp.titan.auth.service.GroupService;
import com.hp.titan.common.util.UserSessionUtil;

 

public class GroupServiceImpl implements GroupService {

	private GroupDao groupDao;
	private GroupUserDao groupUserDao;
	public void addUserToGroup(Integer groupId, Integer[] userIds) throws BaseDaoException {
		for (int i = 0; i < userIds.length; i++)
		{
			groupUserDao.andGroupUser(userIds[i], groupId);
		}
	}

	public void deleteUserFromGroup(Integer groupId, Integer[] userIds) throws BaseDaoException {
		for (int i = 0; i < userIds.length; i++)
		{
			groupUserDao.deleteGroupUser(userIds[i], groupId);
		}
	}

	public void deleteGroupList(int groupId) throws BaseDaoException {
		// TODO Auto-generated method stub
		Integer userId=Integer.valueOf(UserSessionUtil.getUser().getUserId());
		Timestamp date= DateUtils.getCurrentDate();
//		int j = groupIdAry.length; 
//		for (int i = 0; i < j; ++i) { 
//			Integer id = groupIdAry[i];
				try {
					Group group = groupDao.read(groupId);
					Iterator userIter = group.getUsers().iterator();
					while(userIter.hasNext()){
						User user =(User)userIter.next();
						groupUserDao.deleteGroupUser(user.getUserId(), group.getGroupId());
					}
					group.setIsValid(1);
					group.setLastUpdateUser(userId);
					group.setLastUpdate_Date(date);
					groupDao.update(group);//.delete(group);
				} catch (BaseDaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//		}
	}
	
	public List<Group> getAllGroupList() throws BaseDaoException {
		return groupDao.findAllGroup();
	}

	public Group getGroupById(Integer groupId) throws BaseDaoException {
		return groupDao.findById(groupId);
	}

	public void saveGroup(Group group) throws BaseDaoException {
		groupDao.saveOrUpdate(group);
	}


	public Group findGroupAndUserList(Integer groupId) throws BaseDaoException {
		// TODO Auto-generated method stub
		return groupDao.findGroupAndUserList(groupId);
	}
	
	public UserGroup findGroupUserById(Integer userId) throws BaseDaoException{
		return groupUserDao.findGroupUserById(userId);
	}
	
	
	public GroupDao getGroupDao() {
		return groupDao;
	}

	public void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	public GroupUserDao getGroupUserDao() {
		return groupUserDao;
	}

	public void setGroupUserDao(GroupUserDao groupUserDao) {
		this.groupUserDao = groupUserDao;
	}
	


	 

}
