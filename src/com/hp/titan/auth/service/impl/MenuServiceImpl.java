package com.hp.titan.auth.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.hp.app.exception.BaseDaoException;
import com.hp.titan.auth.dao.MenuDao;
import com.hp.titan.auth.model.Auth;
import com.hp.titan.auth.model.Menu;
import com.hp.titan.auth.model.Role;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.model.ViewMenu2Tree;
import com.hp.titan.auth.model.ViewMenuTree;
import com.hp.titan.auth.service.MenuService;
 

 

public class MenuServiceImpl implements MenuService
{
	private MenuDao menuDao;
	@Override
	public List<ViewMenuTree> getAllMenuTree() throws BaseDaoException {
		// TODO Auto-generated method stub
		List<ViewMenuTree> menuList = new ArrayList<ViewMenuTree>();
		
		List<Menu> oneMenuList = menuDao.findOneMenu();
		for(Menu menu1: oneMenuList){
			ViewMenuTree menu = new ViewMenuTree();
			List<ViewMenu2Tree> menu2List = menuDao.findSecoundMenuByOne(menu1.getMenuId(),null);
			for(ViewMenu2Tree viewMeun2 :menu2List){
				List<Menu> menu3List =menuDao.findThirdMenuBySecond(viewMeun2.getMenu2().getMenuId(),null);
				viewMeun2.setMenu3List(menu3List);
			}
			menu.setMenu1(menu1);
			menu.setMenu2List(menu2List);
			menuList.add(menu);
		}
			
		return menuList;
	}

	public List<ViewMenuTree> getUserMenuList(User user) throws BaseDaoException{
		List<ViewMenuTree> userMenuList = new ArrayList<ViewMenuTree>();
		List<Menu> oneMenuList = menuDao.findOneMenuByUserCode(user.getUserCode());
		for(Menu oneMenu :oneMenuList){
					ViewMenuTree viewMenuTree = new ViewMenuTree();
					viewMenuTree.setMenu1(oneMenu);
					
					List<ViewMenu2Tree> secoundMenuList =menuDao.findSecoundMenuByUserCode(user.getUserCode(), oneMenu.getMenuId());
					int menu3Count =0;
					for(ViewMenu2Tree viewMeun2 :secoundMenuList){
						List<Menu> menu3List =menuDao.findThirdMenuByUserCode(user.getUserCode(), viewMeun2.getMenu2().getMenuId());
						 menu3Count=menu3Count+menu3List.size();
						 viewMeun2.setMenu3List(menu3List);
					}
					viewMenuTree.setMenu3Count(menu3Count);
					viewMenuTree.setMenu2List(secoundMenuList);
					
					userMenuList.add(viewMenuTree);
		}
		
		return userMenuList;
	}
	@Override
	public Menu getMenuById(Integer menuId) throws BaseDaoException {
		// TODO Auto-generated method stub
		return menuDao.findById(menuId);
	}

	@Override
	public void saveMenu(Menu menu) throws BaseDaoException {
		// TODO Auto-generated method stub
		
	}

	public MenuDao getMenuDao() {
		return menuDao;
	}

	public void setMenuDao(MenuDao menuDao) {
		this.menuDao = menuDao;
	}
 
 
	

	

}
