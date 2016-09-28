package com.hp.titan.test.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.hp.app.action.DefaultBaseAction;
import com.hp.app.common.util.JsonUtil;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.model.Role;
import com.hp.titan.auth.service.UserService;
import com.hp.titan.auth.vo.UserVo;
import com.hp.titan.common.util.UserSessionUtil;
import com.hp.titan.project.model.Project;
import com.hp.titan.project.model.ProjectModule;
import com.hp.titan.project.service.ProjectService;
import com.hp.titan.test.model.TestPlanCase;
import com.hp.titan.test.model.TestPlanCaseId;
import com.hp.titan.test.model.Testcase;
import com.hp.titan.test.model.Testplan;
import com.hp.titan.test.service.TestcaseService;
import com.hp.titan.test.service.TestplanService;
import com.hp.titan.test.vo.TestplanVo;


public class TestplanAction extends DefaultBaseAction {
			
	private static final long serialVersionUID = 1L;
	
	public TestplanService testplanService;
	public UserService userService;
	public ProjectService projectService;
	private TestcaseService testcaseService;
	
	private Testplan testplan;
	private List<User> userList; 
	private List<ProjectModule> moduleList;
	private UserVo userVo = new UserVo();
	private List<Project> projectList;
	private List<Testplan> testplanList;
	private String testplanName;
	private List<TestplanVo> testplanVoList;
	private TestplanVo testplanVo;
	private String testplanId;
	private String[] testplanIdAry;
	
	private String testPlanJson;
	private String testCasesJson;
	private int isAdmin;
	private String currentUserName;
	private String newplanName;
	
