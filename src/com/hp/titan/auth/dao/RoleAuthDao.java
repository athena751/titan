package com.hp.titan.auth.dao;



import java.util.List;

import org.hibernate.Query;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.auth.model.Auth;
import com.hp.titan.auth.model.RoleAuth;
import com.hp.titan.auth.model.RoleAuthId;
import com.hp.titan.auth.model.UserRole;


public class RoleAuthDao extends DefaultBaseDao<RoleAuth, String>
{
	/**
	 * 添加一个用户的角色
	 */
	public void andRoleAuth(String authId,String roleId) throws BaseDaoException
	{
		List<RoleAuth> roleAuthList =this.findRoleAuthById(authId,roleId);
		if(roleAuthList.size()==0){
			RoleAuth roleAuth=new RoleAuth();
			RoleAuthId raid=new RoleAuthId();
			raid.setAuthId(authId);
			raid.setRoleId(roleId);
			roleAuth.setId(raid);
			this.getHibernateTemplate().save(roleAuth);
		}
	}
	/**
	 * 删除一个用户的角色
	 */
	public void deleteRoleAuth(String authId,String roleId) throws BaseDaoException
	{
		RoleAuth roleAuth=new RoleAuth();
		RoleAuthId raid=new RoleAuthId();
		Auth auth=new Auth();
		auth.setAuthId(authId);
		raid.setAuthId(authId);
		raid.setRoleId(roleId);
		roleAuth.setId(raid);
		roleAuth.setAuth(auth);
		this.getHibernateTemplate().delete(this.getHibernateTemplate().get(RoleAuth.class, raid));
	}

	/**
	 * 根据ID查询角色权限
	 * @param authId
	 * @param roleId
	 * @return
	 */
	public List<RoleAuth> findRoleAuthById(String authId,String roleId) throws BaseDaoException{
		String hql ="from RoleAuth roleAuth where roleAuth.id.authId =:authId and roleAuth.id.roleId =:roleId ";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("authId", authId);
		query.setParameter("roleId", roleId);
		List<RoleAuth> roleAuthList =  query.list();
		return roleAuthList;
	}
	
	public void deleteRoleAuthByAuthId(String authId) throws BaseDaoException{
		String hql ="from RoleAuth roleAuth where roleAuth.id.authId =:authId  ";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("authId", authId);
		List<RoleAuth> roleAuthList =  query.list();
		if(roleAuthList.size()>0){
			for(int i=0;i<roleAuthList.size();i++){
				try {
					this.delete(roleAuthList.get(i));
				} catch (BaseDaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 根据ID查询角色权限
	 * @param authId
	 * @param roleId
	 * @return
	 */
	public RoleAuth findRoleAuthByAuthIdAndRoleId(String authId,String roleId) throws BaseDaoException{
		String hql ="from RoleAuth roleAuth where roleAuth.id.authId =:authId and roleAuth.id.roleId =:roleId ";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("authId", authId);
		query.setParameter("roleId", roleId);
		List<RoleAuth> roleAuthList =  query.list();
		if(roleAuthList.size()>0){
			return roleAuthList.get(0);
		}else{
			return null;
		}
	
	}
	
}
