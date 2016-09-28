package com.hp.titan.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.cxf.common.util.StringUtils;

import com.hp.app.action.DefaultBaseAction;
import com.hp.app.common.util.DateUtils;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.auth.model.Auth;
import com.hp.titan.auth.model.Group;
import com.hp.titan.auth.model.Role;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.model.UserProject;
import com.hp.titan.auth.model.UserProjectId;
import com.hp.titan.auth.model.ViewMenuTree;
import com.hp.titan.auth.service.GroupService;
import com.hp.titan.auth.service.MenuService;
import com.hp.titan.auth.service.RoleService;
import com.hp.titan.auth.service.UserService;
import com.hp.titan.common.constants.TitanContent;
import com.hp.titan.common.util.Encode;
import com.hp.titan.common.util.UserSessionUtil;
import com.hp.titan.mytitan.model.ExpAccount;
import com.hp.titan.mytitan.model.InBox;
import com.hp.titan.mytitan.model.Point;
import com.hp.titan.mytitan.service.MyTitanService;
import com.hp.titan.notify.EmailManageAction;
import com.hp.titan.project.model.Project;
import com.hp.titan.project.service.ProjectService;

public class LoginAction extends DefaultBaseAction {
	
	private String usercode;
	private String password;
	private String email;
	private String message;
	private String userRoleId;
	private String confirmUserId;
	private String userGroupId;
	private String[] userProjectIds;
	
	private List<ViewMenuTree> userMenuList;
	private MenuService menuService;
	private List<Group> groupList; 
	private List<Role> roleList; 
	private List<User> manageList; 
	private List<Project> projectList;
	
	private User user;
	//private UserGroup userGroup;
	//private UserRole userRole;
	private UserService userService;
	private RoleService roleService;
	private GroupService groupService;
	private ProjectService projectService;
	private MyTitanService mytitanService;
	
//	
//	public LoginAction(WebApplicationContext ctx) {
//		mytitanService = (MyTitanService) ctx.getBean("mytitanService");
//	}
//	
	public String doCheckLogin() throws IOException{
		PrintWriter out = this.getResponse().getWriter();
		try {
			message = userService.checkUserAndPassword(usercode.toUpperCase(),password);
			out.print(message);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			out.flush();
		}
		return null;
	}

	
	

