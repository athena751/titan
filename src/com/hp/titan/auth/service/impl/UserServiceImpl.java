package com.hp.titan.auth.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.cxf.common.util.StringUtils;

import com.hp.app.common.page.Page;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseServiceException;
import com.hp.titan.auth.dao.AuthDao;
import com.hp.titan.auth.dao.GroupDao;
import com.hp.titan.auth.dao.GroupUserDao;
import com.hp.titan.auth.dao.RoleDao;
import com.hp.titan.auth.dao.UserDao;
import com.hp.titan.auth.dao.UserRoleDao;
import com.hp.titan.auth.model.Group;
import com.hp.titan.auth.model.Role;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.model.UserProject;
import com.hp.titan.auth.service.UserService;
import com.hp.titan.auth.vo.UserVo;
import com.hp.titan.common.constants.TitanContent;
import com.hp.titan.common.util.Encode;
import com.hp.titan.project.dao.ProjectDao;
 

 

/**
 * @author kevin
 *
 */
public class UserServiceImpl implements UserService {


	private UserDao userDao;
	private GroupDao groupDao;
	private RoleDao roleDao;
	private UserRoleDao userRoleDao;
	private GroupUserDao groupUserDao;
	private AuthDao authDao;
	private ProjectDao projectDao;
	public void deleteUserList(int userId) throws BaseDaoException {
		// TODO Auto-generated method stub
//		int j = userIdAry.length; 
//		for (int i = 0; i < j; ++i) { 
//			Integer id = userIdAry[i];
				try {
					User user = userDao.read(userId);
					Iterator groupIter = user.getUserGroups().iterator();
					if(groupIter.hasNext()){
						Group group =(Group)groupIter.next();
						groupUserDao.deleteGroupUser(userId, group.getGroupId());
					}
					Iterator roleIter = user.getUserRoles().iterator();
					if(roleIter.hasNext()){
						Role role = (Role)roleIter.next();
						userRoleDao.deleteUserRole(userId, role.getRoleId());
					}
					user.setIsValid(1);
					userDao.update(user);//delete(user);
				} catch (BaseDaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	//	}
	}
	public void saveUser(User user) throws BaseDaoException {
		// TODO Auto-generated method stub
		userDao.saveOrUpdate(user);
	}
	
	/**
	 * 执行修改用户信息
	 */
	public void updateUser(User user) throws BaseDaoException {
		// TODO Auto-generated method stub
		User userUpdate =userDao.findById(user.getUserId());
		userUpdate.setUserCode(user.getUserCode());
		userUpdate.setMail(user.getMail());
		userUpdate.setPassword(user.getPassword());
		userUpdate.setRemark(user.getRemark());
		Set<Group> userGroups  = userUpdate.getUserGroups();
		Iterator groupIter = userGroups.iterator();
		if(groupIter.hasNext()){
			while(groupIter.hasNext()){
				Group group = (Group)groupIter.next();
				groupUserDao.updateUserGroup(user.getUserId(), group.getGroupId(), user.getGroupId());
			}
		}else{
			if(user.getGroupId()!=null){
				groupUserDao.andGroupUser(user.getUserId(), user.getGroupId());
			}
		}
		
		Set<Role> userRoles  = userUpdate.getUserRoles();
		Iterator roleIter = userRoles.iterator();
		if(roleIter.hasNext()){
			while(roleIter.hasNext()){
				Role role =(Role)roleIter.next();
				userRoleDao.updateRole(user.getUserId(), role.getRoleId(), user.getRoleId());
			}
		}else{
			if(user.getRoleId()!=null){
				userRoleDao.andUserRole(user.getUserId(), user.getRoleId());
			}
		}
		
		try {
			userDao.update(userUpdate);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String checkUserAndPassword(String upperCase,String password) throws BaseDaoException {
		User user =getUserByCode(upperCase);
		String rtn ="";
		if(user!=null){
			if(TitanContent.USER_STATUS_APPLYING.equals(user.getStatus())){
				rtn = "login_not_approve";
			}
			else{
				String userPasswor=user.getPassword();
				if(!StringUtils.isEmpty(userPasswor) && userPasswor.equals(Encode.getEncode(password))){
					rtn="login_sucess"; 
				}else{
					rtn="login_password_error";
				}
			}
		}else{
			rtn="login_user_null";
		}
		return rtn;
	}	

	 
	/**
	 * 方法说明：根据id序列返回用户对象集合
	 * @param ids  
	 * @return
	 * @throws BaseServiceException
	 */
	public List<User> findbyIdList(List<Integer> ids) throws BaseDaoException{
			return userDao.findbyIdList(ids);
	}
	 
	public User getUserById(int userId) throws BaseDaoException {
		// TODO Auto-generated method stub
		return userDao.findById(userId);
	}
	 
	public Page getAllUserByPage(UserVo userVo, Page page) throws BaseDaoException {
		// TODO Auto-generated method stub
		return userDao.getAllUserByPage(userVo, page);
	}
	
	public List<User> getAllUserListForSync()throws BaseDaoException{
		return userDao.getAllUserListForSync();
	}
	 
	public List<User> getAllUserByUserVo(UserVo userVo) throws BaseDaoException {
		// TODO Auto-generated method stub
		return userDao.getAllUserByUserVo(userVo);
	}
	
	public List<User> getAllUserByGroupId(String groupId) throws BaseDaoException {
		return userDao.getAllUserByGroupId(groupId);
	}
	
	public List<User> getUserListByGroup(String groupId) throws BaseDaoException {
		return userDao.getUserListByGroup(groupId);
	}
	
	public User getUserByCode(String userCode) throws BaseDaoException {
		// TODO Auto-generated method stub
		User user =userDao.getUserByUserCode(userCode);
		//List<Role> role = roleDao.getRoleByUserCode(user.getUserCode());
		return user;
	}
 
	public boolean isExistUserCode(String userCode) throws BaseDaoException {
		// TODO Auto-generated method stub
//		User user = userDao.getUserByUserCode(userCode);
//		if(user==null){
//			return false;
//		}else{
//			return true;
//		}
		return userDao.ifUserCodeExiest(userCode);
	}
	
	public boolean isExistUserEmail(String email) throws BaseDaoException {
		// TODO Auto-generated method stub
//		User user = userDao.getUserByEmail(email);
//		if(user==null){
//			return false;
//		}else{
//			return true;
//		}
		return userDao.ifUserEmailExiest(email);
	}
	
	public User getUserByEmail(String email) throws BaseDaoException {
		// TODO Auto-generated method stub
		User user = userDao.getUserByEmail(email);
		return user;
	}
	
	public void saveUserProject(UserProject userProject) throws BaseDaoException{
		projectDao.saveUserProject(userProject);
	}
	
	public void deleteUserProject(Integer userId) throws BaseDaoException{
		projectDao.deleteUserProject(userId);
	}
	
	public void setDefaultProject(Integer userId, String defaultProjectId) throws BaseDaoException{
		User user = userDao.findById(userId);
		user.setDefaultprojectId(defaultProjectId);
		userDao.saveOrUpdate(user);
	}
	
	public List<User> getManagerByGroup(String groupId) throws BaseDaoException{
		return userDao.getManagerByGroup(groupId);
	}

	public UserDao getUserDao() {
		return userDao;
	}
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
 
	public GroupDao getGroupDao() {
		return groupDao;
	}
	public void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
	}
	public RoleDao getRoleDao() {
		return roleDao;
	}
	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}
	
	public GroupUserDao getGroupUserDao() {
		return groupUserDao;
	}
	public void setGroupUserDao(GroupUserDao groupUserDao) {
		this.groupUserDao = groupUserDao;
	}
	public AuthDao getAuthDao() {
		return authDao;
	}
	public void setAuthDao(AuthDao authDao) {
		this.authDao = authDao;
	}
	
	public UserRoleDao getUserRoleDao() {
		return userRoleDao;
	}
	public void setUserRoleDao(UserRoleDao userRoleDao) {
		this.userRoleDao = userRoleDao;
	}
	public ProjectDao getProjectDao() {
		return projectDao;
	}
	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}


}
