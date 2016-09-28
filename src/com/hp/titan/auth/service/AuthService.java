package  com.hp.titan.auth.service;

import java.util.List;

import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseServiceException;
import com.hp.titan.auth.model.Auth;


public interface AuthService {
	/**
	 * 保存权限
	 * 
	 * @param auth
	 * @return
	 */
	public void saveAuth(Auth auth) throws BaseDaoException;

	 

	/**
	 * 查询一个权限
	 * 
	 * @param authId
	 * @return
	 */
	public Auth getAuthById(String authId) throws BaseDaoException;

	/**
	 * 查询所有权限
	 * 
	 * @return
	 */
	public List<Auth> getAllAuth(Auth authVo) throws BaseDaoException;

	/**
	 * 通过菜单ID列表查询权限列表
	 */
	public List<Auth> findAllAuthByMenuId(List<Integer> menuIdList) throws BaseDaoException;

	/**
	 * 给角色增加权限
	 * 
	 * @param roleId
	 * @param authId
	 * @return
	 */
	public void addAuthToRole(String roleId, String[] authIds) throws BaseDaoException;

	/**
	 * 从一个角色中删除权限
	 * 
	 * @param roleId
	 * @param authId
	 */
	public void deleteAuthFromRole(String roleId, String[] authIds) throws BaseDaoException;
	
    /**
     * 方法说明：根据id批量删除Auth
     * @param idAry
     * @throws BaseServiceException
     */
	public void deleteAuthList(String[] idAry) throws BaseDaoException;
	
	/**
	 * 方法说明：根据id批量查询Auth
	 * @param idList
	 * @return
	 */
	public List<Auth> findAuthByIdList(List<String> idList) throws BaseDaoException;

}
