package com.hp.titan.test.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.neoremind.sshxcute.core.ConnBean;
import net.neoremind.sshxcute.core.IOptionName;
import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;
import net.sf.json.JSONArray;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.hp.app.action.DefaultBaseAction;
import com.hp.app.common.util.DateUtils;
import com.hp.app.common.util.JsonUtil;
import com.hp.app.common.util.SSHExecClient;
import com.hp.app.config.SysConfiger;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.auth.model.Role;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.service.UserService;
import com.hp.titan.auth.vo.UserVo;
import com.hp.titan.common.constants.TitanContent;
import com.hp.titan.common.util.UserSessionUtil;
import com.hp.titan.common.vo.DefectVo;
import com.hp.titan.common.vo.LoginUserVo;
import com.hp.titan.notify.EmailManageAction;
import com.hp.titan.project.model.DedicatedServer;
import com.hp.titan.project.model.Project;
import com.hp.titan.project.model.ProjectModule;
import com.hp.titan.project.service.ProjectService;
import com.hp.titan.project.vo.ReportVo;
import com.hp.titan.server.model.Server;
import com.hp.titan.server.service.ServerService;
import com.hp.titan.sprint.model.Sprint;
import com.hp.titan.sprint.service.SprintService;
import com.hp.titan.sprint.vo.SprintVo;
import com.hp.titan.test.model.CaseDefect;
import com.hp.titan.test.model.CaseLog;
import com.hp.titan.test.model.DefaultParameter;
import com.hp.titan.test.model.DefaultParameterId;
import com.hp.titan.test.model.DefectInfo;
import com.hp.titan.test.model.Parameter;
import com.hp.titan.test.model.ParameterData;
import com.hp.titan.test.model.RunCase;
import com.hp.titan.test.model.RunCaseServer;
import com.hp.titan.test.model.RunJob;
import com.hp.titan.test.model.TestJobCase;
import com.hp.titan.test.model.TestJobCaseId;
import com.hp.titan.test.model.TestJobPlan;
import com.hp.titan.test.model.TestJobPlanId;
import com.hp.titan.test.model.TestJobServer;
import com.hp.titan.test.model.TestJobServerId;
import com.hp.titan.test.model.Testcase;
import com.hp.titan.test.model.Testjob;
import com.hp.titan.test.model.Testplan;
import com.hp.titan.test.model.UserTag;
import com.hp.titan.test.model.UserTagId;
import com.hp.titan.test.service.TestcaseService;
import com.hp.titan.test.service.TestjobService;
import com.hp.titan.test.service.TestplanService;
import com.hp.titan.test.vo.DefectInfoVo;
import com.hp.titan.test.vo.ParameterVo;
import com.hp.titan.test.vo.RunCaseVo;
import com.hp.titan.test.vo.TestcaserunVo;
import com.hp.titan.test.vo.TestjobInGroupVo;
import com.hp.titan.test.vo.TestjobVo;
import com.hp.titan.test.vo.TestjobrunVo;
import com.jcraft.jsch.JSchException;


public class TestjobAction extends DefaultBaseAction implements Runnable {
			
	private static final long serialVersionUID = 1L;
	
	private TestjobService testjobService;
	private UserService userService;
	private ProjectService projectService;
	private TestcaseService testcaseService;
	private TestplanService testplanService;
	private ServerService serverService;
	private SprintService sprintService;
	
	private Testjob testjob;
	private List<User> userList; 
	private UserVo userVo = new UserVo();
	private List<Project> projectList;
	private List<Testjob> testjobList;
	private List<Testplan> testplanList;
	private List<TestJobCase> testjobcaseList;
	private List<TestJobPlan> testjobplanList;
	private List<Server> serverList;
	private List<Server> jobserverList;
	private List<Parameter> paramList;
	private List<ProjectModule> moduleList;
	private String testjobName;
	private List<TestjobVo> testjobVoList;
	private TestjobVo testjobVo;
	private ParameterVo parameterVo;
	private String testjobId;
	private String testCaseId;
	private String testPlanId;
	private String relatelogPath;
	private String[] testjobIdAry;
	private String[] runcaseIdAry;
	
	private String testJobJson;
	private String paraJson;
	private String runjobcaseJson;
	private String testJobJsonDB;
	private String testCaseJson;
	private String testCaseParaJson;
	private String testJobResultJson;
	private String paraHtml;
	private String serverHtml;
	private String runJobId;
	private String defectNo;
	private String defectType;
	
	private String selectedProjectid;
	private String selectedSprintid;
	private String selectedTestjobid;
	
	private List<Testcase> testcaseList;
	private Testcase testcase;
	private String testcaseId;
	private String[] jobServerIds;
	private TestJobServer testJobServer;
    private TestJobServerId testJobServerId;
    private List<RunJob> runJobList;
    private RunJob runJob;
    private TestjobrunVo testjobrunVo;
    private RunCase runCase; 
    private List<RunCase> runCaseList;
    private String runCaseId;
    private String runCaseState;
    private String runCaseNote;
    private int isAdmin;
    private String currentUserName;
    
    private List<Sprint> sprintList;
    private String sprintId;
    private String navigation = " <div class=\"port_bar\"><a href=\"../project/projectList.do\" class=\"marfin_lef10\">" + UserSessionUtil.getDefaultProjectName() + "</a><span class=\"marfin_lef5\">></span><a href=\"../sprint/sprintList.do\" class=\"marfin_lef5\">Sprint</a><span class=\"marfin_lef5\">></span><a href=\"../test/testjobList.do\" class=\"marfin_lef5\">Test Job</a></div>";
    private ReportVo reportVo;
    private List<DefectInfoVo> defectinfoVoList;
    private List<UserTag> usertagList;
    private List<TestjobInGroupVo> testjobingroupvoList; 
    
    SSHExecClient ssh = null;
    
