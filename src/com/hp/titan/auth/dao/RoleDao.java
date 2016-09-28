package com.hp.titan.auth.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.Query;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.auth.model.Role;
import com.hp.titan.common.constants.TitanContent;
import com.hp.titan.common.util.UserSessionUtil;



public class RoleDao extends DefaultBaseDao<Role, String> {
	   
	/**
	 * 根据用户KM用户名 查询用户拥有的角色
	 */
	@SuppressWarnings("unchecked")
	public List<Role> getRoleByUserCode(String userCode)  throws BaseDaoException{
		String hql = "select t.role from UserRole t where t.id.userCode = ? ";
		return this.getHibernateTemplate().find(hql, userCode);
	}
	
	
	/**
	 * 根据用户KM用户名 查询用户拥有的角色名称
	 */
	@SuppressWarnings("unchecked")
	public List<String> getRoleNameByUserCode(String userCode)  throws BaseDaoException{
		String hql = "select t.role.roleName from UserRole t where t.id.userCode = ? ";
		return this.getHibernateTemplate().find(hql, userCode);
	}
	
	/**
	 * @param obj
	 * @throws BaseDaoException
	 */
	public void saveOrUpdate(Role obj) throws BaseDaoException{
		this.getSession().saveOrUpdate(this.getSession().merge(obj));
	}
	/**
	 * 方法说明：查询所有的role
	 * @return
	 * @throws BaseDaoException
	 */
	public List<Role> findAll() throws BaseDaoException{
		String hql = "from Role where isValid=0  ";
		if(null != UserSessionUtil.getUser() && !UserSessionUtil.getUser().getUserCode().equals("ADMIN")){
			hql=hql+" and roleName!='"+TitanContent.ROLE_ADMIN+"'";
		}
		return this.getHibernateTemplate().find(hql); 
	}

	/**
	 * 方法说明：根据id得到role以及user集合
	 * @param roleId
	 * @return
	 * @throws BaseDaoException
	 */
	public Role findRoleAndUserList(String roleId) throws BaseDaoException{
		String hql = "from Role as role where role.roleId in (:roleId)";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roleId", roleId);
		List<Role> roleList =  query.list();
		if(roleList.size() > 0){
			Role  role = (Role)roleList.get(0);
			Set tmp = role.getUserRoles();
			/*****  获取延迟加载的用户集合 *******/
			int size = tmp.size();
			return role;
		}
		return null;
	}
	
	
	/**
	 * 方法说明：根据id得到role以及auth集合
	 * @param roleId
	 * @return
	 * @throws BaseDaoException
	 */
	public Role findRoleAndAuthList(String roleId) throws BaseDaoException{
		String hql = "from Role as role where role.roleId =:roleId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roleId", roleId);
		List<Role> roleList =  query.list();
		if(roleList.size() > 0){
			Role  role = (Role)roleList.get(0);
			Set tmp = role.getAuths();
			//*****  获取延迟加载的用户集合 *******//
			int size = tmp.size();
			return role;
		}
		return null;
	}
	
	public Role findById(String id) throws BaseDaoException
	  {
	    return (Role) getHibernateTemplate().get(Role.class, id);
	  }
	
	  public Role read(String id)
	  {
	    return (Role) getHibernateTemplate().get(Role.class, id);
	  }
	  
	  /**
	   * 更具角色名称查角色
	   * @param roleName
	   * @return
	   */
		public Role getRoleByName(String roleName) throws BaseDaoException {
			String hql = "from Role as role where role.roleName =:roleName";
			Query query = this.getSession().createQuery(hql);
			query.setParameter("roleName", roleName);
			List<Role> roleList =  query.list();
			if(roleList.size() > 0){
				Role  role = (Role)roleList.get(0);
				return role;
			}
			return null;
		}
}
