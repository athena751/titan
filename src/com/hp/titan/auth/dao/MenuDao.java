package com.hp.titan.auth.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.auth.model.Auth;
import com.hp.titan.auth.model.Menu;
import com.hp.titan.auth.model.Role;
import com.hp.titan.auth.model.ViewMenu2Tree;


public class MenuDao extends DefaultBaseDao<Menu, String> {
	/**
	 * @param obj
	 * @throws BaseDaoException
	 */
	public void saveOrUpdate(Menu obj) throws BaseDaoException{
		this.getHibernateTemplate().saveOrUpdate(obj);
	}
	/**
	 * 方法说明：查询所有的Menu
	 * @return
	 * @throws BaseDaoException
	 */
	public List<Menu> findAllMenu() throws BaseDaoException{
	 
		String hql = "from Menu   where isValid=0 ";
		return this.getHibernateTemplate().find(hql);
	}
	
	public Menu findById(Integer id) throws BaseDaoException
	  {
	    return (Menu) getHibernateTemplate().get(Menu.class, id);
	  }
	
	/**
	 * 查询一级菜单
	 * @param idList
	 * @return
	 */
	public List<Menu> findOneMenu() throws BaseDaoException{
		String hql = "from Menu  where menuLevel=1 and isValid=0  order by menuId ";
		return this.getHibernateTemplate().find(hql);
	} 
	
	/**
	 * 查询一级下的二级菜单
	 * @param idList
	 * @return
	 */
	public List<ViewMenu2Tree> findSecoundMenuByOne(int oneMenuId,List idList) throws BaseDaoException{
		String hql = "from Menu  where menuLevel=2 and isValid=0  and supperMenuId=:oneMenuId  ";
		if(idList!=null){
			hql=hql+" and menuId in (:menuId)";
		}
		Query query =this.getSession().createQuery(hql);
		query.setParameter("oneMenuId", oneMenuId);
		if(idList!=null){
			query.setParameterList("menuId", idList);
		}
		
		List<Menu> secondMenuList =query.list();
		List<ViewMenu2Tree> menu2List = new ArrayList<ViewMenu2Tree>();
		for(Menu menu: secondMenuList){
			ViewMenu2Tree  viewMenu2Tree =new ViewMenu2Tree();
			viewMenu2Tree.setMenu2(menu);
			menu2List.add(viewMenu2Tree);
		}
		return menu2List;
	} 
	
	/**
	 * 查询二级下的三级菜单
	 * @param idList
	 * @return
	 */
	public List<Menu> findThirdMenuBySecond(int seconddMenuId,List idList) throws BaseDaoException{
		String hql = "from Menu  where menuLevel=3 and isValid=0  and supperMenuId=:seconddMenuId ";
		if(idList!=null){
			hql=hql+" and menuId in (:menuId)";
		}
		Query query =this.getSession().createQuery(hql);
		if(idList!=null){
			query.setParameterList("menuId", idList);
		}
		query.setParameter("seconddMenuId", seconddMenuId);
		return query.list();
	} 
	
