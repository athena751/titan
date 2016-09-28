package com.hp.titan.auth.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.hp.app.common.util.DateUtils;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseServiceException;
import com.hp.app.service.DefaultBaseService;
import com.hp.titan.auth.dao.AuthDao;
import com.hp.titan.auth.dao.MenuDao;
import com.hp.titan.auth.dao.RoleAuthDao;
import com.hp.titan.auth.dao.RoleDao;
import com.hp.titan.auth.dao.UserDao;
import com.hp.titan.auth.dao.UserRoleDao;
import com.hp.titan.auth.model.Auth;
import com.hp.titan.auth.model.Role;
import com.hp.titan.auth.model.RoleAuth;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.service.RoleService;
import com.hp.titan.common.util.UserSessionUtil;
 

public class RoleServiceImpl extends DefaultBaseService<Role, String> implements RoleService {

	private RoleDao roleDao;
	private AuthDao authDao;
	private RoleAuthDao roleAuthDao ;
	private UserDao userDao;
	private UserRoleDao userRoleDao;
	private MenuDao menuDao;
	

	

	/**
	 * 方法说明：查询所有的role
	 * @return
	 * @throws BaseServiceException
	 */
	public List<Role> findAll() throws BaseDaoException {
		return roleDao.findAll();
		
	}

	public void addRoleByUserCode(String[] userCodeArr, String roleId) throws BaseDaoException {
		// TODO Auto-generated method stub
		
	}

	public void delRoleByUserCode(String[] userCodeArr, String roleId) throws BaseDaoException {
		// TODO Auto-generated method stub
		
	}