	private String navigation = " <div class=\"port_bar\"><a href=\"../project/projectList.do\" class=\"marfin_lef10\">" + UserSessionUtil.getDefaultProjectName() + "</a><span class=\"marfin_lef5\">></span><a href=\"../sprint/sprintList.do\" class=\"marfin_lef5\">Sprint</a><span class=\"marfin_lef5\">></span><a href=\"../test/testjobList.do\" class=\"marfin_lef5\">Test Job</a><span class=\"marfin_lef5\">></span><a href=\"../test/testplanList.do\" class=\"marfin_lef5\">Test Plan</a></div>";

 	
	public String goTestplanCreate(){
		testplan = new Testplan();
		isAdmin = 0;
		try {
			User currentUser = UserSessionUtil.getUser();
		    currentUserName = currentUser.getUserCode();
			List<Role> roles = new ArrayList<Role>(currentUser.getUserRoles());
			if (roles.size() > 0) {
				String roleName = roles.get(0).getRoleName();	
				if("ADMIN".equals(roleName)) isAdmin = 1;
			}
			Set<Project> s = UserSessionUtil.getUser().getUserProjects();
			userList = userService.getAllUserByGroupId(currentUser.getGroupId().toString());
			if(isAdmin == 1){
				projectList = projectService.getAllProjectList();
			}
			else{
				projectList = new ArrayList<Project>();
				if(null != s && s.size() != 0){
					Iterator<Project> it = s.iterator();
					while(it.hasNext()){
						Project p = it.next();
						if(p.getIsValid() == 0){
							projectList.add(p);
						}
					}
				}
			}
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String goTestplanList(){
		isAdmin = 0;
		User currentUser = UserSessionUtil.getUser();
	    currentUserName = currentUser.getUserCode();
		List<Role> roles = new ArrayList<Role>(currentUser.getUserRoles());
		if (roles.size() > 0) {
			String roleName = roles.get(0).getRoleName();	
			if("ADMIN".equals(roleName)) isAdmin = 1;
		}
		try {
			//testplanList = testplanService.getAllTestplan();
//			Set<Project> s = UserSessionUtil.getUser().getUserProjects();
//			if(null == s || s.size() == 0){
//				testplanVoList = null;
//			}
			if(null == testplanVo){
				testplanVo = new TestplanVo(); 
			}
			if(isAdmin == 1){
				testplanVoList = testplanService.findAllTestplan(testplanVo, null);	
				projectList = projectService.getAllProjectList();
			}
			else{
				String defaultProjectId = UserSessionUtil.getDefaultProject();
				if(null == defaultProjectId || "".equals(defaultProjectId)){
					testplanVoList = null;
				}
				else{					
					testplanVo.setProjectId(defaultProjectId);
					testplanVoList = testplanService.findAllTestplan(testplanVo, null);	
					projectList = new ArrayList<Project>();
				    projectList.add(projectService.getProjectById(defaultProjectId));
				}
			}
		    userList = userService.getAllUserByUserVo(userVo);
//		    projectList = projectService.getAllProjectList(s);
//		    if(null != s && s.size() != 0){
//				Iterator<Project> it = s.iterator();
//				while(it.hasNext()){
//					Project p = it.next();
//					if(p.getIsValid() == 0){
//						projectList.add(p);
//					}
//				}
//			}
		    
		    
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String doTestplanSave(){
		try{
			String newPlanCode = testplanService.newTestplanCodeByProject(testplan.getProjectId());
			testplan.setTestplanCode(newPlanCode);
			int currentUserId = UserSessionUtil.getUser().getUserId();
			Date currentDate = new Date();
			testplan.setCreateUserId(currentUserId);
			testplan.setCreateDate(currentDate);
			testplanService.saveTestplan(testplan);
			this.goPlanManage();
			
		}catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String doTestplanUpdate(){
		try{			
			int currentUserId = UserSessionUtil.getUser().getUserId();
			Date currentDate = new Date();
			testplan.setUpdateUserId(currentUserId);
			testplan.setUpdateDate(currentDate);
			testplanService.saveTestplan(testplan);	
			this.goPlanManage();
		}catch (BaseException e) {
			e.printStackTrace();
		}		
		return SUCCESS;
	}
	
	public String goTestplanCopy(){
		String testplanId = testplanIdAry[0];
		Testplan newtestPlan = new Testplan();
		List<TestPlanCase> newtestplanCase = new ArrayList<TestPlanCase>();
		try {
			Testplan oldtestplan = testplanService.getTestplanById(testplanId);
			List<TestPlanCase> oldtestplanCase = testplanService.getPlanCase(testplanId);
			
			// save test plan
			newtestPlan.setIsValid(oldtestplan.getIsValid());
			newtestPlan.setOwnerId(oldtestplan.getOwnerId());
			newtestPlan.setProjectId(oldtestplan.getProjectId());
			newtestPlan.setRemark(oldtestplan.getRemark());
			newtestPlan.setSort(oldtestplan.getSort());
			newtestPlan.setTestplanName(newplanName);
			newtestPlan.setTestplanType(oldtestplan.getTestplanType());
			newtestPlan.setTestplanCode(testplanService.newTestplanCodeByProject(oldtestplan.getProjectId()));
			testplanService.saveTestplan(newtestPlan);
			
			// save test plan case
			Iterator<TestPlanCase> iter = oldtestplanCase.iterator();
			while(iter.hasNext()){
				TestPlanCase oldtpc = iter.next();
				TestPlanCase newtpc = new TestPlanCase();
				TestPlanCaseId newtpcId = new TestPlanCaseId();
				newtpcId.setTestplanId(newtestPlan.getTestplanId());
				newtpcId.setTestcaseId(oldtpc.getId().getTestcaseId());
				newtpc.setId(newtpcId);
				newtpc.setSort(oldtpc.getSort());
				newtestplanCase.add(newtpc);
			}
			testplanService.planManagement(newtestPlan.getTestplanId(), newtestplanCase);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public void checkTestplanName(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			if (testplanService.isExistTestplan(testplanName)) {
				out.print("exist");
				out.flush();
			} else {
				out.print("noexist");
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	public String goTestplanEdit(){
		if ((this.testplanId != null) && !this.testplanId.equals("")){
			String id = this.testplanId;			
			isAdmin = 0;
			try {
				User currentUser = UserSessionUtil.getUser();
			    currentUserName = currentUser.getUserCode();
				List<Role> roles = new ArrayList<Role>(currentUser.getUserRoles());
				if (roles.size() > 0) {
					String roleName = roles.get(0).getRoleName();	
					if("ADMIN".equals(roleName)) isAdmin = 1;
				}
				Set<Project> s = UserSessionUtil.getUser().getUserProjects();
				testplan = testplanService.getTestplanById(id);
				userList = userService.getAllUserByGroupId(currentUser.getGroupId().toString());
				if(isAdmin == 1){
					projectList = projectService.getAllProjectList();
				}
				else{
					projectList = new ArrayList<Project>();
					if(null != s && s.size() != 0){
						Iterator<Project> it = s.iterator();
						while(it.hasNext()){
							Project p = it.next();
							if(p.getIsValid() == 0){
								projectList.add(p);
							}
						}
					}
				}
			} catch (BaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		return SUCCESS;		
	}
	
	public String doTestplanRemove(){
		if ((this.testplanId != null) && !this.testplanId.equals("")){
			String id = this.testplanId;			
			try {
				testplan = testplanService.getTestplanById(id);
				testplan.setIsValid(1);
				int currentUserId = UserSessionUtil.getUser().getUserId();
				Date currentDate = new Date();
				testplan.setUpdateUserId(currentUserId);
				testplan.setUpdateDate(currentDate);
				testplanService.saveTestplan(testplan);
			} catch (BaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		
		return SUCCESS;		
	}
	
	public void goPlanManage(){
		try {
			testplan = testplanService.getTestplanById(testplan.getTestplanId());
			moduleList = projectService.getProjectModuleList(testplan.getProjectId());
			List<Testcase> testCaseList = testcaseService.getAllTestcaseByType(testplan.getTestplanType(), testplan.getProjectId());
			Set<String> s = new HashSet<String>();
			s = this.createTestPlanJson(testplan);
			this.createTestCasesJson(testCaseList, s);
		} catch (BaseException e) {
			e.printStackTrace();
		}
	}
	
	public void getPlanShow(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			testplan = testplanService.getTestplanById(testplanId);
			this.createTestPlanJson(testplan);
			out.print(testPlanJson);
			out.flush();
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			out.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public String doTestPlanManage(){
		List planCaseList = (ArrayList)JsonUtil.JsonToList(JsonUtil.getJsonArrayFromString(testPlanJson));
		Iterator l = planCaseList.iterator();
		Map planMap = (Map)l.next();
		String testPlanId = "";
		if("planFolder".equals(planMap.get("diyType"))){
			testPlanId = planMap.get("id").toString();
		}
		else{
			return SUCCESS;
		}
		List<TestPlanCase> testplancaseList = new ArrayList<TestPlanCase>();
		int i = 0;
		while(l.hasNext()){
			Map m = (Map)l.next();
			if("case".equals(m.get("diyType"))){
				TestPlanCaseId tpcId = new TestPlanCaseId();
				TestPlanCase tpc = new TestPlanCase();
				tpcId.setTestcaseId(m.get("id").toString());
				tpcId.setTestplanId(testPlanId);
				tpc.setId(tpcId);
				tpc.setSort(String.valueOf(i));
				testplancaseList.add(tpc);
			}
			i++;
		}
		try {
			testplanService.planManagement(testPlanId, testplancaseList);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	private Set<String> createTestPlanJson(Testplan testplan){
		Set<String> caseSet = new HashSet<String>();
		JSONArray jsonMembers = new JSONArray();  
		JSONObject member = new JSONObject();  
		member.put("id", testplan.getTestplanId());  
		member.put("pId", 0);  
		member.put("name",testplan.getTestplanName());  
		member.put("testPlanCode", testplan.getTestplanCode());  
		member.put("isParent",true);  
		member.put("open", true);  
		member.put("diyType", "planFolder");  
		jsonMembers.add(member);
		List<Testcase> tcList = new ArrayList<Testcase>();
		try {
			tcList = testplanService.getTestcaseByPlan(testplan.getTestplanId());
		} catch (BaseException e) {
			e.printStackTrace();
		}
		Iterator<Testcase> l = tcList.iterator();
		while(l.hasNext()){
			Testcase tc = l.next();
			caseSet.add(tc.getCaseId());
			JSONObject memberCase = new JSONObject();
			memberCase.put("id", tc.getCaseId());
			memberCase.put("pId", testplan.getTestplanId());
			memberCase.put("name", tc.getCaseName());
			memberCase.put("caseCode", tc.getCaseCode());
			memberCase.put("diyType", "case");
			memberCase.put("diyModule", tc.getModuleId());
			jsonMembers.add(memberCase);
		}
		String testPlanJsonTemp = jsonMembers.toString();
		testPlanJson = testPlanJsonTemp.substring(0,testPlanJsonTemp.length()-1)+",]";
		return caseSet;
	}
	
	private void createTestCasesJson(List<Testcase> testcaseList, Set<String> caseInPlan){
		StringBuffer s = new StringBuffer();
		s.append("[");
		if(null != testcaseList){
			s.append("{id:1, pId:0, name:\"TestCases\", isParent: true, open:true, diyType: \"caseFolder\", diyModule:\"none\"},");
			Iterator<Testcase> l = testcaseList.iterator();
			while(l.hasNext()){
				Testcase tc = l.next();
				if(caseInPlan.contains(tc.getCaseId())){
					continue;
				}
				s.append("{id:\"");
				s.append(tc.getCaseId());
				s.append("\", pId:1, name:\"");
				s.append(tc.getCaseName());
				s.append("\", diyType: \"case\",");
				s.append("diyModule: \"");
				s.append(tc.getModuleId());
				s.append("\"},");
			}
		}
		s.append("]");
		testCasesJson = s.toString().replace("},]", "}]");
	}	
	
	public void checkPlanHasBeenUsed(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			if(testplanService.checkCaseHasBeenUsed(testplanId)){
				out.print("used");
			}
			else{
				out.print("notused");
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}


	public TestplanService getTestplanService() {
		return testplanService;
	}

	public void setTestplanService(TestplanService testplanService) {
		this.testplanService = testplanService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public Testplan getTestplan() {
		return testplan;
	}

	public void setTestplan(Testplan testplan) {
		this.testplan = testplan;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public List<Project> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}
	public List<Testplan> getTestplanList() {
		return testplanList;
	}
	public void setTestplanList(List<Testplan> testplanList) {
		this.testplanList = testplanList;
	}

	public String getTestplanName() {
		return testplanName;
	}

	public void setTestplanName(String testplanName) {
		this.testplanName = testplanName;
	}

	public List<TestplanVo> getTestplanVoList() {
		return testplanVoList;
	}

	public void setTestplanVoList(List<TestplanVo> testplanVoList) {
		this.testplanVoList = testplanVoList;
	}

	public TestplanVo getTestplanVo() {
		return testplanVo;
	}

	public void setTestplanVo(TestplanVo testplanVo) {
		this.testplanVo = testplanVo;
	}

	public String getTestplanId() {
		return testplanId;
	}

	public void setTestplanId(String testplanId) {
		this.testplanId = testplanId;
	}

	public TestcaseService getTestcaseService() {
		return testcaseService;
	}

	public void setTestcaseService(TestcaseService testcaseService) {
		this.testcaseService = testcaseService;
	}

	public String getTestPlanJson() {
		return testPlanJson;
	}

	public void setTestPlanJson(String testPlanJson) {
		this.testPlanJson = testPlanJson;
	}

	public String getTestCasesJson() {
		return testCasesJson;
	}

	public void setTestCasesJson(String testCasesJson) {
		this.testCasesJson = testCasesJson;
	}

	public String[] getTestplanIdAry() {
		return testplanIdAry;
	}

	public void setTestplanIdAry(String[] testplanIdAry) {
		this.testplanIdAry = testplanIdAry;
	}

	public List<ProjectModule> getModuleList() {
		return moduleList;
	}

	public void setModuleList(List<ProjectModule> moduleList) {
		this.moduleList = moduleList;
	}

	public String getNavigation() {
		return navigation;
	}

	public void setNavigation(String navigation) {
		this.navigation = navigation;
	}

	public int getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getCurrentUserName() {
		return currentUserName;
	}

	public void setCurrentUserName(String currentUserName) {
		this.currentUserName = currentUserName;
	}

	public String getNewplanName() {
		return newplanName;
	}

	public void setNewplanName(String newplanName) {
		this.newplanName = newplanName;
	}
}