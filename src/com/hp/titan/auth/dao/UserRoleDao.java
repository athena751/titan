package com.hp.titan.auth.dao;

import java.util.List;

import org.hibernate.Query;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.auth.model.Role;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.model.UserRole;
import com.hp.titan.auth.model.UserRoleId;


public class UserRoleDao extends DefaultBaseDao<UserRole, String>
{
	
	/**
	 * 添加一个用户的角色
	 */
	public void andUserRole(Integer userId,String roleId) throws BaseDaoException
	{	
		List<UserRole> userRoleList =this.findUserRoleById(userId,roleId);
		if(userRoleList.size()==0){
			UserRole userRole=new UserRole();
			UserRoleId urid=new UserRoleId();
			urid.setUserId(userId);
			urid.setRoleId(roleId);
			userRole.setId(urid);
			this.getHibernateTemplate().save(userRole);
		}
	}

	public List<UserRole> findUserRoleById(Integer userId,String roleId) throws BaseDaoException{
		String hql ="from UserRole userrole where userrole.id.userId =:userId and userrole.id.roleId =:roleId ";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("userId", userId);
		query.setParameter("roleId", roleId);
		List<UserRole> userRoleList =  query.list();
		return userRoleList;
	}
	
	public List<User> findUserByRoleName(String roleName) throws BaseDaoException{
		String hql ="select userrole.user from UserRole userrole where userrole.role.roleName =:roleName ";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roleName", roleName);
		List<User> userList =  query.list();
		return userList;
	}
	

	/**
	 * 删除一个用户的角色
	 */
	public void deleteUserRole(Integer userId,String roleId) throws BaseDaoException
	{
		UserRole userRole=new UserRole();
		UserRoleId urid=new UserRoleId();
		urid.setRoleId(roleId);
		urid.setUserId(userId);
		userRole.setId(urid);
		this.getHibernateTemplate().delete(this.getHibernateTemplate().get(UserRole.class, urid));
	}
	
	
	public void saveUserRole(List<UserRole> userRoleList) throws BaseDaoException
	{
		for (UserRole userRole : userRoleList)
		{
			//save(userRole);
		}
	}
	

	
  
	/**
	 * 判断用户是否有指定的权限
	 * 
	 * @param userCode
	 * 
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	public boolean hasRoleByUserCode(String userCode, String roleName) throws BaseDaoException
	{
		String hql = "select r.role from UserRole r where  r.id.userCode=? and r.role.roleName=?";
		List<Role> roleList = this.getHibernateTemplate().find(hql,
				new Object[]
				{ userCode, roleName });
		return roleList.size() == 0 ? false : true;
	}

	/**
	 * 更改用户角色
	 * 
	 */
	public void updateRole(Integer userId, String oldRoleId,String newRoleId) throws BaseDaoException
	{
		String hql = "update UserRole userRole set userRole.role.roleId=? where userRole.id.userId = ? and userRole.role.roleId=?";

		this.getHibernateTemplate().bulkUpdate(hql,new Object[]{newRoleId,userId,oldRoleId});
	}
}