	public void deleteList(int roleId) throws BaseDaoException {
		// TODO Auto-generated method stub
		Integer userId=Integer.valueOf(UserSessionUtil.getUser().getUserId());
		Timestamp date= DateUtils.getCurrentDate();
//		int j = idAry.length; 
//		for (int i = 0; i < j; ++i) { 
//			String id = idAry[i];
				Role role = roleDao.read(Integer.toString(roleId));
				/******* 删除角色以及角色和权限的关联关系 ******/
				Iterator authIter = role.getAuths().iterator();
				while(authIter.hasNext()){
					Auth auth =(Auth)authIter.next();
					roleAuthDao.deleteRoleAuth(auth.getAuthId(), role.getRoleId());
				}
				/******* 删除角色以及角色和用户的关联关系 ******/
				Iterator userIter = role.getUserRoles().iterator();
				while(userIter.hasNext()){
					User user =(User)userIter.next();
					userRoleDao.deleteUserRole(user.getUserId(), role.getRoleId());
				}	
				try {
					role.setIsValid(1);
					role.setLastUpdate_Date(date);
					role.setLastUpdateUser(userId);
					roleDao.update(role);//.delete(role);
					
				} catch (BaseDaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//      }
	}

	public Role findRoleAndUserList(String roleId) throws BaseDaoException {
		// TODO Auto-generated method stub
		return roleDao.findRoleAndUserList(roleId);
	}

	public List<Role> getAllRole() throws BaseDaoException {
		// TODO Auto-generated method stub
		return null;
	}

	public Role getRole(String key) throws BaseDaoException {
		// TODO Auto-generated method stub
		return roleDao.findById(key);
	}

	public Role getRoleById(String roleId) throws BaseDaoException {
		// TODO Auto-generated method stub
		return null;
	}

	public Role getRoleByName(String roleName) throws BaseDaoException {
		// TODO Auto-generated method stub
		return roleDao.getRoleByName(roleName);
	}

	public List<Role> getRoleByUserCode(String userCode) throws BaseDaoException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getRoleNameByUserCode(String userCode) throws BaseDaoException {
		// TODO Auto-generated method stub
		return null;
	}

	public void save(Role role) throws BaseDaoException {
		// TODO Auto-generated method stub
			roleDao.saveOrUpdate(role);
	}

 

	public String create(Role arg0)  {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(Role arg0) {
		// TODO Auto-generated method stub
		
	}

	public Role read(String arg0, String... arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public Role findRoleAndAuthList(String roleId) throws BaseDaoException {
		// TODO Auto-generated method stub
		Role role =roleDao.findRoleAndAuthList(roleId);
		return role;
	}

	public void doAddAuthToRole(String[] userIdAry,String roleId ) throws BaseDaoException {
		/******** 在关联前 ，先查询用户，看是否存在 *********/
		List<String> idList = new ArrayList<String>();
		for (int i = 0; userIdAry.length > i; i++) {
			idList.add(userIdAry[i]);
		}
		/******** 根据用户id序列查询用户列表 *********/
		List<Auth> authList = authDao.findAuthByIdList(idList);
		for(int i =0;i<authList.size();i++){
			roleAuthDao.andRoleAuth(authList.get(i).getAuthId(), roleId);
		}
	}
	public void doDeleteRoleAuth(List<String> idList, String roleId) throws BaseDaoException {
		// TODO Auto-generated method stub
	 
		/******** 根据用户id序列查询用户列表 *********/
		List<Auth> authList = authDao.findAuthByIdList(idList);
		for(int i =0;i<authList.size();i++){
			roleAuthDao.deleteRoleAuth(authList.get(i).getAuthId(), roleId);
		}
	}
	
	@Override
	public void doSaveMenuRoleAuth(String[] authIdAry, List<Integer> menuIdList,
			String roleId) throws BaseDaoException {
		List<Auth> authList = authDao.findAllAuthByMenuId(menuIdList);
		for(int i =0;i<authList.size();i++){
			RoleAuth roleAuth =roleAuthDao.findRoleAuthByAuthIdAndRoleId(authList.get(i).getAuthId(), roleId);
			if(roleAuth!=null){
				try {
					roleAuthDao.delete(roleAuth);
				} catch (BaseDaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		if(authIdAry!=null){
			List<String> idList = new ArrayList<String>();
			for (int i = 0; authIdAry.length > i; i++) {
				idList.add(authIdAry[i]);
			}
			List<Auth> authAndList = authDao.findAuthByIdList(idList);
			for(int i =0;i<authAndList.size();i++){
				roleAuthDao.andRoleAuth(authAndList.get(i).getAuthId(), roleId);
			}
		}
	}
	
	public void doAddUserToRole(String[] userIdAry,Role role) throws BaseDaoException {
		/******** 在关联前 ，先查询用户，看是否存在 *********/
		List<Integer> idList = new ArrayList<Integer>();
		for (int i = 0; userIdAry.length > i; i++) {
			idList.add(Integer.parseInt(userIdAry[i]));
		}
		/******** 根据用户id序列查询用户列表 *********/
		List<User> userList = userDao.findUserByIdList(idList);
		for(int i =0;i<userList.size();i++){
			userRoleDao.andUserRole(userList.get(i).getUserId(), role.getRoleId());
		}
	}
	
	public void doDeleteRoleUser(String[] userIdAry, Role role) throws BaseDaoException {
		// TODO Auto-generated method stub
		List<Integer> idList = new ArrayList<Integer>();
		for (int i = 0; userIdAry.length > i; i++) {
			idList.add(Integer.parseInt(userIdAry[i]));
		}
		/******** 根据用户id序列查询用户列表 *********/
		List<User> userList = userDao.findUserByIdList(idList);
		for(int i =0;i<userList.size();i++){
			userRoleDao.deleteUserRole(userList.get(i).getUserId(), role.getRoleId());
		}
	}
	
	public List<User> findUserByRoleName(String roleName) throws BaseDaoException{
		return userRoleDao.findUserByRoleName(roleName);
	}
	
	

	
	public AuthDao getAuthDao() {
		return authDao;
	}

	public void setAuthDao(AuthDao authDao) {
		this.authDao = authDao;
	}
	
	public RoleAuthDao getRoleAuthDao() {
		return roleAuthDao;
	}

	public void setRoleAuthDao(RoleAuthDao roleAuthDao) {
		this.roleAuthDao = roleAuthDao;
	}

	public RoleDao getRoleDao() {
		return roleDao;
	}

	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public UserRoleDao getUserRoleDao() {
		return userRoleDao;
	}

	public void setUserRoleDao(UserRoleDao userRoleDao) {
		this.userRoleDao = userRoleDao;
	}


	public MenuDao getMenuDao() {
		return menuDao;
	}

	public void setMenuDao(MenuDao menuDao) {
		this.menuDao = menuDao;
	}

}
