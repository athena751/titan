package com.hp.titan.auth.dao;



import java.util.List;

import org.hibernate.Query;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.auth.model.Group;
import com.hp.titan.auth.model.RoleAuth;
import com.hp.titan.auth.model.RoleAuthId;
import com.hp.titan.auth.model.UserGroup;
import com.hp.titan.auth.model.UserGroupId;


public class GroupUserDao extends DefaultBaseDao<UserGroup, String>
{
	/**
	 * 給用戶添加到組
	 */
	public void andGroupUser(Integer userId,Integer groupId) throws BaseDaoException
	{
		UserGroup groupUser=new UserGroup();
		UserGroupId guid=new UserGroupId();
		guid.setGroupId(groupId);
		guid.setUserId(userId);
		groupUser.setId(guid);
		List<UserGroup> userGroupList =this.findGroupUserById(userId,groupId);
		if(userGroupList.size()==0){
			this.getHibernateTemplate().save(groupUser);
		}
		
	}
	/**
	 * 删除組中用戶
	 */
	public void deleteGroupUser(Integer userId,Integer groupId) throws BaseDaoException
	{
		UserGroup groupUser=new UserGroup();
		UserGroupId guid=new UserGroupId();
		Group  group = new Group();
		group.setGroupId(groupId);
		guid.setGroupId(groupId);
		guid.setUserId(userId);
		groupUser.setId(guid);
		groupUser.setGroup(group);
		this.getHibernateTemplate().delete(this.getHibernateTemplate().get(UserGroup.class, guid));
	}

	public List<UserGroup> findGroupUserById(Integer userId,Integer groupId) throws BaseDaoException{
		String hql ="from UserGroup usergroup where usergroup.id.userId =:userId and usergroup.id.groupId =:groupId ";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("userId", userId);
		query.setParameter("groupId", groupId);
		List<UserGroup> userGroupList =  query.list();
		return userGroupList;
	}
	
	public UserGroup findGroupUserById(Integer userId) throws BaseDaoException{
		String hql ="from UserGroup usergroup where usergroup.id.userId =:userId ";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("userId", userId);
		List<UserGroup> userGroupList =  query.list();
		return userGroupList.size()>0?userGroupList.get(0):null;
	}
	
	
	/**
	 * 更改用户用户组
	 * 
	 */
	public void updateUserGroup(Integer userId, Integer oldGroupId,Integer newGroupId) throws BaseDaoException
	{
		String hql = "update UserGroup userGroup set userGroup.group.groupId=? where userGroup.id.userId = ? and userGroup.group.groupId=?";

		this.getHibernateTemplate().bulkUpdate(hql,new Object[]{newGroupId,userId,oldGroupId});
	}
	 
}
