package com.hp.titan.project.action;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.io.diff.SVNDiffWindowApplyBaton;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.hp.app.action.DefaultBaseAction;
import com.hp.app.common.util.DateUtils;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.auth.model.Group;
import com.hp.titan.auth.model.Role;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.service.GroupService;
import com.hp.titan.auth.service.RoleService;
import com.hp.titan.common.constants.TitanContent;
import com.hp.titan.common.util.SVNDiff;
import com.hp.titan.common.util.UserSessionUtil;
import com.hp.titan.common.vo.DefectVo;
import com.hp.titan.common.vo.LoginUserVo;
import com.hp.titan.common.vo.UserstoryVo;
import com.hp.titan.mytitan.model.CommitPath;
import com.hp.titan.mytitan.model.CommitReport;
import com.hp.titan.mytitan.model.ProjectRallyQuix;
import com.hp.titan.mytitan.model.Reportary;
import com.hp.titan.mytitan.model.UserstoryInfo;
import com.hp.titan.mytitan.service.MyTitanService;
import com.hp.titan.project.model.CommitusDefect;
import com.hp.titan.project.model.CommitusDefectId;
import com.hp.titan.project.model.DedicatedServer;
import com.hp.titan.project.model.Project;
import com.hp.titan.project.model.ProjectModule;
import com.hp.titan.project.service.ProjectService;
import com.hp.titan.project.vo.ProjectVo;
import com.hp.titan.project.vo.ReportVo;
import com.hp.titan.sprint.model.Sprint;
import com.hp.titan.sprint.model.SprintId;
import com.hp.titan.sprint.service.SprintService;
import com.hp.titan.test.model.DefectInfo;
import com.hp.titan.test.model.ParameterData;
import com.hp.titan.test.model.RunJob;
import com.hp.titan.test.service.TestcaseService;
import com.hp.titan.test.service.TestjobService;
import com.hp.titan.test.vo.TestjobVo;

public class ProjectAction  extends DefaultBaseAction {
	
	private List<Project> projectList;
	private List<Project> otherProjectList;
	private List<ProjectModule> moduleList;
	private List<DedicatedServer> dedicatedServerList;
	private List<ParameterData> projectParaList;
	private List<User> manageList; 
	private List<Group> groupList;
	private List<Reportary> reportaryList;
	private List<ProjectRallyQuix> projectRallyList;
	private List<ProjectRallyQuix> projectQuixList;
	private List<DefectInfo> mysubitteddefectList;
	private List<UserstoryInfo> usinfoList;
	private Project project;
	private Sprint sprint;
	private ProjectVo projectVo;
	private Reportary reportary; 
	private ProjectRallyQuix projectRallyQuix;
	private String[] projectIdAry;
	private String[] rallyNameIdAry;
	private String[] quixNameIdAry;
	private String[] reportaryIdAry;
	
	private ProjectService projectService;
	private RoleService roleService;
	private GroupService groupService;
	private SprintService sprintService;
	private TestcaseService testcaseService;
	private MyTitanService mytitanService;
	private TestjobService testjobService;
	
	private String projectName;
	private String projectId;
	private String reportaryId;
	private String projectnameinRally;
	private String projectnameinQuix;
	private String jsonObj;
	private String showType;
	private String component;
	private String defaultProjectId;
	private String pageTable1_length;
	private String pageTableSvn_length;
	private String pageTableRally_length;
	private String pageTableQuix_length;
	private String settingType;
	
	private String navigation = " <div class=\"port_bar\"><a href=\"../project/projectList.do\" class=\"marfin_lef10\">" + UserSessionUtil.getDefaultProjectName() + "</a></div>";
	
	private TestjobVo testjobVo;
	
	private List<TestjobVo> testjobVoList;
	private List<RunJob> runjobList;
	private List<TestjobVo> testjobVoPassList;
	private List<TestjobVo> testjobVoFailList;
	
