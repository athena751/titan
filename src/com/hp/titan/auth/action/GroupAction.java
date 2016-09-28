package com.hp.titan.auth.action;

import java.sql.Timestamp;
import java.util.List;

import com.hp.app.action.DefaultBaseAction;
import com.hp.app.common.page.Page;
import com.hp.app.common.util.DateUtils;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseServiceException;
import com.hp.titan.auth.model.Group;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.service.GroupService;
import com.hp.titan.auth.service.UserService;
import com.hp.titan.auth.vo.GroupVo;
import com.hp.titan.auth.vo.UserVo;
import com.hp.titan.common.util.UserSessionUtil;
 
 

/**
 * @author xu.yang@hp.com
 *
 */
public class GroupAction extends DefaultBaseAction {


	public GroupService groupService; 
	public UserService userService; 
	private List<Group> groupList;
	private Group group;
	private GroupVo groupVo;
	private Integer[] groupIdAry;
	private Integer groupId;
	private Page page =new Page();
	private Page userPages;
	private UserVo userVo=new UserVo();
	private Integer[] userIdAry;	 
	private List<User> userList;
	/**
	 * 方法说明：查询所有权限，用于显示权限管理主页面
	 * @return
	 */
	public String doSearchGroups(){
       /**查询所有权限**/
		try {
			groupList =   groupService.getAllGroupList();
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String goGroupCreate(){
		group =new Group();
		return SUCCESS;
	}
	
	
	/**
	 *
	 * @return
	 * @throws BaseServiceException
	 */
	public String doGroupSave(){
		group.setUsers(null);
		if(group.getGroupId() != 0){
			group.setCreateDate(Timestamp.valueOf(groupVo.getStrCreateDate()));
			group.setLastUpdateUser(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
			group.setLastUpdate_Date(DateUtils.getCurrentDate());
		}
		else{
			group.setCreateUser(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
			group.setCreateDate(DateUtils.getCurrentDate());
		}
		try {
			groupService.saveGroup(group);
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
	public String doGroupRemove(){
		/** 需要删除的权限id通过roleIdAry传递到后台，由DAO实现删除权限以及权限和用户的关联关系**/
//		if ((this.groupIdAry != null) && (this.groupIdAry.length != 0)) {
		      try {		    	 
				groupService.deleteGroupList(groupId);				
			} catch (BaseDaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		    }
		return SUCCESS;
	}
	
	
	/**
	 * 方法说明：初始化更新权限页面数据，然后跳转到权限更新页面
	 * @return
	 * @throws BaseServiceException
	 */
	public String goGroupUpdate(){
		/***** 只有当所传权限id为且仅为一个时，才进行处理 ******/
//		 if ((this.groupIdAry != null) && (this.groupIdAry.length == 1)) {
//		      Integer groupId = this.groupIdAry[0];
		      try {
				group= groupService.getGroupById(groupId);
			} catch (BaseDaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      return SUCCESS;
//		    }
//		 return ERROR;
	}
	
	
	
	/**
	 * 方法说明：查询角色及角色所属用户
	 * 
	 * @return
	 * @throws BaseServiceException
	 */
	public String goGroupUserMng() {
		/***** 只有当所传角色id为且仅为一个时，才进行处理 ******/
		if ((this.groupIdAry != null) && (this.groupIdAry.length == 1)) {
			groupId = this.groupIdAry[0];
			try {
				group = groupService.findGroupAndUserList(groupId);
			} catch (BaseDaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return SUCCESS;
		}
		return ERROR;
	}

	/**
	 *  查询所有用户
	 * @return
	 */
	public String goAllUsersList(){
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
	public String doAddUserToGroup() {
		try {
			groupService.addUserToGroup(groupId,userIdAry);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;

	}

	/**
	 * 方法说明：解除当前角色和所选用户关联（即用户不再属于当前角色）
	 * 
	 * @return
	 * @throws BaseServiceException
	 */
	public String doDeleteGroupUser() {
		try {
			groupService.deleteUserFromGroup(groupId,userIdAry);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
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

	public Integer[] getUserIdAry() {
		return userIdAry;
	}

	public void setUserIdAry(Integer[] userIdAry) {
		this.userIdAry = userIdAry;
	}

	public GroupService getGroupService() {
		return groupService;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public List<Group> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<Group> groupList) {
		this.groupList = groupList;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Integer[] getGroupIdAry() {
		return groupIdAry;
	}

	public void setGroupIdAry(Integer[] groupIdAry) {
		this.groupIdAry = groupIdAry;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public GroupVo getGroupVo() {
		return groupVo;
	}

	public void setGroupVo(GroupVo groupVo) {
		this.groupVo = groupVo;
	}

}
