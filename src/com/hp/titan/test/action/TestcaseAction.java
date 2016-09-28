package com.hp.titan.test.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hp.app.action.DefaultBaseAction;
import com.hp.app.common.file.FileUpload;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.auth.model.Role;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.service.UserService;
import com.hp.titan.auth.vo.UserVo;
import com.hp.titan.common.constants.TitanContent;
import com.hp.titan.common.util.UserSessionUtil;
import com.hp.titan.project.model.DedicatedServer;
import com.hp.titan.project.model.Project;
import com.hp.titan.project.model.ProjectModule;
import com.hp.titan.project.service.ProjectService;
import com.hp.titan.test.model.CaseLog;
import com.hp.titan.test.model.Parameter;
import com.hp.titan.test.model.ParameterData;
import com.hp.titan.test.model.RunCaseServer;
import com.hp.titan.test.model.RunCaseServerId;
import com.hp.titan.test.model.TestJobCase;
import com.hp.titan.test.model.TestPlanCase;
import com.hp.titan.test.model.Testcase;
import com.hp.titan.test.model.Testjob;
import com.hp.titan.test.model.Testplan;
import com.hp.titan.test.service.TestcaseService;
import com.hp.titan.test.service.TestjobService;
import com.hp.titan.test.service.TestplanService;
import com.hp.titan.test.vo.CaseReportVo;
import com.hp.titan.test.vo.TestcaseVo;
import com.hp.titan.test.vo.TestjobVo;

public class TestcaseAction extends DefaultBaseAction {

	private static final long serialVersionUID = 1L;

	public TestcaseService testcaseService;
	public UserService userService;
	public ProjectService projectService;
	public TestplanService testplanService;
	public TestjobService testjobService;

	private Testcase testcase;
	private TestcaseVo testcaseVo;
	private List<User> userList;
	private UserVo userVo = new UserVo();
	private List<Project> projectList;
	private List<Testcase> testcaseList;
	private List<TestcaseVo> testcaseVoList;
	private List<ParameterData> parameterdataList;
	private List<ParameterData> serverdataList;
	private String caseName;
	private String jsonObj;
	private String caseId;
	private String[] testcaseIdAry;
	private List<Parameter> paramList;
	private List<CaseLog> logList;
	private List<ProjectModule> moduleList;
	private List<DedicatedServer> dedicatedServerList;
	private List<String> fileList;
	private String delFileName;
	private String isNew = "false";
	private int isAdmin;
	private String currentUserName;
	private String projectId;
	private List<CaseReportVo> caseRepVoList;
	private CaseReportVo  caseReportVo;

	// for upload file
	private String Filename;
	private String upload;
	private File file;
	private String fileFileName;
	private String fileContentType;
	private String navigation = " <div class=\"port_bar\"><a href=\"../project/projectList.do\" class=\"marfin_lef10\">" + UserSessionUtil.getDefaultProjectName() + "</a><span class=\"marfin_lef5\">></span><a href=\"../sprint/sprintList.do\" class=\"marfin_lef5\">Sprint</a><span class=\"marfin_lef5\">></span><a href=\"../test/testjobList.do\" class=\"marfin_lef5\">Test Job</a><span class=\"marfin_lef5\">></span><a href=\"../test/testplanList.do\" class=\"marfin_lef5\">Test Plan</a><span class=\"marfin_lef5\">></span><a href=\"../test/testcaseList.do\" class=\"marfin_lef5\">Test Case</a></div>";

