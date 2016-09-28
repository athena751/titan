package com.hp.titan.sprint.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.hp.app.action.DefaultBaseAction;
import com.hp.app.common.util.DateUtils;
import com.hp.app.dao.IdGenerator;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.auth.model.Role;
import com.hp.titan.auth.model.User;
import com.hp.titan.common.constants.TitanContent;
import com.hp.titan.common.util.UserSessionUtil;
import com.hp.titan.common.vo.SprintCommonVo;
import com.hp.titan.project.model.Project;
import com.hp.titan.project.model.ProjectModule;
import com.hp.titan.project.service.ProjectService;
import com.hp.titan.project.vo.ReportVo;
import com.hp.titan.sprint.model.Sprint;
import com.hp.titan.sprint.model.SprintId;
import com.hp.titan.sprint.service.SprintService;
import com.hp.titan.sprint.vo.SprintVo;
import com.hp.titan.test.model.RunJob;
import com.hp.titan.test.service.TestjobService;
import com.hp.titan.test.vo.DefectInfoVo;
import com.hp.titan.test.vo.TestjobVo;

public class SprintAction  extends DefaultBaseAction {
	
	private List<Sprint> sprintList;
	private List<Sprint> spList;
	private List<ProjectModule> moduleList;
	private List<Project> projectList;
	private Sprint sprint;
	private SprintVo sprintVo;
	private List<SprintVo> sprintVoList;
	private String[] sprintIdAry;
	
	private SprintService sprintService;
	private ProjectService projectService;
	public TestjobService testjobService;
	
	private String sprintName;
	private String projectId;
	private String projectName = "";
	private String sprintId;
	private String showType;
	private String projectNameInRally;
	private String selectSprintid;
	private String navigation = " <div class=\"port_bar\"><a href=\"../project/projectList.do\" class=\"marfin_lef10\">" + UserSessionUtil.getDefaultProjectName() + "</a><span class=\"marfin_lef5\">></span><a href=\"../sprint/sprintList.do\" class=\"marfin_lef5\">Sprint</a></div>";
	
	private TestjobVo testjobVo;
	
	public List<TestjobVo> testjobVoList;
	private List<RunJob> runjobList;
	private List<TestjobVo> testjobVoPassList;
	private List<TestjobVo> testjobVoFailList;
	