	public String goTestjobCreate(){
		testjob = new Testjob();
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
			serverList = serverService.getServerByOwner(currentUser.getUserId());
			usertagList = testjobService.getUserTag(currentUser.getUserId());
			
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
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String goTestjobList(){
		isAdmin = 0;
		testjobingroupvoList = new  ArrayList<TestjobInGroupVo>();
		try {
			User currentUser = UserSessionUtil.getUser();
		    currentUserName = currentUser.getUserCode();
			List<Role> roles = new ArrayList<Role>(currentUser.getUserRoles());
			if (roles.size() > 0) {
				String roleName = roles.get(0).getRoleName();	
				if("ADMIN".equals(roleName)) isAdmin = 1;
			}
			//testjobList = testjobService.getAlltestjob();
			if(null == testjobVo){
				testjobVo = new TestjobVo();
			}
			if(this.sprintId != null){
				testjobVo.setSprintId(sprintId);
			}
//			Set<Project> s = UserSessionUtil.getUser().getUserProjects();
//			if(null == s || s.size() == 0){
//				testjobVoList = null;
//			}
			if(null == testjobVo){
				testjobVo = new TestjobVo();
			}
			if(isAdmin == 1){
				testjobVoList = testjobService.findAllTestjob(testjobVo, null);
				if(null != testjobVoList && testjobVoList.size() != 0){
					testjobingroupvoList = this.putTestJobInGroup();
				}
				projectList = projectService.getAllProjectList();
			}
			else{
				String defaultProjectId = UserSessionUtil.getDefaultProject();
				
				testjobVo.setProjectId(defaultProjectId);
				if(null == defaultProjectId || "".equals(defaultProjectId)){
					testjobVoList = null;
				}
				else{
					testjobVoList = testjobService.findAllTestjob(testjobVo, null);
					if(null != testjobVoList && testjobVoList.size() != 0){
						testjobingroupvoList = this.putTestJobInGroup();
					}
					projectList = new ArrayList<Project>();
				    projectList.add(projectService.getProjectById(defaultProjectId));
				}
			}
		    userList = userService.getAllUserByUserVo(userVo);
		    
		    //sprintList =  sprintService.findSprintByProject(projectList.get(1).getProjectId());
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
	
	private List<TestjobInGroupVo> putTestJobInGroup(){
		List<TestjobInGroupVo> testjobInGroupList = new ArrayList<TestjobInGroupVo>();
		// Get the first tag
		String tag = "";
		Iterator<TestjobVo> iter = testjobVoList.iterator();
		TestjobVo tjv = iter.next();
		tag = tjv.getTag();
		if(null == tag){
			tag = "other";
		}
		List<TestjobVo> tjVoList = new ArrayList<TestjobVo>();
		tjVoList.add(tjv);
		TestjobInGroupVo tjInG = new TestjobInGroupVo();
		while(iter.hasNext()){
			tjv = iter.next();
			if(tag.equals(tjv.getTag())){
				tjVoList.add(tjv);
			}
			else{
				tjInG.setTag(tag);
				tjInG.setTestjobVoList(tjVoList);
				testjobInGroupList.add(tjInG);
				
				tag = tjv.getTag();
				tjVoList = new ArrayList<TestjobVo>();
				tjVoList.add(tjv);
				tjInG = new TestjobInGroupVo();
			}
		}
		tjInG.setTag(tag);
		tjInG.setTestjobVoList(tjVoList);
		testjobInGroupList.add(tjInG);
		return testjobInGroupList;
	}
	
	public String doTestjobSave(){
		try{
			User currentUser = UserSessionUtil.getUser();
			String newJobCode = testjobService.newTestjobCodeByProject();
			Date currentDate = new Date();
			testjob.setTestjobCode(newJobCode);
			testjob.setState(TitanContent.RUN_JOB_STATUS_ACTIVE);
			testjob.setCreateUserId(currentUser.getUserId());
			testjob.setCreateDate(currentDate);
//			testjob.setUpdateDate(currentDate);		
			if(null == testjob.getTag() || "".equals(testjob.getTag())){
				testjob.setTag("other");
			}
			testjobService.saveTestjob(testjob);
			if(null != testjob.getTag() && !"".equals(testjob.getTag())){
				UserTagId utId = new UserTagId();
				UserTag ut = new UserTag();
				utId.setUserId(currentUser.getUserId());
				utId.setTag(testjob.getTag());
				ut.setId(utId);
				testjobService.saveUserTag(ut);
			}
			this.doTestjobServerSave(testjob);
			this.goTestJobManage();
		}catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String doTestjobServerSave(Testjob testjob){
	  try{
		if(this.jobServerIds != null && this.jobServerIds.length != 0){
		 for(int i=0; i<jobServerIds[0].split(",").length; i++){
			 if(!"".equals(jobServerIds[0].split(",")[i])){
				 testJobServer = new TestJobServer();
				 testJobServerId = new TestJobServerId();
				 testJobServerId.setServerId(jobServerIds[0].split(",")[i]);
				 testJobServerId.setTestjobId(testjob.getTestjobId());
				 testJobServer.setId(testJobServerId);
				 testjobService.saveTestJobServer(testJobServer);
			 }
		  }
		 }	
		}catch (BaseException e) {
			e.printStackTrace();
		}		
		return SUCCESS;
	}	
	
	public String doTestjobUpdate(){
		User currentUser = UserSessionUtil.getUser();
		try{	
			Date currentDate = new Date();
			Testjob tj = testjobService.getTestjobById(testjob.getTestjobId());
			
//			testjob.setPlancaseJson(tj.getPlancaseJson());
			testjob.setPlancaseJson(this.createTestJobPlanCaseJsonAll(testjob));
			testjob.setCreateDate(tj.getCreateDate());
			testjob.setCreateUserId(tj.getCreateUserId());
			testjob.setState(TitanContent.RUN_JOB_STATUS_ACTIVE);
			testjob.setUpdateDate(currentDate);
			testjob.setUpdateUserId(currentUser.getUserId());
			if(null == testjob.getTag() || "".equals(testjob.getTag())){
				testjob.setTag("other");
			}
			if(null != testjob.getTag() && !"".equals(testjob.getTag())){
				UserTagId utId = new UserTagId();
				UserTag ut = new UserTag();
				utId.setUserId(currentUser.getUserId());
				utId.setTag(testjob.getTag());
				ut.setId(utId);
				testjobService.saveUserTag(ut);
			}
			testjobService.saveTestjob(testjob);			
			testjobService.clearServerFromJob(testjob.getTestjobId());
			this.doTestjobServerSave(testjob);
			this.goTestJobManage();
		}catch (BaseException e) {
			e.printStackTrace();
		}		
		return SUCCESS;
	}
	
	public String dotestjobRemove(){
		try{
			Testjob tj = testjobService.getTestjobById(testjobId);
			tj.setIsValid(1);
			int currentUserId = UserSessionUtil.getUser().getUserId();
			Date currentDate = new Date();
			tj.setUpdateUserId(currentUserId);
			tj.setUpdateDate(currentDate);
			testjobService.saveTestjob(tj);
		}catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public void checkTestjobName(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			if (testjobService.isExistTestjob(testjobName)) {
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
	public String goTestjobEdit(){
		if ((this.testjobId != null) && !this.testjobId.equals("")){
			String id = this.testjobId;	
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
				testjob = testjobService.getTestjobById(id);
				userList = userService.getAllUserByGroupId(currentUser.getGroupId().toString());
				usertagList = testjobService.getUserTag(currentUser.getUserId());
				serverList = serverService.getAllServerList();
//				serverList = serverService.getServerByOwner(currentUser.getUserId());
				jobserverList = testjobService.getJobServer(testjobId, 0);
				sprintList = sprintService.findSprintByProject(testjob.getProjectId());
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
				e.printStackTrace();
			}
		}	
		return SUCCESS;		
	}
	
	public String doTestjobRemove(){
		if ((this.testjobId != null) && !this.testjobId.equals("")){
			String id = this.testjobId;			
			try {
				testjob = testjobService.getTestjobById(id);
				testjob.setIsValid(1);
				testjobService.saveTestjob(testjob);
			} catch (BaseException e) {
				e.printStackTrace();
			}
		}	
		
		return SUCCESS;		
	}
	
	@SuppressWarnings("unchecked")
	public String goTestJobManage(){
		isAdmin = 0;
		try {
			User currentUser = UserSessionUtil.getUser();
		    currentUserName = currentUser.getUserCode();
			List<Role> roles = new ArrayList<Role>(currentUser.getUserRoles());
			if (roles.size() > 0) {
				String roleName = roles.get(0).getRoleName();	
				if("ADMIN".equals(roleName)) isAdmin = 1;
			}
//			testjob = testjobService.getTestjobById(this.testjobIdAry[0]);
			Set<Project> s = UserSessionUtil.getUser().getUserProjects();
			Set<String> planSet = new HashSet<String>();
			Set<String> caseSet = new HashSet<String>();
			jobserverList = testjobService.getJobServer(testjob.getTestjobId(), currentUser.getUserId());
			userList = userService.getAllUserByUserVo(userVo);
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
			if(null != testjob.getPlancaseJson() && !"".equals(testjob.getPlancaseJson())){
				List jobPlanCaseList = (ArrayList)JsonUtil.JsonToList(JsonUtil.getJsonArrayFromString(testjob.getPlancaseJson()));
				Map jobPlanCaseMap = (HashMap)jobPlanCaseList.get(0);
				List childrenList = (List)jobPlanCaseMap.get("children");
				if(null != childrenList && childrenList.size() != 0){
					Iterator l = childrenList.iterator();
					while(l.hasNext()){
						Map m = (Map)l.next();
						if("plan".equals(m.get("diyType"))){
							planSet.add(m.get("id").toString());
						}
						else if("caseInPlan".equals(m.get("diyType"))){
							continue;
						}
						else if("case".equals(m.get("diyType"))){
							caseSet.add(m.get("id").toString());
						}
					}
				}
			}
			moduleList = projectService.getProjectModuleList(testjob.getProjectId());
			List<Testcase> testCaseList = testcaseService.getAllTestcaseByType(testjob.getJobType(), testjob.getProjectId());
			List<Testplan> testPlanList = testplanService.getAllTestplanByType(testjob.getJobType(), testjob.getProjectId());
			
			if(null == testjob.getPlancaseJson() || "".equals(testjob.getPlancaseJson())){
				this.createTestJobJson(testjob);
			}
			else{
//				testJobJson = testjob.getPlancaseJson();
				testJobJson = this.createTestJobPlanCaseJsonAll(testjob);
			}
			this.createTestPlansCasesJson(testCaseList, testPlanList, planSet, caseSet);
			
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String goAutoTestJobRun(){
		this.testRunPage(testjobId);
		return SUCCESS;
	}
	
	
	public String goMTestJobRun(){
		if ((this.testjobId!= null) && !this.testjobId.equals("")){
			String id = this.testjobId;			
			try {
				Set<Project> s = UserSessionUtil.getUser().getUserProjects();
				runJobList = testjobService.getAllRunJob(id);
				testjob = testjobService.getTestjobById(id);
				userList = userService.getAllUserByUserVo(userVo);
//				projectList = projectService.getAllProjectList(s);
				projectList = new ArrayList<Project>();
				sprintList = sprintService.findSprintByProject(testjob.getProjectId());
				testplanList = testjobService.getTestplanByJobId(id);
				testcaseList = testjobService.getTestcaseByJobId(id);
				jobserverList = testjobService.getJobServer(id, UserSessionUtil.getUser().getUserId());
				
				if(null != s && s.size() != 0){
					Iterator<Project> it = s.iterator();
					while(it.hasNext()){
						Project p = it.next();
						if(p.getIsValid() == 0){
							projectList.add(p);
						}
					}
				}
				
				if (testplanList.size()!=0){
					for (Testplan tp : testplanList){
					  testcaseList.addAll(testplanService.getTestcaseByPlan(tp.getTestplanId()));
					}
				}				
				runJob = new RunJob();
				int currentUserId = UserSessionUtil.getUser().getUserId();
				Date currentDate = new Date();
				runJob.setJobId(id);
				runJob.setStartTime(currentDate);
				runJob.setResult(TitanContent.RUN_JOB_STATUS_ACTIVE);
				runJob.setIsDone(0);
				runJob.setOwner(UserSessionUtil.getUser());
				runJob.setCreateUserId(currentUserId);
				runJob.setIsValid(0);
				
				testjobService.saveRunJob(runJob);	
				
				if (testcaseList.size()!= 0){
					for (Testcase testcase : testcaseList){
						runCase = new RunCase();
						runCase.setCaseId(testcase.getCaseId());
						runCase.setRunJobId(runJob.getRunJobId());
						runCase.setState(TitanContent.RUN_CASE_STATUS_NOTRUN);
						runCase.setIsValid(0);
						testjobService.saveRunCase(runCase);
					}
				}				
				runCaseList = testjobService.getRunCaseByRunjobId(runJob.getRunJobId());
				
			} catch (BaseException e) {
				e.printStackTrace();
			}
		}	
		return SUCCESS;		
	}
	
	public String goViewMJobRun(){
		if ((this.runJobId!= null) && !this.runJobId.equals("")){
			String id = this.runJobId;
			try {
				Set<Project> s = UserSessionUtil.getUser().getUserProjects();
				runJob = testjobService.getRunJobById(id);
				testjob = testjobService.getTestjobById(runJob.getJobId());
				jobserverList = testjobService.getJobServer(runJob.getJobId(), UserSessionUtil.getUser().getUserId());
				userList = userService.getAllUserByUserVo(userVo);
//				projectList = projectService.getAllProjectList(s);
				projectList = new ArrayList<Project>();
				sprintList = sprintService.findSprintByProject(testjob.getProjectId());
				testplanList = testjobService.getTestplanByJobId(runJob.getJobId());
				testcaseList = testjobService.getTestcaseByJobId(runJob.getJobId());
				runCaseList = testjobService.getRunCaseByRunjobId(id);		
				
				if (testplanList.size()!=0){
					for (Testplan tp : testplanList){
					  testcaseList.addAll(testplanService.getTestcaseByPlan(tp.getTestplanId()));
					}
				}	
				
				if(null != s && s.size() != 0){
					Iterator<Project> it = s.iterator();
					while(it.hasNext()){
						Project p = it.next();
						if(p.getIsValid() == 0){
							projectList.add(p);
						}
					}
				}
				
			} catch (BaseException e) {
				e.printStackTrace();
			}
		}
		
		return SUCCESS;
	}
	
	public void testRunPage(String testJobId){
		try {
			testjob = testjobService.getTestjobById(testJobId);
			serverList = testjobService.getJobServer(testJobId, UserSessionUtil.getUser().getUserId());
			runJobList = testjobService.getAllRunJob(testJobId);
			if(TitanContent.TEST_JOB_STATUS_RUNNING.equals(testjob.getState())){
				this.getCurrentRunJobId(testjob.getTestjobId());
			}
			if(null != runJobList && runJobList.size() > 5){
				runJobList = runJobList.subList(0, 5);
			}
			Map<String, String> defaultParaMap = testjobService.getDefaultParaMap(Integer.valueOf(UserSessionUtil.getUser().getUserId()), testjob.getTestjobId());
			this.getServerParameterHtml(testJobId, defaultParaMap);
			
			if(null == testjob.getPlancaseJson() || "".equals(testjob.getPlancaseJson())){
				this.createTestJobJson(testjob);
			}
			else{
//				testJobJson = testjob.getPlancaseJson();
				testJobJson = this.createTestJobPlanCaseJsonAll(testjob);
			}
		} catch (BaseException e) {
			e.printStackTrace();
		}
	}
	
	public void getJobShow(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			testjob = testjobService.getTestjobById(testjobId);
			testJobJson = this.createTestJobPlanCaseJsonAll(testjob);
			out.print(testJobJson);
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
	
	public void getServerParameterHtml(String sestJobId, Map<String, String> defaultParaMap) throws BaseException{
		List<Parameter> pList = testjobService.getParameterByJobId(sestJobId);
		List<Parameter> pList1 = testjobService.getParameterByJobPlanId(sestJobId);
		List<Parameter> pList2 = testjobService.getServerParaByJobId(sestJobId);
		List<Parameter> pList3 = testjobService.getServerParaByJobPlanId(sestJobId);
		List<Parameter> totalPList = new ArrayList<Parameter>();
		if(null != pList){
			totalPList.addAll(pList);
		}
		if(null != pList1){
			totalPList.addAll(pList1);
		}
		if(null != pList2){
			totalPList.addAll(pList2);
		}
		if(null != pList3){
			totalPList.addAll(pList3);
		}
		if(null != totalPList && totalPList.size() != 0){
			Set<String> s = new HashSet<String>();
//			s.add(totalPList.get(0).getParaValue());
//			paramList = new ArrayList<Parameter>();
//			paramList.add(totalPList.get(0));
			StringBuffer sb = new StringBuffer();
			sb.append("<table id=\"pageTable\" width=\"98%\" height=\"50%\"><tr><th width=\"30%\">Parameter name</th><th width=\"30%\">Value</th></tr>");
			
			for(int i = 0; i < totalPList.size(); i++){
				if("none".equals(totalPList.get(i).getType()) || null == totalPList.get(i).getParaValue() || "".equals(totalPList.get(i).getParaValue())){
					continue;
				}
				if(!s.contains(totalPList.get(i).getParaValue())){
					sb.append("<tr>");
					sb.append("<td align=\"center\">");
					sb.append(totalPList.get(i).getParaValue());
					sb.append("</td>");
					sb.append("<td  align=\"center\">");
					if("host".equals(totalPList.get(i).getType())){
						sb.append("<select id=\"parameterVo.");
						sb.append(totalPList.get(i).getParaValue().replace("$", ""));
						sb.append("\"  name=\"");
						sb.append(totalPList.get(i).getParaValue().replace("$", ""));
						sb.append("\" class=\"select\" style=\"width:180px\">");
						sb.append("<option value=\"\" selected>Please Select</option>");
						if(null != serverList){
							for(int j = 0; j < serverList.size(); j++){
								sb.append("<option value=\"");
								sb.append(serverList.get(j).getServerId());
								sb.append("\"");
								if(defaultParaMap.get(totalPList.get(i).getParaValue().replace("$", "")) != null && defaultParaMap.get(totalPList.get(i).getParaValue().replace("$", "")).equals(serverList.get(j).getServerId())){
									sb.append("selected");
								}
								sb.append(">");
								sb.append(serverList.get(j).getServerName());
								
								sb.append("</option>");
							}
						}
						sb.append("</select>");
					}
					
					else if("ip".equals(totalPList.get(i).getType())){
						sb.append("<select id=\"parameterVo.");
						sb.append(totalPList.get(i).getParaValue().replace("$", ""));
						sb.append("\"  name=\"");
						sb.append(totalPList.get(i).getParaValue().replace("$", ""));
						sb.append("\" class=\"select\" style=\"width:180px\">");
						sb.append("<option value=\"\" selected>Please Select</option>");
						if(null != serverList){
							for(int j = 0; j < serverList.size(); j++){
								sb.append("<option value=\"");
								sb.append(serverList.get(j).getServerId());
								sb.append("\"");
								if(defaultParaMap.get(totalPList.get(i).getParaValue().replace("$", "")) != null){
									sb.append("selected");
								}
								sb.append(">");
								sb.append(serverList.get(j).getServerIp());
								sb.append("</option>");
							}
						}
						sb.append("</select>");
					}
					else{
						sb.append("<input id=\"parameterVo.");
						sb.append(totalPList.get(i).getParaValue().replace("$", ""));
						sb.append("\"  name=\"");
						sb.append(totalPList.get(i).getParaValue().replace("$", ""));
						sb.append("\" style=\"width:180px;\" value=\"");
						if(defaultParaMap.get(totalPList.get(i).getParaValue().replace("$", "")) != null){
							sb.append(defaultParaMap.get(totalPList.get(i).getParaValue().replace("$", "")));
						}
						sb.append("\" />");
					}
					sb.append("</td></tr>");
					s.add(totalPList.get(i).getParaValue());
				}
			}
			sb.append("</table>");
			paraHtml = sb.toString();
		}
	}
	
	public String goTestcaseRun(){
		if ((this.runCaseId != null) && !this.runCaseId.equals("")){
			String id = this.runCaseId;			
			try {
				Set<Project> s = UserSessionUtil.getUser().getUserProjects();
				runCase = testjobService.getRunCaseById(id);
				testcase = testcaseService.getTestcaseById(runCase.getCaseId());			
				userList = userService.getAllUserByUserVo(userVo);
//				projectList = projectService.getAllProjectList(s);
				projectList = new ArrayList<Project>();
		        moduleList = projectService.getProjectModuleList(testcase.getProjectId());
		        if(null != s && s.size() != 0){
					Iterator<Project> it = s.iterator();
					while(it.hasNext()){
						Project p = it.next();
						if(p.getIsValid() == 0){
							projectList.add(p);
						}
					}
				}
			} catch (BaseException e) {
				e.printStackTrace();
			}
		}	
		
		return SUCCESS;
	}
	
	public String doRuncaseSave(){
		try {
			Set<Project> s = UserSessionUtil.getUser().getUserProjects();
			runCase = testjobService.getRunCaseById(this.runCase.getRunCaseId());
			runCase.setState(this.runCaseState);
			runCase.setRemark(runCaseNote);

			testjobService.saveRunCase(runCase);
			runJob = testjobService.getRunJobById(runCase.getRunJobId());
			testjob = testjobService.getTestjobById(runJob.getJobId());
			userList = userService.getAllUserByUserVo(userVo);
//			projectList = projectService.getAllProjectList(s);
			projectList = new ArrayList<Project>();
			runCaseList = testjobService.getRunCaseByRunjobId(runJob.getRunJobId());
			testplanList = testjobService.getTestplanByJobId(runJob.getJobId());
			testcaseList = testjobService.getTestcaseByJobId(runJob.getJobId());
			jobserverList = testjobService.getJobServer(testjob.getTestjobId(), UserSessionUtil.getUser().getUserId());
			
			if(null != s && s.size() != 0){
				Iterator<Project> it = s.iterator();
				while(it.hasNext()){
					Project p = it.next();
					if(p.getIsValid() == 0){
						projectList.add(p);
					}
				}
			}
			
			if (testplanList.size()!=0){
				for (Testplan tp : testplanList){
				  testcaseList.addAll(testplanService.getTestcaseByPlan(tp.getTestplanId()));
				}
			}
			
			//update status of run job
			int pnum=0, fnum = 0, nnum = 0, snum = 0;
			
			for(RunCase runcase : runCaseList){
				if (runcase.getState().equals(TitanContent.RUN_CASE_STATUS_FAIL)){
					fnum++;
				}else if (runcase.getState().equals(TitanContent.RUN_CASE_STATUS_NOTRUN)){
					nnum++;
				}else if (runcase.getState().equals(TitanContent.RUN_CASE_STATUS_SKIP)){
					snum++;
				}else if (runcase.getState().equals(TitanContent.RUN_CASE_STATUS_SUCCESS)){
					pnum++;
				}
			}
			if(nnum>0){
			    runJob.setResult(TitanContent.RUN_JOB_STATUS_ACTIVE);
			}
			if(fnum>0 && nnum==0){
				runJob.setResult(TitanContent.RUN_JOB_STATUS_FAIL);
			}
			if(pnum == runCaseList.size() || (pnum + snum) == runCaseList.size()){
				runJob.setResult(TitanContent.RUN_JOB_STATUS_SUCCESS);
			}			
			if(snum == runCaseList.size()){
				runJob.setResult(TitanContent.RUN_CASE_STATUS_SKIP);
			}
			
			testjobService.saveRunJob(runJob);
			
			//update status of test job
			
			testjob.setState(runJob.getResult());
			testjob.setUpdateDate(DateUtils.getCurrentDate());
			testjobService.saveTestjob(testjob);

		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String goBatchUpdateResult(){
		
		try {
			if ((this.runcaseIdAry != null)) {
				int j = runcaseIdAry.length; 
				 for (int i = 0; i < j; ++i) { 
					String id = runcaseIdAry[i];
					runCase = testjobService.getRunCaseById(id);
					runCase.setState(this.runCaseState);
					testjobService.saveRunCase(runCase);
				 }
			}
			Set<Project> s = UserSessionUtil.getUser().getUserProjects();
			runJob = testjobService.getRunJobById(runCase.getRunJobId());
			testjob = testjobService.getTestjobById(runJob.getJobId());
			userList = userService.getAllUserByUserVo(userVo);
//			projectList = projectService.getAllProjectList(s);
			projectList = new ArrayList<Project>();
			sprintList = sprintService.findSprintByProject(testjob.getProjectId());
			runCaseList = testjobService.getRunCaseByRunjobId(runJob.getRunJobId());
			testplanList = testjobService.getTestplanByJobId(runJob.getJobId());
			testcaseList = testjobService.getTestcaseByJobId(runJob.getJobId());
			jobserverList = testjobService.getJobServer(testjob.getTestjobId(), UserSessionUtil.getUser().getUserId());
			
			if(null != s && s.size() != 0){
				Iterator<Project> it = s.iterator();
				while(it.hasNext()){
					Project p = it.next();
					if(p.getIsValid() == 0){
						projectList.add(p);
					}
				}
			}
			
			if (testplanList.size()!=0){
				for (Testplan tp : testplanList){
				  testcaseList.addAll(testplanService.getTestcaseByPlan(tp.getTestplanId()));
				}
			}
			
			//update status of run job
			int pnum=0, fnum = 0, nnum = 0, snum = 0;
			
			for(RunCase runcase : runCaseList){
				if (runcase.getState().equals(TitanContent.RUN_CASE_STATUS_FAIL)){
					fnum++;
				}else if (runcase.getState().equals(TitanContent.RUN_CASE_STATUS_NOTRUN)){
					nnum++;
				}else if (runcase.getState().equals(TitanContent.RUN_CASE_STATUS_SKIP)){
					snum++;
				}else if (runcase.getState().equals(TitanContent.RUN_CASE_STATUS_SUCCESS)){
					pnum++;
				}
			}
			if(nnum>0){
			    runJob.setResult(TitanContent.RUN_JOB_STATUS_ACTIVE);
			}
			if(fnum>0 && nnum==0){
				runJob.setResult(TitanContent.RUN_JOB_STATUS_FAIL);
			}
			if(pnum == runCaseList.size() || (pnum + snum) == runCaseList.size()){
				runJob.setResult(TitanContent.RUN_JOB_STATUS_SUCCESS);
			}			
			if(snum == runCaseList.size()){
				runJob.setResult(TitanContent.RUN_CASE_STATUS_SKIP);
			}
			testjobService.saveRunJob(runJob);
			
			//update status of test job
			
			testjob.setState(runJob.getResult());
			testjob.setUpdateDate(DateUtils.getCurrentDate());
			testjobService.saveTestjob(testjob);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
		
	}
	public String goJobRunList(){
		if ((this.testjobId != null) && !this.testjobId.equals("")){
			String id = this.testjobId;			
			try {
				User currentUser = UserSessionUtil.getUser();
			    currentUserName = currentUser.getUserCode();
				List<Role> roles = new ArrayList<Role>(currentUser.getUserRoles());
				if (roles.size() > 0) {
					String roleName = roles.get(0).getRoleName();	
					if("ADMIN".equals(roleName)) isAdmin = 1;
				}
				Set<Project> s = currentUser.getUserProjects();
				runJobList = testjobService.getAllRunJob(id);
				if(runJobList != null && runJobList.size()!=0) 	reportVo = this.getReportVoFromJob(runJobList.get(0).getRunJobId());
				testjob = testjobService.getTestjobById(id);
				userList = userService.getAllUserByUserVo(userVo);
				sprintList = sprintService.findSprintByProject(testjob.getProjectId()); 
				testcaseList = testjobService.getTestcaseByJobId(id);
				jobserverList = testjobService.getJobServer(testjobId, currentUser.getUserId());
				defectinfoVoList = testjobService.getDefectInfoByTestJobId(testjobId);
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
				if(null != s && s.size() != 0){
					Iterator<Project> it = s.iterator();
					while(it.hasNext()){
						Project p = it.next();
						if(p.getIsValid() == 0){
							projectList.add(p);
						}
					}
				}
			} catch (BaseException e) {
				e.printStackTrace();
			}
		}	
		return SUCCESS;		
	}
	
	public ReportVo getReportVoFromJob(String runId){
		 try{   	  
		  reportVo = new ReportVo();
	   	  int total = 0;
	   	  int pass = 0;
	   	  int fail = 0;
	   	  int notrun = 0;
	   	  int skip = 0;
	   	  int running  = 0;
	   	  if(runId != null && !runId.equals("")){
	   	    	pass = testjobService.getRunCaseByResult(runId, TitanContent.RUN_CASE_STATUS_SUCCESS).size();
	   	    	total = testjobService.getRunCaseByRunjobId(runId).size();
	   	    	fail = testjobService.getRunCaseByResult(runId, TitanContent.RUN_CASE_STATUS_FAIL).size();
	   	    	notrun = notrun + testjobService.getRunCaseByResult(runId, TitanContent.RUN_CASE_STATUS_NOTRUN).size();
	   	    	skip = skip + testjobService.getRunCaseByResult(runId, TitanContent.RUN_CASE_STATUS_SKIP).size();
	   	    	running = running + testjobService.getRunCaseByResult(runId, TitanContent.RUN_CASE_STATUS_RUNNING).size();
	   	  }
	   	  NumberFormat numberFormat1 = NumberFormat.getInstance();
	   	  numberFormat1.setMinimumFractionDigits(0);
	   	  String cpasscent = numberFormat1.format((float)pass/(float)total*100);
	   	  String cfailcent = numberFormat1.format((float)fail/(float)total*100);
	   	  String cnotruncent = numberFormat1.format((float)notrun/(float)total*100);
	   	  String cskipcent = numberFormat1.format((float)skip/(float)total*100);
	   	  String cruncent = numberFormat1.format((float)running/(float)total*100);
	   	  reportVo.setTestcaseTotal(total);
	   	  reportVo.setTestcasePass(pass);
	   	  reportVo.setTestcaseFail(fail);
	   	  reportVo.setTestcaseNotrun(notrun);
	   	  reportVo.setTestcaseSkip(skip);
	   	  reportVo.setTestcaseRuning(running);
	   	  reportVo.setCasepassCent(cpasscent);
	   	  reportVo.setCasefailCent(cfailcent);
	   	  reportVo.setCasenotrunCent(cnotruncent);
	   	  reportVo.setCaseskipCent(cskipcent);
	   	  reportVo.setCaserunCent(cruncent);
		 }catch (BaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		   }
			return reportVo;
		}
	
//	public void doRunOnCommand(){
//		User user = null;
//		try {
//			user = userService.getUserByCode("admin");
//		} catch (BaseDaoException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		 Map<String, String> authMap = new HashMap<String, String>();
//		Iterator roleIter =user.getUserRoles().iterator();
//		while(roleIter.hasNext()){
//			Role role =	(Role)roleIter.next();
//			Iterator authIter =role.getAuths().iterator();
//			while(authIter.hasNext()){
//				Auth auth =	(Auth)authIter.next();
//				authMap.put(auth.getAuthName(), auth.getAuthName());
//			}
//			
//		}
//		
//		user.setAuthMap(authMap);
//		user.setLoginTime(new Date());
//		UserSessionUtil.setUser(user);
//		Iterator<Project> projIter = user.getUserProjects().iterator();
//		if(projIter.hasNext()){
//			UserSessionUtil.setDefaultProject(projIter.next().getProjectId());
//		}
//		this.doCreateRunJob();
//		this.createAllRunCases();
//		this.getJenkinsPara();
//		this.doCaseInBack(runJobId);
//	}
///http://localhost:8089/titan/test/doRunOnCommand.do?testjobId=8ae584a843435ac4014356bbdad3004b	
	public void doCreateRunJob(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			RunJob rj = new RunJob();
			rj.setJobId(testjobId);
			rj.setStartTime(DateUtils.getCurrentDate());
			rj.setCreateDate(DateUtils.getCurrentDate());
			rj.setCreateUserId(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
			rj.setIsValid(0);
			testjobService.saveRunJob(rj);
			
			List<String> serverUsedList = this.getUsedServerList(paraJson);
			Iterator<String> iter = serverUsedList.iterator();
			while(iter.hasNext()){
				String serverId = iter.next();
				Server s = serverService.getServerById(serverId);
				s.setStatus("used");
				
				serverService.doSaveServer(s);
			}
			
			Testjob tj = testjobService.getTestjobById(testjobId);
			tj.setState(TitanContent.TEST_JOB_STATUS_RUNNING);
			testjobService.saveTestjob(tj);
			runJobId = rj.getRunJobId();
			out.print(rj.getRunJobId());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	public void doStopRunJob(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			Testjob tj = testjobService.getTestjobById(testjobId);
			tj.setState(TitanContent.TEST_JOB_STATUS_PENDING);
			testjobService.saveTestjob(tj);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	public void doCreateRunCase(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			List jobPlanCaseList = (ArrayList)JsonUtil.JsonToList(JsonUtil.getJsonArrayFromString(runjobcaseJson.replace("},]", "}]")));
			Iterator it = jobPlanCaseList.iterator();
			while(it.hasNext()){
				Map<String, String> m = (Map)it.next();
	            RunCase rc = new RunCase();
	            rc.setRunJobId(m.get("runJobId"));
	            rc.setCaseId(m.get("testCaseId"));
	            if(null != m.get("testPlanId")){
	            	rc.setPlanId(m.get("testPlanId"));
				}
	            rc.setIsValid(0);
	            rc.setState(TitanContent.RUN_CASE_STATUS_NOTRUN);
	            rc.setCreateDate(DateUtils.getCurrentDate());
	            rc.setCreateUserId(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
	            testjobService.saveRunCase(rc);
			}
			
			out.print("success");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
		
	}//new RunCase(); 
	
	public void createAllRunCases(){
		try {
			RunCase rc = null;
			testplanList = testjobService.getTestplanByJobId(testjobId);
			testcaseList = testjobService.getTestcaseByJobId(testjobId);
			Map<String, String> testPlanMap = new HashMap<String, String> ();
			Map<String, String> testcaseMap = new HashMap<String, String> ();
			int total = 0;
			if(null != testplanList){
				total += testplanList.size();
				for(int j = 0; j < testplanList.size(); j++){
					testPlanMap.put(testplanList.get(j).getSort(), testplanList.get(j).getTestplanId());
				}
			}
			if(null != testcaseList){
				total += testcaseList.size();
				for(int k = 0; k < testcaseList.size(); k++){
					testcaseMap.put(testcaseList.get(k).getSort(), testcaseList.get(k).getCaseId());
				}
			}
			for(int i = 1; i <= total; i++){
				if(null != testPlanMap.get(String.valueOf(i))){
					List<Testcase> caseInPlan = testplanService.getTestcaseByPlan(testPlanMap.get(String.valueOf(i)));
					Iterator<Testcase> it = caseInPlan.iterator();
					while(it.hasNext()){
						rc = new RunCase();
			            rc.setRunJobId(runJobId);
						rc.setCaseId(it.next().getCaseId());
						rc.setPlanId(testPlanMap.get(String.valueOf(i)));
						rc.setIsValid(0);
						rc.setState(TitanContent.RUN_CASE_STATUS_NOTRUN);
			            rc.setCreateDate(DateUtils.getCurrentDate());
			            rc.setCreateUserId(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
			            testjobService.saveRunCase(rc);
					}
				}
				else if(null != testcaseMap.get(String.valueOf(i))){
					rc = new RunCase();
		            rc.setRunJobId(runJobId);
					rc.setCaseId(testcaseMap.get(String.valueOf(i)));
					rc.setIsValid(0);
		            rc.setState(TitanContent.RUN_CASE_STATUS_NOTRUN);
		            rc.setCreateDate(DateUtils.getCurrentDate());
		            rc.setCreateUserId(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
		            testjobService.saveRunCase(rc);
				}
			}
			
		} catch (BaseException e) {
			e.printStackTrace();
		} 
	}
	
	
	public void doCasesRun(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			this.run(); 
			out.print(runJobId);
			//new MyThread(runJobId);
		}catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			out.close();
		}
	}
	
//	public void getJenkinsPara(){
//		StringBuffer s = new StringBuffer();
//		s.append("[{");
//		try {
//			List<ParameterData> parameterdataList = testcaseService.getParameterData("");
//			Iterator<ParameterData> it = parameterdataList.iterator();
//			while(it.hasNext()){
//				ParameterData pd = it.next();
//				if(null != pd.getJenkins() && !"".equals(pd.getJenkins())){
//					s.append("\"");
//					s.append(pd.getParadataName().replace("$", ""));
//					s.append("\":\"");
//					s.append(pd.getJenkins());
//					s.append("\",");
//				}
//			}
//		} catch (BaseException e) {
//			e.printStackTrace();
//		}
//		s.append("}]");
//		paraJson = s.toString();
//	}
	
	public void doCaseInBack(){
		String rjRes = TitanContent.RUN_JOB_STATUS_SUCCESS;
		String tjState = TitanContent.TEST_JOB_STATUS_SUCCESS;
		RunJob rj;
		Integer userId = UserSessionUtil.getUser().getUserId();
		String userEmail = UserSessionUtil.getUser().getMail();
		try {
			rj = testjobService.getRunJobById(runJobId);
			Testjob tj = testjobService.getTestjobById(rj.getJobId());
			List<RunCase> rcList = testjobService.getRunCaseByRunjobId(runJobId);
			if(null != rcList && rcList.size() != 0){
				List paraMapList = null;
				if(null != paraJson){
					paraMapList = (ArrayList)JsonUtil.JsonToList(JsonUtil.getJsonArrayFromString(paraJson.replace(",}", "}")));
				}
				Map paraMap = new HashMap();
		        if(null != paraMapList){
		        	paraMap = (Map)paraMapList.get(0);
		        }
				Iterator<RunCase> it = rcList.iterator();
				while(it.hasNext()){
					tj = testjobService.getTestjobById(rj.getJobId());
					if(TitanContent.TEST_JOB_STATUS_PENDING.equals(tj.getState())){
						rjRes = TitanContent.RUN_JOB_STATUS_ABORT;
						tjState = TitanContent.TEST_JOB_STATUS_ABORT;
						break;
					}
					if(TitanContent.TEST_JOB_STATUS_SUCCESS.equals(tj.getState()) || TitanContent.TEST_JOB_STATUS_FAIL.equals(tj.getState())){
						break;
					}
					RunCase rc = it.next();
					String cRes = this.doCaseRun(rc, paraMap);
					if(TitanContent.RUN_JOB_STATUS_SUCCESS.equals(rjRes)){
						if("".equals(cRes) || TitanContent.RUN_CASE_STATUS_FAIL.equals(cRes)){
							rjRes = TitanContent.RUN_JOB_STATUS_FAIL;
							tjState = TitanContent.TEST_JOB_STATUS_FAIL;
						}
					}
				}
			}
			rj.setResult(rjRes);
			rj.setEndTime(DateUtils.getCurrentDate());
			rj.setUpdateDate(DateUtils.getCurrentDate());
			rj.setUpdateUserId(Integer.valueOf(userId));
			testjobService.saveRunJob(rj);
			tj.setState(tjState);
			tj.setUpdateDate(DateUtils.getCurrentDate());
			tj.setUpdateUserId(Integer.valueOf(userId));
			testjobService.saveTestjob(tj);
			
			List<String> serverUsedList = this.getUsedServerList(paraJson);
			Iterator<String> iter = serverUsedList.iterator();
			while(iter.hasNext()){
				String serverId = iter.next();
				Server s = serverService.getServerById(serverId);
				s.setStatus("Not used");
				
				serverService.doSaveServer(s);
			}
			
			//notification
			reportVo = this.getReportVoFromJob(runJobId);
			EmailManageAction email = new EmailManageAction();
			String title = tj.getTestjobName() + " - Test job #" + tj.getTestjobCode() + "-" +  tj.getState();
			if(tjState.equals(TitanContent.TEST_JOB_STATUS_FAIL)||tjState.equals(TitanContent.TEST_JOB_STATUS_SUCCESS)||tjState.equals(TitanContent.TEST_JOB_STATUS_ABORT)){
			   email.sendEmail(title, this.createEmailContext(title,reportVo), userEmail);
			}
		}
		catch (BaseException e) {
			e.printStackTrace();
		}
	}
	
	private String createEmailContext(String title, ReportVo reportVo){
		StringBuffer sb = new StringBuffer();
		sb.append(title);
		sb.append("<br>");
		sb.append("Test Result Summary:");
		sb.append("<br>");
		sb.append("<table border='1' width='100%'>");
		sb.append("<tr><td width='10%'>Testcase Total</td><td width='10%'>" + reportVo.getTestcaseTotal() + "</td><td width='10%'> 100% </td></tr>");
		sb.append("<tr><td>Testcase Pass</td><td>" + reportVo.getTestcasePass() + "</td><td>" + reportVo.getCasepassCent() +"%</td></tr>");
		sb.append("<tr><td>Testcase Fail</td><td>" + reportVo.getTestcaseFail() + "</td><td>" + reportVo.getCasefailCent() +"%</td></tr>");
		sb.append("<tr><td>Testcase Not run</td><td>" + reportVo.getTestcaseNotrun() + "</td><td>" + reportVo.getCasenotrunCent() +"%</td></tr>");
		sb.append("<tr><td>Testcase Skipped</td><td>" + reportVo.getTestcaseSkip() + "</td><td>" + reportVo.getCaseskipCent() +"%</td></tr>");
		sb.append("<tr><td>Testcase Running</td><td>" + reportVo.getTestcaseRuning() + "</td><td>" + reportVo.getCaserunCent() +"%</td></tr>");
		sb.append("</table>");
		sb.append("<br>");
		sb.append("Check output at " + TitanContent.TITAN_URL + " to view the details.");
		sb.append("<br>");
		sb.append("<br>");
		sb.append("DO NOT REPLY TO THIS EMAIL." );
		return sb.toString();
	}
	
	public String doCaseRun(RunCase runCase, Map paraMap){
		ssh = null;
		String caseRes = "";
		// para info
		try {
			// result para
			List<Parameter> resultParaList = testcaseService.getParaResult(runCase.getCaseId());
			// run server
			RunCaseServer runCaseServer;
			runCaseServer = testcaseService.findCaseServerById(runCase.getCaseId());
	        Server serverInfo = new Server();
			if(null != runCaseServer && null != runCaseServer.getId() && null != runCaseServer.getId().getDedicatedserverId() && !"".equals(runCaseServer.getId().getDedicatedserverId())){
				DedicatedServer ds = projectService.getDedicatedServerById(runCaseServer.getId().getDedicatedserverId());
				serverInfo.setServerName(ds.getServerHostname());
				serverInfo.setServerIp(ds.getServerIp());
				serverInfo.setServerAccount(ds.getServerAccount());
				serverInfo.setServerPasswd(ds.getServerPasswd());
			}
			else if(null != runCaseServer && null != runCaseServer.getId() && null != runCaseServer.getId().getServerData() &&!"".equals(runCaseServer.getId().getServerData())){
				serverInfo = serverService.getServerById(String.valueOf(paraMap.get(runCaseServer.getId().getServerData().replace("$", ""))));
			}
			// command
			Testcase tc = testcaseService.getTestcaseById(runCase.getCaseId());
            String command = tc.getCommand();
            List<Parameter> paraList = testcaseService.getParameters(runCase.getCaseId());
            if(null != paraList && paraList.size() != 0){
				Iterator<Parameter> l = paraList.iterator();
				StringBuffer s = new StringBuffer(" ");
				while(l.hasNext()){
					Parameter pv = l.next();
					s.append(pv.getParaName());
					s.append(" ");
					if("host".equals(pv.getType()) || "ip".equals(pv.getType())){
						Server ps = serverService.getServerById(String.valueOf(paraMap.get(pv.getParaValue().replace("$", ""))));
						if("host".equals(pv.getType())){
							s.append(ps.getServerName());
						}
						else{
							s.append(ps.getServerIp());
						}
					}
					else if(!"none".equals(pv.getType())){
						s.append(paraMap.get(pv.getParaValue().replace("$", "")));
					}
					s.append(" ");
//					s.append(parameterDataVo.getParaDataMap().get(pv.getParaValue()));
				}
				command += s.toString();
			}
            // ssh run
            runCase.setStartTime(DateUtils.getCurrentDate());
            runCase.setState(TitanContent.RUN_CASE_STATUS_RUNNING);
            testjobService.saveRunCase(runCase);
            
            ConnBean cb = new ConnBean(serverInfo.getServerIp(), serverInfo.getServerAccount(), serverInfo.getServerPasswd());
			CustomTask sampleTask = new ExecCommand(command );
			Result res = new Result();
			try {
					Long timeout = !"".equals(tc.getTimeout())?20*60*1000l:Long.parseLong(tc.getTimeout())*60*1000l;
					SSHExecClient.setOption(IOptionName.TIMEOUT, timeout);
					SSHExecClient.showEnvConfig();
					ssh = null;
					ssh = SSHExecClient.getInstance(cb); 
		            Boolean ifConnected = ssh.connect(cb); 
		            if(!ifConnected){
		            	caseRes = TitanContent.RUN_CASE_STATUS_FAIL;
		            	runCase.setState(caseRes);
		            	runCase.setCommand(command);
		            	runCase.setEndTime(DateUtils.getCurrentDate());
		                testjobService.saveRunCase(runCase);
		                this.saveRunLog(SysConfiger.getProperty("file.log.path") + runCase.getRunJobId(), runCase.getCaseId(), "Connection failed..");
		                return caseRes;
		            }
	            if(null != tc.getLocation() && !"".equals(tc.getLocation())){
		            File file = new File("/opt/upload" + tc.getLocation().replaceAll(" ", ""));
		            if(file.exists()){
		            	String fpath = tc.getLocation().replaceAll(" ", "");
		            	if(fpath.endsWith("/")){
		            		fpath = fpath.substring(0, fpath.length() - 1);
		            	}
		            	ssh.uploadAllDataToServer("/opt/upload" + fpath, "/opt/titan");
		            	CustomTask unzipTask = new ExecCommand("cd /opt/titan", "unzip -o \"*.zip\"");
		            	res = ssh.exec(unzipTask);
		            }
	            }
	            System.out.println("***********location*************");
	            if(!"".equals(sampleTask.getCommand())){
	            	res = ssh.exec(sampleTask);
	            }
			} catch (TaskExecFailException e) {
				e.printStackTrace();
				this.saveRunLog(SysConfiger.getProperty("file.log.path") + runCase.getRunJobId(), runCase.getCaseId(), e.getMessage());
			} catch (BaseException e) {
				e.printStackTrace();
			} catch (JSchException e){
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				ssh.disconnect();
				ssh = null;
			}
			// update run case
			if(this.checkRes(resultParaList, res.rc)){
				caseRes = TitanContent.RUN_CASE_STATUS_SUCCESS;
        	}
        	else{
        		caseRes = TitanContent.RUN_CASE_STATUS_FAIL;
        	}
			// For testing begin
			// For testing end
			runCase.setState(caseRes);
			runCase.setCommand(command);
			runCase.setEndTime(DateUtils.getCurrentDate());
			runCase.setRunServer(serverInfo.getServerIp());
            testjobService.saveRunCase(runCase);
            // save log
            if(null != res.sysout && !"".equals(res.sysout)){
            	this.saveRunLog(SysConfiger.getProperty("file.log.path") + runCase.getRunJobId(), runCase.getCaseId(), res.sysout);
            }
            else if(null != res.error_msg && !"".equals(res.error_msg)){
            	this.saveRunLog(SysConfiger.getProperty("file.log.path") + runCase.getRunJobId(), runCase.getCaseId(), res.error_msg);
            }
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return caseRes;
	}
	
	/*
	public String doCaseRun1(RunCase rc){
		SSHExecClient ssh = null;
		try {
			// save run case
//			RunJob rj = testjobService.getRunJobById(runJobId);
//            RunCase rc = new RunCase();
//            rc.setRunJobId(rj.getRunJobId());
//            rc.setCaseId(testCaseId);
//            if(null != testPlanId){
//            	rc.setPlanId(testPlanId);
//			}
//            rc.setStartTime(DateUtils.getCurrentDate());
//            rc.setIsValid(0);
//            rc.setCreateDate(DateUtils.getCurrentDate());
//            rc.setCreateUserId(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
//            testjobService.saveRunCase(rc);
            
            List paraMapList = (ArrayList)JsonUtil.JsonToList(JsonUtil.getJsonArrayFromString(paraJson.replace(",}", "}")));
            Map paraMap = new HashMap();
            if(null != paraMapList){
            	paraMap = (Map)paraMapList.get(0);
            }
            
            RunCaseServer runCaseServer = testcaseService.findCaseServerById(testCaseId);
            Server serverInfo = new Server();
			if(null != runCaseServer && null != runCaseServer.getId() && null != runCaseServer.getId().getDedicatedserverId() && !"".equals(runCaseServer.getId().getDedicatedserverId())){
				DedicatedServer ds = projectService.getDedicatedServerById(runCaseServer.getId().getDedicatedserverId());
				serverInfo.setServerName(ds.getServerHostname());
				serverInfo.setServerIp(ds.getServerIp());
				serverInfo.setServerAccount(ds.getServerName());
				serverInfo.setServerPasswd(ds.getServerPasswd());
			}
			else if(null != runCaseServer && null != runCaseServer.getId() && null != runCaseServer.getId().getServerData() &&!"".equals(runCaseServer.getId().getServerData())){
				serverInfo = serverService.getServerById(String.valueOf(paraMap.get(runCaseServer.getId().getServerData().replace("$", ""))));
			}
			
//			List<ParameterVo> paraInfoList = testjobService.getParameterInfo(testjobId, testPlanId, testCaseId);
            
            Testcase tc = testcaseService.getTestcaseById(testCaseId);
            String command = tc.getCommand();
            List<Parameter> paraList = testcaseService.getParameters(testCaseId);
            if(null != paraList && paraList.size() != 0){
				Iterator<Parameter> l = paraList.iterator();
				StringBuffer s = new StringBuffer(" ");
				while(l.hasNext()){
					Parameter pv = l.next();
					s.append(pv.getParaName());
					s.append(" ");
					if(pv.getParaValue().indexOf("hostname") > 0 || pv.getParaValue().indexOf("ip") > 0){
						Server ps = serverService.getServerById(String.valueOf(paraMap.get(pv.getParaValue().replace("$", ""))));
						if(pv.getParaValue().indexOf("hostname") > 0){
							s.append(ps.getServerName());
						}
						else{
							s.append(ps.getServerIp());
						}
					}
					else if(pv.getParaValue().indexOf("none") == -1){
						s.append(paraMap.get(pv.getParaValue().replace("$", "")));
					}
					s.append(" ");
//					s.append(parameterDataVo.getParaDataMap().get(pv.getParaValue()));
				}
				command += s.toString();
			}
			ConnBean cb = new ConnBean(serverInfo.getServerIp(), serverInfo.getServerAccount(), serverInfo.getServerPasswd());
			
			CustomTask sampleTask = new ExecCommand(command );
			Result res = new Result();
			try {
				ssh = SSHExecClient.getInstance(cb);  
	            ssh.connect(); 
	            if(null != tc.getLocation() && !"".equals(tc.getLocation())){
		            File file = new File("/opt/upload" + tc.getLocation().replaceAll(" ", ""));
		            if(file.exists()){
		            	String fpath = tc.getLocation().replaceAll(" ", "");
		            	if(fpath.endsWith("/")){
		            		fpath = fpath.substring(0, fpath.length() - 1);
		            	}
		            	ssh.uploadAllDataToServer("/opt/upload" + fpath, "/opt/titan");
		            	//ssh.uploadAllDataToServer("C:/opt/upload/m11y/InstallQlogicFCDrivers", "/home/yangxu/temp");
		            }
	            }
				res = ssh.exec(sampleTask);
			} catch (TaskExecFailException e) {
				e.printStackTrace();
				this.saveRunLog(SysConfiger.getProperty("file.log.path") + rj.getRunJobId(), testCaseId, e.getMessage());
			} catch (BaseException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
            // update run job
            List<Parameter> resultParaList = testcaseService.getParaResult(testCaseId);
//            rj.setEndTime(DateUtils.getCurrentDate());
//            if(null == rj.getResult() || TitanContent.RUN_JOB_STATUS_SUCCESS.equals(rj.getResult())){
//            	if(this.checkRes(resultParaList, res.rc)){
//            		rj.setResult(TitanContent.RUN_JOB_STATUS_SUCCESS);
//            	}
//            	else{
//            		rj.setResult(TitanContent.RUN_JOB_STATUS_FAIL);
//            	}
//            }
            
            if(this.checkRes(resultParaList, res.rc)){
        		rc.setState(TitanContent.RUN_TEST_CASE_SUCCESS);
        	}
        	else{
        		rc.setState(TitanContent.RUN_TEST_CASE_FAIL);
        	}
            	
            testjobService.saveRunJob(rj);
            // update run case
            rc.setCommand(command);
            rc.setEndTime(DateUtils.getCurrentDate());
            testjobService.saveRunCase(rc);
            // save log
//          List<CaseLog> clList = testcaseService.getCaseLogs(testCaseId);
//            if(null != clList && clList.size() != 0){
//            	CaseLog cl = clList.get(0);
            if(null != res.sysout && !"".equals(res.sysout)){
            	this.saveRunLog(SysConfiger.getProperty("file.log.path") + rj.getRunJobId(), testCaseId, res.sysout);
            }
            else if(null != res.sysout && !"".equals(res.error_msg)){
            	this.saveRunLog(SysConfiger.getProperty("file.log.path") + rj.getRunJobId(), testCaseId, res.error_msg);
            }
		} catch (BaseException e) {
			e.printStackTrace();
		}
		finally {
			ssh.disconnect();  
		}
	}*/
	
	public Boolean checkRes(List<Parameter> resultParaList, int runRes){
		if(null == resultParaList || resultParaList.size() == 0){
			if(runRes == 0){
				return true;
			}
			else{
				return false;
			}
		}
		Iterator<Parameter> l = resultParaList.iterator();
		while(l.hasNext()){
			Parameter p = l.next();
			if(String.valueOf(runRes).equals(p.getParaName())){
				return true;
			}
		}
		return false;
	}
	
	public void getRunCaseLog() throws IOException{
		PrintWriter out = null;
		BufferedReader reader = null;
		try {
			File filePath = new File(SysConfiger.getProperty("file.log.path") + runJobId + "/" + testCaseId);
			if(!filePath.exists()){
				out = this.getResponse().getWriter();
				out.print("No logs");
				out.flush();
				return;
			}
			reader = new BufferedReader(new FileReader(SysConfiger.getProperty("file.log.path") + runJobId + "/" + testCaseId));
			String tempString;
			StringBuffer s = new StringBuffer();
			while ((tempString = reader.readLine()) != null){
				tempString = tempString.replace("<", "&lt;").replace(">", "&gt;");
				s.append(tempString);
				s.append("<br>");
			}
//			s.append("\",");
			
			List<CaseLog> clList = testcaseService.getCaseLogs(testCaseId);
			Iterator<CaseLog> l = clList.iterator();
			while(l.hasNext()){
				CaseLog cl = l.next();
				s.append("\"");
				s.append(cl.getLogLocation());
				s.append(cl.getLogName());
				s.append("\",");
			}
			out = this.getResponse().getWriter();
			out.print(s.toString());
			out.flush();
		} catch (IOException e) {
			if(null == out){
				out = this.getResponse().getWriter();;
			}
			out.print("");
			out.flush();
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		}finally {
			out.close();
		}
	}
	
	public void getRunCaseHistory() throws IOException{
		PrintWriter out = null;
		try {
			StringBuffer s = new StringBuffer();
//			List<CaseDefect> cdList = testcaseService.getCaseDefectList(testCaseId);
//			if(null != cdList && cdList.size() != 0){
//				Iterator<CaseDefect> iter = cdList.iterator();
//				while(iter.hasNext()){
//					CaseDefect cd = iter.next();
//					if(TitanContent.CASE_DEFECT_TYPE_RALLY.equals(cd.getDefectType())){
//						s.append("Defect No.: <a href=\"#\" onclick=\"getRallyDetailInfo('");
//						s.append(cd.getDefectNo());
//						s.append("')\">");
//						s.append(cd.getDefectNo());
//						s.append("</a>");
//					}
//					else if(TitanContent.CASE_DEFECT_TYPE_QUIX.equals(cd.getDefectType())){
//						s.append("Quix No.: ");
//						s.append(cd.getDefectNo());
//					}
//					s.append("<br>");
//				}
//			}
//			s.append("<br>");
			int defectCount = testcaseService.getdefectCount(testCaseId, testjobId);
			List<RunCaseVo> rcVoList = testcaseService.getRunCaseHistory(testCaseId, testjobId);
			s.append("Total run:");
			s.append(rcVoList.size());
			s.append("<br>");
			s.append("Total defect:");
			s.append(defectCount);
			s.append("<br>");
			s.append("<br>");
			Iterator<RunCaseVo> iter1 = rcVoList.iterator();
			while (iter1.hasNext()){
				RunCaseVo rcVo = iter1.next();
				s.append("User: ");
				s.append(rcVo.getRunUnser());
				s.append("<br>");
				s.append("Start time: ");
				s.append(rcVo.getStartTime()); 
				s.append("<br>");
				s.append("Result: ");
				s.append(rcVo.getState());
				s.append("<br>");
				List<CaseDefect> cdList = testcaseService.getCaseDefectList(rcVo.getRuncaseId());
				if(null != cdList && cdList.size() != 0){
					Iterator<CaseDefect> iter = cdList.iterator();
					while(iter.hasNext()){
						CaseDefect cd = iter.next();
						s.append("Defect No.: <a href=\"#\" onclick=\"getDefectDetailInfo('");
						s.append(cd.getDefectNo());
						s.append("')\"><font color=\"red\">");
						s.append(cd.getDefectNo());
						s.append("</font></a>");
						s.append("<br>");
					}
				}
				s.append("<a href=\"#\" onclick=\"gotoRunjob('");
				s.append(rcVo.getRunjobId());
				s.append("')\">");
				s.append("<font color=\"blue\">Link to this run job</font>");
				s.append("</a>");
				s.append("<br>");
				s.append("<br>");
			}
			out = this.getResponse().getWriter();
			out.print(s.toString());
			out.flush();
		} catch (IOException e) {
			if(null == out){
				out = this.getResponse().getWriter();;
			}
			out.print("");
			out.flush();
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		}finally {
			out.close();
		}
	}
	
	public void getDefectByRunCaseId(){
		PrintWriter out = null;
		try {
			List<CaseDefect> cdList = testcaseService.getCaseDefectList(runCaseId);
			StringBuffer s = new StringBuffer();
			if(null != cdList && cdList.size() != 0){
				Iterator<CaseDefect> iter = cdList.iterator();
				while(iter.hasNext()){
					CaseDefect cd = iter.next();
					s.append("Defect No.: <a href=\"#\" onclick=\"getDefectDetailInfo('");
					s.append(cd.getDefectNo());
					s.append("')\"><font color=\"red\">");
					s.append(cd.getDefectNo());
					s.append("</font></a>");
					s.append("<br>");
				}
			}
			s.append("<button type=\"button\" id=\"bntDefInAdd\" onclick=\"editDefect()\">Add Defect</button>");
			out = this.getResponse().getWriter();
			out.print(s.toString());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		}finally {
			out.close();
        }
	}
	
	/**
	 * After get defect info from rally, format it as HTML style
	 * @throws IOException
	 */
	public void getDefectDetailInfo() throws IOException{
		PrintWriter out = null;
		StringBuffer s = new StringBuffer();
		try {
			DefectVo defect;
			if(defectNo.indexOf("QXCR") >= 0){
				defect = testcaseService.getDefectInfoByDefectNoFromQuix(defectNo);
			}
			else{
				defect = testcaseService.getDefectInfoByDefectNoFromRally(defectNo);
			}
			if(null != defect){
				if(defectNo.indexOf("QXCR") >= 0){
					s.append("<B>");
					s.append(defect.getAddUrl());
					s.append("</B>");
				}
				else{
					s.append("<B><a href=\"");
					s.append(TitanContent.RALLY_URL);
					s.append(defect.getObjectId());
					s.append("\">");//The url is like this: https://rally1.rallydev.com/slm/#/detail/defect/15868032791
					s.append(defect.getDefectNum());
					s.append("</a></B>");
				}
				s.append("<br>Name:");
				s.append(defect.getDefectName());
				s.append("<br>SubmittedBy:");
				s.append(defect.getSubmittedBy());
				s.append("<br>Last update date:");
				s.append(defect.getLastupdateDate());
				s.append("<br>Priority:");
				s.append(defect.getPriority());
				s.append("<br>State:");
				s.append(defect.getState());
			}
			else{
				s.append("No defect exist!");
			}
            out = this.getResponse().getWriter();
			out.print(s.toString());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		}finally {
			out.close();
        }
	}
	
	/**
	 * When set defect to failed test case, that defect info will be saved into DefectInfo
	 */
	public void doSaveCaseDefect(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			List<CaseDefect> cdList = testcaseService.getCaseDefectByDefectNo(defectNo);
			if(null != cdList && cdList.size() != 0){
				out.print("duplicated");
				return;
			}
			DefectVo result = null;
			DefectInfo di = new DefectInfo();
			if("rally".equals(defectType)){
				result = testcaseService.getDefectInfoByDefectNoFromRally(defectNo);
				LoginUserVo submitUser = testcaseService.getUserInfoFromRallyByObjectId(result.getSubmittedbyObjectid());
				di.setSubmittEmail(submitUser.getEmail());
			}
			else if("quix".equals(defectType)){
				result = testcaseService.getDefectInfoByDefectNoFromQuix(defectNo);
				di.setSubmittEmail(result.getSubmittedBy());
				di.setAddUrl(result.getAddUrl());
			}
			if(null == result){
				out.print("noDefect");
				return;
			}
			
			
			di.setObjectId(result.getObjectId());
			di.setDefectNum(result.getDefectNum());
			di.setDefectName(result.getDefectName());
			di.setPriority(result.getPriority());
			di.setState(result.getState());
			di.setSubmittedBy(result.getSubmittedBy());
			di.setLastupdateDate(DateUtils.convertStringToDate(result.getLastupdateDate()));
			di.setUserStory(result.getUserStory());
			
			testcaseService.saveDefectInfo(di);
			
			CaseDefect cd = new CaseDefect();
			cd.setDefectNo(defectNo);
			cd.setDefectType(defectType);
			cd.setRuncaseId(runCaseId);
			cd.setCreateDate(DateUtils.getCurrentDate());
			cd.setCreateUserId(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
		
			testcaseService.saveCaseDefect(cd);
			out.print("success");
			out.flush();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 finally {
			out.close();
		}
	}
	
	public void getRunCaseRelateLog(){
		PrintWriter out = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(relatelogPath));
			String tempString;
			StringBuffer s = new StringBuffer();
			while ((tempString = reader.readLine()) != null){
				tempString = tempString.replace("<", "&lt;").replace(">", "&gt;");
				s.append(tempString);
				s.append("<br>");
			}
			out = this.getResponse().getWriter();
			out.print(s.toString());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	public void saveRunLog(String path, String name, String logInfo){
		File filePath = new File(path);
		File file = new File(name);
		FileWriter fw = null;  
        try {
        	if (!filePath.exists()) {
        		filePath.mkdirs();   
        	}
        	if(!file.exists() ){
        		file.createNewFile();
        	}
			fw = new FileWriter(path + "/" + name);
	        fw.write(logInfo); 
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(null != fw){
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void getCaseInfo(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			//<div style='padding:10px;'>a<input type='text' id='yourname' name='yourname' value='aa' /></div>
			testcase = testcaseService.getTestcaseById(testCaseId);
			paramList = testcaseService.getParameters(testCaseId);
			StringBuffer s = new StringBuffer();
			s.append("<div style='padding:10px;'>Case name:");
			s.append("<input type='text' value='");
			s.append(testcase.getCaseName());
			s.append("' readonly='true' /></div>");
			s.append("<div style='padding:10px;'>Case Code:");
			s.append("<input type='text' value='");
			s.append(testcase.getCaseCode());
			s.append("' readonly='true' /></div>");
			s.append("<div style='padding:10px;'>Command:");
			s.append("<input type='text' value='");
			s.append(testcase.getCommand());
			s.append("' readonly='true' /></div>");
			
			Iterator<Parameter> l = paramList.iterator();
			while(l.hasNext()){
				Parameter p = l.next();
				s.append("<div style='padding:10px;'>Parameter name:");
				s.append("<input type='text' value='");
				s.append(p.getParaName());
				s.append("' readonly='true' /></div>");
				s.append("<div style='padding:10px;'>Parameter value:");
				s.append("<input type='text' value='");
				s.append(p.getParaValue());
				s.append("' readonly='true' /></div>");
			}
			
			out.print(s.toString());
			out.flush();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	public String doTestJobManageRun(){
		this.doTestJobManage();
		this.testRunPage(testjobId);
		
		return SUCCESS;
	}
	
	public String goMTestJobManageRun(){
		this.doTestJobManage();
		this.goMTestJobRun();
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String doTestJobManage(){
		List jobPlanCaseList = (ArrayList)JsonUtil.JsonToList(JsonUtil.getJsonArrayFromString(testJobJson));
		Iterator l = jobPlanCaseList.iterator();
		Map jobMap = (HashMap)l.next();
		String testJobId = "";
		if("testJob".equals(jobMap.get("diyType"))){
			testJobId = jobMap.get("id").toString();
			testjobId = testJobId;
		}
		else{
			return SUCCESS;
		}
		try {
			testjob = testjobService.getTestjobById(testJobId);
//			if(TitanContent.TESTPLAN_MANUAL.equals(testjob.getJobType())){
				testjobplanList = new ArrayList<TestJobPlan>();
				testjobcaseList = new ArrayList<TestJobCase>();
				l = jobPlanCaseList.iterator();
				int i = 0;
				while(l.hasNext()){
					Map m = (Map)l.next();
					if("plan".equals(m.get("diyType"))){
						TestJobPlanId tjpId = new TestJobPlanId();
						TestJobPlan tjp = new TestJobPlan();
						tjpId.setTestjobId(testJobId);
						tjpId.setTestplanId(m.get("id").toString());
						tjp.setId(tjpId);
						tjp.setSort(String.valueOf(i));
						testjobplanList.add(tjp);
					}
					else if("caseInPlan".equals(m.get("diyType"))){
						continue;
					}
					else if("case".equals(m.get("diyType"))){
						TestJobCaseId tjcId = new TestJobCaseId();
						TestJobCase tjc = new TestJobCase();
						tjcId.setTestcaseId(m.get("id").toString());
						tjcId.setTestjobId(testJobId);
						tjc.setId(tjcId);
						tjc.setSort(String.valueOf(i));
						testjobcaseList.add(tjc);
					}
					i++;
				}
				testjobService.jobManagement(testJobId, testjobplanList, testjobcaseList);
//			}
//			else{
				testjobService.jobManagement(testJobId, testJobJsonDB);
//			}
		} catch (BaseException e) {
			e.printStackTrace();
		}
		this.testRunPage(testJobId);
		return SUCCESS;
	}
	
	
//	public String goCaseManageBox() throws IOException{
//		PrintWriter out = this.getResponse().getWriter();
//		try {
//			jobserverList = testjobService.getJobServer(testjobId);
//			paramList = testcaseService.getParameters(testCaseId);
//			serverList = serverService.getAllServerList();
//			String strServerHtml = this.serverServiceHtml();
//			
//			StringBuffer s = new StringBuffer();
//			s.append("<div class=\"msg-div\"> <p>Server:</p>");
//			s.append("<select id=\"serverId\" name=\"testcaseserver.serverId\"  class=\"select\" style=\"width:180px\">");
//			Iterator<Server> l = jobserverList.iterator();
//			while(l.hasNext()){
//				Server server = l.next();
//				s.append("<option value=\"");
//				s.append(server.getServerId());
//				s.append("\">");
//				s.append(server.getServerName());
//				s.append("</option>");
//			}
//			s.append("</select>");
//			s.append("<br><br>");
//
//			s.append("<p>Parameter:</p>");
//			Iterator<Parameter> m = paramList.iterator();
//			while(m.hasNext()){
//				Parameter p = m.next();
//				if(!"INPUT".equals(p.getType())){
//					continue;
//				}
//				else if("$hostname".equals(p.getParaValue())){
//					s.append("<p>");
//					s.append(p.getParaName());
//					s.append(":<select id=\"");
//					s.append(p.getParaId());
//					s.append("\" name=\"testcasepara\"  class=\"select\" style=\"width:180px\">");
//					s.append(strServerHtml);
//					s.append("</select>");
//					s.append("</p>");
//				}
//				else{
//					s.append("<p>");
//					s.append(p.getParaName());
//					s.append(": <input type=\"text\" id=\"");
//					s.append(p.getParaId());
//					s.append("\"  name=\"testcasepara\" style=\"width:75px;\" value=\"1\" />");
//					s.append("</p>");
//				}
//			}
//			s.append("</div>");
//			out.print(s.toString());
//		} catch (BaseException e) {
//			e.printStackTrace();
//		} finally{
//			out.close();
//		}
//		return null;
//	}
	
//	@SuppressWarnings("unchecked")
//	public String doCaseManageBox() throws IOException{
//		PrintWriter out = this.getResponse().getWriter();
//		List testCaseParaList = (ArrayList)JsonUtil.JsonToList(JsonUtil.getJsonArrayFromString(testCaseParaJson));
//		Map testCaseParaMap = (Map)testCaseParaList.get(0);
//		try {
//			testjobService.saveRunCasePorperty(testCaseParaMap);
//			out.print("success");
//		} catch (BaseException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	private String serverServiceHtml(){
		StringBuffer s = new StringBuffer();
		Iterator<Server> l = serverList.iterator();
		while(l.hasNext()){
			Server server = l.next();
			s.append("<option value=\"");
			s.append(server.getServerId());
			s.append("\">");
			s.append(server.getServerName());
			s.append("</option>");
		}
		return s.toString();
	}
	
	private void createTestJobJson(Testjob testJob){
		StringBuffer s = new StringBuffer();
		s.append("[{id:\"");
		s.append(testJob.getTestjobId());
		s.append("\", pId:0, name:\"");
		s.append(testJob.getTestjobName());
		s.append("\", isParent: true, open:true, ");
		s.append("diyType: \"testJob\", diyModule:\"none\"},");
		s.append("]");
		testJobJson = s.toString();
	}
	
	private String createTestJobPlanCaseJsonAll(Testjob testJob){
		StringBuffer s = new StringBuffer();
		s.append("[{\"id\":\"");
		s.append(testJob.getTestjobId());
		s.append("\", \"pId\":\"0\", \"name\":\"");
		s.append(testJob.getTestjobName());
		s.append("\", \"isParent\": \"true\", \"open\":\"true\", ");
		s.append("\"diyType\": \"testJob\", \"diyModule\":\"none\", \"children\":[");
		List<Testplan> tpList = new ArrayList<Testplan>();
		List<Testcase> tcList = new ArrayList<Testcase>();
		try {
			tpList = testjobService.getTestplanByJobId(testJob.getTestjobId());
			tcList = testjobService.getTestcaseByJobId(testjob.getTestjobId());
		} catch (BaseException e) {
			e.printStackTrace();
		}
		List<Integer> intList = sortPlanCaseBySort(tpList,tcList);
		for(int i=0;i<intList.size();i++){
			int inner = intList.get(i);
			if(tpList!=null){
				for(int j=0;j<tpList.size();j++){
					Testplan tp = tpList.get(j);
					if(inner==Integer.parseInt(tp.getSort())){
						s.append(this.createTestPlanJsonAll(tp));
						s.append(",");
						break;
					}
				}
			}
			if(tcList!=null){
				for(int k=0;k<tcList.size();k++){
					Testcase tc = tcList.get(k);
					if(inner == Integer.parseInt(tc.getSort())){
						s.append(this.createTestPlanByTestcase(tc));
						s.append(",");
						break;
					}
				}
			}
		}
		s.append("]");
		s.append("}]");
		return s.toString();
	}
	
	private List<Integer> sortPlanCaseBySort(List<Testplan> ltp,List<Testcase> ltc){
		List<Integer> ilist = new ArrayList<Integer>(); 
		for(int i=0;i<ltp.size();i++){
			Testplan tp = ltp.get(i);
			tp.getSort();
			ilist.add(Integer.parseInt(tp.getSort()));
		}
		for(int j=0;j<ltc.size();j++){
			Testcase tc = ltc.get(j);
			tc.getSort();
			ilist.add(Integer.parseInt(tc.getSort()));
			
		}
		Collections.sort(ilist);
		return ilist;
	}
	
	private String createTestPlanJsonAll(Testplan testplan){
		StringBuffer s = new StringBuffer();
		s.append("{\"id\":\"");
		s.append(testplan.getTestplanId());
		s.append("\", \"pId\":\"0\", \"name\":\"");
		s.append(testplan.getTestplanName());
		s.append("\",\"planCode\":\"");
		s.append(testplan.getTestplanCode());
		s.append("\", \"isParent\": \"true\", \"open\":\"true\", ");
		s.append("\"diyType\": \"plan\", \"children\": [");
		
		List<Testcase> tcList = new ArrayList<Testcase>();
		try {
			tcList = testplanService.getTestcaseByPlan(testplan.getTestplanId());
		} catch (BaseException e) {
			e.printStackTrace();
		}
		for(int i = 0;i<tcList.size();i++){
			Testcase tc = tcList.get(i);
			s.append("{\"id\":\"");
			s.append(tc.getCaseId());
			s.append("\", \"pId\":\"");
			s.append(testplan.getTestplanId());
			s.append("\",\"name\":\"");
			s.append(tc.getCaseName());
			s.append("\",\"caseCode\":\"");
			s.append(tc.getCaseCode());
			s.append("\", \"diyType\": \"caseInPlan\",");
			s.append("\"diyModule\": \"");
			s.append(tc.getModuleId());
			s.append("\"}");
			if(i!=tcList.size()-1){
				s.append(",");
			}
		}
		s.append("]");
		return s.append("}").toString();
	}
	
	private String createTestPlanByTestcase(Testcase testcase){
		StringBuffer s = new StringBuffer();
		s.append("{\"id\":\"");
		s.append(testcase.getCaseId());
		s.append("\", \"pId\":\"");
		s.append(testcase.getProjectId());
		s.append("\", \"name\":\"");
		s.append(testcase.getCaseName());
		s.append("\", \"caseCode\":\"");
		s.append(testcase.getCaseCode());
		s.append("\", \"isParent\": \"false\", \"open\":\"true\", ");
		
		s.append("\"diyType\": \"case\"");
		return s.append("}").toString();
	}
	
	private void createTestPlansCasesJson(List<Testcase> testcaseList, List<Testplan> testplanList, Set<String> planSet, Set<String> caseSet){
		StringBuffer s = new StringBuffer();
		s.append("[");
		if(null != testcaseList){
			s.append("{id:1, pId:0, name:\"TestCases\", isParent: true, open:true, diyType: \"caseFolder\", diyModule:\"none\"},");
			Iterator<Testcase> l = testcaseList.iterator();
			while(l.hasNext()){
				Testcase tc = l.next();
				if(null != caseSet && caseSet.size() != 0 && caseSet.contains(tc.getCaseId())){
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
		if(null != testplanList){
			s.append("{id:2, pId:0, name:\"TestPlans\", isParent: true, open:true, diyType: \"planFolder\", diyModule:\"none\"},");
			Iterator<Testplan> n = testplanList.iterator();
			while(n.hasNext()){
				Testplan tp = n.next();
				if(null != planSet && planSet.size() != 0 && planSet.contains(tp.getTestplanId())){
					continue;
				}
				s.append("{id:\"");
				s.append(tp.getTestplanId());
				s.append("\", pId:2, name:\"");
				s.append(tp.getTestplanName());
				s.append("\", diyType: \"plan\",");
				s.append("diyModule: \"none\"},");
//				List<Testcase> testCases = new ArrayList<Testcase>(tp.getTestcases());
				List<Testcase> testCases = new ArrayList<Testcase>();
				try {
					testCases = testplanService.getTestcaseByPlan(tp.getTestplanId());
				} catch (BaseException e) {
					e.printStackTrace();
				}
				Iterator<Testcase> m = testCases.iterator();
				while(m.hasNext()){
					Testcase tcintp = m.next();
					s.append("{id:\"");
					s.append(tcintp.getCaseId());
					s.append("\", pId:\"");
					s.append(tp.getTestplanId());
					s.append("\", name:\"");
					s.append(tcintp.getCaseName());
					s.append("\", diyType: \"caseInPlan\",");
					s.append("diyModule: \"none\"},");
				}
			}
		}
		s.append("]");
		testCaseJson = s.toString().replace("},]", "}]");
	}	

	public void doupdatejobStatus(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			RunJob rj = testjobService.getRunJobById(runJobId);
			Testjob tj = testjobService.getTestjobById(testjobId);
			if(TitanContent.RUN_JOB_STATUS_SUCCESS.equals(rj.getResult())){
				tj.setState(TitanContent.TEST_JOB_STATUS_SUCCESS);
			}
			else{
				tj.setState(TitanContent.TEST_JOB_STATUS_FAIL);
			}
			testjobService.saveTestjob(tj);
			
			out.print("success");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	public void checkIsTestJobRunning(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			Testjob tj = testjobService.getTestjobById(testjobId);
			if(TitanContent.TEST_JOB_STATUS_RUNNING.equals(tj.getState()) || TitanContent.TEST_JOB_STATUS_PENDING.equals(tj.getState())){
				out.print("running");
			}else{
				out.print("notrunning");
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
	
	/////////////////////////
	public String testjobResult(){
		try {
			testjobrunVo = testjobService.getRunJobInfoById(runJobId);
			List<TestcaserunVo> rcVoList = testjobService.getRunCaseInfoById(runJobId);
			if(null != rcVoList && rcVoList.size() != 0){
				this.createTestJobResultJson(rcVoList);
			}
			else{
				testJobResultJson = "";
				testjobrunVo.setSuccessCount(0);
				testjobrunVo.setFailCount(0);
			}
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	private void createTestJobResultJson( List<TestcaserunVo> rcVoList){
		StringBuffer s = new StringBuffer();
		int successCount = 0;
		int failCount = 0;
		s.append("[{id:\"1\", pId:0, name:\"");
		s.append(testjobrunVo.getTestjobName());
		s.append("\", isParent: true, open:true, ");
		s.append("diyType: \"testJob\", diyModule:\"none\", ");
		if(TitanContent.RUN_JOB_STATUS_SUCCESS.equals(testjobrunVo.getResult())){
			s.append(" icon: \"../images/zTree/3.png\"");
		}
		else if(TitanContent.RUN_JOB_STATUS_FAIL.equals(testjobrunVo.getResult())){
			s.append(" icon: \"../images/zTree/5.png\"");
		}
		s.append("},");
		Iterator<TestcaserunVo> l = rcVoList.iterator();
		Set<String> planidSet = new HashSet<String>();
		while(l.hasNext()){
			TestcaserunVo tcrVo = l.next();
			if(null == tcrVo.getTestplanId() || "".equals(tcrVo.getTestplanId())){
				s.append("{id:\"");
				s.append(tcrVo.getTestcaseId());
				s.append("\", pId:1, name:\"");
				s.append(tcrVo.getTestcaseName());
				s.append("\", diyType: \"case\",");
				s.append("diyCommand: \"");
				s.append(tcrVo.getCommand());
				s.append("\", diyRuncaseId: \"");
				s.append(tcrVo.getRuntestcaseId());
				s.append("\",");
				if(TitanContent.RUN_CASE_STATUS_SUCCESS.equals(tcrVo.getState())){
					successCount++;
					s.append(" icon: \"../images/zTree/3.png\"");
				}
				else if(TitanContent.RUN_CASE_STATUS_FAIL.equals(tcrVo.getState())){
					failCount++;
					s.append(" icon: \"../images/zTree/5.png\"");
				}
				s.append("},");
			}
			else{
				try {
					if(!planidSet.contains(tcrVo.getTestplanId())){
						planidSet.add(tcrVo.getTestplanId());
						Testplan tp = testplanService.getTestplanById(tcrVo.getTestplanId());
						s.append("{id:\"");
						s.append(tcrVo.getTestplanId());
						s.append("\", pId:1, name:\"");
						s.append(tp.getTestplanName());
						s.append("\", diyType: \"plan\",");
						s.append("diyModule: \"none\",");
						if(getPlanResult(rcVoList, tcrVo.getTestplanId())){
							s.append(" icon: \"../images/zTree/3.png\"");
						}
						else{
							s.append(" icon: \"../images/zTree/5.png\"");
						}
						s.append("},");
					}
					s.append("{id:\"");
					s.append(tcrVo.getTestcaseId());
					s.append("\", pId:\"");
					s.append(tcrVo.getTestplanId());
					s.append("\", name:\"");
					s.append(tcrVo.getTestcaseName());
					s.append("\", diyType: \"caseInPlan\",");
					s.append("diyCommand: \"");
					s.append(tcrVo.getCommand());
					s.append("\", diyRuncaseId: \"");
					s.append(tcrVo.getRuntestcaseId());
					s.append("\",");
					if(TitanContent.RUN_CASE_STATUS_SUCCESS.equals(tcrVo.getState())){
						successCount++;
						s.append(" icon: \"../images/zTree/3.png\"");
					}
					else if(TitanContent.RUN_CASE_STATUS_FAIL.equals(tcrVo.getState())){
						failCount++;
						s.append(" icon: \"../images/zTree/5.png\"");
					}
					s.append("},");
					
				} catch (BaseException e) {
					e.printStackTrace();
				}
			}
			
		}
		s.append("]");
		testJobResultJson = s.toString().replace("},]", "}]");
		testjobrunVo.setSuccessCount(successCount);
		testjobrunVo.setFailCount(failCount);
	}	
	
	public Boolean getPlanResult(List<TestcaserunVo> tcvList, String planId){
		Iterator<TestcaserunVo> it = tcvList.iterator();
		while(it.hasNext()){
			TestcaserunVo tcv = it.next();
			if(null != tcv.getTestplanId() && planId.equals(tcv.getTestplanId()) && TitanContent.RUN_CASE_STATUS_FAIL.equals(tcv.getState())){
				return false;
			}
		}
		return true;
	}
	/////////////////////////
	
	@Override
	public void run() {
    	this.doCaseInBack();
    	return;
	}

	public void getCurrentRunJobId(String testjobId){
		try {
			runJobList = testjobService.getAllRunJob(testjobId);
			if(null != runJobList && runJobList.size() != 0){
				runJobId = runJobList.get(0).getRunJobId();
			}
			else{
				runJobId = "";
			}
		} catch (BaseException e) {
			e.printStackTrace();
		} 
	}
	
	public void getCaseState(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			Testjob tj = testjobService.getTestjobById(testjobId);
			RunCaseVo rcv = new RunCaseVo();
			rcv.setCaseId(testCaseId);
			rcv.setPlanId(testPlanId);
			rcv.setRunjobId(runJobId);
			String strOutput = "";
			
			runCase = testjobService.getRunCaseByVo(rcv);
			if(null != runCase){
				if(TitanContent.RUN_CASE_STATUS_SUCCESS.equals(runCase.getState())){
					strOutput = "success";
				}
				else if(TitanContent.RUN_CASE_STATUS_FAIL.equals(runCase.getState())){
					strOutput = "fail";
				}
				else if(TitanContent.RUN_CASE_STATUS_NOTRUN.equals(runCase.getState())){
					strOutput = "notRun";
				}
				else if(TitanContent.RUN_CASE_STATUS_RUNNING.equals(runCase.getState())){
					strOutput = "running";
				}
			}
			else{
				strOutput = "notSelect";
			}
			if(!TitanContent.TEST_JOB_STATUS_RUNNING.equals(tj.getState())){
				strOutput += "#";
			}
			out.print(strOutput);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	public void getChartXml(){
		List<ReportVo> list = new ArrayList<ReportVo>();
		ReportVo re=new ReportVo();
		PrintWriter out = null;
		try {
			runJobList = testjobService.getAllRunJob(testjobId);
		} catch (BaseException e1) {
			e1.printStackTrace();
		}
		reportVo = this.getReportVoFromJob(runJobList.get(0).getRunJobId());
		try {
			re.setTestcasePass(reportVo.getTestcasePass());
			re.setTestcaseFail(reportVo.getTestcaseFail());
			re.setTestcaseNotrun(reportVo.getTestcaseNotrun());
			re.setTestcaseSkip(reportVo.getTestcaseSkip());
			re.setTestcaseRuning(reportVo.getTestcaseRuning());
			list.add(re);
			JSONArray json = JSONArray.fromObject(list);
			out = this.getResponse().getWriter();
			out.print(json);
			out.flush();
		} catch (IOException ie) {
			ie.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	public String getChartXmlForCase(){
		Document document = DocumentHelper.createDocument();
		// root element
		Element rootElement=document.addElement("chart");
		rootElement.addAttribute("showLegend", "1");
		rootElement.addAttribute("legendPosition", "RIGHT");
		rootElement.addAttribute("caption", "Cases in Project");
		rootElement.addAttribute("showValues", "0");
		rootElement.addAttribute("xAxisName", "Sprints");
		rootElement.addAttribute("yAxisName", "Test jobs");
		rootElement.addAttribute("paletteColors", "00FF00, FF3300, CCCCFF, 33CCCC, 99FF99");
		
		// set element
		Element passElement = rootElement.addElement("set");
		passElement.addAttribute("label", "pass");
		passElement.addAttribute("value", String.valueOf(reportVo.getTestcasePass()));
		passElement.addAttribute("color", "00FF00");
		
		Element failElement = rootElement.addElement("set");
		failElement.addAttribute("label","fail");
		failElement.addAttribute("value",String.valueOf(reportVo.getTestcaseFail()));
		failElement.addAttribute("color", "FF3300");
		
		Element notrunElement = rootElement.addElement("set");
		notrunElement.addAttribute("label","not run");
		notrunElement.addAttribute("value",String.valueOf(reportVo.getTestcaseNotrun()));
		notrunElement.addAttribute("color", "CCCCFF");
		
		Element skippedElement = rootElement.addElement("set");
		skippedElement.addAttribute("label","skipped");
		skippedElement.addAttribute("value",String.valueOf(reportVo.getTestcaseSkip()));
		skippedElement.addAttribute("color", "33CCCC");
		
		Element runningElement = rootElement.addElement("set");
		runningElement.addAttribute("label","running");
		runningElement.addAttribute("value",String.valueOf(reportVo.getTestcaseRuning()));
		runningElement.addAttribute("color", "99FF99");
		
		return document.asXML();
	}
	
	public void saveDefaultPara(){
		PrintWriter out = null;
		int currentUserId = UserSessionUtil.getUser().getUserId();
		try {
			List paraMapList = null;
			if(null != paraJson){
				paraMapList = (ArrayList)JsonUtil.JsonToList(JsonUtil.getJsonArrayFromString(paraJson.replace(",}", "}")));
			}
			Map paraMap = new HashMap();
	        if(null != paraMapList){
	        	paraMap = (Map)paraMapList.get(0);
	        }
	        List<DefaultParameter> dpl= new ArrayList<DefaultParameter>();
	        for(Object o : paraMap.keySet()){  
	        	DefaultParameterId dpId = new DefaultParameterId();
	        	DefaultParameter dp = new DefaultParameter();
	        	dpId.setUserId(currentUserId);
	        	dpId.setTestjobId(testjobId);
	        	dpId.setParadataName(String.valueOf(o));
	        	dp.setId(dpId);
	        	dp.setDefaultValue(String.valueOf(paraMap.get(o)));
	        	dpl.add(dp);
	        }
		
			testjobService.saveDefaultePara(dpl, currentUserId);
			out = this.getResponse().getWriter();
			out.print("");
			out.flush();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	public String goTestjobReport(){
		
		    String id = "";
			try {
				User currentUser = UserSessionUtil.getUser();
				List<Testjob> testjobList1 = testjobService.getAllTestjob();
				testjobList = new ArrayList<Testjob>();
				if(testjobList1!=null&&testjobList1.size()!=0){
					for(Testjob job:testjobList1){
						if(job.getProject().getGroupId()==currentUser.getGroupId()) testjobList.add(job);
					}
				}
				if(testjobList != null && testjobList.size()!=0) id = testjobList.get(0).getTestjobId();
				if ((this.testjobId != null) && !this.testjobId.equals("")){
					 id = this.testjobId;
				}				
				
			    currentUserName = currentUser.getUserCode();
				projectList = projectService.getAllProjectList();
				
				runJobList = testjobService.getAllRunJob(id);
				if(runJobList != null && runJobList.size()!=0) 	reportVo = this.getReportVoFromJob(runJobList.get(0).getRunJobId());
				testjob = testjobService.getTestjobById(id);
				userList = userService.getAllUserByUserVo(userVo);
				if(testjob!=null) sprintList = sprintService.findSprintByProject(testjob.getProjectId()); 
				testcaseList = testjobService.getTestcaseByJobId(id);
				jobserverList = testjobService.getJobServer(testjobId, 0);
				defectinfoVoList = testjobService.getDefectInfoByTestJobId(testjobId);
				selectedTestjobid = testjobId;
				
			} catch (BaseException e) {
				e.printStackTrace();
			}
		navigation = "<div class=\"port_bar\"><a href=\"../project/projectList.do\" class=\"marfin_lef10\">" + UserSessionUtil.getDefaultProjectName() + "</a>->Test job report</div>";
		return SUCCESS;		
	}
	
	private List<String>getUsedServerList(String paraJsonData){
		List<String> res = new ArrayList<String>();
		List paraMapList = (ArrayList)JsonUtil.JsonToList(JsonUtil.getJsonArrayFromString(paraJsonData.replace(",}", "}")));
		Map paraMap = new HashMap();
        if(null != paraMapList){
        	paraMap = (Map)paraMapList.get(0);
        }
        Iterator<String> iter = paraMap.keySet().iterator();
        while(iter.hasNext()){
        	try {
        		String pdName = iter.next();
				ParameterData pd = testjobService.findParameterDataByParaName("$" + pdName);
				if(TitanContent.PARAMETER_TYPE_HOST.equals(pd.getType())){
					res.add(String.valueOf(paraMap.get(pdName)));
				}
			} catch (BaseException e) {
				e.printStackTrace();
			}
        }
		return res;
	}
	
	public void getTestjobBySprint(){
		PrintWriter out = null;
		if (this.sprintId != null){
		  try{	
			testjobList = testjobService.findTestjobBySprint(sprintId);
			out = this.getResponse().getWriter();
			this.getResponse().setHeader("Cache-Control", "no-cache");
			this.getResponse().setContentType("text/json;charset=UTF-8");
			this.getResponse().setCharacterEncoding("utf-8");
			
			testjobVoList = new ArrayList<TestjobVo>();
			
            if(testjobList != null && testjobList.size() != 0){
			  for(Testjob tj : testjobList){
				  testjobVo = new TestjobVo();
				  testjobVo.setTestjobID(tj.getTestjobId());
				  testjobVo.setTestjobName(tj.getTestjobName());
				  testjobVo.setTestjobCode(tj.getTestjobCode());
				 
				  testjobVoList.add(testjobVo);				
			  }
            }	
			JSONArray json = JSONArray.fromObject(testjobVoList);
			out.print(json);
			out.flush();
		  } catch (IOException ie) {
				ie.printStackTrace();
			} catch (BaseException be) {
				be.printStackTrace();
			} finally {
				out.close();
			}
		}	
	}
	
	public TestjobService getTestjobService() {
		return testjobService;
	}

	public void setTestjobService(TestjobService testjobService) {
		this.testjobService = testjobService;
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

	public Testjob getTestjob() {
		return testjob;
	}

	public void setTestjob(Testjob testjob) {
		this.testjob = testjob;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public UserVo getUserVo() {
		return userVo;
	}

	public void setUserVo(UserVo userVo) {
		this.userVo = userVo;
	}

	public List<Project> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}

	public List<Testjob> getTestjobList() {
		return testjobList;
	}

	public void setTestjobList(List<Testjob> testjobList) {
		this.testjobList = testjobList;
	}

	public String getTestjobName() {
		return testjobName;
	}

	public void setTestjobName(String testjobName) {
		this.testjobName = testjobName;
	}

	public List<TestjobVo> getTestjobVoList() {
		return testjobVoList;
	}

	public void setTestjobVoList(List<TestjobVo> testjobVoList) {
		this.testjobVoList = testjobVoList;
	}

	public TestjobVo getTestjobVo() {
		return testjobVo;
	}

	public void setTestjobVo(TestjobVo testjobVo) {
		this.testjobVo = testjobVo;
	}

	public String getTestjobId() {
		return testjobId;
	}

	public void setTestjobId(String testjobId) {
		this.testjobId = testjobId;
	}

	public TestcaseService getTestcaseService() {
		return testcaseService;
	}

	public void setTestcaseService(TestcaseService testcaseService) {
		this.testcaseService = testcaseService;
	}

	public String getTestJobJson() {
		return testJobJson;
	}

	public void setTestJobJson(String testJobJson) {
		this.testJobJson = testJobJson;
	}

	public String getTestCaseJson() {
		return testCaseJson;
	}

	public void setTestCaseJson(String testCaseJson) {
		this.testCaseJson = testCaseJson;
	}

	public String[] getTestjobIdAry() {
		return testjobIdAry;
	}

	public void setTestjobIdAry(String[] testjobIdAry) {
		this.testjobIdAry = testjobIdAry;
	}

	public List<Testplan> getTestplanList() {
		return testplanList;
	}

	public void setTestplanList(List<Testplan> testplanList) {
		this.testplanList = testplanList;
	}

	public TestplanService getTestplanService() {
		return testplanService;
	}

	public void setTestplanService(TestplanService testplanService) {
		this.testplanService = testplanService;
	}

	public List<TestJobCase> getTestjobcaseList() {
		return testjobcaseList;
	}

	public void setTestjobcaseList(List<TestJobCase> testjobcaseList) {
		this.testjobcaseList = testjobcaseList;
	}

	public List<TestJobPlan> getTestjobplanList() {
		return testjobplanList;
	}

	public void setTestjobplanList(List<TestJobPlan> testjobplanList) {
		this.testjobplanList = testjobplanList;
	}

	public String getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(String testCaseId) {
		this.testCaseId = testCaseId;
	}


	public List<Parameter> getParamList() {
		return paramList;
	}

	public void setParamList(List<Parameter> paramList) {
		this.paramList = paramList;
	}

	public List<Server> getJobserverList() {
		return jobserverList;
	}

	public void setJobserverList(List<Server> jobserverList) {
		this.jobserverList = jobserverList;
	}

	public List<Server> getServerList() {
		return serverList;
	}

	public void setServerList(List<Server> serverList) {
		this.serverList = serverList;
	}

	public ServerService getServerService() {
		return serverService;
	}

	public void setServerService(ServerService serverService) {
		this.serverService = serverService;
	}

	public String getTestCaseParaJson() {
		return testCaseParaJson;
	}

	public void setTestCaseParaJson(String testCaseParaJson) {
		this.testCaseParaJson = testCaseParaJson;
	}

	public List<ProjectModule> getModuleList() {
		return moduleList;
	}

	public void setModuleList(List<ProjectModule> moduleList) {
		this.moduleList = moduleList;
	}

	public String getTestPlanId() {
		return testPlanId;
	}

	public void setTestPlanId(String testPlanId) {
		this.testPlanId = testPlanId;
	}

	public List<Testcase> getTestcaseList() {
		return testcaseList;
	}

	public void setTestcaseList(List<Testcase> testcaseList) {
		this.testcaseList = testcaseList;
	}

	public Testcase getTestcase() {
		return testcase;
	}

	public void setTestcase(Testcase testcase) {
		this.testcase = testcase;
	}

	public String getTestcaseId() {
		return testcaseId;
	}

	public void setTestcaseId(String testcaseId) {
		this.testcaseId = testcaseId;
	}

	public String[] getJobServerIds() {
		return jobServerIds;
	}

	public void setJobServerIds(String[] jobServerIds) {
		this.jobServerIds = jobServerIds;
	}

	public TestJobServer getTestJobServer() {
		return testJobServer;
	}

	public void setTestJobServer(TestJobServer testJobServer) {
		this.testJobServer = testJobServer;
	}

	public TestJobServerId getTestJobServerId() {
		return testJobServerId;
	}

	public void setTestJobServerId(TestJobServerId testJobServerId) {
		this.testJobServerId = testJobServerId;
	}

	public List<RunJob> getRunJobList() {
		return runJobList;
	}

	public void setRunJobList(List<RunJob> runJobList) {
		this.runJobList = runJobList;
	}

	public String getRunJobId() {
		return runJobId;
	}

	public void setRunJobId(String runJobId) {
		this.runJobId = runJobId;
	}

	public RunJob getRunJob() {
		return runJob;
	}

	public void setRunJob(RunJob runJob) {
		this.runJob = runJob;
	}

	public RunCase getRunCase() {
		return runCase;
	}

	public void setRunCase(RunCase runCase) {
		this.runCase = runCase;
	}

	public List<RunCase> getRunCaseList() {
		return runCaseList;
	}

	public void setRunCaseList(List<RunCase> runCaseList) {
		this.runCaseList = runCaseList;
	}

	public String getRunCaseId() {
		return runCaseId;
	}

	public void setRunCaseId(String runCaseId) {
		this.runCaseId = runCaseId;
	}

	public String getRunCaseState() {
		return runCaseState;
	}

	public void setRunCaseState(String runCaseState) {
		this.runCaseState = runCaseState;
	}

	public String getTestJobResultJson() {
		return testJobResultJson;
	}

	public void setTestJobResultJson(String testJobResultJson) {
		this.testJobResultJson = testJobResultJson;
	}

	public String getRelatelogPath() {
		return relatelogPath;
	}

	public void setRelatelogPath(String relatelogPath) {
		this.relatelogPath = relatelogPath;
	}

	public SprintService getSprintService() {
		return sprintService;
	}

	public void setSprintService(SprintService sprintService) {
		this.sprintService = sprintService;
	}

	public List<Sprint> getSprintList() {
		return sprintList;
	}

	public void setSprintList(List<Sprint> sprintList) {
		this.sprintList = sprintList;
	}

	public String getTestJobJsonDB() {
		return testJobJsonDB;
	}

	public void setTestJobJsonDB(String testJobJsonDB) {
		this.testJobJsonDB = testJobJsonDB;
	}

	public String getParaHtml() {
		return paraHtml;
	}

	public void setParaHtml(String paraHtml) {
		this.paraHtml = paraHtml;
	}

	public String getServerHtml() {
		return serverHtml;
	}

	public void setServerHtml(String serverHtml) {
		this.serverHtml = serverHtml;
	}

	public ParameterVo getParameterVo() {
		return parameterVo;
	}

	public void setParameterVo(ParameterVo parameterVo) {
		this.parameterVo = parameterVo;
	}

	public String getParaJson() {
		return paraJson;
	}

	public void setParaJson(String paraJson) {
		this.paraJson = paraJson;
	}

	public TestjobrunVo getTestjobrunVo() {
		return testjobrunVo;
	}

	public void setTestjobrunVo(TestjobrunVo testjobrunVo) {
		this.testjobrunVo = testjobrunVo;
	}

	public String[] getRuncaseIdAry() {
		return runcaseIdAry;
	}

	public void setRuncaseIdAry(String[] runcaseIdAry) {
		this.runcaseIdAry = runcaseIdAry;
	}

	public String getSprintId() {
		return sprintId;
	}

	public void setSprintId(String sprintId) {
		this.sprintId = sprintId;
	}

	public String getRunCaseNote() {
		return runCaseNote;
	}

	public void setRunCaseNote(String runCaseNote) {
		this.runCaseNote = runCaseNote;
	}
	
	public String getRunjobcaseJson() {
		return runjobcaseJson;
	}

	public void setRunjobcaseJson(String runjobcaseJson) {
		this.runjobcaseJson = runjobcaseJson;
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

	public ReportVo getReportVo() {
		return reportVo;
	}

	public void setReportVo(ReportVo reportVo) {
		this.reportVo = reportVo;
	}

	public String getDefectNo() {
		return defectNo;
	}

	public void setDefectNo(String defectNo) {
		this.defectNo = defectNo;
	}

	public String getDefectType() {
		return defectType;
	}

	public void setDefectType(String defectType) {
		this.defectType = defectType;
	}

	public List<UserTag> getUsertagList() {
		return usertagList;
	}

	public void setUsertagList(List<UserTag> usertagList) {
		this.usertagList = usertagList;
	}

	public List<TestjobInGroupVo> getTestjobingroupvoList() {
		return testjobingroupvoList;
	}

	public void setTestjobingroupvoList(List<TestjobInGroupVo> testjobingroupvoList) {
		this.testjobingroupvoList = testjobingroupvoList;
	}

	public List<DefectInfoVo> getDefectinfoVoList() {
		return defectinfoVoList;
	}

	public void setDefectinfoVoList(List<DefectInfoVo> defectinfoVoList) {
		this.defectinfoVoList = defectinfoVoList;
	}

	public String getSelectedProjectid() {
		return selectedProjectid;
	}

	public void setSelectedProjectid(String selectedProjectid) {
		this.selectedProjectid = selectedProjectid;
	}

	public String getSelectedSprintid() {
		return selectedSprintid;
	}

	public void setSelectedSprintid(String selectedSprintid) {
		this.selectedSprintid = selectedSprintid;
	}

	public String getSelectedTestjobid() {
		return selectedTestjobid;
	}

	public void setSelectedTestjobid(String selectedTestjobid) {
		this.selectedTestjobid = selectedTestjobid;
	}
}