	public String goTestcaseList() {
		isAdmin = 0;
		User currentUser = UserSessionUtil.getUser();
		currentUserName = currentUser.getUserCode();
		List<Role> roles = new ArrayList<Role>(currentUser.getUserRoles());
		if (roles.size() > 0) {
			String roleName = roles.get(0).getRoleName();	
			if("ADMIN".equals(roleName)) isAdmin = 1;
		}
		try {
			// testcaseList = testcaseService.getAllTestcase();
//			Set<Project> s = UserSessionUtil.getUser().getUserProjects();
//			if(null == s || s.size() == 0){
//				testcaseVoList = null;
//			}
			if(null == testcaseVo){
				testcaseVo = new TestcaseVo();
			}
			if(isAdmin == 1){
				testcaseVoList = testcaseService.findAllTestcase(testcaseVo, null);
				projectList = projectService.getAllProjectList();
			}
			else{
				String defaultProjectId = UserSessionUtil.getDefaultProject();
				if(null == defaultProjectId || "".equals(defaultProjectId)){
					testcaseVoList = null;
				}
				else{					
					testcaseVo.setProjectId(defaultProjectId);
					testcaseVoList = testcaseService.findAllTestcase(testcaseVo, null);
					projectList = new ArrayList<Project>();
					projectList.add(projectService.getProjectById(defaultProjectId));
				}
			}
			userList = userService.getAllUserByUserVo(userVo);
//			projectList = projectService.getAllProjectList(s);
//			if(null != s && s.size() != 0){
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

	public String goSearchTestcase() {
		isAdmin = 0;
		User currentUser = UserSessionUtil.getUser();
		currentUserName = currentUser.getUserCode();
		List<Role> roles = new ArrayList<Role>(currentUser.getUserRoles());
		if (roles.size() > 0) {
			String roleName = roles.get(0).getRoleName();	
			if("ADMIN".equals(roleName)) isAdmin = 1;
		}
		try {
			Set<Project> s = UserSessionUtil.getUser().getUserProjects();
			testcaseVoList = testcaseService.findAllTestcase(testcaseVo, null);
			userList = userService.getAllUserByUserVo(userVo);
//			projectList = projectService.getAllProjectList(s);
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
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String goTestcaseCreate() {
		testcase = new Testcase();
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
			
//			parameterdataList = testcaseService.getParameterData("");
			parameterdataList = new ArrayList<ParameterData>();
			//serverdataList = testcaseService.getParameterData("host");
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
			isNew = "true";
		} catch (BaseDaoException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String goTestcaseUpdate() {
		System.out.println(isNew);		
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
			String updateId = null;
			if ((this.testcaseIdAry != null)
					&& (this.testcaseIdAry.length == 1)) {
				updateId = this.testcaseIdAry[0];
			} else if( this.caseId != null ) {
				updateId = this.caseId;
			}
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
			
			if(updateId!=null){
				testcase = testcaseService.getTestcaseById(updateId);
				parameterdataList = testcaseService.getParameterData("");
				List<ParameterData>parameterdataList1 = projectService.findByProjectIdAndType(testcase.getProjectId(), "");
				parameterdataList.addAll(parameterdataList1);
				serverdataList = testcaseService.getParameterData("host");
				List<ParameterData>serverdataList1 = projectService.findByProjectIdAndType(testcase.getProjectId(), "host");
				serverdataList.addAll(serverdataList1);
				paramList = testcaseService.getParametersByCaseId(updateId);
				logList = testcaseService.getCaseLogs(updateId);
				moduleList = projectService.getProjectModuleList(testcase.getProjectId());
				dedicatedServerList = projectService.getDedicatedServerList(testcase.getProjectId());
				RunCaseServer runCaseServer = testcaseService.findCaseServerById(updateId);
				if(null != runCaseServer && null != runCaseServer.getId()){
					if(null != runCaseServer.getId().getDedicatedserverId() && !"".equals(runCaseServer.getId().getDedicatedserverId())){
						testcase.setDedicatedserverId(runCaseServer.getId().getDedicatedserverId());
					}
					else if(null != runCaseServer.getId().getServerData() && !"".equals(runCaseServer.getId().getServerData())){
						testcase.setRunonserverData(runCaseServer.getId().getServerData());
					}
				}
			}
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String goTestcaseView() {
		System.out.println(isNew);		
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
			userList = userService.getAllUserByUserVo(userVo);
			String updateId = null;
			if ((this.testcaseIdAry != null)
					&& (this.testcaseIdAry.length == 1)) {
				updateId = this.testcaseIdAry[0];
			} else if( this.caseId != null ) {
				updateId = this.caseId;
			}
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
			
			if(updateId!=null){
				testcase = testcaseService.getTestcaseById(updateId);
				parameterdataList = testcaseService.getParameterData("");
				List<ParameterData>parameterdataList1 = projectService.findByProjectIdAndType(testcase.getProjectId(), "");
				parameterdataList.addAll(parameterdataList1);
				serverdataList = testcaseService.getParameterData("host");
				List<ParameterData>serverdataList1 = projectService.findByProjectIdAndType(testcase.getProjectId(), "host");
				serverdataList.addAll(serverdataList1);
//				paramList = testcaseService.getParameters(updateId);
				paramList = testcaseService.getParametersByCaseId(updateId);
				logList = testcaseService.getCaseLogs(updateId);
				moduleList = projectService.getProjectModuleList(testcase.getProjectId());
				dedicatedServerList = projectService.getDedicatedServerList(testcase.getProjectId());
				RunCaseServer runCaseServer = testcaseService.findCaseServerById(updateId);
				if(null != runCaseServer && null != runCaseServer.getId()){
					if(null != runCaseServer.getId().getDedicatedserverId() && !"".equals(runCaseServer.getId().getDedicatedserverId())){
						testcase.setDedicatedserverId(runCaseServer.getId().getDedicatedserverId());
					}
					else if(null != runCaseServer.getId().getServerData() && !"".equals(runCaseServer.getId().getServerData())){
						testcase.setRunonserverData(runCaseServer.getId().getServerData());
					}
				}
			}
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public void getServerParameterData(){
		PrintWriter out = null;
		try {
			List<ParameterData>projectParaList = testcaseService.getParameterData("host");
			List<ParameterData>projectParaList1 = projectService.findByProjectIdAndType(projectId, "host");
			projectParaList.addAll(projectParaList1);
			out = this.getResponse().getWriter();
			this.getResponse().setHeader("Cache-Control", "no-cache");
			this.getResponse().setContentType("text/json;charset=UTF-8");
			this.getResponse().setCharacterEncoding("utf-8");
			JSONArray json = JSONArray.fromObject(projectParaList);
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

	public void getParameterData(){
		PrintWriter out = null;
		try {
			List<ParameterData>projectParaList = testcaseService.getParameterData("");
			List<ParameterData>projectParaList1 = projectService.findByProjectIdAndType(projectId, "");
			projectParaList.addAll(projectParaList1);
			out = this.getResponse().getWriter();
			this.getResponse().setHeader("Cache-Control", "no-cache");
			this.getResponse().setContentType("text/json;charset=UTF-8");
			this.getResponse().setCharacterEncoding("utf-8");
			JSONArray json = JSONArray.fromObject(projectParaList);
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
	
	public void checkTestcaseName() {
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			if (testcaseService.isExistTestcase(caseName)) {
				out.print("exist");
			} else {
				out.print("noexist");
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

	public String doTestcaseSave() {
		System.out.println(isNew);

		JSONObject json = JSONObject.fromObject(jsonObj);
		JSONArray paraArray = json.getJSONArray("paraArray");
		JSONArray logArray = json.getJSONArray("logArray");

		int currentUserId = UserSessionUtil.getUser().getUserId();
		Date currentDate = new Date();
		testcase.setCreateUserId(currentUserId);
		testcase.setCreateDate(currentDate);
		testcase.setUpdateUserId(currentUserId);
		testcase.setUpdateDate(currentDate);
		
		try {
			String newCaseCode = testcaseService
					.newTestcaseCodeByProject(testcase.getProjectId());
			testcase.setCaseCode(newCaseCode);

			String testcaseName = testcase.getCaseName();
			String projectName = projectService.getProjectById(testcase.getProjectId()).getProjectName();
			String location = "/"+projectName+"/"+testcaseName+"/";
			testcase.setLocation(location);		
			
			testcaseService.saveTestcase(testcase);
			this.caseId = testcase.getCaseId();

			List<Parameter> parameters = new ArrayList<Parameter>();
			for (int i = 0; i < paraArray.size(); i++) {
				JSONObject param = paraArray.getJSONObject(i);
				Parameter paramObj = new Parameter();
				paramObj.setCaseId(this.caseId);
				if (param.containsKey("paraType"))
					paramObj.setType(param.getString("paraType"));
				if (param.containsKey("paraName"))
					paramObj.setParaName(param.getString("paraName"));
				if (param.containsKey("paraValue"))
					paramObj.setParaValue(param.getString("paraValue"));
				if (param.containsKey("paraOrder"))
					paramObj.setParaOrder(new Integer(param
							.getString("paraOrder")));
				if (param.containsKey("description"))
					paramObj.setDescription(param.getString("description"));
				paramObj.setCreateUserId(currentUserId);
				paramObj.setCreateDate(currentDate);
				paramObj.setUpdateUserId(currentUserId);
				paramObj.setUpdateDate(currentDate);
				parameters.add(paramObj);
			}
			testcaseService.saveParameters(parameters);

			List<CaseLog> logs = new ArrayList<CaseLog>();
			for (int i = 0; i < logArray.size(); i++) {
				JSONObject log = logArray.getJSONObject(i);
				CaseLog logObj = new CaseLog();
				logObj.setCaseId(this.caseId);
				if (log.containsKey("logName"))
					logObj.setLogName(log.getString("logName"));
				if (log.containsKey("logLocation"))
					logObj.setLogLocation(log.getString("logLocation"));
				if (log.containsKey("remark"))
					logObj.setRemark(log.getString("remark"));
				logObj.setCreateUserId(currentUserId);
				logObj.setCreateDate(currentDate);
				logObj.setUpdateUserId(currentUserId);
				logObj.setUpdateDate(currentDate);
				logs.add(logObj);
			}
			testcaseService.saveCaseLogs(logs);
			if(!testcase.getType().equalsIgnoreCase("Manual")){
				RunCaseServerId runCaseServerId = new RunCaseServerId();
				RunCaseServer runCaseServer = new RunCaseServer();
				runCaseServer.setId(runCaseServerId);
				runCaseServerId.setCaseId(testcase.getCaseId());
				if(null != testcase.getDedicatedServerId() && !"".equals(testcase.getDedicatedServerId())){
					runCaseServerId.setDedicatedserverId(testcase.getDedicatedServerId());
				}
				else{
					runCaseServerId.setServerData(testcase.getRunonserverData());
				}
				testcaseService.saveCaseServer(runCaseServer);
			}

		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String doTestcaseUpdate() {
		System.out.println(isNew);		
		
		JSONObject json = JSONObject.fromObject(jsonObj);
		JSONArray paraArray = json.getJSONArray("paraArray");
		JSONArray logArray = json.getJSONArray("logArray");

		int currentUserId = UserSessionUtil.getUser().getUserId();
		Date currentDate = new Date();
		testcase.setCreateUserId(currentUserId);
		testcase.setCreateDate(currentDate);
		testcase.setUpdateUserId(currentUserId);
		testcase.setUpdateDate(currentDate);
		try {
			this.caseId = testcase.getCaseId();				
			
			String testcaseName = testcase.getCaseName();
			String projectName = projectService.getProjectById(testcase.getProjectId()).getProjectName();
			String newLocation = "/"+projectName+"/"+testcaseName+"/";
			testcase.setLocation(newLocation);		
			
			testcaseService.moveTestcaseFiles(caseId, newLocation);
			
			testcaseService.saveTestcase(testcase);
					
			if(paraArray.size()>0){
				//delete
				paramList = testcaseService.getParameters(this.caseId);
				if(null != paramList){
					for(Parameter delParam : paramList){
						boolean isDel = true;
						for (int i = 0; i <paraArray.size(); i++){
							JSONObject param = paraArray.getJSONObject(i);
							if(delParam.getParaId()
									.equals(param.getString("paraId"))){
								isDel = false;
								break;
							}
						}
						if(isDel){
							testcaseService.deleteParameter(delParam);
						}
					}
				}
				
				//update and add
				List<Parameter> parameters = new ArrayList<Parameter>();
				for (int i = 0; i < paraArray.size(); i++) {
					JSONObject param = paraArray.getJSONObject(i);
					Parameter paramObj = new Parameter();
					String paraId = param.getString("paraId");
					if(paraId!=null && !"".equals(paraId)){
						paramObj.setParaId(paraId);
					}
					paramObj.setCaseId(caseId);
					if (param.containsKey("paraType"))
						paramObj.setType(param.getString("paraType"));
					if (param.containsKey("paraName"))
						paramObj.setParaName(param.getString("paraName"));
					if (param.containsKey("paraValue"))
						paramObj.setParaValue(param.getString("paraValue"));
					if (param.containsKey("paraOrder"))
						paramObj.setParaOrder(new Integer(param
								.getString("paraOrder")));
					if (param.containsKey("description"))
						paramObj.setDescription(param.getString("description"));
					paramObj.setCreateUserId(currentUserId);
					paramObj.setCreateDate(currentDate);
					paramObj.setUpdateUserId(currentUserId);
					paramObj.setUpdateDate(currentDate);
					parameters.add(paramObj);
				}
				testcaseService.saveParameters(parameters);
			}

			if(logArray.size()>0){
				//delete
				logList = testcaseService.getCaseLogs(this.caseId);
				for(CaseLog delLog : logList){
					boolean isDel = true;
					for (int i = 0; i <logArray.size(); i++){
						JSONObject log = logArray.getJSONObject(i);
						if(delLog.getLogId().equals(log.getString("logId"))){
							isDel = false;
							break;
						}
					}
					if(isDel){
						testcaseService.deleteCaseLog(delLog);
					}
				}
				
				//update and add
				List<CaseLog> logs = new ArrayList<CaseLog>();
				for (int i = 0; i < logArray.size(); i++) {
					JSONObject log = logArray.getJSONObject(i);
					CaseLog logObj = new CaseLog();
					String logId = log.getString("logId");
					if(logId!=null && !"".equals(logId)){
						logObj.setLogId(logId);
					}
					logObj.setCaseId(caseId);
					if (log.containsKey("logName"))
						logObj.setLogName(log.getString("logName"));
					if (log.containsKey("logLocation"))
						logObj.setLogLocation(log.getString("logLocation"));
					if (log.containsKey("remark"))
						logObj.setRemark(log.getString("remark"));
					logObj.setCreateUserId(currentUserId);
					logObj.setCreateDate(currentDate);
					logObj.setUpdateUserId(currentUserId);
					logObj.setUpdateDate(currentDate);
					logs.add(logObj);
				}
				testcaseService.saveCaseLogs(logs);
				
			}
			if(!testcase.getType().equalsIgnoreCase("Manual")){
				RunCaseServerId runCaseServerId = new RunCaseServerId();
				RunCaseServer runCaseServer = new RunCaseServer();
				runCaseServer.setId(runCaseServerId);
				runCaseServerId.setCaseId(caseId);
				if(null != testcase.getDedicatedServerId() && !"".equals(testcase.getDedicatedServerId())){
					runCaseServerId.setDedicatedserverId(testcase.getDedicatedServerId());
				}
				else{
					runCaseServerId.setServerData(testcase.getRunonserverData());
				}
				testcaseService.deleteCaseServer(caseId);
				testcaseService.saveCaseServer(runCaseServer);
			}
			this.fileList = testcaseService.getFileListByTestcaseId(caseId);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String goTestcaseDisable() {

		try {
			if(this.caseId != null&&!this.caseId.equals("")){
				String updateId = this.caseId;
				testcase = testcaseService.getTestcaseById(updateId);
				testcase.setIsValid(1);
				testcaseService.saveTestcase(testcase);
//				testcaseService.deleteCaseServer(updateId);
//				paramList = testcaseService.getParameters(updateId);
//				logList = testcaseService.getCaseLogs(updateId);
//				for (Parameter pa : paramList) {
//					pa.setIsValid(1);
//				}
//				for (CaseLog log : logList) {
//					log.setIsValid(1);
//				}
//				testcaseService.saveParameters(paramList);
//				testcaseService.saveCaseLogs(logList);
			}
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String doTestcaseDelete(){
		
		try {
			testcaseService.deleteTestcase(caseId);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	public void doFileDelete(){
		try {
			testcaseService.deleteTestcaseFile(caseId, delFileName);
		} catch (BaseException e) {
			e.printStackTrace();
		}
	}
	
	public void doTestcaseBatchUpload() {
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			Testcase tc = testcaseService.getTestcaseById(caseId);
			String path = tc.getLocation();			
			FileUpload.upload(path, file, fileFileName);
			
			out.print(fileFileName);
			out.flush();		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	public void getTestCaseDetail(){
		PrintWriter out = null;
		StringBuffer s = new StringBuffer();
		try {
			out = this.getResponse().getWriter();
			Testcase tc = testcaseService.getTestcaseById(caseId);
			s.append("<tr><td align=\"left\" class=\"tablecontent_title\" width=\"35%\">Name</td><td align=\"left\" width=\"65%\">");
			s.append(tc.getCaseName());
			s.append("</td></tr>");
			
			s.append("<tr><td align=\"left\" class=\"tablecontent_title\" width=\"35%\">Description</td><td align=\"left\" width=\"65%\">");
			s.append(tc.getDescription());
			s.append("</td></tr>");
			
			s.append("<tr><td align=\"left\" class=\"tablecontent_title\" width=\"35%\">Command</td><td align=\"left\" width=\"65%\">");
			s.append(tc.getCommand());
			s.append("</td></tr>");
			out.print(s.toString());
			out.flush();		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	public void checkCaseHasBeenUsed(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			if(testcaseService.checkCaseHasBeenUsed(caseId)){
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
	
	public void remCaseHasBeenUsed(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			List<TestPlanCase> pcList = testcaseService.getPlanByCaseId(caseId);
			String planName="";
			if(null != pcList && pcList.size() != 0){
				planName=" test plan : ";
			    for(TestPlanCase tpc : pcList){
			    	Testplan tp = testplanService.getTestplanById(tpc.getId().getTestplanId());
			    	if(null != tp){
			    		planName += tp.getTestplanName();
			    		planName += ";";
			    	}
			    }
			}
			List<TestJobCase> jcList = testcaseService.getJobByCaseId(caseId);
			String jobName="";
			if(null != jcList && jcList.size() !=0){
				jobName=" test job : ";
				for(TestJobCase tjc : jcList){
					Testjob tj = testjobService.getTestjobById(tjc.getId().getTestjobId());
					jobName += tj.getTestjobName();
					jobName += ";";
				}
			}
			if(testcaseService.checkCaseHasBeenUsed(caseId)){
				out.print(planName + jobName);
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
	
	public String goTestcaseReport(){
		
		isAdmin = 0;
		User currentUser = UserSessionUtil.getUser();
		currentUserName = currentUser.getUserCode();
		List<Role> roles = new ArrayList<Role>(currentUser.getUserRoles());
		if (roles.size() > 0) {
			String roleName = roles.get(0).getRoleName();	
			if("ADMIN".equals(roleName)) isAdmin = 1;
		}
		try {
			if(null == testcaseVo){
				testcaseVo = new TestcaseVo();
				if (this.projectId != null || !"".equals(this.projectId)) {
					testcaseVo.setProjectId(this.projectId);
				}
			}
			projectList = projectService.getAllProjectList();
			if(isAdmin == 1){
				testcaseVoList = testcaseService.findAllTestcase(testcaseVo, null);
			}
			else{
				String defaultProjectId = UserSessionUtil.getDefaultProject();
				if(null == defaultProjectId || "".equals(defaultProjectId)){
					testcaseVoList = null;
				}
				else{	
					if (this.projectId != null || !"".equals(this.projectId)) {
						testcaseVo.setProjectId(this.projectId);
					}else 	testcaseVo.setProjectId(defaultProjectId);
					testcaseVoList = testcaseService.findAllTestcase(testcaseVo, null);
				}
			}
			caseRepVoList = new ArrayList<CaseReportVo>();
		    NumberFormat numberFormat1 = NumberFormat.getInstance();
		   	numberFormat1.setMinimumFractionDigits(0);
			if(testcaseVoList!=null &&testcaseVoList.size()!=0){
				for(TestcaseVo casevo : testcaseVoList){
					int total = 0;
				   	int pass = 0;
				   	int fail = 0;
				   	total = testcaseService.getCaseNumByResult(casevo.getCaseID(), TitanContent.CASE_REPORT_TOALL);
				   	fail = testcaseService.getCaseNumByResult(casevo.getCaseID(), TitanContent.CASE_REPORT_FAIL);
				   	pass = testcaseService.getCaseNumByResult(casevo.getCaseID(), TitanContent.CASE_REPORT_PASS);
				   	String cpasscent = numberFormat1.format((float)pass/(float)total*100);
				   	String cfailcent = numberFormat1.format((float)fail/(float)total*100);
					caseReportVo = new CaseReportVo();
					caseReportVo.setCaseCode(casevo.getCaseCode());
					caseReportVo.setCaseID(casevo.getCaseID());
					caseReportVo.setCaseName(casevo.getCaseName());
					caseReportVo.setCaseRunTotal(total);
					caseReportVo.setCaseRunFail(fail);
					caseReportVo.setCaseRunPass(pass);
					caseReportVo.setCasepassCent(cpasscent);
					caseReportVo.setCasefailCent(cfailcent);
					caseRepVoList.add(caseReportVo);
				}
			}
			userList = userService.getAllUserByUserVo(userVo);
			
		} catch (BaseException e) {
			e.printStackTrace();
		}
		navigation = "<div class=\"port_bar\"><a href=\"../project/projectList.do\" class=\"marfin_lef10\">" + UserSessionUtil.getDefaultProjectName() + "</a>->Test ccase report</div>";
		return SUCCESS;	
}

	public TestcaseService getTestcaseService() {
		return testcaseService;
	}

	public void setTestcaseService(TestcaseService testcaseService) {
		this.testcaseService = testcaseService;
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

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

	public String getJsonObj() {
		return jsonObj;
	}

	public void setJsonObj(String jsonObj) {
		this.jsonObj = jsonObj;
	}

	public TestcaseVo getTestcaseVo() {
		return testcaseVo;
	}

	public void setTestcaseVo(TestcaseVo testcaseVo) {
		this.testcaseVo = testcaseVo;
	}

	public UserVo getUserVo() {
		return userVo;
	}

	public void setUserVo(UserVo userVo) {
		this.userVo = userVo;
	}

	public List<TestcaseVo> getTestcaseVoList() {
		return testcaseVoList;
	}

	public void setTestcaseVoList(List<TestcaseVo> testcaseVoList) {
		this.testcaseVoList = testcaseVoList;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String[] getTestcaseIdAry() {
		return testcaseIdAry;
	}

	public void setTestcaseIdAry(String[] testcaseIdAry) {
		this.testcaseIdAry = testcaseIdAry;
	}

	public List<Parameter> getParamList() {
		return paramList;
	}

	public void setParamList(List<Parameter> paramList) {
		this.paramList = paramList;
	}

	public List<CaseLog> getLogList() {
		return logList;
	}

	public void setLogList(List<CaseLog> logList) {
		this.logList = logList;
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

	public List<String> getFileList() {
		return fileList;
	}

	public void setFileList(List<String> fileList) {
		this.fileList = fileList;
	}

	public String getFilename() {
		return Filename;
	}

	public void setFilename(String filename) {
		Filename = filename;
	}

	public String getUpload() {
		return upload;
	}

	public void setUpload(String upload) {
		this.upload = upload;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public String getDelFileName() {
		return delFileName;
	}

	public void setDelFileName(String delFileName) {
		this.delFileName = delFileName;
	}

	public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}

	public List<ParameterData> getParameterdataList() {
		return parameterdataList;
	}

	public void setParameterdataList(List<ParameterData> parameterdataList) {
		this.parameterdataList = parameterdataList;
	}

	public List<ParameterData> getServerdataList() {
		return serverdataList;
	}

	public void setServerdataList(List<ParameterData> serverdataList) {
		this.serverdataList = serverdataList;
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

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public List<CaseReportVo> getCaseRepVoList() {
		return caseRepVoList;
	}

	public void setCaseRepVoList(List<CaseReportVo> caseRepVoList) {
		this.caseRepVoList = caseRepVoList;
	}

	public CaseReportVo getCaseReportVo() {
		return caseReportVo;
	}

	public void setCaseReportVo(CaseReportVo caseReportVo) {
		this.caseReportVo = caseReportVo;
	}

	public TestplanService getTestplanService() {
		return testplanService;
	}

	public void setTestplanService(TestplanService testplanService) {
		this.testplanService = testplanService;
	}

	public TestjobService getTestjobService() {
		return testjobService;
	}

	public void setTestjobService(TestjobService testjobService) {
		this.testjobService = testjobService;
	}

}