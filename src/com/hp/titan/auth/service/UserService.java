package com.hp.titan.auth.service;

import java.util.List;

import com.hp.app.common.page.Page;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseServiceException;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.model.UserProject;
import com.hp.titan.auth.vo.UserVo;

public interface UserService {

	/**
	 * 获得所有用户带分页
	 * @return
	 */
	public Page getAllUserByPage(UserVo userVo,Page page) throws BaseDaoException ;
	
	/**
	 * 根据查询条件获得所有用户
	 * @return
	 */
	
	public List<User> getAllUserListForSync()throws BaseDaoException;
	
	public List<User> getAllUserByUserVo(UserVo userVo) throws BaseDaoException ;
	
	public List<User> getAllUserByGroupId(String groupId) throws BaseDaoException ;
	
	public List<User> getUserListByGroup(String groupId) throws BaseDaoException;
	
	public List<User> getManagerByGroup(String groupId) throws BaseDaoException;
	
	/**
	 * 刪除用户
	 * 
	 * @return
	 */
	public void deleteUserList(int userId) throws BaseDaoException;
	
	/**
	 * 保存用户
	 * 
	 * @param auth
	 * @return
	 */
	public void saveUser(User user) throws BaseDaoException;
	
	/**
	 * 修改用户
	 * 
	 * @param auth
	 * @return
	 */
	public void updateUser(User user) throws BaseDaoException;
	
	/**
	 * 根据用户ID查询用户基本信息
	 * 
	 * @param userId
	 *            用户ID
	 */
	public User getUserById(int userId) throws BaseDaoException;

	/**
	 * 根据用户KM用户名查询用户基本信息
	 * 
	 * @param userCode
	 *            用户KM用户名
	 */
	public User getUserByCode(String userCode) throws BaseDaoException;

	/**
	 * 根据用户KM用户名查询用户基本信息
	 * 
	 * @param userCode
	 *            用户KM用户名
	 */
	public boolean isExistUserCode(String userCode) throws BaseDaoException;
	

	/**
	 * 方法说明：根据id序列返回用户对象集合
	 * @param ids  
	 * @return
	 * @throws BaseServiceException
	 */
	List<User> findbyIdList(List<Integer> ids ) throws BaseDaoException;
	
	/**
	 * 判断用户邮件是否存在
	 * 
	 * @param userCode
	 *            用户KM用户名
	 */
	public boolean isExistUserEmail(String email) throws BaseDaoException ;
	
	/**
	 * 根据用户邮件查人员信息
	 * @param email
	 * @return
	 */
	public User getUserByEmail(String email) throws BaseDaoException;

	/**
	 * 
	 * @Title: checkUserAndPassword 
	 * @Description: TODO 
	 * @param upperCase
	 * @return String
	 */
	public String checkUserAndPassword(String upperCase,String password) throws BaseDaoException;
	
	public void saveUserProject(UserProject userProject) throws BaseDaoException;
	
	public void deleteUserProject(Integer userId) throws BaseDaoException;
	
	public void setDefaultProject(Integer userId, String defaultProjectId) throws BaseDaoException;
	
}
