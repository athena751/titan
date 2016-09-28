package com.hp.titan.auth.service;

import java.sql.Timestamp;
import java.util.List;

import com.hp.app.exception.BaseDaoException;
import com.hp.titan.auth.model.Auth;
import com.hp.titan.auth.model.Group;
import com.hp.titan.auth.model.UserGroup;


public interface GroupService {
	/**
	 * 保存用户组
	 * 
	 * @param auth
	 * @return
	 */
	public void saveGroup(Group group) throws BaseDaoException;

	 

	/**
	 * 查询一个用户组
	 * 
	 * @param authId
	 * @return
	 */
	public Group getGroupById(Integer groupId) throws BaseDaoException;

	/**
	 * 查询所有用户组
	 * 
	 * @return
	 */
	public List<Group> getAllGroupList()throws BaseDaoException;

	/**
	 * 刪除組
	 * 
	 * @return
	 */
	public void deleteGroupList(int groupId) throws BaseDaoException;

	/**
	 * 给组增加用户
	 * 
	 * @param roleId
	 * @param authId
	 * @return
	 */
	public void addUserToGroup(Integer groupId, Integer[] userIds) throws BaseDaoException;

	/**
	 * 从一个组中删除用户
	 * 
	 * @param roleId
	 * @param authId
	 */
	public void deleteUserFromGroup(Integer groupId, Integer[] userIds) throws BaseDaoException;
	
	
	/**
	 * 方法说明：根据id得到group以及user集合
	 * @param roleId
	 * @return
	 * @throws BaseDaoException
	 */
	public Group findGroupAndUserList(Integer groupId) throws BaseDaoException;
	
	
	public UserGroup findGroupUserById(Integer userId) throws BaseDaoException;
}
