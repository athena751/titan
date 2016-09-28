package com.hp.titan.auth.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.hp.app.action.DefaultBaseAction;
import com.hp.app.common.page.Page;
import com.hp.app.common.util.DateUtils;
import com.hp.app.config.SpringUtil;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseServiceException;
import com.hp.titan.auth.model.Auth;
import com.hp.titan.auth.model.Menu;
import com.hp.titan.auth.model.Role;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.model.ViewMenu2Tree;
import com.hp.titan.auth.model.ViewMenuTree;
import com.hp.titan.auth.service.AuthService;
import com.hp.titan.auth.service.MenuService;
import com.hp.titan.auth.service.RoleService;
import com.hp.titan.auth.service.UserService;
import com.hp.titan.auth.vo.RoleVo;
import com.hp.titan.auth.vo.UserVo;
import com.hp.titan.common.util.UserSessionUtil;

/**
 * @author ibm
 * 
 */
public class RoleManagerAction extends DefaultBaseAction {

	private static final long serialVersionUID = 1L;

	public RoleService roleService;
	public AuthService authService;
	public UserService userService; 
	private List<Role> roleList; /* 角色列表 */
	private List<Auth> authList; /* 权限列表 */
	private List<ViewMenuTree> meunTreeList;	/*菜单树*/
	private MenuService menuService;
	private Auth auth  ;
	private Role role;
	private RoleVo roleVo;
	private Page page =new Page();
	private Page userPages;
	private UserVo userVo=new UserVo();

	private String roleId;	/* 角色id */
	private String roleName;	/* 角色名称 */

	private String[] roleIdAry; 	/* 角色id序列 用于页面对角色的操作 */
	private String[] userIdAry;    	/* 用户id序列 用于页面对角色用户的操作 */

	/* 用户列表 */
	private List<User> userList;
	private Set<User> userSet;
	/********* 分页操作 ***********/
	private String[] authIdAry;			/*权限id序列*/
	private String[] userAuthIdAry;		/*用户权限id序列*/
	private Integer[] menuIdAry;
	