	private ReportVo reportVo;
	private List<DefectInfoVo> defectinfoVoList;
	// Get all the Sprints
	public String doSearchSprints(){
		try {
			List<Role> roles = new ArrayList<Role>(UserSessionUtil.getUser().getUserRoles());
			if(this.projectId != null){
			  sprintList = sprintService.findSprintByProject(projectId);
			  if(sprintList != null && sprintList.size() != 0){
				  projectName = sprintList.get(0).getProject().getProjectName();
			  }
			}
			else if(TitanContent.ROLE_ADMIN.equals(roles.get(0).getRoleName())){
				sprintList = sprintService.getAllSprintList(null);
			}
			else{
				String defaultProjeceId = UserSessionUtil.getDefaultProject();
				if(null == defaultProjeceId || "".equals(defaultProjeceId)){
					sprintList = null;
				}
				else{
					sprintList =   sprintService.findSprintByProject(defaultProjeceId);
					if(sprintList != null && sprintList.size() != 0){
						projectName = sprintList.get(0).getProject().getProjectName();
					}
				}
			}
			if("" != projectName){
				projectName += "'s ";
			}
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	// Go to create sprint page
	public String goSprintCreate(){
		// Get all the manage user
		sprint = new Sprint();
		SprintId id = new SprintId();
		sprint.setId(id);
		int isAdmin = 0;
		User currentUser = UserSessionUtil.getUser();
		List<Role> roles = new ArrayList<Role>(currentUser.getUserRoles());
		if (roles.size() > 0) {
			String roleName = roles.get(0).getRoleName();	
			if("ADMIN".equals(roleName)) isAdmin = 1;
		}
			Set<Project> s = UserSessionUtil.getUser().getUserProjects();
			if(isAdmin == 1){
				try {
					projectList = projectService.getAllProjectList();
				} catch (BaseDaoException e) {
					e.printStackTrace();
				}
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
		return SUCCESS;
	}
	
	// Save sprint
	public String doSprintSave(){
		if(sprint.getId().getSprintId() != null && sprint.getId().getProjectId() != null){
			sprint.setCreateDate(Timestamp.valueOf(sprintVo.getStrCreateDate()));
			sprint.setLastUpdateUser(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
			sprint.setLastUpdate_Date(DateUtils.getCurrentDate());
		}
		else{
			sprint.setCreateUser(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
			sprint.setCreateDate(DateUtils.getCurrentDate());
			SprintId sprintId = new SprintId();
			sprintId.setSprintId(IdGenerator.createStringId().replaceAll("-", ""));
			sprintId.setProjectId(sprint.getId().getProjectId());
			sprint.setId(sprintId);
		}
		try {
			sprint.setStartDate(DateUtils.convertStringToDate(sprintVo.getStartDate()));
			sprint.setEndDate(DateUtils.convertStringToDate(sprintVo.getEndDate()));
			sprintService.saveSprint(sprint);
		} catch (BaseDaoException e) {
			e.printStackTrace();
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	// Go sprint edit
	public String goSprintUpdate(){
		 if ((this.sprintIdAry != null) && (this.sprintIdAry.length == 1)) {
		      String sprintId = this.sprintIdAry[0];
		      int isAdmin = 0;
		      try {
		    	  User currentUser = UserSessionUtil.getUser();
					List<Role> roles = new ArrayList<Role>(currentUser.getUserRoles());
					if (roles.size() > 0) {
						String roleName = roles.get(0).getRoleName();	
						if("ADMIN".equals(roleName)) isAdmin = 1;
					}
					String [] temp = sprintId.split("&");
					SprintId id = new SprintId();
					id.setSprintId(temp[0]);
					id.setProjectId(temp[1]);
		    	  sprint= sprintService.getSprintById(id);
		    	  reportVo = this.getReportVoFromProject(sprint.getId().getProjectId(), sprintId);
		    	  Set<Project> s = UserSessionUtil.getUser().getUserProjects();
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
		 return ERROR;
	}
	// Go sprint edit
	public String goSprintView(){
		 if ((this.sprintId != null) && !this.sprintId.equals("") && this.projectId != null && !this.projectId.equals("")) {
		      String sprintId = this.sprintId;
		      String projectId = this.projectId;
		      int isAdmin = 0;
		      try {
		    	  User currentUser = UserSessionUtil.getUser();
					List<Role> roles = new ArrayList<Role>(currentUser.getUserRoles());
					if (roles.size() > 0) {
						String roleName = roles.get(0).getRoleName();	
						if("ADMIN".equals(roleName)) isAdmin = 1;
					}
					SprintId id = new SprintId();
					id.setSprintId(sprintId);
					id.setProjectId(projectId);
		    	  sprint= sprintService.getSprintById(id);
		    	  reportVo = this.getReportVoFromProject(sprint.getId().getProjectId(), sprintId);
		    	  defectinfoVoList = sprintService.getDefectInfoBySprintId(sprintId);
		    	  Set<Project> s = UserSessionUtil.getUser().getUserProjects();
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
		 return ERROR;
	}
	
	public String goSprintReport(){
		      try {
		    	  User currentUser = UserSessionUtil.getUser();		
				  projectList = projectService.getProjectByGroup(currentUser.getGroupId());
		    	  if ((this.sprintId != null) && !this.sprintId.equals("")) {
		    		  selectSprintid = sprintId;
				      sprintList = sprintService.findSprintByProject(projectId);
				      SprintId id = new SprintId();
					  id.setSprintId(sprintId);
					  id.setProjectId(projectId);
			    	  sprint= sprintService.getSprintById(id);
			    	  reportVo = this.getReportVoFromProject(sprint.getId().getProjectId(), sprintId);
		    	  }else if (projectList!=null&& projectList.size()!=0){
		    		  projectId = projectList.get(0).getProjectId();
		    		  sprintList = sprintService.findSprintByProject(projectId);
		    		  if(sprintList!=null && sprintList.size()!=0){
		    			  sprintId = sprintList.get(0).getId().getSprintId();  
		    			  selectSprintid = sprintId;
		    			  SprintId id = new SprintId();
						  id.setSprintId(sprintId);
						  id.setProjectId(projectId);
				    	  sprint= sprintService.getSprintById(id);
				    	  reportVo = this.getReportVoFromProject(sprint.getId().getProjectId(), sprintId);
		    			  }
		    	  }
		    	 
			} catch (BaseDaoException e) {
				e.printStackTrace();
			}
			navigation = "<div class=\"port_bar\"><a href=\"../project/projectList.do\" class=\"marfin_lef10\">" + UserSessionUtil.getDefaultProjectName() + "</a>->Sprint report</div>";
		    return SUCCESS;
		
	}
	
	public void getChartXml(){
		List<ReportVo> list= new ArrayList<ReportVo>();
		PrintWriter out = null;
		try {
			SprintId id = new SprintId();
			id.setSprintId(sprintId);
			id.setProjectId(projectId);
			sprint= sprintService.getSprintById(id);
		} catch (BaseDaoException e1) {
			e1.printStackTrace();
		}
		reportVo = this.getReportVoFromProject(sprint.getId().getProjectId(), sprintId);
//		String strXml = "";
//		if("0".equals(showType)){
//			strXml = this.getChartXmlForJob();
//		}
//		else{
//			strXml = this.getChartXmlForCase();
//		}
		ReportVo re=new ReportVo();
		re.setTestcasePass(reportVo.getTestcasePass());
		re.setTestcaseFail(reportVo.getTestcaseFail());
		re.setTestcaseNotrun(reportVo.getTestcaseNotrun());
		re.setTestcaseRuning(reportVo.getTestcaseRuning());
		re.setTestcaseSkip(reportVo.getTestcaseSkip());
		re.setTestjobPass(reportVo.getTestjobPass());
		re.setTestjobFail(reportVo.getTestjobFail());
		re.setTestjobPending(reportVo.getTestjobPending());
		re.setTestjobRunning(reportVo.getTestjobRunning());
		re.setTestjobActive(reportVo.getTestjobActive());
		re.setTestjobAbort(reportVo.getTestjobAbort());
		list.add(re);
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
		rootElement.addAttribute("caption", "Jobs in Project");
		rootElement.addAttribute("showValues", "0");
		rootElement.addAttribute("xAxisName", "Sprints");
		rootElement.addAttribute("yAxisName", "Test jobs");
		rootElement.addAttribute("paletteColors", "00FF00, FF3300, CCCCFF, 33CCCC, 99FF99, 88FF88");
		
		// set element
		Element passElement = rootElement.addElement("set");
		passElement.addAttribute("label", "pass");
		passElement.addAttribute("value", String.valueOf(reportVo.getTestjobPass()));
		passElement.addAttribute("color", "00FF00");
		
		Element failElement = rootElement.addElement("set");
		failElement.addAttribute("label","fail");
		failElement.addAttribute("value",String.valueOf(reportVo.getTestjobFail()));
		failElement.addAttribute("color", "FF3300");
		
		Element activeElement = rootElement.addElement("set");
		activeElement.addAttribute("label","active");
		activeElement.addAttribute("value",String.valueOf(reportVo.getTestjobActive()));
		activeElement.addAttribute("color", "CCCCFF");
		
		Element pendingElement = rootElement.addElement("set");
		pendingElement.addAttribute("label","pending");
		pendingElement.addAttribute("value",String.valueOf(reportVo.getTestjobPending()));
		pendingElement.addAttribute("color", "33CCCC");
		
		Element runningElement = rootElement.addElement("set");
		runningElement.addAttribute("label","running");
		runningElement.addAttribute("value",String.valueOf(reportVo.getTestjobRunning()));
		runningElement.addAttribute("color", "99FF99");
		
		Element abortElement = rootElement.addElement("set");
		abortElement.addAttribute("label","abort");
		abortElement.addAttribute("value",String.valueOf(reportVo.getTestjobRunning()));
		abortElement.addAttribute("color", "88FF88");
		
		return document.asXML();
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
	
	public ReportVo getReportVoFromProject(String projectId, String sprintId){
		
		 try{
		  testjobVo = new TestjobVo();
	   	  testjobVo.setProjectId(projectId);
	   	  if(sprintId != null) testjobVo.setSprintId(sprintId);
	   	  testjobVoList = testjobService.findAllTestjob(testjobVo, null);
	   	  testjobVo.setTestjobState(TitanContent.TEST_JOB_STATUS_SUCCESS);
//	  	  testjobVoPassList = testjobService.findAllTestjob(testjobVo, null);
	   	  int jobPassNum = testjobService.findAllTestjob(testjobVo, null).size();
	   	  testjobVo.setTestjobState(TitanContent.TEST_JOB_STATUS_FAIL);
//	   	  testjobVoFailList = testjobService.findAllTestjob(testjobVo, null);
	   	  int jobFailNum = testjobService.findAllTestjob(testjobVo, null).size();
	   	  testjobVo.setTestjobState(TitanContent.TEST_JOB_STATUS_ACTIVE);
	   	  int jobActiveNum = testjobService.findAllTestjob(testjobVo, null).size();
	   	  testjobVo.setTestjobState(TitanContent.TEST_JOB_STATUS_RUNNING);
	      int jobRunNum = testjobService.findAllTestjob(testjobVo, null).size();
	      testjobVo.setTestjobState(TitanContent.TEST_JOB_STATUS_PENDING);
	      int jobPendNum = testjobService.findAllTestjob(testjobVo, null).size();
	      testjobVo.setTestjobState(TitanContent.TEST_JOB_STATUS_ABORT);
	      int jobAbortNum = testjobService.findAllTestjob(testjobVo, null).size();
	      
	   	  
	   	  
	   	  reportVo = new ReportVo();
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
	
	// Do sprint delete
	public String doSprintRemove(){
		if ((this.sprintIdAry != null) && (this.sprintIdAry.length != 0)) {
			try {
				String [] temp = this.sprintIdAry[0].split("&");
				SprintId id = new SprintId();
				id.setSprintId(temp[0]);
				id.setProjectId(temp[1]);
				sprintService.deleteSprintList(id);
			} catch (BaseDaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	// Check if sprint name existed
	public String checkSprintName() throws IOException{
		PrintWriter out = this.getResponse().getWriter();
		  try {
			if(sprintService.isExistSprint(sprintName)){
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
	
	public void getSprintsByPorject(){
		PrintWriter out = null;
		if (this.projectId != null){
		  try{	
			moduleList = projectService.getProjectModuleList(projectId);
			sprintList = sprintService.findSprintByProject(projectId);
			out = this.getResponse().getWriter();
			this.getResponse().setHeader("Cache-Control", "no-cache");
			this.getResponse().setContentType("text/json;charset=UTF-8");
			this.getResponse().setCharacterEncoding("utf-8");
			
			sprintVoList = new ArrayList<SprintVo>();
			
            if(sprintList != null && sprintList.size() != 0){
			  for(Sprint sp : sprintList){
				sprintVo = new SprintVo();
				sprintVo.setSprintId(sp.getId().getSprintId());
				sprintVo.setSprintName(sp.getSprintName());	
				sprintVoList.add(sprintVo);				
			  }
            }	
			JSONArray json = JSONArray.fromObject(sprintVoList);
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
	
	// Check if the project has sprint
	public String checkSprintHasJob() throws IOException{
		PrintWriter out = this.getResponse().getWriter();
		List<TestjobVo> l = null;
		TestjobVo testjobVo = new TestjobVo();
		testjobVo.setSprintId(sprintId);
		try {
			l = testjobService.findAllTestjob(testjobVo, null);
		} catch (BaseException e) {
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
	
	public void doSynchronProject(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			List<SprintCommonVo> sprintCommonVoList = projectService.getSprintByProjectName(projectNameInRally);
			if(null != sprintCommonVoList && sprintCommonVoList.size() != 0){
				sprintList = new ArrayList<Sprint>();
				Iterator<SprintCommonVo> iter = sprintCommonVoList.iterator();
				while(iter.hasNext()){
					SprintCommonVo scVo = iter.next();
					sprint = new Sprint();
					SprintId id = new SprintId();
					sprint.setEndDate(DateUtils.convertStringToDate(scVo.getEndDate()));
					id.setProjectId(projectId);
					id.setSprintId(scVo.getObjectId());
					sprint.setId(id);
					sprint.setStartDate(DateUtils.convertStringToDate(scVo.getStartDate()));
					sprint.setSprintName(scVo.getSprintName());
					sprint.setCreateUser(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
					sprint.setCreateDate(DateUtils.getCurrentDate());
					sprintList.add(sprint);
				}
				sprintService.saveSprints(sprintList);
				out.print("success");
			}
			else{
				out.print("notFound");
			}
			out.flush();
		} catch (IOException ie) {
			ie.printStackTrace();
		} catch (BaseException be) {
			be.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	public List<Sprint> getSprintList() {
		return sprintList;
	}

	public void setSprintList(List<Sprint> sprintList) {
		this.sprintList = sprintList;
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

	public String[] getSprintIdAry() {
		return sprintIdAry;
	}

	public void setSprintIdAry(String[] sprintIdAry) {
		this.sprintIdAry = sprintIdAry;
	}

	public SprintVo getSprintVo() {
		return sprintVo;
	}

	public void setSprintVo(SprintVo sprintVo) {
		this.sprintVo = sprintVo;
	}

	public String getSprintName() {
		return sprintName;
	}

	public void setSprintName(String sprintName) {
		this.sprintName = sprintName;
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

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public List<ProjectModule> getModuleList() {
		return moduleList;
	}

	public void setModuleList(List<ProjectModule> moduleList) {
		this.moduleList = moduleList;
	}

	public List<Sprint> getSpList() {
		return spList;
	}

	public void setSpList(List<Sprint> spList) {
		this.spList = spList;
	}

	public List<SprintVo> getSprintVoList() {
		return sprintVoList;
	}

	public void setSprintVoList(List<SprintVo> sprintVoList) {
		this.sprintVoList = sprintVoList;
	}

	public String getNavigation() {
		return navigation;
	}

	public void setNavigation(String navigation) {
		this.navigation = navigation;
	}

	public String getSprintId() {
		return sprintId;
	}

	public void setSprintId(String sprintId) {
		this.sprintId = sprintId;
	}

	public TestjobService getTestjobService() {
		return testjobService;
	}

	public void setTestjobService(TestjobService testjobService) {
		this.testjobService = testjobService;
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

	public List<DefectInfoVo> getDefectinfoVoList() {
		return defectinfoVoList;
	}

	public void setDefectinfoVoList(List<DefectInfoVo> defectinfoVoList) {
		this.defectinfoVoList = defectinfoVoList;
	}

	public String getProjectNameInRally() {
		return projectNameInRally;
	}

	public void setProjectNameInRally(String projectNameInRally) {
		this.projectNameInRally = projectNameInRally;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getSelectSprintid() {
		return selectSprintid;
	}

	public void setSelectSprintid(String selectSprintid) {
		this.selectSprintid = selectSprintid;
	}

}