	private ReportVo reportVo;
	private List<ReportVo> reportVoList;
	private List<Sprint> sprintList;
	

	
	// Get all the Projects
	public String doSearchProjects(){
		defaultProjectId = UserSessionUtil.getDefaultProject();
		List<Role> roles = new ArrayList<Role>(UserSessionUtil.getUser().getUserRoles());
		if(TitanContent.ROLE_ADMIN.equals(roles.get(0).getRoleName())){
			try {
				projectList = projectService.getAllProjectList();
			} catch (BaseDaoException e) {
				e.printStackTrace();
			}
		}
		else{
			projectList = new ArrayList<Project>();
			Set<Project> s = UserSessionUtil.getUser().getUserProjects();
			if(null != s && s.size() != 0){
				Iterator<Project> it = s.iterator();
				while(it.hasNext()){
					Project p = it.next();
					if(p.getIsValid() == 0){
						projectList.add(p);
					}
				}
			}
			try {
				otherProjectList = projectService.getOtherProjectList(s);
			} catch (BaseException e) {
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	// Go to create project page
	public String goProjectCreate(){
		// Get all the manage user
		project = new Project();
		try {
			manageList = roleService.findUserByRoleName(TitanContent.ROLE_MANAGER);
			groupList = groupService.getAllGroupList();
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	// Save project
	public String doProjectSave(){
		
		JSONObject json = JSONObject.fromObject(jsonObj);
		JSONArray moduleArray = json.getJSONArray("moduleArray");
		JSONArray serverArray = json.getJSONArray("serverArray");
		JSONArray projectParaArray = json.getJSONArray("projectParaArray");

		try {
			if(project.getProjectId() != null){
				project.setCreateDate(Timestamp.valueOf(projectVo.getStrCreateDate()));
				project.setLastUpdateUser(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
				project.setLastUpdate_Date(DateUtils.getCurrentDate());
			}
			else{
				project.setCreateUser(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
				project.setCreateDate(DateUtils.getCurrentDate());
			}
			projectService.saveProject(project);
			
			String projectId = project.getProjectId();
			int currentUserId = UserSessionUtil.getUser().getUserId();
			Date currentDate = new Date();
			if(moduleArray.size()==0){
				//when front page delete all together
				projectService.deleteModulesByProjectId(projectId);
			}else{
				//when front page delete but not all
				moduleList = projectService.getProjectModuleList(projectId);
				for(ProjectModule delModule : moduleList){
					boolean isDel = true;
					for (int i = 0; i <moduleArray.size(); i++){
						JSONObject module = moduleArray.getJSONObject(i);
						if(delModule.getModuleId().equals(module.getString("moduleId"))){
							isDel = false;
							break;
						}
					}
					if(isDel){
						projectService.deleteModule(delModule);
					}
				}
				//update and add
				List<ProjectModule> modules = new ArrayList<ProjectModule>();
				for (int i = 0; i < moduleArray.size(); i++) {
					JSONObject module = moduleArray.getJSONObject(i);
					ProjectModule moduleObj = new ProjectModule();
					String moduleId = module.getString("moduleId");
					if(moduleId!=null && !"".equals(moduleId)){
						moduleObj.setModuleId(moduleId);
					}
					moduleObj.setProjectId(projectId);
					moduleObj.setModuleName(module.getString("moduleName"));
					moduleObj.setRemark(module.getString("remark"));
					moduleObj.setCreateUserId(currentUserId);
					moduleObj.setCreateDate(currentDate);
					moduleObj.setUpdateUserId(currentUserId);
					moduleObj.setUpdateDate(currentDate);
					modules.add(moduleObj);
				}
				projectService.saveProjectModules(modules);
			}			
			if(serverArray.size()==0){
				//when front page delete all together
				projectService.deleteDedicatedServersByProjectId(projectId);
			}else{
				//when front page delete but not all
				dedicatedServerList = projectService.getDedicatedServerList(projectId);
				for(DedicatedServer delServer: dedicatedServerList){
					boolean isDel = true;
					for(int i=0; i<serverArray.size(); i++){
						JSONObject server = serverArray.getJSONObject(i);
						if(delServer.getServerId()
								.equals(server.getString("serverId"))){
							isDel = false;
							break;
						}
					}
					if(isDel){
						projectService.deleteDedicatedServer(delServer);
					}
				}
				//update and add
				List<DedicatedServer> servers = new ArrayList<DedicatedServer>();
				for(int i=0; i<serverArray.size(); i++){
					JSONObject server = serverArray.getJSONObject(i);
					DedicatedServer serverObj = new DedicatedServer();
					String serverId = server.getString("serverId");
					if(serverId!=null && !"".equals(serverId)){
						serverObj.setServerId(serverId);
					}
					serverObj.setProjectId(projectId);
					serverObj.setServerName(server.getString("serverName"));
					serverObj.setServerHostname(server.getString("serverHostname"));
					serverObj.setServerIp(server.getString("serverIp"));
					serverObj.setServerAccount(server.getString("serverAccount"));
					serverObj.setServerPasswd(server.getString("serverPasswd"));
					serverObj.setRemark(server.getString("remark"));
					servers.add(serverObj);
				}
				projectService.saveDedicatedServers(servers);
			}
			List<ParameterData> paraList = new ArrayList<ParameterData>();
			projectService.deleteParaDataByProjectId(projectId);
			if(projectParaArray.size()>0){
				for(int i = 0; i < projectParaArray.size(); i++){
					ParameterData pd = new ParameterData();
					JSONObject pdJson = projectParaArray.getJSONObject(i);
					pd.setParadataName(pdJson.getString("projectParaName"));
					pd.setProjectId(projectId);
					pd.setType(pdJson.getString("projectParaType"));
					paraList.add(pd);
				}
				projectService.saveParaData(paraList);
			}
			
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	// Go project edit
	public String goProjectUpdate(){
//		 if ((this.projectIdAry != null) && (this.projectIdAry.length == 1)) {
//		      String projectId = this.projectIdAry[0];
		      try {
		    	  project= projectService.getProjectById(projectId);
		    	  moduleList = projectService.getProjectModuleList(projectId);
		    	  dedicatedServerList = projectService.getDedicatedServerList(projectId);
		    	  manageList = roleService.findUserByRoleName(TitanContent.ROLE_MANAGER);
		    	  groupList = groupService.getAllGroupList();
		    	  projectParaList = projectService.findParameterDataByProjectId(projectId);
		    	  
		    	  List<ReportVo> reportVoList1 = new ArrayList<ReportVo>();
		    	  sprintList = sprintService.findSprintByProject(projectId);
		    	  if(sprintList != null && sprintList.size()!= 0){
		    		  for(Sprint spt: sprintList){
		    			  reportVoList1.add(this.getReportVoFromProject(projectId, spt));
		    		  }
		    	  }
		    	  reportVoList = reportVoList1;
		    	  reportVo = this.getReportVoFromProject(projectId, null);
		    	  
			} catch (BaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      return SUCCESS;
//		 }
//		 return ERROR;
	}
	
	public String goProjectView(){
		 if ((this.projectId != null) && !this.projectId.equals("")) {
		      String projectId = this.projectId;
		      try {
		    	  project= projectService.getProjectById(projectId);
		    	  moduleList = projectService.getProjectModuleList(projectId);
		    	  dedicatedServerList = projectService.getDedicatedServerList(projectId);
		    	  manageList = roleService.findUserByRoleName(TitanContent.ROLE_MANAGER);
		    	  groupList = groupService.getAllGroupList();
		    	  this.createReportVoList();
		    	  reportVo = this.getReportVoFromProject(projectId, null);
			} catch (BaseException e) {
				e.printStackTrace();
			}
		      return SUCCESS;
		 }
		 return ERROR;
	}
	
	public String goProjectReport(){
		projectId = "";
		User currentUser = UserSessionUtil.getUser();		
		try {
			projectList = projectService.getProjectByGroup(currentUser.getGroupId());
			if(null != projectList && projectList.size() != 0){
				if (this.projectId == null || "".equals(this.projectId)) {
					projectId = projectList.get(0).getProjectId();
				}
				project = projectService.getProjectById(projectId);
				moduleList = projectService.getProjectModuleList(projectId);
				dedicatedServerList = projectService
						.getDedicatedServerList(projectId);
				manageList = roleService
						.findUserByRoleName(TitanContent.ROLE_MANAGER);
				groupList = groupService.getAllGroupList();
	
				this.createReportVoList();
				reportVo = this.getReportVoFromProject(projectId, null);
			}
			} catch (BaseDaoException e) {
				e.printStackTrace();
			} catch (BaseException e) {
				e.printStackTrace();
		}
		navigation = "<div class=\"port_bar\"><a href=\"../project/projectList.do\" class=\"marfin_lef10\">"
				+ UserSessionUtil.getDefaultProjectName()
				+ "</a>->Project report</div>";
		return SUCCESS;
	}
	
	public void createReportVoList() {
		reportVoList = new ArrayList<ReportVo>();
		try {
			sprintList = sprintService.findSprintByProject(projectId);
		} catch (BaseDaoException e) {
			e.printStackTrace();
		}
		if (sprintList != null && sprintList.size() != 0) {
			for (Sprint spt : sprintList) {
				reportVoList.add(this.getReportVoFromProject(projectId, spt));
			}
		}
	}
	
	public void getChartXml(){
		List<ReportVo> list= new ArrayList<ReportVo>();
		PrintWriter out = null;
		this.createReportVoList();
//		String strXml = "";
//		if("0".equals(showType)){
//			strXml = this.getChartXmlForJob();
//		}
//		else{
//			strXml = this.getChartXmlForCase();
//		}
		Iterator<ReportVo> it = reportVoList.iterator();
		while(it.hasNext()){
			ReportVo rv = it.next();
			list.add(rv);
		}
		try {
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
	
	public String getChartXmlForJob(){
		Document document = DocumentHelper.createDocument();
		// root element
		Element rootElement=document.addElement("chart");
		rootElement.addAttribute("showLegend", "1");
		rootElement.addAttribute("legendPosition", "RIGHT");
		rootElement.addAttribute("caption", "Sprints in Project");
		rootElement.addAttribute("showValues", "0");
		rootElement.addAttribute("xAxisName", "Sprints");
		rootElement.addAttribute("yAxisName", "Test jobs");//paletteColors 
		rootElement.addAttribute("paletteColors", "00FF00, FF3300, CCCCFF, 33CCCC, 99FF99, 88FF88");
		
		// columns element
		Element categoriesElement = rootElement.addElement("categories");
		// pass data element
		Element passDataElement = rootElement.addElement("dataset");
		passDataElement.addAttribute("seriesName", "pass");
		
		// fail data element
		Element failDataElement = rootElement.addElement("dataset");
		failDataElement.addAttribute("seriesName", "fail");
		
		// active data element
		Element activeDataElement = rootElement.addElement("dataset");
		activeDataElement.addAttribute("seriesName", "active");
		
		// pending data element
		Element pendingDataElement = rootElement.addElement("dataset");
		pendingDataElement.addAttribute("seriesName", "pending");
		
		// running data element
		Element runningDataElement = rootElement.addElement("dataset");
		runningDataElement.addAttribute("seriesName", "running");
		
		// running data element
		Element abortDataElement = rootElement.addElement("dataset");
		abortDataElement.addAttribute("seriesName", "abort");
		
		Iterator<ReportVo> it = reportVoList.iterator();
		while(it.hasNext()){
			ReportVo rv = it.next();
			Element sprintElement = categoriesElement.addElement("category");
			sprintElement.addAttribute("label", rv.getSprintName());
			
			Element passValueElement = passDataElement.addElement("set");
			passValueElement.addAttribute("value", String.valueOf(rv.getTestjobPass()));
			passValueElement.addAttribute("color", "00FF00");
			
			Element failValueElement = failDataElement.addElement("set");
			failValueElement.addAttribute("value", String.valueOf(rv.getTestjobFail()));
			failValueElement.addAttribute("color", "FF3300");
			
			Element activeValueElement = activeDataElement.addElement("set");
			activeValueElement.addAttribute("value", String.valueOf(rv.getTestjobActive()));
			activeValueElement.addAttribute("color", "CCCCFF");
			
			Element pendingValueElement = pendingDataElement.addElement("set");
			pendingValueElement.addAttribute("value", String.valueOf(rv.getTestjobPending()));
			pendingValueElement.addAttribute("color", "33CCCC");
			
			Element runningValueElement = runningDataElement.addElement("set");
			runningValueElement.addAttribute("value", String.valueOf(rv.getTestjobRunning()));
			runningValueElement.addAttribute("color", "99FF99");
			
			Element abortValueElement = abortDataElement.addElement("set");
			abortValueElement.addAttribute("value", String.valueOf(rv.getTestjobAbort()));
			abortValueElement.addAttribute("color", "88FF88");
		}
		return document.asXML();
	}
	
	public String getChartXmlForCase(){
		Document document = DocumentHelper.createDocument();
		// root element
		Element rootElement=document.addElement("chart");
		rootElement.addAttribute("showLegend", "1");
		rootElement.addAttribute("legendPosition", "RIGHT");
		rootElement.addAttribute("caption", "Sprints in Project");
		rootElement.addAttribute("showValues", "0");
		rootElement.addAttribute("xAxisName", "Sprints");
		rootElement.addAttribute("yAxisName", "Test cases");
		rootElement.addAttribute("paletteColors", "00FF00, FF3300, CCCCFF, 33CCCC, 99FF99");
		
		// columns element
		Element categoriesElement = rootElement.addElement("categories");
		// pass data element
		Element passDataElement = rootElement.addElement("dataset");
		passDataElement.addAttribute("seriesName", "pass");
		
		// fail data element
		Element failDataElement = rootElement.addElement("dataset");
		failDataElement.addAttribute("seriesName", "fail");
		
		// not run data element
		Element notRunDataElement = rootElement.addElement("dataset");
		notRunDataElement.addAttribute("seriesName", "notrun");
		
		// skipped data element
		Element skippedDataElement = rootElement.addElement("dataset");
		skippedDataElement.addAttribute("seriesName", "skipped");
		
		// running data element
		Element runningDataElement = rootElement.addElement("dataset");
		runningDataElement.addAttribute("seriesName", "running");
		
		Iterator<ReportVo> it = reportVoList.iterator();
		while(it.hasNext()){
			ReportVo rv = it.next();
			Element sprintElement = categoriesElement.addElement("category");
			sprintElement.addAttribute("label", rv.getSprintName());
			
			Element passValueElement = passDataElement.addElement("set");
			passValueElement.addAttribute("value", String.valueOf(rv.getTestcasePass()));
			passValueElement.addAttribute("color", "00FF00");
			
			Element failValueElement = failDataElement.addElement("set");
			failValueElement.addAttribute("value", String.valueOf(rv.getTestcaseFail()));
			failValueElement.addAttribute("color", "FF3300");
			
			Element notRunValueElement = notRunDataElement.addElement("set");
			notRunValueElement.addAttribute("value", String.valueOf(rv.getTestcaseNotrun()));
			notRunValueElement.addAttribute("color", "CCCCFF");
			
			Element skippedValueElement = skippedDataElement.addElement("set");
			skippedValueElement.addAttribute("value", String.valueOf(rv.getTestcaseSkip()));
			skippedValueElement.addAttribute("color", "33CCCC");
			
			Element runningValueElement = runningDataElement.addElement("set");
			runningValueElement.addAttribute("value", String.valueOf(rv.getTestcaseRuning()));
			runningValueElement.addAttribute("color", "99FF99");
		}
		return document.asXML();
	}
	
	public ReportVo getReportVoFromProject(String projectId, Sprint spt){
	 testjobVo = new TestjobVo();
	 reportVo  = new ReportVo();
	 String sprintId = null;
	 if(null != spt){
		 sprintId = spt.getId().getSprintId();
		 reportVo.setSprintName(spt.getSprintName());
	 }
	 try{
   	  testjobVo.setProjectId(projectId);
   	  if(sprintId != null) testjobVo.setSprintId(sprintId);
   	  testjobVoList = testjobService.findAllTestjob(testjobVo, null);
   	  testjobVo.setTestjobState(TitanContent.TEST_JOB_STATUS_SUCCESS);
//  	  testjobVoPassList = testjobService.findAllTestjob(testjobVo, null);
   	  int jobPassNum = testjobService.findAllTestjob(testjobVo, null).size();
   	  testjobVo.setTestjobState(TitanContent.TEST_JOB_STATUS_FAIL);
//   	  testjobVoFailList = testjobService.findAllTestjob(testjobVo, null);
   	  int jobFailNum = testjobService.findAllTestjob(testjobVo, null).size();
   	  testjobVo.setTestjobState(TitanContent.TEST_JOB_STATUS_ACTIVE);
   	  int jobActiveNum = testjobService.findAllTestjob(testjobVo, null).size();
   	  testjobVo.setTestjobState(TitanContent.TEST_JOB_STATUS_RUNNING);
      int jobRunNum = testjobService.findAllTestjob(testjobVo, null).size();
      testjobVo.setTestjobState(TitanContent.TEST_JOB_STATUS_PENDING);
      int jobPendNum = testjobService.findAllTestjob(testjobVo, null).size();
      testjobVo.setTestjobState(TitanContent.TEST_JOB_STATUS_ABORT);
      int jobAbortNum = testjobService.findAllTestjob(testjobVo, null).size();
      
   	  
   	  
   	  reportVo.setTestjobTotal(testjobVoList.size());
   	  reportVo.setTestjobPass(jobPassNum);
   	  reportVo.setTestjobFail(jobFailNum);
   	  reportVo.setTestjobActive(jobActiveNum);
   	  reportVo.setTestjobPending(jobPendNum);
   	  reportVo.setTestjobRunning(jobRunNum);
   	  reportVo.setTestjobAbort(jobAbortNum);
   	  NumberFormat numberFormat = NumberFormat.getInstance();
   	  numberFormat.setMinimumFractionDigits(0);
   	  String passCent = numberFormat.format((float)jobPassNum/(float)testjobVoList.size()*100);
   	  String failCent = numberFormat.format((float)jobFailNum/(float)testjobVoList.size()*100);
   	  String activeCent = numberFormat.format((float)jobActiveNum/(float)testjobVoList.size()*100);
      String pendCent = numberFormat.format((float)jobPendNum/(float)testjobVoList.size()*100);
      String runCent = numberFormat.format((float)jobRunNum/(float)testjobVoList.size()*100);
      String abortCent = numberFormat.format((float)jobAbortNum/(float)testjobVoList.size()*100);
   	  reportVo.setPassCent(passCent);
   	  reportVo.setFailCent(failCent);
   	  reportVo.setActiveCent(activeCent);
   	  reportVo.setPendCent(pendCent);
   	  reportVo.setRunCent(runCent);
   	  reportVo.setAbortCent(abortCent);
   	  
   	  int total = 0;
   	  int pass = 0;
   	  int fail = 0;
   	  int notrun = 0;
   	  int skip = 0;
   	  int running  = 0;
   	  
   	  if(testjobVoList!=null&&testjobVoList.size()!=0){
   	       for(TestjobVo testjobVo : testjobVoList ){
   	    	runjobList = testjobService.getAllRunJob(testjobVo.getTestjobID());
   	    	 if (runjobList != null && runjobList.size()!=0){
   	    	   pass = pass + testjobService.getRunCaseByResult(runjobList.get(0).getRunJobId(), TitanContent.RUN_CASE_STATUS_SUCCESS).size();
   	    	   total = total + testjobService.getRunCaseByRunjobId(runjobList.get(0).getRunJobId()).size();
   	    	   fail = fail + testjobService.getRunCaseByResult(runjobList.get(0).getRunJobId(), TitanContent.RUN_CASE_STATUS_FAIL).size();
   	    	   notrun = notrun + testjobService.getRunCaseByResult(runjobList.get(0).getRunJobId(), TitanContent.RUN_CASE_STATUS_NOTRUN).size();
   	    	   skip = skip + testjobService.getRunCaseByResult(runjobList.get(0).getRunJobId(), TitanContent.RUN_CASE_STATUS_SKIP).size();
   	    	   running = running + testjobService.getRunCaseByResult(runjobList.get(0).getRunJobId(), TitanContent.RUN_CASE_STATUS_RUNNING).size();
   	    	}
   		 }
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
	
	// Do project delete
	public String doProjectRemove(){
		if ((this.projectIdAry != null) && (this.projectIdAry.length != 0)) {
			try {
				projectService.deleteProjectList(this.projectIdAry[0]);
			} catch (BaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	// Check if project name existed
	public String checkProjectName() throws IOException{
		PrintWriter out = this.getResponse().getWriter();
		  try {
			if(projectService.isExistProject(projectName)){
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

	// Check if the project has sprint
	public String checkProjectHasSpring() throws IOException{
		PrintWriter out = this.getResponse().getWriter();
		sprint = new Sprint();
		SprintId sprintId = new SprintId();
		sprintId.setProjectId(projectId);
		sprint.setId(sprintId);
		List<Sprint> l = null;
		try {
			l = sprintService.getSprintList(sprint);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(null != l && l.size() != 0){
			out.print("exist");
			out.flush();
		}
		else{
			out.print("noexist");
			out.flush();
		}
		out.close();
		return null;
	}
	
	public void getProjectModules(){
		PrintWriter out = null;
		try {
			moduleList = projectService.getProjectModuleList(projectId);
			
			out = this.getResponse().getWriter();
			this.getResponse().setHeader("Cache-Control", "no-cache");
			this.getResponse().setContentType("text/json;charset=UTF-8");
			this.getResponse().setCharacterEncoding("utf-8");
			
			JSONArray json = JSONArray.fromObject(moduleList);
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
	
	public void getDedicatedServers(){
		PrintWriter out = null;
		try {
			dedicatedServerList = projectService.getDedicatedServerList(projectId);
			
			out = this.getResponse().getWriter();
			this.getResponse().setHeader("Cache-Control", "no-cache");
			this.getResponse().setContentType("text/json;charset=UTF-8");
			this.getResponse().setCharacterEncoding("utf-8");
			
			JSONArray json = JSONArray.fromObject(dedicatedServerList);
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
	
	public String goProjectAddRally(){
		projectRallyQuix = new ProjectRallyQuix();
		try {
			project= projectService.getProjectById(projectId);
		} catch (BaseDaoException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String goProjectAddQuix(){
		projectRallyQuix = new ProjectRallyQuix();
		try {
			project= projectService.getProjectById(projectId);
		} catch (BaseDaoException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String doProjectAddRally(){
		try {
			projectService.doProjectAddRally(projectRallyQuix);
			projectId = projectRallyQuix.getProjectId();
		} catch (BaseException e) {
			e.printStackTrace();
		}
		settingType = "Rally";
		return SUCCESS;
	}
	
	public String doProjectAddQuix(){
		try {
			projectService.doProjectAddRally(projectRallyQuix);
			projectId = projectRallyQuix.getProjectId();
		} catch (BaseException e) {
			e.printStackTrace();
		}
		settingType = "Quix";
		return SUCCESS;
	}
	
	/**
	 * This is for System->go rally project update
	 * Go to project rally edit page
	 * @return
	 */
	public String goUpdateRally(){
		try {
			projectRallyQuix = projectService.getProjectRallyQuixById(rallyNameIdAry[0]);
			project = projectService.getProjectById(projectRallyQuix.getProjectId());
			projectId = project.getProjectId();
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * This is for System->go rally project update
	 * Go to project rally edit page
	 * @return
	 */
	public String goUpdateQuix(){
		try {
			projectRallyQuix = projectService.getProjectRallyQuixById(quixNameIdAry[0]);
			project = projectService.getProjectById(projectRallyQuix.getProjectId());
			projectId = project.getProjectId();
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * This is for System->go rally project delete
	 * Go to project rally edit page
	 * @return
	 */
	public String doRallyDelete(){
		try {
			projectRallyQuix = projectService.getProjectRallyQuixById(rallyNameIdAry[0]);
			projectRallyQuix.setIsValid(1);
			projectId = projectRallyQuix.getProjectId();
			projectService.doProjectAddRally(projectRallyQuix);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		settingType = "Rally";
		return SUCCESS;
	}
	
	/**
	 * This is for System->go quix project delete
	 * Go to project rally edit page
	 * @return
	 */
	public String doQuixDelete(){
		try {
			projectRallyQuix = projectService.getProjectRallyQuixById(quixNameIdAry[0]);
			projectRallyQuix.setIsValid(1);
			projectId = projectRallyQuix.getProjectId();
			projectService.doProjectAddRally(projectRallyQuix);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		settingType = "Quix";
		return SUCCESS;
	}
	
	/**
	 * This is for System->reportary List
	 * To list all the reportary info
	 * @return
	 */
	public String reportaryList(){
		try {
			// SVN
			reportaryList = projectService.getReportaryByProjectId(projectId);
			// Rally
			projectRallyList = projectService.getProjectRallyByProjectId(projectId);
			// Quix
			projectQuixList = projectService.getProjectQuixByProjectId(projectId);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * This is for System->go reportary Save
	 * Go to create reportary page
	 * @return
	 */
	public String goProjectAddReport(){
		reportary = new Reportary();
	    try {
			project= projectService.getProjectById(projectId);
			moduleList = projectService.getProjectModuleList(projectId);
		} catch (BaseDaoException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * This is for System->do reportary Save
	 * Save reportary info
	 * @return
	 */
	public String doReportarySave(){
		try {
			reportary.setReportaryId(null);
			reportary.setProjectId(projectId);
			projectService.saveReportary(reportary);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		settingType = "SVN";
		return SUCCESS;
	}
	
	/**
	 * This is for System->go reportary update
	 * Go to reportary edit page
	 * @return
	 */
	public String goReportaryUpdate(){
		try {
			reportary = projectService.getReportaryByReportaryId(reportaryIdAry[0]);
			project = projectService.getProjectById(reportary.getProjectId());
			moduleList = projectService.getProjectModuleList(reportary.getProjectId());
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * This is for System->go reportary delete
	 * Delete 
	 * @return
	 */
	public String doReportaryDelete(){
		try {
			reportary = projectService.getReportaryByReportaryId(reportaryIdAry[0]);
			projectId = reportary.getProjectId();
			reportary.setIsValid(1);
			projectService.saveReportary(reportary);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		settingType = "SVN";
		return SUCCESS;
	}
	
	/**
	 * This is for System->go reportary update
	 * Do reportary edit page
	 * @return
	 */
	public String doReportaryUpdate(){
		try {
			Reportary orginalReportary = projectService.getReportaryByReportaryId(reportary.getReportaryId());
			reportary.setProjectId(projectId);
			projectService.saveReportary(reportary);
			
			if(!orginalReportary.getSvnUrl().equals(reportary.getSvnUrl())){
				this.clearSyncedRepositoryData(reportary.getReportaryId());
			}
		} catch (BaseException e) {
			e.printStackTrace();
		}
		settingType = "SVN";
		return SUCCESS;
	}
	
	/**
	 * This is for System->go reportary delete
	 * Do reportary delete
	 * @return
	 */
	public String goReportaryDelete(){
		try {
			reportary = projectService.getReportaryByReportaryId(reportaryIdAry[0]);
			reportary.setIsValid(1);
			projectService.saveReportary(reportary);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * This is for System->go reportary sync
	 */
	@SuppressWarnings("unchecked")
	public void syncCommit(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			reportary = projectService.getReportaryByReportaryId(reportaryId);
			String url = reportary.getSvnUrl();
			String name = reportary.getUserName();
			String password = reportary.getPwd();
			
			String lastVersion = projectService.getMaxSvnVersion(reportaryId);
			
			DAVRepositoryFactory.setup();
			SVNURL svnUrl = SVNURL.parseURIDecoded(url);
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
			SVNRepository repo = SVNRepositoryFactory.create(svnUrl);
			
			repo.setAuthenticationManager(authManager);

			// Create a diff client
			SVNClientManager clientManager = SVNClientManager.newInstance(null, authManager);
			SVNDiffClient diffClient = clientManager.getDiffClient();


			// Get svn log for entire repo history
			long currentRev = repo.getLatestRevision();
			if(null != lastVersion && lastVersion.equals(String.valueOf(currentRev))){
				return;
			}
			ArrayList<SVNLogEntry> entries = new ArrayList<SVNLogEntry>(repo.log(new String[] { "" }, null, Long.valueOf(lastVersion), currentRev, true, true));
			// Diff all subsequent revisions
			int j = 1;
			for ( ; j < entries.size(); j++) {
				int changedThisCommit = 0;
				SVNLogEntry current = entries.get(j);
				if(null != reportary.getBeginDate()){
					if(current.getDate().before(reportary.getBeginDate()) ){
						continue;
					}
				}
				if(projectService.ifSvnRevisionExist(current.getRevision(), reportaryId)){
					continue;
				}
				SVNLogEntry prev = entries.get(j-1);
				ByteArrayOutputStream bo = new ByteArrayOutputStream();
				System.out.println("Revision " + current.getRevision()
						+ " committed by " + current.getAuthor() + "at" + current.getDate());

				diffClient.doDiff(svnUrl, SVNRevision.HEAD, SVNRevision
						.create(prev.getRevision()), SVNRevision.create(current
						.getRevision()), true, false, bo);

				BufferedReader br = new BufferedReader(new InputStreamReader(
						new ByteArrayInputStream(bo.toByteArray())));
				CommitReport cr = new CommitReport();
				SVNDiff svnDiff = new SVNDiff();
				int i = svnDiff.getAllCommitLines(br);
				cr.setCodeChange(String.valueOf(i));
				String comment = current.getMessage();
				if(null != comment){
					if(comment.indexOf("<") > 0){
						comment.replace("<", "&lt;");
					}
					else if(comment.indexOf(">") > 0){
						comment.replace(">", "&gt;");
					}
				}
				cr.setComment(comment);
				cr.setReportaryId(reportaryId);
				String author = current.getAuthor();
				if(author.indexOf("@hp.com") < 0){
					author += "@hp.com";
				}
				cr.setCommittedBy(author);
				cr.setCommitTime(current.getDate());
				cr.setRevision(Integer.valueOf(String.valueOf(current.getRevision())));
				projectService.saveCommitReport(cr);
				// save commit us, defect info if have
				if(null != comment && !"".equals(comment)){
					List<CommitusDefect> commitUSDEList = new ArrayList<CommitusDefect>();
					CommitusDefect commitusDefect;
					CommitusDefectId commitusDefectId;
					Pattern p = Pattern.compile("\\b(?i)(DE|QXCR|US)(\\d+)\\b");
					Matcher m = p.matcher(comment);
					Set<String> s;
					while(m.find()){
						// Avoid duplicate defect
						s = new HashSet<String>();
						if(!s.contains(m.group())){
							commitusDefectId = new CommitusDefectId();
							commitusDefect = new CommitusDefect();
							commitusDefectId.setCommitreportId(cr.getCommitreportId());
							commitusDefectId.setUsdefectNum(m.group().toUpperCase());
							commitusDefect.setId(commitusDefectId);
							commitUSDEList.add(commitusDefect);
							s.add(m.group());
						}
						s.clear();
					}
					projectService.saveCommitUSDEList(commitUSDEList);
				}
				List<CommitPath> cpList = new ArrayList<CommitPath>();
				Map<String, String> pathMap = current.getChangedPaths();
				Set<String> pathSet = pathMap.keySet();
				Iterator<String> iter = pathSet.iterator();
				
				Map<String, Integer> svnDiffMap = svnDiff.getMap();
				while(iter.hasNext()){
					String strPath = iter.next();
					CommitPath cp = new CommitPath();
					cp.setCommitreportId(cr.getCommitreportId());
					cp.setPath(strPath);
					for(String s :svnDiffMap.keySet()){
						if(strPath.contains(s)){
							cp.setCodeChange(String.valueOf(svnDiffMap.get(s)));
							break;
						}
					}
					cpList.add(cp);
				}
				projectService.saveCommitPath(cpList);
				bo.close();
				br.close();
			}
			out.print("noexist");
			out.flush();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (SVNException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	public String goRallyDetailView(){
		try {
			DefectInfo di = new DefectInfo();
			di.setComponent(component);
			di.setProjectName(projectnameinRally);
			mysubitteddefectList = testcaseService.getDefectInfoByBean(di);
			usinfoList = projectService.getUSInfoByProjectName(projectnameinRally);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String goQuixDetailView(){
		try {
			DefectInfo di = new DefectInfo();
			di.setComponent(component);
			di.setProjectName(projectnameinQuix);
			mysubitteddefectList = testcaseService.getDefectInfoByBean(di);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * This is for project setting
	 * Sync rally
	 */
	public void syncRally(){
		PrintWriter out = null;
		try {
			List<UserstoryVo> usvoList = projectService.getRallyUSInofoByPorject(projectnameinRally);
			this.saveUSInfo(usvoList);
			
			List<DefectVo> dvoList = projectService.getRallyDefectInfoByProjectModule(projectnameinRally, component);
			if(null != dvoList && dvoList.size() != 0){
				this.saveDefectInfo(dvoList);
			}
			out = this.getResponse().getWriter();
			out.print("Success");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
        }
	}
	
	/**
	 * This is for project setting
	 * Sync quix
	 */
	public void syncQuix(){
		PrintWriter out = null;
		try {
			List<DefectVo> dvo = projectService.getQuixDefectInfoByProjectModule(projectnameinQuix, component);
			if(null != dvo && dvo.size() != 0){
				this.saveDefectInfo(dvo);
			}
			out = this.getResponse().getWriter();
			out.print("Success");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
        }
	}
	
	private void saveUSInfo(List<UserstoryVo> usvoList){
		if(null != usvoList){
			try {
				List<UserstoryInfo> usInfoList = new ArrayList<UserstoryInfo>();
				Iterator<UserstoryVo> iter = usvoList.iterator();
				Map<String, String> objectNameMap = new HashMap<String, String>();
				while(iter.hasNext()){
					UserstoryVo usVo = iter.next();
					UserstoryInfo usInfo = new UserstoryInfo();
					usInfo.setTaskActuals(usVo.getTaskActuals());
					usInfo.setPlanEstimate(usVo.getPlanEstimate());
					usInfo.setObjectId(usVo.getObjectId());
					usInfo.setState(usVo.getState());
					usInfo.setTaskTodos(usVo.getTaskTodos());
					usInfo.setUsName(usVo.getUsName());
					usInfo.setUsNum(usVo.getUsNum());
					usInfo.setTaskEstimates(usVo.getTaskEstimates());
					usInfo.setProjectinRally(usVo.getProjectinRally());
					usInfo.setState(usVo.getState());
					usInfo.setParentNum(usVo.getParentNum());
					
					if(usVo.getAcceptedDate() != null && !"".equals(usVo.getAcceptedDate())){
						usInfo.setAcceptedDate(DateUtils.convertStringToDate(usVo.getAcceptedDate()));
					}
					if(null == usVo.getOwnerObj() || "".equals(usVo.getOwnerObj())){
						usInfo.setOwnerEmail("");
					}
					else if(objectNameMap.get(usVo.getOwnerObj()) != null && !"".equals(objectNameMap.get(usVo.getOwnerObj()))){
						usInfo.setOwnerEmail(objectNameMap.get(usVo.getOwnerObj()).replace("@hp.com", "@hpe.com"));
					}
					else{
						LoginUserVo ownerUser = testcaseService.getUserInfoFromRallyByObjectId(usVo.getOwnerObj());
						usInfo.setOwnerEmail(ownerUser.getEmail().replace("@hp.com", "@hpe.com"));
						objectNameMap.put(usVo.getOwnerObj(), ownerUser.getEmail().replace("@hp.com", "@hpe.com"));
					}
					usInfoList.add(usInfo);
				}
			
				mytitanService.saveMyUserStories(usInfoList);
			} catch (BaseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	private void saveDefectInfo(List<DefectVo> dvo){
		try {
			Iterator<DefectVo> iter = dvo.iterator();
			DefectInfo di = new DefectInfo();
			Map<String, String> objectNameMap = new HashMap<String, String>();
			while(iter.hasNext()){
				DefectVo defect = iter.next();
				di = new DefectInfo();
				di.setObjectId(defect.getObjectId());
				di.setDefectNum(defect.getDefectNum());
				di.setDefectName(defect.getDefectName());
				di.setPriority(defect.getPriority());
				di.setState(defect.getState());
				di.setSubmittedBy(defect.getSubmittedBy());
				if(defect.getDefectNum().indexOf("QXCR") >= 0){
					di.setSubmittEmail(defect.getSubmittedBy());
				}
				else{
					if(objectNameMap.get(defect.getSubmittedbyObjectid()) != null && !"".equals(objectNameMap.get(defect.getSubmittedbyObjectid()))){
						di.setSubmittEmail(objectNameMap.get(defect.getSubmittedbyObjectid()).replace("@hp.com", "@hpe.com"));
					}
					else{
						LoginUserVo submitUser = testcaseService.getUserInfoFromRallyByObjectId(defect.getSubmittedbyObjectid());
						di.setSubmittEmail(submitUser.getEmail().replace("@hp.com", "@hpe.com"));
						objectNameMap.put(defect.getSubmittedbyObjectid(), submitUser.getEmail().replace("@hp.com", "@hpe.com"));
					}
				}
				di.setDeveloper(defect.getDeveloper());
				if(defect.getDeveloper() != null){
					di.setDeveloperEmail(defect.getDeveloper().replace("@hp.com", "@hpe.com"));
				}
				di.setComponent(defect.getComponent());
				di.setProjectName(defect.getProjectName());
				di.setLastupdateDate(DateUtils.convertStringToDate(defect.getLastupdateDate()));
				di.setSubmitDate(DateUtils.convertStringToDate(defect.getSubmitDate()));
				di.setUserStory(defect.getUserStory());
				testcaseService.saveDefectInfo(di);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void clearSyncedRepositoryData(String reportaryId){
		try {
			projectService.clearSyncedRepositoryData(reportaryId);
		} catch (BaseException e) {
			e.printStackTrace();
		}
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

	public List<User> getManageList() {
		return manageList;
	}

	public void setManageList(List<User> manageList) {
		this.manageList = manageList;
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String[] getProjectIdAry() {
		return projectIdAry;
	}

	public void setProjectIdAry(String[] projectIdAry) {
		this.projectIdAry = projectIdAry;
	}

	public ProjectVo getProjectVo() {
		return projectVo;
	}

	public void setProjectVo(ProjectVo projectVo) {
		this.projectVo = projectVo;
	}

	public List<Group> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<Group> groupList) {
		this.groupList = groupList;
	}

	public GroupService getGroupService() {
		return groupService;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public SprintService getSprintService() {
		return sprintService;
	}

	public void setSprintService(SprintService sprintService) {
		this.sprintService = sprintService;
	}

	public Sprint getSprint() {
		return sprint;
	}

	public void setSprint(Sprint sprint) {
		this.sprint = sprint;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getJsonObj() {
		return jsonObj;
	}

	public void setJsonObj(String jsonObj) {
		this.jsonObj = jsonObj;
	}

	public List<ProjectModule> getModuleList() {
		return moduleList;
	}

	public void setModuleList(List<ProjectModule> moduleList) {
		this.moduleList = moduleList;
	}

	public List<DedicatedServer> getDedicatedServerList() {
		return dedicatedServerList;
	}

	public void setDedicatedServerList(List<DedicatedServer> dedicatedServerList) {
		this.dedicatedServerList = dedicatedServerList;
	}

	public String getNavigation() {
		return navigation;
	}

	public void setNavigation(String navigation) {
		this.navigation = navigation;
	}

	public String getDefaultProjectId() {
		return defaultProjectId;
	}

	public void setDefaultProjectId(String defaultProjectId) {
		this.defaultProjectId = defaultProjectId;
	}

	public List<Project> getOtherProjectList() {
		return otherProjectList;
	}

	public void setOtherProjectList(List<Project> otherProjectList) {
		this.otherProjectList = otherProjectList;
	}

	public TestjobVo getTestjobVo() {
		return testjobVo;
	}

	public void setTestjobVo(TestjobVo testjobVo) {
		this.testjobVo = testjobVo;
	}

	public List<TestjobVo> getTestjobVoList() {
		return testjobVoList;
	}

	public void setTestjobVoList(List<TestjobVo> testjobVoList) {
		this.testjobVoList = testjobVoList;
	}

	public TestjobService getTestjobService() {
		return testjobService;
	}

	public void setTestjobService(TestjobService testjobService) {
		this.testjobService = testjobService;
	}

	public List<TestjobVo> getTestjobVoPassList() {
		return testjobVoPassList;
	}

	public void setTestjobVoPassList(List<TestjobVo> testjobVoPassList) {
		this.testjobVoPassList = testjobVoPassList;
	}

	public List<TestjobVo> getTestjobVoFailList() {
		return testjobVoFailList;
	}

	public void setTestjobVoFailList(List<TestjobVo> testjobVoFailList) {
		this.testjobVoFailList = testjobVoFailList;
	}

	public ReportVo getReportVo() {
		return reportVo;
	}

	public void setReportVo(ReportVo reportVo) {
		this.reportVo = reportVo;
	}

	public List<Sprint> getSprintList() {
		return sprintList;
	}

	public void setSprintList(List<Sprint> sprintList) {
		this.sprintList = sprintList;
	}

	public List<ReportVo> getReportVoList() {
		return reportVoList;
	}

	public void setReportVoList(List<ReportVo> reportVoList) {
		this.reportVoList = reportVoList;
	}

	public List<RunJob> getRunjobList() {
		return runjobList;
	}

	public void setRunjobList(List<RunJob> runjobList) {
		this.runjobList = runjobList;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public List<ParameterData> getProjectParaList() {
		return projectParaList;
	}

	public void setProjectParaList(List<ParameterData> projectParaList) {
		this.projectParaList = projectParaList;
	}

	public Reportary getReportary() {
		return reportary;
	}

	public void setReportary(Reportary reportary) {
		this.reportary = reportary;
	}

	public ProjectRallyQuix getProjectRallyQuix() {
		return projectRallyQuix;
	}

	public void setProjectRallyQuix(ProjectRallyQuix projectRallyQuix) {
		this.projectRallyQuix = projectRallyQuix;
	}

	public String[] getRallyNameIdAry() {
		return rallyNameIdAry;
	}

	public void setRallyNameIdAry(String[] rallyNameIdAry) {
		this.rallyNameIdAry = rallyNameIdAry;
	}

	public String[] getQuixNameIdAry() {
		return quixNameIdAry;
	}

	public void setQuixNameIdAry(String[] quixNameIdAry) {
		this.quixNameIdAry = quixNameIdAry;
	}

	public List<Reportary> getReportaryList() {
		return reportaryList;
	}

	public void setReportaryList(List<Reportary> reportaryList) {
		this.reportaryList = reportaryList;
	}

	public List<ProjectRallyQuix> getProjectRallyList() {
		return projectRallyList;
	}

	public void setProjectRallyList(List<ProjectRallyQuix> projectRallyList) {
		this.projectRallyList = projectRallyList;
	}

	public List<ProjectRallyQuix> getProjectQuixList() {
		return projectQuixList;
	}

	public void setProjectQuixList(List<ProjectRallyQuix> projectQuixList) {
		this.projectQuixList = projectQuixList;
	}

	public String[] getReportaryIdAry() {
		return reportaryIdAry;
	}

	public void setReportaryIdAry(String[] reportaryIdAry) {
		this.reportaryIdAry = reportaryIdAry;
	}

	public String getReportaryId() {
		return reportaryId;
	}

	public void setReportaryId(String reportaryId) {
		this.reportaryId = reportaryId;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getProjectnameinRally() {
		return projectnameinRally;
	}

	public void setProjectnameinRally(String projectnameinRally) {
		this.projectnameinRally = projectnameinRally;
	}

	public String getProjectnameinQuix() {
		return projectnameinQuix;
	}

	public void setProjectnameinQuix(String projectnameinQuix) {
		this.projectnameinQuix = projectnameinQuix;
	}

	public List<DefectInfo> getMysubitteddefectList() {
		return mysubitteddefectList;
	}

	public void setMysubitteddefectList(List<DefectInfo> mysubitteddefectList) {
		this.mysubitteddefectList = mysubitteddefectList;
	}

	public TestcaseService getTestcaseService() {
		return testcaseService;
	}

	public void setTestcaseService(TestcaseService testcaseService) {
		this.testcaseService = testcaseService;
	}

	public String getPageTable1_length() {
		return pageTable1_length;
	}

	public void setPageTable1_length(String pageTable1Length) {
		pageTable1_length = pageTable1Length;
	}

	public String getPageTableSvn_length() {
		return pageTableSvn_length;
	}

	public void setPageTableSvn_length(String pageTableSvnLength) {
		pageTableSvn_length = pageTableSvnLength;
	}

	public String getPageTableRally_length() {
		return pageTableRally_length;
	}

	public void setPageTableRally_length(String pageTableRallyLength) {
		pageTableRally_length = pageTableRallyLength;
	}

	public String getPageTableQuix_length() {
		return pageTableQuix_length;
	}

	public void setPageTableQuix_length(String pageTableQuixLength) {
		pageTableQuix_length = pageTableQuixLength;
	}

	public String getSettingType() {
		return settingType;
	}

	public void setSettingType(String settingType) {
		this.settingType = settingType;
	}

	public MyTitanService getMytitanService() {
		return mytitanService;
	}

	public void setMytitanService(MyTitanService mytitanService) {
		this.mytitanService = mytitanService;
	}

	public List<UserstoryInfo> getUsinfoList() {
		return usinfoList;
	}

	public void setUsinfoList(List<UserstoryInfo> usinfoList) {
		this.usinfoList = usinfoList;
	}

}