	private String[] authIdSelAry;
	private String[] authIdHasAry;
	/**
	 * 方法说明：查询所有角色，用于显示角色管理主页面
	 * 
	 * @return
	 */
	public String doSearch() {
		/** 查询所有角色 **/
		try {
			roleList = roleService.findAll();
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/** 返回成功页面，即角色管理首页 **/
		return SUCCESS;
	}

	public String goRoleCreate() {
		role = new Role();
		return SUCCESS;
	}

	/**
	 * 方法说明：初始化更新角色页面数据，然后跳转到角色更新页面
	 * 
	 * @return
	 * @throws BaseServiceException
	 */
	public String goRoleUpdate() {
//		if ((this.roleIdAry != null) && (this.roleIdAry.length == 1)) {
//			String updateId = this.roleIdAry[0];
			try {
				role = roleService.getRole(roleId);
			} catch (BaseDaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return SUCCESS;
//		}
//		return ERROR;
	}

	/**
	 * 方法说明：完成增加角色的操作
	 * 
	 * @return
	 * @throws BaseServiceException
	 */
	public String doRoleSave() {
		if(null == role.getRoleId()){
			role.setCreateUser(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
			role.setCreateDate(DateUtils.getCurrentDate());
		}
		else{
			role.setCreateDate(Timestamp.valueOf(roleVo.getStrCreateDate()));
			role.setLastUpdateUser(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
			role.setLastUpdate_Date(DateUtils.getCurrentDate());
		}
		try {
			roleService.save(role);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 方法说明：实现角色删除操作
	 * 
	 * @return
	 * @throws BaseServiceException
	 */
	public String doRoleRemove() {
		/** 需要删除的角色id通过roleIdAry传递到后台，由DAO实现删除角色以及角色和用户的关联关系 **/
//		if ((this.roleIdAry != null) && (this.roleIdAry.length != 0)) {
			/** 删除角色及角色和用户的关联关系 **/
			try {
				roleService.deleteList(Integer.parseInt(roleId));
			} catch (BaseDaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		}
		/*** 跳到角色管理主页面 ****/
		return SUCCESS;
	}

	/**
	 * 方法说明：查询角色及角色所属用户
	 * 
	 * @return
	 * @throws BaseServiceException
	 */
	public String goRoleAuthMng() {
		/***** 只有当所传角色id为且仅为一个时，才进行处理 ******/
		try {
//		if ((this.roleIdAry != null) && (this.roleIdAry.length == 1)) {
//			roleId = this.roleIdAry[0];
			role = roleService.findRoleAndAuthList(roleId);
			authList =  new ArrayList<Auth>();
			if(role.getAuths()!=null){
				Iterator auIter =role.getAuths().iterator();
				while(auIter.hasNext()){
					Auth authHas = (Auth)auIter.next();
					authHas.setIschecked(true);
					if(authHas.getMenuId()!=null){
						Menu menu =	menuService.getMenuById(authHas.getMenuId());
						authHas.setMenu(menu);
					}
					authList.add(authHas);
				}
			}
			meunTreeList = menuService.getAllMenuTree();
			
		for(ViewMenuTree viewMenuTree :meunTreeList){
				Menu oneMenu =viewMenuTree.getMenu1();
				for(Auth auth : authList){
					if(auth.getMenu()!=null){
						if(oneMenu.getMenuId().equals(auth.getMenu().getMenuId())){
							oneMenu.setIscheckedMenu(true);
						}
					}
				}
				List<ViewMenu2Tree> viewMenu2TreeList =viewMenuTree.getMenu2List();
				for(ViewMenu2Tree viewMenu2Tree :viewMenu2TreeList){
					Menu secondMenu =viewMenu2Tree.getMenu2();
					for(Auth auth : authList){
						if(auth.getMenu()!=null){
							if(secondMenu.getMenuId().equals(auth.getMenu().getMenuId())){
								secondMenu.setIscheckedMenu(true);
							}
						}
					}
					List<Menu> menu3List = viewMenu2Tree.getMenu3List();
					for(Menu menu3 : menu3List){
						for(Auth auth : authList){
							if(auth.getMenu()!=null){
								if(menu3.getMenuId().equals(auth.getMenu().getMenuId())){
									menu3.setIscheckedMenu(true);
								}
							}
						}
					}
				}
			}
			
			return SUCCESS;
//		}
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * 查询菜单下的所有权限
	 * @return
	 */
	public String doSearchMenuAuth(){
      
		List<Integer> idList = new ArrayList<Integer>();
		for (int i = 0; menuIdAry.length > i; i++) {
				idList.add(menuIdAry[i]);
		}
		try {
		authList =   authService.findAllAuthByMenuId(idList);
		role = roleService.findRoleAndAuthList(roleId);
		
		if(role.getAuths()!=null){
			for(Auth auth :authList){
				String authId = auth.getAuthId();
				Iterator auIter =role.getAuths().iterator();
				while(auIter.hasNext()){
					Auth authHas = (Auth)auIter.next();
					if(authId.equals(authHas.getAuthId())){
						auth.setIschecked(true);
					}
				}
			}
		}
	
		meunTreeList = menuService.getAllMenuTree();
		
		for(ViewMenuTree viewMenuTree :meunTreeList){
			Menu oneMenu =viewMenuTree.getMenu1();
			for(Integer menuId : idList){
					if(oneMenu.getMenuId().equals(menuId)){
						oneMenu.setIscheckedMenu(true);
					}
			}
			List<ViewMenu2Tree> viewMenu2TreeList =viewMenuTree.getMenu2List();
			for(ViewMenu2Tree viewMenu2Tree :viewMenu2TreeList){
				Menu secondMenu =viewMenu2Tree.getMenu2();
				for(Integer menuId : idList){
						if(secondMenu.getMenuId().equals(menuId)){
							secondMenu.setIscheckedMenu(true);
						}
				}
				List<Menu> menu3List = viewMenu2Tree.getMenu3List();
				for(Menu menu3 : menu3List){
					for(Integer menuId : idList){
							if(menu3.getMenuId().equals(menuId)){
								menu3.setIscheckedMenu(true);
							}
					}
				}
			}
		}
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
 
	/**
	 * 保存菜单角色权限
	 * 
	 * @return
	 * @throws BaseServiceException
	 */
	public String doSaveMenuRoleAuth() {
		List<Integer> menuIdList = new ArrayList<Integer>();
		for (int i = 0; menuIdAry.length > i; i++) {
			menuIdList.add(menuIdAry[i]);
		}
		try {
			roleService.doSaveMenuRoleAuth(authIdAry, menuIdList, roleId);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return doSearchMenuAuth();
	}

	/**
	 * 方法说明：解除当前角色和所选用户关联（即用户不再属于当前角色）
	 * 
	 * @return
	 * @throws BaseServiceException
	 */
	public String doDeleteRoleAuth() {
		List<String> idList = new ArrayList<String>();
		for (int i = 0; authIdHasAry.length > i; i++) {
			idList.add(authIdHasAry[i]);
		}
		try {
			roleService.doDeleteRoleAuth(idList, roleId);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}

	
	/**
	 * 方法说明：查询角色及角色所属用户
	 * 
	 * @return
	 * @throws BaseServiceException
	 */
	public String goRoleUserMng() {
		/***** 只有当所传角色id为且仅为一个时，才进行处理 ******/
		if ((this.roleIdAry != null) && (this.roleIdAry.length == 1)) {
			roleId = this.roleIdAry[0];
			try {
				role = roleService.findRoleAndUserList(roleId);
			} catch (BaseDaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return SUCCESS;
		}
		return ERROR;
	}

	/**
	 * 
	 * @return
	 */
	public String goSearchUsersList(){
       /**查询所有用户**/
		try {
			userList = userService.getAllUserByUserVo(null);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
	

	/**
	 * 方法说明：关联角色和用户（即将用户分到当前角色中）
	 * 
	 * @return
	 * @throws BaseServiceException
	 */
	public String doAddUserToRole() {
		try {
			roleService.doAddUserToRole(userIdAry,role);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		roleId = role.getRoleId();
		return SUCCESS;

	}

	/**
	 * 方法说明：解除当前角色和所选用户关联（即用户不再属于当前角色）
	 * 
	 * @return
	 * @throws BaseServiceException
	 */
	public String doDeleteRoleUser() {
		try {
			roleService.doDeleteRoleUser(userIdAry, role);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		roleId = role.getRoleId();
		return SUCCESS;
	}
	
	
	
	
	
	public AuthService getAuthService() {
		return authService;
	}

	public void setAuthService(AuthService authService) {
		this.authService = authService;
	}

	public List<Auth> getAuthList() {
		return authList;
	}

	public void setAuthList(List<Auth> authList) {
		this.authList = authList;
	}

	/**
	 * 方法说明：查询角色及角色所属用户
	 * 
	 * @return
	 * @throws BaseServiceException
	 */
	public String doSearchUsers() {
		/***** 只有当所传角色id为且仅为一个时，才进行处理 ******/
		if ((this.roleIdAry != null) && (this.roleIdAry.length == 1)) {

			String roleId = this.roleIdAry[0];
			RoleService roleService = (RoleService) SpringUtil.getBean("roleService");

			/*** 查询角色及角色所属用户 ***/
			Set<User> userSet = null;
			Role role = null;
			try {
				role = roleService.findRoleAndUserList(roleId);
			} catch (BaseDaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/**** 取出user集合 注意做非空处理 防止页面报错 ***/
			if (role != null) {
				userSet = role.getUserRoles();
			} else {
				userSet = new HashSet<User>();
			}

			this.userSet = userSet;

			this.roleId = roleId;

			/*** 跳到角色管理主页面 ****/
			return SUCCESS;
		}
		/** 跳转到错误页面，提示用户数据异常 **/
		return ERROR;
	}

	/**
	 * 方法说明：实现用户查询，现可接受的查询条件为 userName
	 * 查询的数据不包括当前角色（此方法用于向角色添加用户，当前角色即为接受用户的角色）所属的用户
	 * 
	 * @return
	 * @throws BaseServiceException
	 */
	public String doSearchNotAddUser() {

		/***** 判断先觉条件 角色id是否存在 ****/
		if (this.roleId != null) {
			RoleService roleService = (RoleService) SpringUtil.getBean("roleService");

			/****** 查询当前角色所属用户 ******/
			Role role = null;
			try {
				role = roleService.findRoleAndUserList(this.roleId);
			} catch (BaseDaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/***** 取出当前角色所属用户 注意做判空处理 *****/
			Set<User> userSet;
			if (role != null) {
				userSet = role.getUserRoles();
			} else {
				userSet = new HashSet<User>();
			}

			StringBuffer idsStrBuf = new StringBuffer();
			Iterator<User> iterator = userSet.iterator();

			/***** 将当前角色所属用户的id连接起来 ，形成形如 1111,2222,11112 的字符串 传给相应接口 *****/
			while (iterator.hasNext()) {
				User user = iterator.next();
				idsStrBuf.append(user.getUserId() + ",");
			}
			String idsStr = idsStrBuf.toString();
			if (idsStr.length() > 1) {
				idsStr = idsStr.substring(0, idsStr.length() - 1);
			}

			/******* 暂时是使用userName 作为用户可选的查询条件 ********/

			UserService userService = (UserService) SpringUtil.getBean("userService");

			/******** 查询不属于当前角色的其他所有用户 *******/
			if (this.pageNo < 1)
				this.pageNo = 1;
			// this.pageResults = userService.findUserList(idsStr, this.userVo
			// ,FcmConstant.PAGESIZE ,this.pageNo);

			// this.userList = this.pageResults.getResults();

			/******** 为防页面保存 做判空处理 *********/
			if (null == this.userList) {
				this.userList = new ArrayList<User>();
			}


			/*** 跳到角色管理主页面 ****/
			return SUCCESS;
		}
		/** 跳转到错误页面，提示用户数据异常 **/
		return ERROR;
	}

	/**
	 * 方法说明：解除当前角色和所选用户关联（即用户不再属于当前角色）
	 * 
	 * @return
	 * @throws BaseServiceException
	 */
	public String doDeleteUser() {
		/********** 当前角色id 和所选用户id序列必须存在 **********/
		try {
		if ((this.roleId != null) && (this.userIdAry != null)
				&& (this.userIdAry.length != 0)) {

			RoleService roleService = (RoleService) SpringUtil.getBean("roleService");
			/****** 查询当前角色实体 ****/
			Role role = roleService.findRoleAndUserList(this.roleId);

			UserService userService = (UserService) SpringUtil.getBean("userService");
			/******** 在解除关联前 ，先查询用户，看是否存在 *********/
			List<Integer> idList = new ArrayList<Integer>();
			for (int i = 0; this.userIdAry.length > i; i++) {
				idList.add(Integer.valueOf(this.userIdAry[i]));
			}
			/******** 根据用户id序列查询用户列表 *********/
			List<User> userList = userService.findbyIdList(idList);

			if ((role != null) && (role.getUserRoles() != null)) {

				Set<User> userSet = role.getUserRoles();
				/********** 解除关联 配置 role为主控方 **********/
				userSet.removeAll(userList);

				/****** 持久化到数据库 ********/
				roleService.save(role);
			}
			/*** 跳到角色管理主页面 ****/
			return SUCCESS;
		}
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/** 跳转到错误页面，提示用户数据异常 **/
		return ERROR;
	}

	/**
	 * 方法说明：关联角色和用户（即将用户分到当前角色中）
	 * 
	 * @return
	 * @throws BaseServiceException
	 */
	public String doAddUser() {
		/********** 当前角色id 和所选用户id序列必须存在 **********/
		try{
		if ((this.roleId != null) && (this.userIdAry != null)
				&& (this.userIdAry.length != 0)) {

			UserService userService = (UserService) SpringUtil.getBean("userService");
			/******** 在关联前 ，先查询用户，看是否存在 *********/
			List<Integer> idList = new ArrayList<Integer>();
			for (int i = 0; this.userIdAry.length > i; i++) {
				idList.add(Integer.valueOf(this.userIdAry[i]));
			}
			/******** 根据用户id序列查询用户列表 *********/
			List<User> userList = userService.findbyIdList(idList);

			RoleService roleService = (RoleService) SpringUtil.getBean("roleService");
			/****** 查询当前角色实体 ****/
			Role role = roleService.findRoleAndUserList(this.roleId);
			if (role != null && (role.getUserRoles() != null)) {
				Set<User> userSet = role.getUserRoles();
				/******* 建立关联 主控方为role *********/
				userSet.addAll(userList);
				/********** 将关联持久化到数据库中 **********/
				roleService.save(role);
			}
			/*** 跳到角色管理主页面 ****/
			return SUCCESS;
		}
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/** 跳转到错误页面，提示用户数据异常 **/
		return ERROR;
	}

 

	public String[] getUserIdAry() {
		return userIdAry;
	}

	public void setUserIdAry(String[] userIdAry) {
		this.userIdAry = userIdAry;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}


	public String[] getRoleIdAry() {
		return roleIdAry;
	}

	public void setRoleIdAry(String[] roleIdAry) {
		this.roleIdAry = roleIdAry;
	}
 

 


	// private PageResults pageResults = new PageResults();

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public Set<User> getUserSet() {
		return userSet;
	}

	public void setUserSet(Set<User> userSet) {
		this.userSet = userSet;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public Page getUserPages() {
		return userPages;
	}

	public void setUserPages(Page userPages) {
		this.userPages = userPages;
	}

	public UserVo getUserVo() {
		return userVo;
	}

	public void setUserVo(UserVo userVo) {
		this.userVo = userVo;
	}
	

	public String[] getAuthIdAry() {
		return authIdAry;
	}

	public void setAuthIdAry(String[] authIdAry) {
		this.authIdAry = authIdAry;
	}

	public String[] getUserAuthIdAry() {
		return userAuthIdAry;
	}

	public void setUserAuthIdAry(String[] userAuthIdAry) {
		this.userAuthIdAry = userAuthIdAry;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	public List<ViewMenuTree> getMeunTreeList() {
		return meunTreeList;
	}

	public void setMeunTreeList(List<ViewMenuTree> meunTreeList) {
		this.meunTreeList = meunTreeList;
	}

	public MenuService getMenuService() {
		return menuService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	public Auth getAuth() {
		return auth;
	}

	public void setAuth(Auth auth) {
		this.auth = auth;
	}

	 

	public Integer[] getMenuIdAry() {
		return menuIdAry;
	}

	public void setMenuIdAry(Integer[] menuIdAry) {
		this.menuIdAry = menuIdAry;
	}

	public String[] getAuthIdSelAry() {
		return authIdSelAry;
	}

	public void setAuthIdSelAry(String[] authIdSelAry) {
		this.authIdSelAry = authIdSelAry;
	}

	public String[] getAuthIdHasAry() {
		return authIdHasAry;
	}

	public void setAuthIdHasAry(String[] authIdHasAry) {
		this.authIdHasAry = authIdHasAry;
	}

	public RoleVo getRoleVo() {
		return roleVo;
	}

	public void setRoleVo(RoleVo roleVo) {
		this.roleVo = roleVo;
	}
}