	/**
	 * 查询用户的一级菜单
	 * @param userCode
	 * @return
	 */
	public List<Menu>  findOneMenuByUserCode(String userCode) throws BaseDaoException{
		String sql = "SELECT distinct(M.MENU_ID) as MENU_ID,M.MENU_LEVEL,M.MENU_CODE,M.MENU_NAME,M.SUPPER_MENU_ID,M.ACTION_URL,M.IMG_URL,M.MENU_ORDER " +
				" FROM titan_auth AS A" +
				",titan_user AS U" +
				",titan_user_role AS UR" +
				",titan_role_auth AS RA" +
				",titan_menu AS M" +
				" WHERE U.USER_CODE	='" +userCode.toUpperCase()+"'"+
				" AND U.USER_ID = UR.USER_ID" +
				" AND UR.ROLE_ID = RA.ROLE_ID" +
				" AND RA.AUTH_ID = A.AUTH_ID" +
				" AND M.MENU_ID = A.MENU_ID" +
				//"   --AND A.AUTH_TYPE = 'MENU'	" +
				" AND A.AUTH_NAME LIKE 'MENU%'" +
				" AND A.AUTH_NAME NOT LIKE '%BTN%'" +
				" AND M.MENU_LEVEL = 1" +
				" ORDER BY M.MENU_ORDER,MENU_ID;";
		Query query =this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		List<Menu> oneMenuList = new ArrayList<Menu>();
		for(Object[] object : objects){
			Menu menu = new Menu();
			menu.setMenuId(new Integer(object[0].toString()));
			menu.setMenuLevel(new Integer(object[1].toString()));
			menu.setMenuCode(object[2].toString());
			if(object[3]!=null){
				menu.setMenuName(object[3].toString());
			}
			if(object[4]!=null){
				menu.setSupperMenuId(new Integer(object[4].toString()));
			}
			if(object[5]!=null){
				menu.setActionUrl(object[5].toString());
			}
			if(object[6]!=null){
				menu.setImgUrl(object[6].toString());
			}
			oneMenuList.add(menu);
		}
		
		return oneMenuList;
	}
	
	
	
	
	/**
	 * 查询用户的一级菜单
	 * @param userCode
	 * @return
	 */
	public List<ViewMenu2Tree>  findSecoundMenuByUserCode(String userCode,int oneMenuId) throws BaseDaoException{
		String sql = "SELECT distinct(M.MENU_ID) as MENU_ID,M.MENU_LEVEL,M.MENU_CODE,M.MENU_NAME,M.SUPPER_MENU_ID,M.ACTION_URL,M.IMG_URL,M.MENU_ORDER " +
				" FROM titan_auth AS A" +
				",titan_user AS U" +
				",titan_user_role AS UR" +
				",titan_role_auth AS RA" +
				",titan_menu AS M" +
				" WHERE U.USER_CODE	='" +userCode.toUpperCase()+"'"+
				" AND U.USER_ID = UR.USER_ID" +
				" AND UR.ROLE_ID = RA.ROLE_ID" +
				" AND RA.AUTH_ID = A.AUTH_ID" +
				" AND M.MENU_ID = A.MENU_ID" +
				//"   --AND A.AUTH_TYPE = 'MENU'	" +
				" AND A.AUTH_NAME LIKE 'MENU%'" +
				" AND A.AUTH_NAME NOT LIKE '%BTN%'" +
				" AND M.MENU_LEVEL = 2" +
				" AND M.SUPPER_MENU_ID =" +oneMenuId+
				" ORDER BY M.MENU_ORDER,MENU_ID;";
		Query query =this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		List<Menu> seconudMenuList = new ArrayList<Menu>();
		for(Object[] object : objects){
			Menu menu = new Menu();
			menu.setMenuId(new Integer(object[0].toString()));
			menu.setMenuLevel(new Integer(object[1].toString()));
			menu.setMenuCode(object[2].toString());
			if(object[3]!=null){
				menu.setMenuName(object[3].toString());
			}
			if(object[4]!=null){
				menu.setSupperMenuId(new Integer(object[4].toString()));
			}
			if(object[5]!=null){
				menu.setActionUrl(object[5].toString());
			}
			if(object[6]!=null){
				menu.setImgUrl(object[6].toString());
			}
			seconudMenuList.add(menu);
		}
		
		List<ViewMenu2Tree> menu2List = new ArrayList<ViewMenu2Tree>();
		for(Menu menu: seconudMenuList){
			ViewMenu2Tree  viewMenu2Tree =new ViewMenu2Tree();
			viewMenu2Tree.setMenu2(menu);
			menu2List.add(viewMenu2Tree);
		}
		return menu2List;
	}
	
	
	
	/**
	 * 查询用户的一级菜单
	 * @param userCode
	 * @return
	 */
	public List<Menu>  findThirdMenuByUserCode(String userCode,int secoundMenuId) throws BaseDaoException{
		String sql = "SELECT  distinct(M.MENU_ID) as MENU_ID,M.MENU_LEVEL,M.MENU_CODE,M.MENU_NAME,M.SUPPER_MENU_ID,M.ACTION_URL,M.IMG_URL,M.MENU_ORDER " +
				" FROM titan_auth AS A" +
				",titan_user AS U" +
				",titan_user_role AS UR" +
				",titan_role_auth AS RA" +
				",titan_menu AS M" +
				" WHERE U.USER_CODE	='" +userCode.toUpperCase()+"'"+
				" AND U.USER_ID = UR.USER_ID" +
				" AND UR.ROLE_ID = RA.ROLE_ID" +
				" AND RA.AUTH_ID = A.AUTH_ID" +
				" AND M.MENU_ID = A.MENU_ID" +
				//"   --AND A.AUTH_TYPE = 'MENU'	" +
				" AND A.AUTH_NAME LIKE 'MENU%'" +
				" AND A.AUTH_NAME NOT LIKE '%BTN%'" +
				" AND M.MENU_LEVEL = 3" +
				" AND M.SUPPER_MENU_ID =" +secoundMenuId+
				" ORDER BY M.MENU_ORDER,MENU_ID;";
		Query query =this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		List<Menu> thirdMenuList = new ArrayList<Menu>();
		for(Object[] object : objects){
			Menu menu = new Menu();
			menu.setMenuId(new Integer(object[0].toString()));
			menu.setMenuLevel(new Integer(object[1].toString()));
			menu.setMenuCode(object[2].toString());
			if(object[3]!=null){
				menu.setMenuName(object[3].toString());
			}
			if(object[4]!=null){
				menu.setSupperMenuId(new Integer(object[4].toString()));
			}
			if(object[5]!=null){
				menu.setActionUrl(object[5].toString());
			}
			if(object[6]!=null){
				menu.setImgUrl(object[6].toString());
			}
			thirdMenuList.add(menu);
		}
		
		return thirdMenuList;
	}
}
