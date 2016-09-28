package com.hp.titan.auth.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.cxf.common.util.StringUtils;

import com.hp.app.action.DefaultBaseAction;
import com.hp.app.common.page.Page;
import com.hp.app.common.util.DateUtils;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.app.exception.BaseServiceException;
import com.hp.titan.auth.model.Group;
import com.hp.titan.auth.model.Role;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.model.UserProject;
import com.hp.titan.auth.model.UserProjectId;
import com.hp.titan.auth.service.GroupService;
import com.hp.titan.auth.service.RoleService;
import com.hp.titan.auth.service.UserService;
import com.hp.titan.auth.service.ViewUserInfoService;
import com.hp.titan.auth.vo.UserVo;
import com.hp.titan.common.constants.TitanContent;
import com.hp.titan.common.util.UserSessionUtil;
import com.hp.titan.notify.EmailManageAction;
import com.hp.titan.project.model.Project;
import com.hp.titan.project.service.ProjectService;
import com.hp.titan.common.util.Encode;;

public class UserAction extends DefaultBaseAction {
	public UserService userService;
	public GroupService groupService;
	private ProjectService projectService;
	public RoleService roleService;
	private List<Group> groupList; 
	private List<User> userList; 
	private List<Role> roleList; 
	private List<Project> projectList;
	private Page userPages;
	private Page page = new Page();
	private UserVo userVo = new UserVo();
	private User user;
	private Integer[] userIdAry;
	private String[] userProjectIds;
	private String userCode;
	private String email;
	private ViewUserInfoService viewUserInfoService;
	private Integer userId;
	private String rejectReason;
	private String userGroupId;

