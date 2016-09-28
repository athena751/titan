package com.hp.titan.auth.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import com.hp.app.action.DefaultBaseAction;
import com.hp.app.common.util.DateUtils;
import com.hp.app.common.util.JsonUtil;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseServiceException;
import com.hp.titan.auth.model.Auth;
import com.hp.titan.auth.model.ViewMenuTree;
import com.hp.titan.auth.service.AuthService;
import com.hp.titan.auth.service.MenuService;
import com.hp.titan.auth.vo.AuthVo;
import com.hp.titan.common.util.UserSessionUtil;
 
 

/**
 * @author xu.yang@hp.com
 *
 */
public class AuthAction extends DefaultBaseAction {

	
	public AuthService authService;  
	private List<Auth> authList;	
	private Auth auth;
	private AuthVo authVo;
	private String[] authIdAry;
	private List<ViewMenuTree> meunTreeList;
	private MenuService menuService;
	private Integer menuIdAry;
	private String authId;

	

	/**
	 * 查询选中的权限信息
	 * @return
	 * @throws IOException 
	 */
	public void selectAuthInfo() throws IOException{
		try {
			auth= authService.getAuthById(authId);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		auth.setRoles(null);
		this.getResponse().setCharacterEncoding("utf-8");
		this.getResponse().getWriter().write(JsonUtil.getJsonStringFromObject(auth));
	}
	
	

	public String goAuthCreate(){
		auth =new Auth();
		return SUCCESS;
	}
	
	/**
	 * 方法说明：查询所有权限，用于显示权限管理主页面
	 * @return
	 */
	public String doSearch(){
       /**查询所有权限**/
		if(menuIdAry!=null ){
			auth=new Auth();
			auth.setMenuId(menuIdAry);
		}
		try {
			authList =   authService.getAllAuth(auth);
			meunTreeList = menuService.getAllMenuTree();
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
	

	
	/**
	 * 方法说明：完成增加权限的操作
	 * @return
	 * @throws BaseServiceException
	 */
	public String doAuthSave(){
			if(menuIdAry!=null){
				auth.setMenuId(menuIdAry);
			}
			if(auth.getAuthId().equals("")){
				auth.setAuthId(null);
				auth.setCreateUser(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
				auth.setCreateDate(DateUtils.getCurrentDate());
			}
			else{
//				auth.setCreateDate(Timestamp.valueOf(authVo.getStrCreateDate()));
				auth.setCreateDate(DateUtils.getCurrentDate());
				auth.setLastUpdateUser(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
				auth.setLastUpdate_Date(DateUtils.getCurrentDate());
			}
			try {
				authService.saveAuth(auth);
			} catch (BaseDaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return SUCCESS;
	}
	
	
	/**
	 * 方法说明：实现权限删除操作
	 * @return
	 * @throws BaseServiceException
	 */
	public String doAuthRemove(){
		if ((this.authIdAry != null) && (this.authIdAry.length != 0)) {
		      try {
				authService.deleteAuthList(this.authIdAry);
			} catch (BaseDaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    }
		return SUCCESS;
	}
	
	
	/**
	 * 方法说明：初始化更新权限页面数据，然后跳转到权限更新页面
	 * @return
	 * @throws BaseServiceException
	 */
	public String goAuthUpdate(){
		/***** 只有当所传权限id为且仅为一个时，才进行处理 ******/
		 if ((this.authIdAry != null) && (this.authIdAry.length == 1)) {
			 
		      String updateId = this.authIdAry[0];
		      try {
				auth= authService.getAuthById(updateId);
			} catch (BaseDaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      return SUCCESS;
		    }
		 return ERROR;
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

	public Auth getAuth() {
		return auth;
	}

	public void setAuth(Auth auth) {
		this.auth = auth;
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
	 
	
	public String[] getAuthIdAry() {
		return authIdAry;
	}

	public void setAuthIdAry(String[] authIdAry) {
		this.authIdAry = authIdAry;
	}

	public Integer getMenuIdAry() {
		return menuIdAry;
	}

	public void setMenuIdAry(Integer menuIdAry) {
		this.menuIdAry = menuIdAry;
	}
	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}



	public AuthVo getAuthVo() {
		return authVo;
	}



	public void setAuthVo(AuthVo authVo) {
		this.authVo = authVo;
	}

}
