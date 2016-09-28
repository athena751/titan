package  com.hp.titan.auth.service;

import java.util.List;

import com.hp.app.exception.BaseDaoException;
import com.hp.titan.auth.model.Menu;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.model.ViewMenuTree;


public interface MenuService {
	/**
	 * 保存菜单
	 * 
	 * @param auth
	 * @return
	 */
	public void saveMenu(Menu menu) throws BaseDaoException;

	 

	/**
	 * 查询一个菜单
	 * 
	 * @param MenuId
	 * @return
	 */
	public Menu getMenuById(Integer menuId) throws BaseDaoException;

	/**
	 * 查询所有菜单
	 * 
	 * @return
	 */
	public List<ViewMenuTree> getAllMenuTree() throws BaseDaoException;

	/**
	 * 查询当前用户的菜单
	 * @param user
	 * @return
	 */
	public List<ViewMenuTree> getUserMenuList(User user) throws BaseDaoException;

}