	/**
	 * 方法说明：查询所有权限，用于显示权限管理主页面
	 * 
	 * @return
	 */
	public String doSearchUsers() {
		/** 查询所有权限 **/
		//userPages = userService.getAllUserByPage(userVo, page);
		userVo.setStatus(TitanContent.USER_STATUS_NORMAL);
		try {
			userList = userService.getAllUserByUserVo(userVo);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//userList = viewUserInfoService.getAllUserViewList(userVo);
		return SUCCESS;
	}


	public ViewUserInfoService getViewUserInfoService() {
		return viewUserInfoService;
	}


	public void setViewUserInfoService(ViewUserInfoService viewUserInfoService) {
		this.viewUserInfoService = viewUserInfoService;
	}


	public String goUserCreate() {
		user = new User();
		try {
			groupList = groupService.getAllGroupList();
			roleList = roleService.findAll();
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 方法说明：执行用户新增
	 * 
	 * @return
	 * @throws BaseServiceException
	 */
	public String doUserSave() {
		try{
		if(user.getUserId()==null){
			//新增用户
			user.setUserCode(user.getUserCode().toUpperCase());
			user.setStatus(TitanContent.USER_STATUS_NORMAL);
			user.setCreateUser(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
			user.setCreateDate(DateUtils.getCurrentDate());
			user.setPassword(Encode.getEncode(user.getPassword()));
			userService.saveUser(user);

			if (!StringUtils.isEmpty(user.getGroupId().toString())) {
				groupService.addUserToGroup(user.getGroupId(), new Integer[] { user
						.getUserId() });
			}
			
			if (!StringUtils.isEmpty(user.getRoleId())) {
				roleService.doAddUserToRole(new String[] { user.getUserId()
						.toString() }, new Role(user.getRoleId()));
			}
		}else{
			user.setUserCode(user.getUserCode().toUpperCase());
			user.setStatus(TitanContent.USER_STATUS_NORMAL);
			user.setCreateDate(Timestamp.valueOf(userVo.getStrCreateDate()));
			user.setLastUpdateUser(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
			user.setLastUpdate_Date(DateUtils.getCurrentDate());
			if(!userService.getUserByCode(user.getUserCode()).getPassword().equals(user.getPassword()))
				user.setPassword(Encode.getEncode(user.getPassword()));
			userService.updateUser(user);
		}
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR;
		}
		
		return SUCCESS;
	}

	/**
	 * 方法说明：执行用户修改
	 * 
	 * @return
	 * @throws BaseServiceException
	 */
	public String doUserUpdate() {
		user.setUserCode(user.getUserCode().toUpperCase());
		try {
			userService.updateUser(user);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 方法说明：实现权限删除操作
	 * 
	 * @return
	 * @throws BaseServiceException
	 */
	public String doUserRemove() {
		/** 需要删除的权限id通过roleIdAry传递到后台，由DAO实现删除权限以及权限和用户的关联关系 **/
//		if ((this.userIdAry != null) && (this.userIdAry.length != 0)) {
			try {
				userService.deleteUserList(userId);
			} catch (BaseDaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		}
		return SUCCESS;
	}

	/**
	 *进入用户详细
	 * 
	 * @return
	 * @throws BaseServiceException
	 */
	public String goUserDetail() {
		User currentUser = UserSessionUtil.getUser();
		if(currentUser.getUserId() != userId){
			userId = currentUser.getUserId();
		}
		try {
			user = userService.getUserById(userId);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	public String goUserDetailUpdate() {
		try{
		user = userService.getUserById(user.getUserId());
		List<Group> groups = new ArrayList<Group>(user.getUserGroups());
		Set<Project> s = UserSessionUtil.getUser().getUserProjects();
		if (groups.size() > 0) {
			user.setGroupId(groups.get(0).getGroupId());
		}
		projectList = projectService.getProjectByGroup(user.getGroupId());
		List<Role> roles = new ArrayList<Role>(user.getUserRoles());
		if (roles.size() > 0) {
			user.setRoleId(roles.get(0).getRoleId());
		}
		groupList = groupService.getAllGroupList();
		roleList = roleService.findAll();
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String doUserDetailUpdate() {
		//执行用户修改
		user.setUserCode(user.getUserCode().toUpperCase());
		try {
			if(!userService.getUserByCode(user.getUserCode()).getPassword().equals(user.getPassword()))
				user.setPassword(Encode.getEncode(user.getPassword()));
			 user.setGroupId(Integer.parseInt(userGroupId));
			 userService.updateUser(user);
			 this.doUserProjectSave(user.getUserId());
			 user = userService.getUserById(user.getUserId());
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String doUserProjectSave(Integer userId){
		 try{
			 userService.deleteUserProject(userId);
			if(this.userProjectIds != null && this.userProjectIds.length != 0 && !"".equals(this.userProjectIds[0])){
			 for(int i=0; i<userProjectIds[0].split(",").length; i++){
				 UserProject up = new UserProject();
				 UserProjectId upId = new UserProjectId();
				 upId.setProjectId(userProjectIds[0].split(",")[i]);
				 
				 upId.setUserId(userId);
				 up.setId(upId);
				 userService.saveUserProject(up);			 
			  }
			}	
		}catch (BaseException e) {
			e.printStackTrace();
		}		
		return SUCCESS;
	}	
	
	/**
	 * 方法说明：初始化更新权限页面数据，然后跳转到权限更新页面
	 * 
	 * @return
	 * @throws BaseServiceException
	 */
	public String goUserUpdate() {
		//Integer userId = this.userIdAry[0];
		try{
		user = userService.getUserById(userId);
		List<Group> groups = new ArrayList<Group>(user.getUserGroups());
		if (groups.size() > 0) {
			user.setGroupId(groups.get(0).getGroupId());
		}
		List<Role> roles = new ArrayList<Role>(user.getUserRoles());
		if (roles.size() > 0) {
			user.setRoleId(roles.get(0).getRoleId());
		}
		groupList = groupService.getAllGroupList();
		roleList = roleService.findAll();
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 方法说明：AJAX检查用户CODE是否存在
	 * 
	 * @return
	 * @throws IOException 
	 */
	public String checkUserCode() throws IOException {
		 PrintWriter out = this.getResponse().getWriter();
		/** 判断当前CODE是否存在 **/
		  try {
			if(userService.isExistUserCode(userCode)){
				  out.print("exist");
				  out.flush();
			  }else{
				  out.print("noexist");
				  out.flush();
			  }
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			out.close();
		}
		return null;
	}
	
	/**
	 * 方法说明：AJAX检查用户mail是否存在
	 * 
	 * @return
	 * @throws IOException 
	 */
	public String checkUserMail() throws IOException {
		 PrintWriter out = this.getResponse().getWriter();
		/** 判断当前CODE是否存在 **/
		  try {
			if(userService.isExistUserEmail(email)){
				  out.print("exist");
				  out.flush();
			  }else{
				  out.print("noexist");
				  out.flush();
			  }
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			out.close();
		}
		return null;
	}
	
	/**
	 * List all the applies that point to that manager
	 * @return
	 */
	public String applicationList(){
		UserVo vo = new UserVo();
		vo.setConfirmUserId(String.valueOf(UserSessionUtil.getUser().getUserId()));
		vo.setStatus(TitanContent.USER_STATUS_APPLYING);
		try {
			userList = userService.getAllUserByUserVo(vo);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	/**
	 * Approve a apply
	 * @return
	 */
	public String applicationApprove(){
//		Integer userId = this.userIdAry[0];
		User u = null;
		try{
			u = userService.getUserById(userId);
			u.setConfirmUserId(null);
			u.setStatus(TitanContent.USER_STATUS_NORMAL);
			u.setCreateUser(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
			u.setCreateDate(DateUtils.getCurrentDate());
			userService.saveUser(u);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		EmailManageAction email = new EmailManageAction();
		String emailContext = createEmailContext("approve", u.getUserCode());
		email.sendEmail("You have been approved!", emailContext, u.getMail());
		return SUCCESS;
	}
	
	public String applicationReject(){
//		Integer userId = this.userIdAry[0];
		User u = null;
		try {
			u = userService.getUserById(userId);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.doUserRemove();
		
		EmailManageAction email = new EmailManageAction();
		String emailContext = createEmailContext("reject", u.getUserCode());
		email.sendEmail("You have been reject!", emailContext, u.getMail());
		return SUCCESS;
	}
	
	private String createEmailContext(String type, String userName){
		StringBuffer sb = new StringBuffer();
		sb.append("Hi ");
		sb.append(userName);
		sb.append("\n");
		if("approve".equals(type)){
			sb.append("    You have been approved of register Titan!\n\n");
		}
		else if("reject".equals(type)){
			sb.append("    You have been rejected of register Titan! The reason is as follows: " + rejectReason + " \n");
		}
		sb.append("Best Regard");
		return sb.toString();
	}
	
	public String goHistory(){
		return SUCCESS;
	}
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public Page getUserPages() {
		return userPages;
	}

	public void setUserPages(Page userPages) {
		this.userPages = userPages;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public UserVo getUserVo() {
		return userVo;
	}

	public void setUserVo(UserVo userVo) {
		this.userVo = userVo;
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
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer[] getUserIdAry() {
		return userIdAry;
	}

	public void setUserIdAry(Integer[] userIdAry) {
		this.userIdAry = userIdAry;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}
	

	public Integer getUserId() {
		return userId;
	}


	public void setUserId(Integer userId) {
		this.userId = userId;
	}


	public String getRejectReason() {
		return rejectReason;
	}


	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}


	public List<Project> getProjectList() {
		return projectList;
	}


	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}


	public ProjectService getProjectService() {
		return projectService;
	}


	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}


	public String[] getUserProjectIds() {
		return userProjectIds;
	}


	public void setUserProjectIds(String[] userProjectIds) {
		this.userProjectIds = userProjectIds;
	}


	public String getUserGroupId() {
		return userGroupId;
	}


	public void setUserGroupId(String userGroupId) {
		this.userGroupId = userGroupId;
	}
	
	
}