	public String doLogin(){
		if(!StringUtils.isEmpty(usercode)){
			User user = null;
			Integer  groupId = 0;
			Point point = null;
			ExpAccount exp = null;
			Integer inBoxCount = 0;
			
			try {
				user = userService.getUserByCode(usercode.toUpperCase());
				point = mytitanService.getPointByUserId(user.getUserId());
				exp = mytitanService.getExpByUserId(user.getUserId());
			    groupId = groupService.findGroupUserById(user.getUserId()).getGroup().getGroupId();
			    InBox inbox = new InBox();
			    inbox.setUserId(user.getUserId());
			    inbox.setIsReaded(TitanContent.INBOX_READED_NO);
			    inBoxCount = mytitanService.getInBoxCount(inbox);
			} catch (BaseDaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BaseException e) {
				e.printStackTrace();
			}
			 Map<String, String> authMap = new HashMap<String, String>();
			Iterator roleIter =user.getUserRoles().iterator();
			while(roleIter.hasNext()){
				Role role =	(Role)roleIter.next();
				Iterator authIter =role.getAuths().iterator();
				while(authIter.hasNext()){
					Auth auth =	(Auth)authIter.next();
					authMap.put(auth.getAuthName(), auth.getAuthName());
				}
			}
			
			user.setAuthMap(authMap);
			user.setGroupId(groupId);
			user.setLoginTime(new Date());
			UserSessionUtil.setUser(user);
			UserSessionUtil.setPoint(null != point?point.getPoints():0);
			UserSessionUtil.setExp(null != exp?exp.getExpTotal():0);
			UserSessionUtil.setInbox(inBoxCount);
//			Iterator<Project> projIter = user.getUserProjects().iterator();
//			if(projIter.hasNext()){
//				UserSessionUtil.setDefaultProject(projIter.next().getProjectId());
//			}
			Project p = new Project();
			if(null != user.getDefaultprojectId() && !"".equals(user.getDefaultprojectId())){
				UserSessionUtil.setDefaultProject(user.getDefaultprojectId());
				try {
					p = projectService.getProjectById(user.getDefaultprojectId());
					UserSessionUtil.setDefaultProjectName(p.getProjectName());
				} catch (BaseDaoException e) {
					e.printStackTrace();
				}
				
			}
			else if(null != user.getUserProjects() && user.getUserProjects().size() != 0){
				Iterator<Project> projIter = user.getUserProjects().iterator();
				if(projIter.hasNext()){
					p = projIter.next();
					UserSessionUtil.setDefaultProject(p.getProjectId());
					UserSessionUtil.setDefaultProjectName(p.getProjectName());
				}
			}
		}
		return SUCCESS;
	}
	//进入主页 获得用户菜单
	public String goIndexMain(){
		try {
			userMenuList = menuService.getUserMenuList(UserSessionUtil.getUser());
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	 
	public String doLogout(){
		UserSessionUtil.clearSession();
		return SUCCESS;
	}
	
	
	/**
	 * Go to Register page
	 * @return
	 */
	public String goRegister(){
		user = new User();
		try {
			groupList = groupService.getAllGroupList();
			roleList = roleService.findAll();
			manageList = roleService.findUserByRoleName(TitanContent.ROLE_MANAGER);
			projectList = projectService.getAllProjectList();
			
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//manageList = userRoleList.get(0).getUser();
		return SUCCESS;
	}
	
	/**
	 * Register
	 * @return
	 */
	public String doRegister(){
		try{
			if(userService.isExistUserCode(usercode)){
				return SUCCESS;
			}
			// Save user
		    user = new User();
			user.setUserCode(usercode);
			user.setMail(email);
			user.setPassword(Encode.getEncode(password));
			user.setConfirmUserId(Integer.valueOf(confirmUserId));
			user.setStatus(TitanContent.USER_STATUS_APPLYING);
			
			user.setCreateUser(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
			user.setCreateDate(DateUtils.getCurrentDate());
		
			userService.saveUser(user);
			
			// Save Role
			Role role =roleService.getRole(userRoleId);
			Set<Role> userRoles = new HashSet<Role>();
			if(role!=null){
				userRoles.add(role);
			}
			user.setUserRoles(userRoles);
			roleService.doAddUserToRole(new String[] { user.getUserId().toString()},role);
			
			// Save gourp
			groupService.addUserToGroup(Integer.valueOf(userGroupId), new Integer[] { user.getUserId() });
			
			// Save projects
			this.doUserProjectSave(user.getUserId());
			
			// Send Email to confirm manager
			User managerInfo = new User();
			managerInfo = userService.getUserById(Integer.valueOf(confirmUserId));
			String managerName = managerInfo.getUserCode();
			String emailAdd = managerInfo.getMail();
			String emailContext = createEmailContext(managerName, usercode);
			EmailManageAction email = new EmailManageAction();
			email.sendEmail("You got an application!", emailContext, emailAdd);
		}catch (BaseException e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String doUserProjectSave(Integer userId){
		 try{
			if(this.userProjectIds != null && this.userProjectIds.length != 0){
			 for(int i=0; i<userProjectIds[0].split(",").length; i++){
				 if("".equals(userProjectIds[0].split(",")[i])){
					 continue;
				 }
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
 
	private String createEmailContext(String managerName, String userName){
		StringBuffer sb = new StringBuffer();
		sb.append("Hi ");
		sb.append(managerName);
		sb.append(",\n");
		sb.append(userName);
		sb.append(" is applying for titan, for detail information please login http://\n\n\n");
		sb.append("Thanks and regard");
		return sb.toString();
	}

	public String getUsercode() {
		return usercode;
	}


	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public UserService getUserService() {
		return userService;
	}


	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public RoleService getRoleService() {
		return roleService;
	}


	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}

	public List<ViewMenuTree> getUserMenuList() {
		return userMenuList;
	}


	public void setUserMenuList(List<ViewMenuTree> userMenuList) {
		this.userMenuList = userMenuList;
	}

	public MenuService getMenuService() {
		return menuService;
	}


	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	public List<Group> getGroupList() {
		return groupList;
	}

	public MyTitanService getMytitanService() {
		return mytitanService;
	}




	public void setMytitanService(MyTitanService mytitanService) {
		this.mytitanService = mytitanService;
	}




	public void setGroupList(List<Group> groupList) {
		this.groupList = groupList;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public GroupService getGroupService() {
		return groupService;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public List<User> getManageList() {
		return manageList;
	}

	public void setManageList(List<User> manageList) {
		this.manageList = manageList;
	}

	public String getConfirmUserId() {
		return confirmUserId;
	}

	public void setConfirmUserId(String confirmUserId) {
		this.confirmUserId = confirmUserId;
	}

	public String getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(String userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(String userGroupId) {
		this.userGroupId = userGroupId;
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

}
