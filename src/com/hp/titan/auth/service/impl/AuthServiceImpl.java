package com.hp.titan.auth.service.impl;

import java.sql.Timestamp;
import java.util.List;

import com.hp.app.common.util.DateUtils;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.auth.dao.AuthDao;
import com.hp.titan.auth.dao.MenuDao;
import com.hp.titan.auth.dao.RoleAuthDao;
import com.hp.titan.auth.dao.RoleDao;
import com.hp.titan.auth.model.Auth;
import com.hp.titan.auth.model.Menu;
import com.hp.titan.auth.model.Role;
import com.hp.titan.auth.service.AuthService;
import com.hp.titan.common.util.UserSessionUtil;
 

 

public class AuthServiceImpl implements AuthService
{

	private AuthDao authDao;
	private RoleDao roleDao;
	private RoleAuthDao roleAuthDao;
	private MenuDao menuDao;
	/**
	 * 查询所有的Auth
	 */
	public List<Auth> getAllAuth(Auth authVo) throws BaseDaoException
	{
		List<Auth> auths=authDao.findAll(authVo);
		for(Auth auth :auths){
			if(auth.getMenuId()!=null){
				Menu menu =	menuDao.findById(auth.getMenuId());
				auth.setMenu(menu);
			}
		}
		 return auths;
	}

	/**
	 * 通过菜单ID列表查询权限列表
	 */
	public List<Auth> findAllAuthByMenuId(List<Integer> menuIdList) throws BaseDaoException{
		List<Auth> auths=authDao.findAllAuthByMenuId(menuIdList);
		for(Auth auth :auths){
			if(auth.getMenuId()!=null){
				Menu menu =	menuDao.findById(auth.getMenuId());
				auth.setMenu(menu);
			}
		}
		return auths;
		
	}
	/**
	 * 查询一个权限
	 * 
	 * @param authId
	 *  
	 */
	public Auth getAuthById(String authId) throws BaseDaoException
	{
		 return authDao.findById(authId);
	}

	
	/**
	 * 保存权限
	 * 
	 * @param auth
	 * 
	 */
	public void saveAuth(Auth auth) throws BaseDaoException
	{
		 authDao.saveOrUpdate(auth);
	}
	
	/**
	 *  删除权限
	 */
	public void deleteAuthList(String[] idAry) throws BaseDaoException {
		// TODO Auto-generated method stub
		Integer userId=Integer.valueOf(UserSessionUtil.getUser().getUserId());
		Timestamp date= DateUtils.getCurrentDate();
		int j = idAry.length; 
		for (int i = 0; i < j; ++i) { 
			String id = idAry[i];
				try {
					Auth auth = authDao.read(id);
					roleAuthDao.deleteRoleAuthByAuthId(auth.getAuthId());
					auth.setIsValid(1);
					auth.setLastUpdate_Date(date);
					auth.setLastUpdateUser(userId);
					authDao.update(auth);//.delete(auth);
				} catch (BaseDaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	

	public RoleAuthDao getRoleAuthDao() {
		return roleAuthDao;
	}

	public void setRoleAuthDao(RoleAuthDao roleAuthDao) {
		this.roleAuthDao = roleAuthDao;
	}

	/**
	 * 给角色增加权限
	 * 根据配置文件，由Auth来维护
	 */
	public void addAuthToRole(String roleId, String[] authIds) throws BaseDaoException
	{

		Role role = roleDao.findById(roleId);
		Auth auth = null;
		for (int i = 0; i < authIds.length; i++)
		{
			//auth = authDao.findById(authIds[i]);
			auth.getRoles().add(role);
		//	authDao.save(auth);
		}
	}
	
	
	/**
	 * 从一个角色中删除权限
	 */
	public void deleteAuthFromRole(String roleId, String[] authIds) throws BaseDaoException
	{
		Role role = roleDao.findById(roleId);
		Auth auth = null;
		for (int i = 0; i < authIds.length; i++)
		{
			auth = authDao.findById(authIds[i]);
			auth.getRoles().remove(role);
		}
	}
	
 
	public List<Auth> findAuthByIdList(List<String> idList) throws BaseDaoException {
		// TODO Auto-generated method stub
		return authDao.findAuthByIdList(idList);
	}
	
	public AuthDao getAuthDao()
	{
		return authDao;
	}

	public void setAuthDao(AuthDao authDao)
	{
		this.authDao = authDao;
	}

	public RoleDao getRoleDao()
	{
		return roleDao;
	}

	public void setRoleDao(RoleDao roleDao)
	{
		this.roleDao = roleDao;
	}


	public MenuDao getMenuDao() {
		return menuDao;
	}

	public void setMenuDao(MenuDao menuDao) {
		this.menuDao = menuDao;
	}
	

	

}
