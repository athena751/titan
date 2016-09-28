package com.hp.titan.auth.service;

import java.util.List;

import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseServiceException;
import com.hp.app.service.IBaseService;
import com.hp.titan.auth.model.Role;
import com.hp.titan.auth.model.User;


public interface RoleService  extends IBaseService<Role, String> {
 

	/**
	 * 查询一个角色
	 * 
	 * @param roleId
	 * @return
	 */
	public Role getRoleById(String roleId) throws BaseDaoException;

	/**
	 * 查询所有角色
	 * 
	 * @return
	 */
	public List<Role> getAllRole() throws BaseDaoException;

	/**
	 * 将角色中的权限
	 * 
	 * @return
	 */
	public void doSaveMenuRoleAuth(String[] authIdAry,List<Integer> menuIdList,String roleId) throws BaseDaoException;
	/**
	 * 将权限添加到角色中
	 * 
	 * @return
	 */
	public void doAddAuthToRole(String[] userIdAry,String roleId) throws BaseDaoException;
	
	/**
	 * 将权限从角色中删除
	 * 
	 * @return
	 */
	public void doDeleteRoleAuth(List<String> idList,String roleId) throws BaseDaoException;
	
	
	/**
	 * 将用户添加到角色中
	 * 
	 * @return
	 */
	public void doAddUserToRole(String[] userIdAry,Role role) throws BaseDaoException ;
	/**
	 * 将用户从角色中删除
	 * 
	 * @return
	 */
	public void doDeleteRoleUser(String[] userIdAry, Role role) throws BaseDaoException ;
	
	
	
	/**
	 * 根据用户KM用户名 查询用户拥有的角色
	 */
	public List<Role> getRoleByUserCode(String userCode) throws BaseDaoException;

	 


	/**
	 * 根据KM用户名赋予一组人新角色
	 * 
	 * @param userCodeArr
	 * @param roleId
	 * @return
	 */
	 public void addRoleByUserCode(String[] userCodeArr,String roleId) throws BaseDaoException;

	/**
	 * 根据一组用户KM用户名 取消组用户角色
	 */
	public void delRoleByUserCode(String[] userCodeArr, String roleId) throws BaseDaoException;
	
	/**
	 * 根据roleName查询一个角色
	 * 
	 * @param roleId
	 * @return
	 */
	public Role getRoleByName(String  roleName) throws BaseDaoException;
	
	/**
	 * 根据用户KM用户名 查询用户拥有的角色名称
	 */
	public List<String> getRoleNameByUserCode(String userCode) throws BaseDaoException;
	
	
	
	/**
	 * 方法说明：查询所有的role
	 * @return
	 * @throws BaseServiceException
	 */
	List<Role> findAll() throws BaseDaoException;
	
    /**
     * 方法说明：增加role
     * @param role
     * @throws BaseServiceException
     */
    void save(Role role) throws BaseDaoException;
    
    /**
     * 方法说明：根据id批量删除role
     * @param idAry
     * @throws BaseServiceException
     */
    void deleteList(int roleId) throws BaseDaoException;
    
    /**
     * 方法说明：根据id得到role  但不包括关联的user集合
     * @param key
     * @return
     * @throws BaseServiceException
     */
    Role getRole(String key) throws BaseDaoException;
    
    
    /**
     * 方法说明：根据id得到role以及user集合
     * @param roleId
     * @return
     * @throws BaseServiceException
     */
    Role findRoleAndUserList(String roleId) throws BaseDaoException;
    
    /**
     * 方法说明：根据id得到role以及auth集合
     * @param roleId
     * @return
     * @throws BaseServiceException
     */
    Role findRoleAndAuthList(String roleId) throws BaseDaoException;
    
    /**
     * Get user list by user's role name
     * @param roleName
     * @return
     */
    List<User> findUserByRoleName(String roleName) throws BaseDaoException;
    
}
