package com.hp.titan.mytitan.action;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hp.app.action.DefaultBaseAction;
import com.hp.app.common.util.DateUtils;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.service.UserService;
import com.hp.titan.common.constants.TitanContent;
import com.hp.titan.common.util.UserSessionUtil;
import com.hp.titan.common.vo.DefectVo;
import com.hp.titan.common.vo.LoginUserVo;
import com.hp.titan.common.vo.TaskVo;
import com.hp.titan.common.vo.UserstoryVo;
import com.hp.titan.mytitan.model.CommitPath;
import com.hp.titan.mytitan.model.Consumption;
import com.hp.titan.mytitan.model.ExpAccount;
import com.hp.titan.mytitan.model.ExpLog;
import com.hp.titan.mytitan.model.Goods;
import com.hp.titan.mytitan.model.InBox;
import com.hp.titan.mytitan.model.Point;
import com.hp.titan.mytitan.model.PointHist;
import com.hp.titan.mytitan.model.UserstoryInfo;
import com.hp.titan.mytitan.model.WeeklyReport;
import com.hp.titan.mytitan.model.WeeklyReportTask;
import com.hp.titan.mytitan.model.Winner;
import com.hp.titan.mytitan.service.MyTitanService;
import com.hp.titan.mytitan.vo.MyCommitVo;
import com.hp.titan.notify.EmailManageAction;
import com.hp.titan.project.model.Project;
import com.hp.titan.project.model.ProjectModule;
import com.hp.titan.project.service.ProjectService;
import com.hp.titan.report.vo.StrongFieldReportVo;
import com.hp.titan.test.model.DefectInfo;
import com.hp.titan.test.service.TestcaseService;

@SuppressWarnings("serial")
public class MytitanAction  extends DefaultBaseAction {
	
	private String defaultProjectId;
	private String projectId;
	private String rallyUrl = TitanContent.RALLY_URL;
	private String startDate;
	private String endDate;
	private String sendTo;
	private String reportContent;
	private String jsonObj;
	private String reportId;
	private String reportaryName;
	private String usNum;
	private String reportaryId;
	private String commitId;
	private String selectedsprintId;
	private String sprintId;
	private String navigation = " <div class=\"port_bar\"><a href=\"../project/projectList.do\" class=\"marfin_lef10\">" + UserSessionUtil.getDefaultProjectName() + "</a>";
	private String totalPrice;
	private String pageTable1_length;
	private String pageTable2_length;
	private String consumeId;
	private String rcPoint;
	private String isManager;
	private String selectedNum;
	private String ___t;
	private String selectedNumbers;
	private String tensNum;
	private String digitsNum;
	private String winnerName;
	private String winnerId;
	private String inboxCount;
	private String messageId;
	
	private String[] awardPoints;
	private String[] awardUsers;
	
	private Integer mypoint;
	private Integer myAwardPoint;
	
	private Project project;
	private StrongFieldReportVo strongFieldReportVo;
	private TestcaseService testcaseService;
	private MyTitanService mytitanService;
	private UserService userService;
	private ProjectService projectService;
	private List<Project> projectList;
	private List<DefectInfo> mysubitteddefectList;
	private List<DefectInfo> myowneddefectList;
	private List<WeeklyReport> weeklyreportList;
	private List<UserstoryInfo> usInfoList;
	private List<MyCommitVo> commitList;
	private List<ProjectModule> moduleList;
	private List<PointHist> phList;
	private List<PointHist> aphList;
	private List<User> userList; 
	private List<Goods> goodsList;
	private List<Consumption> consumeList;
	private List<Consumption> ccList;
	private List<InBox> inboxList;
	private List<Winner> winnerList;
	
	private JSONArray json;

	/**
	 * This is for My titan -> My project
	 * Show all the login user's projects 
	 * @return
	 */
	public String doSearchMyProjects(){
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
		defaultProjectId = UserSessionUtil.getDefaultProject();
		navigation += "->My project</div>";
		return SUCCESS;
	}
	
	/**
	 * This is for My titan -> My project
	 * Change the defect project, only defect project's test job plan case can be shown
	 * @return
	 */
	public String goDefaultProjectChange(){
		defaultProjectId = projectId;
		try {
			userService.setDefaultProject(UserSessionUtil.getUser().getUserId(), projectId);
		} catch (BaseDaoException e) {
			e.printStackTrace();
		}
		UserSessionUtil.setDefaultProject(defaultProjectId);
		return SUCCESS;
	}
	
	/**
	 * This is for My titan -> My defect
	 * List all the login user's defect
	 * @return
	 */
	public String mydefectList(){
		String loginUserEmail = UserSessionUtil.getUser().getMail();
		try {
			DefectInfo di = new DefectInfo();
			di.setSubmittEmail(loginUserEmail);
			mysubitteddefectList = testcaseService.getDefectInfoByBean(di);
			
			di = new DefectInfo();
			di.setDeveloperEmail(loginUserEmail);
			myowneddefectList = testcaseService.getDefectInfoByBean(di);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		navigation += "->My defects</div>";
		return SUCCESS;
	}
	
	/**
	 * This is for My titan -> My defect
	 * Refresh all the defects
	 */
	public void refreshDefect() throws IOException{
		PrintWriter out = null;
		String loginUserEmail = UserSessionUtil.getUser().getMail();
		try {
			List<DefectVo> resultList = mytitanService.getRallyDefectInfoByEmail(loginUserEmail);
			List<DefectVo> resultList1 = mytitanService.getRallyDefectInfoByDeveloper(loginUserEmail);
			List<DefectVo> resultList2 = mytitanService.getQuixDefectInfoByEmail(loginUserEmail);
			List<DefectVo> resultList3 = mytitanService.getQuixDefectInfoByDeveloper(loginUserEmail);
			resultList.addAll(resultList1);
			resultList.addAll(resultList2);
			resultList.addAll(resultList3);
			this.saveDefectInfo(resultList);
			
			out = this.getResponse().getWriter();
			out.print("Success");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			out.close();
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
						di.setSubmittEmail(objectNameMap.get(defect.getSubmittedbyObjectid()));
					}
					else{
						LoginUserVo submitUser = testcaseService.getUserInfoFromRallyByObjectId(defect.getSubmittedbyObjectid());
						di.setSubmittEmail(submitUser.getEmail());
						objectNameMap.put(defect.getSubmittedbyObjectid(), submitUser.getEmail());
					}
				}
				di.setDeveloper(defect.getDeveloper());
				di.setDeveloperEmail(defect.getDeveloper());
				di.setComponent(defect.getComponent());
				di.setProjectName(defect.getProjectName());
				di.setLastupdateDate(DateUtils.convertStringToDate(defect.getLastupdateDate()));
				di.setSubmitDate(DateUtils.convertStringToDate(defect.getSubmitDate()));
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
	
	/**
	 * This is for My titan -> My workload
	 * @return
	 */
	public String myworkloadList(){
		try {
			weeklyreportList = mytitanService.getWeeklyReportByUserId(UserSessionUtil.getUser().getUserId());
		} catch (BaseException e) {
			e.printStackTrace();
		}
		navigation += "->My workload</div>";
		return SUCCESS;
	}
	
	/**
	 * This is for My titan -> My strong field
	 * @return
	 */
	public String myStrongField(){
		int isAdmin = 0;
		try{
			User currentUser = UserSessionUtil.getUser();
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
			if(null != startDate && !"".equals(startDate) && null != endDate && !"".equals(endDate)){
				selectedsprintId = sprintId;
//				projectId = "8ae584a8480db70701480e73a34202a3";
				strongFieldReportVo = mytitanService.getStrongFieldReportByUserMail(UserSessionUtil.getUser().getMail(),projectId, startDate, endDate);
			}
		}catch (BaseDaoException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		navigation += "->My strong field</div>";
		return SUCCESS;
		
	}
	
	/**
	 * This is for My titan -> My workload
	 * @return
	 */
	public String myworkloadEdit(){
		return SUCCESS;
	}
	
	/**
	 * This is for My titan -> My workload
	 * Get task info from rally
	 * @return
	 */
	public String getTaskInfo(){
		PrintWriter out = null;
		String loginUserEmail = UserSessionUtil.getUser().getMail();
		try {
			List<TaskVo> taksInfoList = mytitanService.getTaskInfoByUserEmail(loginUserEmail, startDate+"T00:00:00.000Z", endDate+"T23:59:59.000Z");
			StringBuffer sb = new StringBuffer();
			sb.append("Rally Tasks: <table border=\"1\" id=\"tblRallyTask\" width=\"100%\"><tr><th width=\"40%\">Task Name</th><th width=\"10%\">State</th><th width=\"10%\">Actuals</th><th width=\"10%\">ToDo</th><th width=\"20%\">LastUpdateDate</th><th type=\"hidden\">Parent</th></tr>");
			if(null != taksInfoList && taksInfoList.size() != 0){
				Iterator<TaskVo> iter = taksInfoList.iterator();
				while(iter.hasNext()){
					TaskVo taskObj = iter.next();
					
					sb.append("<tr>");
					
					sb.append("<td align=\"left\">");
					sb.append(taskObj.getTaskName());
					sb.append("</td>");
					
					sb.append("<td align=\"center\">");
					if(null != taskObj.getState()){
						sb.append(taskObj.getState());
					}
					sb.append("</td>");
					
					sb.append("<td align=\"center\">");
					if(null != taskObj.getActual()){
						sb.append(taskObj.getActual());
					}
					sb.append("</td>");
					
					sb.append("<td align=\"center\">");
					if(null != taskObj.getTodo()){
						sb.append(taskObj.getTodo());
					}
					sb.append("</td>");
					
					sb.append("<td align=\"center\">");
					if(null != taskObj.getLastupdateDate()){
						sb.append(taskObj.getLastupdateDate());
					}
					sb.append("</td>");
					
					sb.append("<td align=\"center\" type=\"hidden\">");
					if(null != taskObj.getParent()){
						sb.append(taskObj.getParent());
					}
					sb.append("</td>");
					
					sb.append("</tr>");
					
				}
			}
			sb.append("</table>");
			List<String> prq = projectService.getProjectRallyQuixByUserId(UserSessionUtil.getUser().getUserId());
			String projects="";
			if (null != prq && prq.size() != 0){
				for(String projectName : prq){
					projects+="'" + projectName +"', ";
				}
				projects = projects.substring(0,projects.length()-2);
				List<DefectVo> QuixInfoList = projectService.getQuixForReport(loginUserEmail, startDate +" 00:00:00" , endDate +" 23:59:59", projects);
	            sb.append( "Quix List: <table border=\"1\" id=\"tblQuixList\" width=\"100%\"><tr><th width=\"10%\">Quix ID</th><th width=\"30%\">Quix Name</th><th width=\"10%\">State</th><th width=\"10%\">Developer</th><th width=\"10%\">Submitted By</th><th width=\"20%\">LastUpdateDate</th><th type=\"hidden\">Project</th></tr>");
	             if (null != QuixInfoList && QuixInfoList.size() != 0){
	                  Iterator<DefectVo> iter1 = QuixInfoList.iterator();
	                   while (iter1.hasNext()){
	                        DefectVo QuixObj = iter1.next();
	                        
	                        sb.append( "<tr>" );
	                        
	                        sb.append( "<td align=\"left\">" );
	                        sb.append(QuixObj.getDefectNum());
	                        sb.append( "</td>" );
	                        sb.append( "<td align=\"left\">" );
	                        sb.append(QuixObj.getDefectName());
	                        sb.append( "</td>" );
	                        
	                        sb.append( "<td align=\"center\">" );
	                         if (null != QuixObj.getState()){
	                              sb.append(QuixObj.getState());
	                        }
	                        sb.append( "</td>" );
	                        
	                        sb.append( "<td align=\"center\">" );
	                         if (null != QuixObj.getDeveloper()){
	                              sb.append(QuixObj.getDeveloper());
	                        }
	      
	                        sb.append( "</td>" );
	                        
	                        sb.append( "<td align=\"center\">" );
	                         if (null != QuixObj.getSubmittedBy()){
	                              sb.append(QuixObj.getSubmittedBy());
	                        }
	                        sb.append( "</td>" );
	                        
	                        sb.append( "<td align=\"center\">" );
	                         if (null != QuixObj.getLastupdateDate()){
	                              sb.append(QuixObj.getLastupdateDate());
	                        }
	                        sb.append( "</td>" );                             
	                        sb.append( "<td align=\"center\">" );
	                         if (null != QuixObj.getProjectName()){
	                              sb.append(QuixObj.getProjectName());                              
	                        }                       
	                        sb.append( "</td>" );
	                        
	                  }
	            }
	            sb.append( "</table>" );
			}
            sb.append( "<table width=\"100%\" border=\"0\"><tr><td align=\"left\"><input type=\"button\" onclick=\"editCustomTask()\" value=\"Edit your task\"></input></td></tr></table>" );
            out = this .getResponse().getWriter();
            out.print(sb.toString());
            out.flush();

		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
        }
		return SUCCESS;
	}
	
	
	/**
	 * This is for My titan -> My workload
	 * To generate weekly report
	 */
	@SuppressWarnings("unchecked")
	public void generateReport(){
		PrintWriter out = null;
		
		StringBuffer s = new StringBuffer();
		JSONObject json = JSONObject.fromObject(jsonObj);
		JSONArray rallyTaskArray = json.getJSONArray("rallyTaskArray");
		JSONArray quixListArray = json.getJSONArray("quixListArray");
		JSONArray customTaskArray = json.getJSONArray("customTaskArray");
		JSONArray issueMiscPlanArray = json.getJSONArray("issueMiscPlanArray");
		s.append("<div id=\"roll\"><h3>Rally tasks:</h3><br>");
		if(rallyTaskArray.size() > 0){
			s.append("<table cellspacing=\"0px\" border=\"1\"><tr align=\"center\"><th width=\"7%\">No.</th><th width=\"65%\">Name</th><th width=\"10%\">State</th><th width=\"9%\">Actual</th><th width=\"9%\">Todo</th><th width=\"9%\">Parent</th></tr>");
			Iterator<JSONObject> iter = rallyTaskArray.iterator();
			int i = 1;
			while(iter.hasNext()){
				JSONObject rallyTask = iter.next();
				s.append("<tr align=\"center\"><td>");
				s.append(String.valueOf(i));
				s.append("</td>");
				s.append("<td style=\"word-break:break-all\">");
				s.append(rallyTask.getString("taskName"));
				s.append("</td><td>");
				s.append(rallyTask.getString("state"));
				s.append("</td><td>");
				if(null != rallyTask.getString("actual") && !"".equals(rallyTask.getString("actual"))){
					s.append(rallyTask.getString("actual"));
				}
				s.append("</td><td>");
				s.append(rallyTask.getString("todo"));
				s.append("</td><td>");
				s.append(rallyTask.getString("parent"));
				s.append("</td></tr>");
//				if(null != rallyTask.getString("desc") && !"".equals(rallyTask.getString("desc"))){
//					s.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;");
//					s.append(rallyTask.getString("desc"));
//				}
				i++;
			}
			s.append("</table>");
		}
		
		s.append("<h3>Quix List:</h3><br>");
		if(quixListArray.size() > 0){
			s.append("<table cellspacing=\"0px\" border=\"1\"><tr align=\"center\"><th width=\"7%\">No.</th><th width=\"65%\">Name</th><th width=\"10%\">State</th><th width=\"9%\">Developer</th><th width=\"9%\">Submitter</th><th width=\"9%\">UpdateDate</th><th width=\"9%\">Project</th></tr>");
			Iterator<JSONObject> iter = quixListArray.iterator();
			int i = 1;
			while(iter.hasNext()){
				JSONObject QuixInfo = iter.next();
				s.append("<tr align=\"center\"><td>");
				s.append(QuixInfo.getString("QuixID"));
				s.append("</td>");
				s.append("<td style=\"word-break:break-all\">");
				s.append(QuixInfo.getString("QuixName"));
				s.append("</td><td>");
				s.append(QuixInfo.getString("State"));
				s.append("</td><td>");
				if(null != QuixInfo.getString("Developer") && !"".equals(QuixInfo.getString("Developer"))){
					s.append(QuixInfo.getString("Developer"));
				}
				s.append("</td><td>");
				s.append(QuixInfo.getString("Submit"));
				s.append("</td><td>");
				s.append(QuixInfo.getString("Date"));
				s.append("</td><td>");
				s.append(QuixInfo.getString("Project"));
				s.append("</td></tr>");
				
//				if(null != rallyTask.getString("desc") && !"".equals(rallyTask.getString("desc"))){
//					s.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;");
//					s.append(rallyTask.getString("desc"));
//				}
				i++;
			}
			s.append("</table>");
			
		}
		
		s.append("<h3>Custom tasks:</h3><br>");
		if(customTaskArray.size() > 0){
			s.append("<table width=\"100%\" cellspacing=\"0px\" border=\"1\"><tr><th width=\"5%\">No.</th><th width=\"40%\">Name</th><th width=\"10%\">State</th><th width=\"5%\">Percentage</th><th width=\"5%\">Time</th><th width=\"25%\">Description</th></tr>");
			Iterator<JSONObject> iter = customTaskArray.iterator();
			int i = 1;
			while(iter.hasNext()){
				JSONObject customTask = iter.next();
				s.append("<tr><td>");
				s.append(String.valueOf(i));
				s.append("</td>");
				s.append("<td style=\"word-break:break-all\">");
				s.append(customTask.getString("taskName"));
				s.append("</td><td>");
				s.append(customTask.getString("state"));
				s.append("</td><td>");
				if(null != customTask.getString("percentage") && !"".equals(customTask.getString("percentage"))){
					s.append(customTask.getString("percentage"));
					if(customTask.getString("percentage").indexOf("%") < 0){
						s.append("%");
					}
				}
				s.append("</td><td>");
				s.append(customTask.getString("time"));
				s.append("</td><td style=\"word-break:break-all\">");
				if(null != customTask.getString("desc") && !"".equals(customTask.getString("desc"))){
					s.append(customTask.getString("desc"));
				}
				s.append("</td></tr>");
				i++;
			}
			s.append("</table>");
		}
		
		if(issueMiscPlanArray.size() > 0){
			JSONObject issueMiscPlan = issueMiscPlanArray.getJSONObject(0);
			if(null != issueMiscPlan.getString("issue") && !"".equals(issueMiscPlan.getString("issue"))){
				s.append("<br><h3>Issues:</h3>");
				s.append(issueMiscPlan.getString("issue"));
				s.append("<br>");
			}
			if(null != issueMiscPlan.getString("misc") && !"".equals(issueMiscPlan.getString("misc"))){
				s.append("<br><h3>Misc:</h3>");
				s.append(issueMiscPlan.getString("misc"));
				s.append("<br>");
			}
			if(null != issueMiscPlan.getString("plan") && !"".equals(issueMiscPlan.getString("plan"))){
				s.append("<br><h3>Plan for next week:</h3>");
				s.append(issueMiscPlan.getString("plan"));
			}
			
		}
		s.append("</div>");
		
		try {
			out = this.getResponse().getWriter();
			out.print(s.toString().replaceAll("\n", "<br>"));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
        }
	}
	
	/**
	 * This is for My titan -> My workload
	 * Save weekly report to DB
	 * @return
	 */
	public String doSaveReport(){
		String strTitle = this.createTitle();
		//Save to DB
		this.saveReport(TitanContent.WEEKLY_REPORT_DRAFT, strTitle);
		return SUCCESS;
	}
	
	/**
	 * This is for My titan -> My workload
	 * Send weekly report and save it to DB
	 * @return
	 */
	public String doSaveAndSendReport(){
		// Send email
		User u = UserSessionUtil.getUser();
		String strTitle = this.createTitle();
		EmailManageAction email = new EmailManageAction();
		email.sendEmail(strTitle.toString(), reportContent, sendTo);
		
		//Save to DB
		this.saveReport(TitanContent.WEEKLY_REPORT_SENT, strTitle);
		
		//add exp value to exp account
		try{
		    if(!mytitanService.checkExpLogByDetail(u.getUserId())){
			    this.saveExpValue(2, strTitle, "weekly report", u.getUserId());
		    }
		    }catch (BaseException e) {
			    e.printStackTrace();
		}
		return SUCCESS;
	}


/**
	 * save experience value to exp account
	 */
	
	private void saveExpValue(int expValue, String detail, String type, Integer userId){
		try{
			int totalExp = 0;
			ExpAccount ea = mytitanService .getExpByUserId(userId);
			if (null == ea){
	            ea = new ExpAccount();
	            ea.setIsValid(0);
	            ea.setLastUpdateDate(DateUtils. getCurrentDate());
	            ea.setExpTotal(0);
	            ea.setUserId(userId);
			}
	       totalExp = ea.getExpTotal();
	       totalExp += 2;
	       this.saveExpLog(expValue, totalExp, detail, userId, type);
	       ea.setExpTotal( totalExp);
           mytitanService.saveExp(ea);
			
		}catch (BaseException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * This is for My titan -> My workload
	 * Create email title
	 * @return
	 */
	private String createTitle(){
		StringBuffer strTitle = new StringBuffer();
		strTitle.append(UserSessionUtil.getUser().getUserCode());
		strTitle.append("'s weekly report from ");
		strTitle.append(startDate);
		strTitle.append(" to ");
		strTitle.append(endDate);
		return strTitle.toString();
	}
	
	/**
	 * This is for My titan -> My workload
	 * Save weekly report
	 * @param type
	 */
	private void saveReport(String type, String strTitle){
		WeeklyReport wr = new WeeklyReport();
		
		JSONObject json = JSONObject.fromObject(jsonObj);
		JSONArray rallyTaskArray = json.getJSONArray("rallyTaskArray");
		JSONArray quixListArray = json.getJSONArray("quixListArray");
		JSONArray customTaskArray = json.getJSONArray("customTaskArray");
		JSONArray issueMiscPlanArray = json.getJSONArray("issueMiscPlanArray");
		
		wr.setUserId(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
		wr.setType(type);
		wr.setTitle(strTitle);
		wr.setCreateDate(DateUtils.getCurrentDate());
		wr.setSendTo(sendTo);
		if(issueMiscPlanArray.size() > 0){
			JSONObject issueMiscPlan = issueMiscPlanArray.getJSONObject(0);
			if(null != issueMiscPlan.getString("issue") && !"".equals(issueMiscPlan.getString("issue"))){
				wr.setIssue(issueMiscPlan.getString("issue").replaceAll("\n", "<br>"));
			}
			if(null != issueMiscPlan.getString("misc") && !"".equals(issueMiscPlan.getString("misc"))){
				wr.setMisc(issueMiscPlan.getString("misc").replaceAll("\n", "<br>"));
			}
			if(null != issueMiscPlan.getString("plan") && !"".equals(issueMiscPlan.getString("plan"))){
				wr.setPlanNextweek(issueMiscPlan.getString("plan").replaceAll("\n", "<br>"));
			}
		}
		try {
			mytitanService.saveReport(wr);
			List<WeeklyReportTask> reportTaskList = new ArrayList<WeeklyReportTask>();
			if(rallyTaskArray.size() > 0){
				Iterator<JSONObject> iter = rallyTaskArray.iterator();
				while(iter.hasNext()){
					WeeklyReportTask wrt = new WeeklyReportTask();
					JSONObject rallyTask = iter.next();
					wrt.setReportId(wr.getReportId());
					wrt.setTaskType(TitanContent.WEEKLY_REPORT_TASK_RALLY);
					wrt.setTaskName(rallyTask.getString("taskName"));
					wrt.setState(rallyTask.getString("state"));
					if(null != rallyTask.getString("actual") && !"".equals(rallyTask.getString("actual"))){
						wrt.setActuals(rallyTask.getString("actual"));
					}
					else{
						wrt.setActuals("0");
					}
					if(null != rallyTask.getString("todo") && !"".equals(rallyTask.getString("todo"))){
						wrt.setTodo(rallyTask.getString("todo"));
					}
					else{
						wrt.setTodo("0");
					}
					wrt.setParent(rallyTask.getString("parent"));
					wrt.setPercent("0");
					reportTaskList.add(wrt);
				}
			}
			if(quixListArray.size() > 0){
				Iterator<JSONObject> iter = quixListArray.iterator();
				while(iter.hasNext()){
					WeeklyReportTask wrt = new WeeklyReportTask();
					JSONObject quixList = iter.next();
					wrt.setReportId(wr.getReportId());
					wrt.setTaskType(TitanContent.WEEKLY_REPORT_TASK_QUIX);
					wrt.setTaskName(quixList.getString("QuixName"));
					wrt.setState(quixList.getString("State"));
//					if(null != quixList.getString("Developer") && !"".equals(quixList.getString("Developer"))){
//						wrt.setActuals(quixList.getString("Developer"));
//					}					
//					if(null != quixList.getString("Submit") && !"".equals(quixList.getString("Submit"))){
//						wrt.setTodo(quixList.getString("Submit"));
//					}					
//					wrt.set(quixList.getString("Date"));
					wrt.setActuals("0");
					wrt.setPercent("0");
					wrt.setTodo("0");
					reportTaskList.add(wrt);
				}
			}
			
			if(customTaskArray.size() > 0){
				Iterator<JSONObject> iter = customTaskArray.iterator();
				while(iter.hasNext()){
					WeeklyReportTask wrt = new WeeklyReportTask();
					JSONObject customTask = iter.next();
					wrt.setReportId(wr.getReportId());
					wrt.setTaskType(TitanContent.WEEKLY_REPORT_TASK_CUSTOM);
					wrt.setTaskName(customTask.getString("taskName"));
					wrt.setState(customTask.getString("state"));
					if(null != customTask.getString("percentage") && !"".equals(customTask.getString("percentage"))){
						wrt.setPercent(customTask.getString("percentage"));
					}
					else{
						wrt.setPercent("0");
					}
					if(null != customTask.getString("time") && !"".equals(customTask.getString("time"))){
						wrt.setActuals(customTask.getString("time"));
					}
					else{
						wrt.setActuals("0");
					}
					if(null != customTask.getString("desc") && !"".equals(customTask.getString("desc"))){
						wrt.setDesciption(customTask.getString("desc"));
					}
					wrt.setTodo("0");
					reportTaskList.add(wrt);
				}
			}
			if(null != reportTaskList && reportTaskList.size() != 0){
				mytitanService.saveReportTask(reportTaskList);
			}
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This is for My titan -> My workload
	 * Get report info from db
	 */
	public String getReportFromDB(){
		PrintWriter out = null;
		StringBuffer s = new StringBuffer();
		try {
			WeeklyReport report = mytitanService.getReportById(reportId);
			List<WeeklyReportTask> rallyTasksList = mytitanService.getReportTask(reportId, TitanContent.WEEKLY_REPORT_TASK_RALLY);
			List<WeeklyReportTask> quixTasksList = mytitanService.getReportTask(reportId, TitanContent.WEEKLY_REPORT_TASK_QUIX);
			List<WeeklyReportTask> customTasksList = mytitanService.getReportTask(reportId, TitanContent.WEEKLY_REPORT_TASK_CUSTOM);
			
			s.append("<div id=\"roll\"><h3>Rally tasks:</h3><br>");
			if(null != rallyTasksList && rallyTasksList.size() != 0){
				s.append("<table cellspacing=\"0px\" border=\"1\"><tr align=\"center\"><th width=\"7%\">No.</th><th width=\"65%\">Name</th><th width=\"10%\">State</th><th width=\"9%\">Actual</th><th width=\"9%\">Todo</th><th width=\"9%\">Parent</th></tr>");
				Iterator<WeeklyReportTask> iter = rallyTasksList.iterator();
				int i = 1;
				while(iter.hasNext()){
					WeeklyReportTask rallyTask = iter.next();
					s.append("<tr align=\"center\"><td>");
					s.append(String.valueOf(i));
					s.append("</td>");
					s.append("<td style=\"word-break:break-all\">");
					s.append(rallyTask.getTaskName());
					s.append("</td><td>");
					s.append(rallyTask.getState());
					s.append("</td><td>");
					s.append(rallyTask.getActuals());
					s.append("</td><td>");
					s.append(rallyTask.getTodo());
					s.append("</td><td>");
					s.append(rallyTask.getParent());
					s.append("</td></tr>");
//					if(null != rallyTask.getString("desc") && !"".equals(rallyTask.getString("desc"))){
//						s.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;");
//						s.append(rallyTask.getString("desc"));
//					}
					i++;
				}
				s.append("</table>");
			}
			
			s.append("<div id=\"roll\"><h3>Quix tasks:</h3><br>");
			if(null != quixTasksList && quixTasksList.size() != 0){
				s.append("<table cellspacing=\"0px\" border=\"1\"><tr align=\"center\"><th width=\"7%\">No.</th><th width=\"65%\">Name</th><th width=\"10%\">State</th></tr>");
				Iterator<WeeklyReportTask> iter = quixTasksList.iterator();
				int i = 1;
				while(iter.hasNext()){
					WeeklyReportTask quixTask = iter.next();
					s.append("<tr align=\"center\"><td>");
					s.append(String.valueOf(i));
					s.append("</td>");
					s.append("<td style=\"word-break:break-all\">");
					s.append(quixTask.getTaskName());
					s.append("</td><td>");
					s.append(quixTask.getState());
					s.append("</td></tr>");
					i++;
				}
				s.append("</table>");
			}
			
			
			s.append("<h3>Custom tasks:</h3><br>");
			if(null != customTasksList && customTasksList.size() > 0){
				s.append("<table width=\"100%\" cellspacing=\"0px\" border=\"1\"><tr><th width=\"5%\">No.</th><th width=\"40%\">Name</th><th width=\"10%\">State</th><th width=\"5%\">Percentage</th><th width=\"5%\">Time</th><th width=\"25%\">Description</th></tr>");
				Iterator<WeeklyReportTask> iter = customTasksList.iterator();
				int i = 1;
				while(iter.hasNext()){
					WeeklyReportTask customTask = iter.next();
					s.append("<tr><td>");
					s.append(String.valueOf(i));
					s.append("</td>");
					s.append("<td style=\"word-break:break-all\">");
					s.append(customTask.getTaskName());
					s.append("</td><td>");
					s.append(customTask.getState());
					s.append("</td><td>");
					s.append("Percentage: ");
					s.append(customTask.getPercent());
					s.append("%");
					s.append("</td><td>");
					s.append(customTask.getActuals());
					s.append("</td><td style=\"word-break:break-all\">");
					if(null != customTask.getDesciption() && !"".equals(customTask.getDesciption())){
						s.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
						s.append(customTask.getDesciption());
					}
					s.append("</td></tr>");
					i++;
				}
				s.append("</table>");
			}
			
				if(null != report.getIssue() && !"".equals(report.getIssue())){
					s.append("<br><h3>Issues:</h3>");
					s.append(report.getIssue());
					s.append("<br>");
				}
				if(null != report.getMisc() && !"".equals(report.getMisc())){
					s.append("<br><h3>Misc:</h3>");
					s.append(report.getMisc());
					s.append("<br>");
				}
				if(null != report.getPlanNextweek() && !"".equals(report.getPlanNextweek())){
					s.append("<br><h3>Plan for next week:</h3>");
					s.append(report.getPlanNextweek());
				}
				s.append("</div>");
			
			out = this.getResponse().getWriter();
			out.print(s.toString());
			out.flush();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
        }
		return s.toString();
	}
	
	/**
	 * This is for My titan -> My workload
	 * Send weekly report
	 */
	public void doSendReport(){
		PrintWriter out = null;
		try {
			User u = UserSessionUtil.getUser();
			WeeklyReport report = mytitanService.getReportById(reportId);
			EmailManageAction email = new EmailManageAction();
			String reportString = this.getReportFromDB();
			email.sendEmail(report.getTitle(), reportString, report.getSendTo());
			
			report.setType(TitanContent.WEEKLY_REPORT_SENT);
			mytitanService.saveReport(report);
			out = this.getResponse().getWriter();
			out.print("success");
			out.flush();
			if(!mytitanService.checkExpLogByDetail(u.getUserId())){
			    this.saveExpValue(2, report.getTitle(), "weekly report",u.getUserId());
		    }
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
        }
	}
	
	public String[] getAwardPoints() {
		return awardPoints;
	}

	public void setAwardPoints(String[] awardPoints) {
		this.awardPoints = awardPoints;
	}

	public String[] getAwardUsers() {
		return awardUsers;
	}

	public void setAwardUsers(String[] awardUsers) {
		this.awardUsers = awardUsers;
	}

	/**
	 * This is for My titan -> My user stories
	 * List login user's user stories
	 */
	public String myusList(){
		String loginUserEmail = UserSessionUtil.getUser().getMail();
		try {
			usInfoList = mytitanService.getUserStoriesFromDB(loginUserEmail);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		navigation += "->My user stories</div>";
		return SUCCESS;
	}
	
	/**
	 * This is for My titan -> My user stories
	 * Sync login user's user stories
	 */
	public String refreshUs(){
		PrintWriter out = null;
		String loginUserEmail = UserSessionUtil.getUser().getMail();
		usInfoList = new ArrayList<UserstoryInfo>();
		try {
			LoginUserVo loginUserVo = mytitanService.getUserVoByEmail(loginUserEmail);
			List<UserstoryVo> usListVoList = mytitanService.getUserStoriesByUser(loginUserVo.getObjectId());
			if(null != usListVoList){
				Iterator<UserstoryVo> iter = usListVoList.iterator();
				while(iter.hasNext()){
					UserstoryVo usVo = iter.next();
					UserstoryInfo usInfo = new UserstoryInfo();
					usInfo.setTaskActuals(usVo.getTaskActuals());
					usInfo.setPlanEstimate(usVo.getPlanEstimate());
					usInfo.setObjectId(usVo.getObjectId());
					usInfo.setOwnerEmail(loginUserEmail);
					usInfo.setState(usVo.getState());
					usInfo.setTaskTodos(usVo.getTaskTodos());
					usInfo.setUsName(usVo.getUsName());
					usInfo.setUsNum(usVo.getUsNum());
					usInfo.setTaskEstimates(usVo.getTaskEstimates());
					usInfo.setProjectinRally(usVo.getProjectinRally());
					usInfo.setState(usVo.getState());
					usInfo.setOwnerEmail(loginUserEmail);
					usInfoList.add(usInfo);
				}
			}
			mytitanService.saveMyUserStories(usInfoList);
			out = this.getResponse().getWriter();
			out.print("success");
			out.flush();
			
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
        }
		return SUCCESS;
	}
	/**
	 * This is for My titan -> My point
	 * Sync user story and defect info for point calculate
	 */
	
	public String syncPoint(){
		PrintWriter out = null;
		try {
			List<User> userList = userService.getAllUserByUserVo(null);
			Iterator<User> l = userList.iterator();
			while(l.hasNext()){
				User u = l.next();
				this.getTodayPointFare(u);
			}
		} catch (BaseDaoException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	private int getTodayPointFare(User u) throws BaseException{
		int totalPoint = 0;
		int totalExp = 0;
		Calendar calendar = Calendar.getInstance();   			
		calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)-2);
		List<UserstoryVo> usInfoList = mytitanService.getUserStoriesList("Accepted", calendar, u.getMail());
		Point pt = mytitanService.getPointByUserId(u.getUserId());
		ExpAccount ea = mytitanService.getExpByUserId(u.getUserId());
		if(null == pt){
			pt = new Point();
			pt.setIsValid(0);
			pt.setLastUpdateDate(DateUtils.getCurrentDate());
			pt.setPoints(0);
			pt.setExPoint(0);
			pt.setUserId(u.getUserId());
		}
		totalPoint = pt.getPoints();
		if(null == ea){
			ea = new ExpAccount();
			ea.setIsValid(0);
			ea.setLastUpdateDate(DateUtils.getCurrentDate());
			ea.setExpTotal(0);
			ea.setUserId(u.getUserId());
		}
		totalExp = ea.getExpTotal();
		
		// User story
		if(null != usInfoList && usInfoList.size() != 0){
			Iterator<UserstoryVo> usl = usInfoList.iterator();
			while(usl.hasNext()){
				UserstoryVo usVo = usl.next();
				if(this.isDuplicate(usVo.getObjectId(), "User Story")){
					continue;
				}
				String pe = usVo.getPlanEstimate();
				totalPoint += 5;
				this.savePointHistory(usVo.getObjectId(), "User Story",  u.getUserId(), 5, totalPoint);
				totalExp += 5;
				this.saveExpLog(5, totalExp, usVo.getUsName(),u.getUserId(),"User Story");
				
			}
		}	
			
		// Defect for QA
		DefectInfo di = new DefectInfo();
		di.setSubmittEmail(u.getMail());
		di.setSubmitDate(calendar.getTime());
		List<DefectInfo> sdefectList = testcaseService.getDefectInfoByBean(di);
		if(null != sdefectList && sdefectList.size() != 0){
			Iterator<DefectInfo> qal = sdefectList.iterator();
			while(qal.hasNext()){
				DefectInfo defectInfo = qal.next();
				if(this.isDuplicate(defectInfo.getObjectId(), "Submit defect")){
					continue;
				}
				totalPoint += 2;
				this.savePointHistory(defectInfo.getObjectId(), "Submit defect",  u.getUserId(), 2, totalPoint);
				totalExp += 2;
				this.saveExpLog(5, totalExp, defectInfo.getDefectName(), u.getUserId(),"Submit defect");
			}
		}
		
		// Defect for Develop
		DefectInfo di1 = new DefectInfo();
		di1.setDeveloperEmail(u.getMail());
		di1.setLastupdateDate(calendar.getTime());
		List<DefectInfo> sdefectList1 = testcaseService.getDefectInfoByBean(di1);
		if(null != sdefectList1 && sdefectList1.size() != 0){
			Iterator<DefectInfo> devl = sdefectList1.iterator();
			while(devl.hasNext()){
				DefectInfo defectInfo = devl.next();
				if (this.isDuplicate(defectInfo.getObjectId(), "Fix defect")){
					continue;
				}
				totalPoint += 5;
				this.savePointHistory(defectInfo.getObjectId(), "Fix defect",  u.getUserId(), 5, totalPoint);
				totalExp += 5;
				this.saveExpLog(5, totalExp, defectInfo.getDefectName(), u.getUserId(),"Fix defect");
			}
		}
		pt.setPoints(totalPoint);
		ea.setExpTotal(totalExp);
		mytitanService.savePoint(pt);
		mytitanService.saveExp(ea);
		return totalPoint;
	}
	
	
	private void savePointHistory(String objId, String type, int userId, int pointFare, int tempTotalPoint) throws BaseException{
		PointHist ph = new PointHist();
		ph.setPointFare(pointFare);
		ph.setPointTotal(tempTotalPoint);
		ph.setObjectId(objId);
		ph.setType(type);
		ph.setUserId(userId);
		ph.setIsValid(0);
		ph.setCreateDate(DateUtils.getCurrentDate());
		mytitanService.savePointHist(ph);
	}


	private void saveExpLog( int expValue, int expTotal,  String detail,  int userId, String expType) throws BaseException{
		ExpLog el = new ExpLog();
		el.setExpValue(expValue);
		el.setExpTotal(expTotal);
		el.setDetail(detail);
		el.setUserId(userId);
		el.setExpType(expType);
		el.setCreateDate(DateUtils.getCurrentDate());
		mytitanService.saveExpLog(el);	
	}
	
	private Boolean isDuplicate(String objId, String type) throws BaseException{
		PointHist ph = new PointHist();
		ph.setObjectId(objId);
		ph.setType(type);
		List<PointHist> phList = mytitanService.getPointHistByBean(ph);
		if(null != phList && phList.size() != 0){
			return true;
		}
		return false;
	}
	
	/**
	 * This is for My titan -> My user point
	 * list point records of login user
	 */
	
	public String myPointList(){
		User user = UserSessionUtil.getUser();
		mypoint = 0;
		myAwardPoint = 0;
		try {
			phList = mytitanService.getPointHistByUser(user.getUserId());
			aphList = mytitanService.getAwardPointHistByUser(user.getUserId().toString());
			userList = userService.getUserListByGroup(user.getGroupId().toString());
			Point Pt = mytitanService.getPointByUserId(user.getUserId());
			if(null != Pt){
				mypoint = Pt.getPoints();
				myAwardPoint = Pt.getExPoint();
			}
		} catch (BaseException e) {
			e.printStackTrace();
		}catch (ParseException e) {
			e.printStackTrace();
		}
		
		navigation += "->My Point Records</div>";
		return SUCCESS;
	}
	
	public String doAwardPoint(){
		User user = UserSessionUtil.getUser();
		try {
			if(this.awardPoints != null && this.awardPoints.length !=0){
				for(int i=0; i< awardPoints[0].split( "," ).length ; i++){
					if(!"".equals(awardPoints[0].split(",")[i])){
						Point pt = mytitanService.getPointByUserId(new Integer (awardUsers[0].split(",")[i]));
						int apoint = new Integer (awardPoints[0].split(",")[i]);
						int pointTotal = pt.getPoints() + apoint;
						pt.setPoints(pt.getPoints() + apoint);
						mytitanService.savePoint(pt);
						this.savePointHistory(user.getUserId().toString(), "award", pt.getUserId(), apoint, pointTotal);
						
						Point spt = mytitanService.getPointByUserId(user.getUserId());
						int exPoint = spt.getExPoint();
						int exPointTotal = exPoint - apoint;
						if(exPointTotal>=0){
							spt.setExPoint(exPointTotal);
							mytitanService.savePoint(spt);
						}else{
							return ERROR;
						}
					}
					
				}
						
			}
			
		}catch (BaseException e) {
            e.printStackTrace();
        }   

		
        this.myPointList();
		return SUCCESS;
		
	}
	
	/**
	 * This is for My titan -> My user stories
	 * Get us's all tasks
	 */
	public void getUsTask(){
		PrintWriter out = null;
		try {
			List<WeeklyReportTask> usTaskList = mytitanService.getUsTasks(usNum);
			StringBuffer s = new StringBuffer();
			if(null != usTaskList && usTaskList.size() != 0){
				Iterator<WeeklyReportTask> iter = usTaskList.iterator();
				s.append("<table border=\"1\"><thead><th>Name</th><th>State</th><th>Actual</th><th>Todo</th><thead>");
				while(iter.hasNext()){
					WeeklyReportTask usTask = iter.next();
					s.append("<tr><td>");
					s.append(usTask.getTaskName());
					s.append("</td>");
					s.append("<td>");
					s.append(usTask.getState());
					s.append("</td>");
					s.append("<td>");
					s.append(usTask.getActuals());
					s.append("</td>");
					s.append("<td>");
					s.append(usTask.getTodo());
					s.append("</td></tr>");
				}
				s.append("</table>");
			}
			else{
				s.append("No task found!");
			}
			out = this.getResponse().getWriter();
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
	
	/**
	 * This is for Mytitan->my code
	 * List my code submit
	 * @return
	 */
	public String myCommitList(){
		String loginUserEmail = UserSessionUtil.getUser().getMail();
		try {
			commitList = mytitanService.getCommitList(loginUserEmail, "");
		} catch (BaseException e) {
			e.printStackTrace();
		}
		navigation += "->My code commition</div>";
		return SUCCESS;
	}
	
	/**
	 * This is for Mytitan->my code
	 * Show each commit's change list
	 * @return
	 */
	public void myCommitChangeList(){
		PrintWriter out = null;
		try {
			List<CommitPath> changeList = mytitanService.getCommitChangeList(commitId);
			StringBuffer s = new StringBuffer();
			if(null != changeList && changeList.size() != 0){
				Iterator<CommitPath> iter = changeList.iterator();
				s.append("<div id=\"roll\"><table border=\"0\" align=\"center\" width=\"600px\"><thead><tr align=\"center\" width=\"600px\"><td colspan=\"2\"><h2>Total changes:&nbsp;&nbsp;");
				s.append(changeList.size());
				s.append("</h2></td></tr><thead>");
				s.append("<tr align=\"center\"><td colspan=\"2\"><HR width=\"80%\" color=\"#046ec9\" SIZE=\"1\" /></td></tr>");
				s.append("<tr align=\"left\"><th style=\"padding-left:90px\">File Path and Name</th><th <th>Code Change Lines</th></tr>");
				while(iter.hasNext()){
					CommitPath cp = iter.next();
					s.append("<tr align=\"left\">");
					s.append("<td style=\"padding-left:90px\">");
						s.append(cp.getPath());
					s.append("</td>");
					s.append("<td style=\"padding-left:40px\">");
						s.append(cp.getCodeChange());
					s.append("</td>");
					s.append("</tr><thead>");
				}
				s.append("</table></div>");
			}
			else{
				s.append("No changes found!");
			}
			out = this.getResponse().getWriter();
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
	
	/**
	 * This is for Mytitan->my code
	 * List all code submit
	 * @return
	 */
	public String goReportaryView(){
		try {
			commitList = mytitanService.getCommitList("", reportaryId);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * This is for poins shopping mall
	 * @return
	 */
	public String goUsePoint(){
		try {
			goodsList = mytitanService.getAllGoods();
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	public String goNewUsePoint(){
		try {
			goodsList = mytitanService.getAllGoods();
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	/**
	 * This is for check out the cart
	 * @return
	 */
	public String doSaveConsume(){
		JSONObject json = JSONObject.fromObject(jsonObj);
		JSONArray goodsArray = json.getJSONArray("goodsArray");
		Iterator<JSONObject> iter = goodsArray.iterator();
		Consumption c;
		int totalPrice = 0;
		try {
			Point p = mytitanService.getPointByUserId(UserSessionUtil.getUser().getUserId());
			while(iter.hasNext()){
				JSONObject consumeInfo = iter.next();
				Goods g = mytitanService.getGoodsByName(consumeInfo.getString("goodsName"));
				
				c = new Consumption();
				c.setGoodsName(consumeInfo.getString("goodsName"));
				c.setPrice(consumeInfo.getInt("price"));
				c.setStatus(TitanContent.CONSUME_SUBMITTED);
				c.setConsumeDate(DateUtils.getCurrentDate());
				c.setUserId(UserSessionUtil.getUser().getUserId());
				c.setType(g.getType());
				c.setIsValid(0);
				totalPrice += consumeInfo.getInt("price");
				mytitanService.saveConsume(c);
				this.savePointHistory(c.getConsumeId(), "consume", UserSessionUtil.getUser().getUserId(), -consumeInfo.getInt("price"), p.getPoints() - totalPrice);
			}
			p.setPoints(p.getPoints() - totalPrice);
			p.setLastUpdateDate(DateUtils.getCurrentDate());
			mytitanService.savePoint(p);
			UserSessionUtil.setPoint(p.getPoints());
			
			
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	private void sendConsumeInfoToManagers(Set<String> goodSet) throws BaseDaoException{
		User user = UserSessionUtil.getUser();
		int groupId = user.getGroupId();
		
		List<User> managerList = userService.getManagerByGroup(String.valueOf(groupId));
		
		Iterator<User> iter = managerList.iterator();
		EmailManageAction email = new EmailManageAction();
//		String content = this.generateConcent(ccList, rcPoint);
		StringBuffer sb;
		while(iter.hasNext()){
			User u = iter.next();
			sb = new StringBuffer();
			sb.append("Hi ");
			sb.append(u.getUserCode());
			sb.append("<BR><br>");
			sb.append(user.getUserCode());
			sb.append(" consume the ");
			sb.append(goodSet.toString());
			sb.append("<BR>");
			sb.append("For more information, please visit ");
			sb.append("http://mars.chn.hp.com/titan");
			email.sendEmail("Consume notification", sb.toString(), u.getMail().replace("hp.com", "hpe.com"));
		}
		
	}
	
	/**
	 * This is for check out the cart
	 * @return
	 */
	public String doSaveNewConsume(){
		PrintWriter out = null;
		User u = UserSessionUtil.getUser();
		JSONObject json = JSONObject.fromObject(jsonObj);
		JSONArray goodsArray = json.getJSONArray("goodsArray");
		Iterator<JSONObject> iter = goodsArray.iterator();
		Consumption c;
		int totalPrice = 0;
		Set<String> set = new HashSet<String>();
		try {
			Point p = mytitanService.getPointByUserId(UserSessionUtil.getUser().getUserId());
			while(iter.hasNext()){
				JSONObject consumeInfo = iter.next();
				Goods g = mytitanService.getGoodsByName(consumeInfo.getString("goodsName"));
				Integer count = Integer.valueOf(consumeInfo.getString("count"));
				for(int i = 0; i < count; i++){
					c = new Consumption();
					c.setGoodsName(consumeInfo.getString("goodsName"));
					c.setPrice(consumeInfo.getInt("price"));
					c.setStatus(TitanContent.CONSUME_SUBMITTED);
					c.setConsumeDate(DateUtils.getCurrentDate());
					c.setUserId(UserSessionUtil.getUser().getUserId());
					c.setType(g.getType());
					c.setIsValid(0);
					totalPrice += consumeInfo.getInt("price");
					mytitanService.saveConsume(c);
					this.savePointHistory(c.getConsumeId(), "consume", UserSessionUtil.getUser().getUserId(), -consumeInfo.getInt("price"), p.getPoints() - totalPrice);
					this.saveExpValue(2, consumeInfo.getString("goodsName"), "Consume", u.getUserId());
					if(!set.contains(consumeInfo.getString("goodsName"))){
						set.add(consumeInfo.getString("goodsName"));
					}
				}
			}
			
			InBox inbox = new InBox();
			inbox.setUserId(u.getUserId());
			List<InBox> inboxList = mytitanService.getInBoxByBean(inbox);
			if(inboxList == null || inboxList.size() == 0 || this.ifGetTreasure(inboxList.get(inboxList.size() - 1).getCreateTime())){
				inbox.setCreateTime(DateUtils.getCurrentDate());
				inbox.setIsReaded(TitanContent.INBOX_READED_NO);
				inbox.setIsValid(0);
				inbox.setMsgName("Treasure Box");
				inbox.setMsgType(TitanContent.INBOX_MSG_TREASURE);
				mytitanService.saveInbox(inbox);
				UserSessionUtil.setInbox(UserSessionUtil.getInbox() + 1);
				inboxCount = String.valueOf(UserSessionUtil.getInbox() + 1);
			}
			
			p.setPoints(p.getPoints() - totalPrice);
			p.setLastUpdateDate(DateUtils.getCurrentDate());
			mytitanService.savePoint(p);
			UserSessionUtil.setPoint(p.getPoints());
				
			List<Consumption> cList = mytitanService.hasGamble(UserSessionUtil.getUser().getUserId());
			Boolean hasGamble = false;
			if(null != cList){
				hasGamble = true;
			}
			out = this.getResponse().getWriter();
			if(hasGamble){
				out.print("yes");
			}
			else{
				out.print("no");
			}
			this.sendConsumeInfoToManagers(set);
			out.flush();
			
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			out.close();
	    }
		return SUCCESS;
	}
	
	private Boolean ifGetTreasure(Date lastDate){
		Boolean res = false;
		Date currentDate = DateUtils.getCurrentDate();
		long diff = currentDate.getTime() - lastDate.getTime();
	    long days = diff / (1000 * 60 * 60 * 24);
	    if(days >= 6){
	    	res = true;
	    }
		return res;
	}
	
	
	/**
	 * This is for Gamble
	 * @return
	 */
	public String goGamblingList(){
		try {
			mypoint = UserSessionUtil.getPoint();
			inboxCount = String.valueOf(UserSessionUtil.getInbox());
			consumeList = mytitanService.hasGamble(UserSessionUtil.getUser().getUserId());
		} catch (BaseException e) {
			e.printStackTrace();
		}
		navigation += "->Gamble</div>";
		return SUCCESS;
	}
	
	/**
	 * This is for select number
	 * @return
	 */
	public String selectNumber(){
		User user = UserSessionUtil.getUser();
		int groupId = user.getGroupId();
		try {
			List<Integer> numList = mytitanService.getSelectNumberByGroup(groupId);
			if(null == numList){
				selectedNumbers = "[]";
				return SUCCESS;
			}
			StringBuffer sb = new StringBuffer();
			Iterator<Integer> l = numList.iterator();
			sb.append("[");
			while(l.hasNext()){
				Integer i = l.next();
				sb.append("'");
				sb.append(i);
				sb.append("',");
			}
			sb.append("]");
			selectedNumbers = sb.toString().replace("',]", "']");
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * This is for if the number has been selected
	 */
	public void ifnumSelected(){
		PrintWriter out = null;
		User user = UserSessionUtil.getUser();
		int groupId = user.getGroupId();
		try {
			String selected = "no";
			List<Integer> numList = mytitanService.getSelectNumberByGroup(groupId);
			if(null != numList && numList.indexOf(Integer.valueOf(selectedNum)) >= 0){
				selected = "yes";
			}
			out = this.getResponse().getWriter();
			out.print(selected);
			out.flush();
			
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
        }
	}
	
	/**
	 * This is for submit select number
	 * @return
	 */
	public void doSubmitSelect(){
		PrintWriter out = null;
		try {
			Consumption consume = mytitanService.getConsumptionById(consumeId);
			consume.setSelectedNum(Integer.valueOf(selectedNum));
			mytitanService.saveConsume(consume);
			out = this.getResponse().getWriter();
			out.print("success");
			out.flush();
			
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
        }
	}
	
	/**
	 * This is for check out the cart
	 * To check if the user has enough points
	 * @return
	 */
	public void hasEnoughPoints(){
		PrintWriter out = null;
		try {
			String res = "no";
			Point p = mytitanService.getPointByUserId(UserSessionUtil.getUser().getUserId());
			if(p.getPoints() >= Integer.valueOf(totalPrice)){
				res = "yes";
			}
			out = this.getResponse().getWriter();
			out.print(res);
			out.flush();
			
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
        }
	}
	
//	public String gambleConsume(){
//		User user = UserSessionUtil.getUser();
//		int groupId = user.getGroupId();
//		try {
//			List<Integer> numList = mytitanService.getSelectNumberByGroup(groupId);
//			Iterator<Integer> iter = numList.iterator();
//			StringBuffer sbTen = new StringBuffer();
//			StringBuffer sbDig = new StringBuffer();
//			while(iter.hasNext()){
//				int num = iter.next();
//				sbTen.append(this.getTens(num));
//				sbTen.append(",");
//				sbDig.append(this.getDigits(num));
//				sbDig.append(",");
//			}
//			tensNum = sbTen.toString();
//			tensNum = tensNum.substring(0, tensNum.length() - 1);
//			digitsNum = sbDig.toString();
//			digitsNum = digitsNum.substring(0, digitsNum.length() - 1);
//		} catch (BaseException e) {
//			e.printStackTrace();
//		}
//		return SUCCESS;
//	}
	
	public String gambleConsume(){
		User user = UserSessionUtil.getUser();
		int groupId = user.getGroupId();
		try {
			List<Integer> numList = mytitanService.getSelectNumberByGroup(groupId);
			if(null == numList){
				selectedNumbers = "0";
				return SUCCESS;
			}
			Iterator<Integer> iter = numList.iterator();
			StringBuffer numsb = new StringBuffer();
			while(iter.hasNext()){
				int num = iter.next();
				numsb.append(num);
				numsb.append(",");
			}
			selectedNumbers = numsb.toString();
			selectedNumbers = selectedNumbers.substring(0, selectedNumbers.length() - 1);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String firing(){
		try {
			Consumption c = new Consumption();
			c.setType(3);
			c.setStatus(TitanContent.CONSUME_SUBMITTED);
			consumeList = mytitanService.getConsumptionByBean(c);
			Iterator<Consumption> iter = consumeList.iterator();
			User u = mytitanService.getGambleWinner(Integer.valueOf(selectedNum));
			winnerName = u.getUserCode();
			String goodsName = "";
			while(iter.hasNext()){
				Consumption consume = iter.next();
				consume.setStatus(TitanContent.CONSUME_FINISHED);
				mytitanService.saveConsume(consume);
				if("".equals(goodsName)){
					goodsName = consume.getGoodsName();
				}
			}
			Winner winner = new Winner();
			winner.setGoodsName(goodsName);
			winner.setStatus(TitanContent.CONSUME_SUBMITTED);
			winner.setCreateDate(DateUtils.getCurrentDate());
			winner.setUserId(u.getUserId());
			mytitanService.saveWinner(winner);
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} 
		return SUCCESS;
	}
	
	/**
	 * This is for My Titan -> My Consume
	 * @return
	 */
	public String goMyConsumeList(){
		Consumption c = new Consumption();
		c.setUserId(UserSessionUtil.getUser().getUserId());
		mypoint = UserSessionUtil.getPoint();
		inboxCount = String.valueOf(UserSessionUtil.getInbox());
		try {
			consumeList = mytitanService.getConsumptionByBean(c);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String cancellConsum(){
		try{
			Consumption c = new Consumption();			
		    if(this.consumeId != null){
		    	c.setConsumeId(this.consumeId);
		    	consumeList = mytitanService.getConsumptionByBean(c);
		    	if(null != consumeList && consumeList.size() != 0 ){
		    	    c = consumeList.get(0);
		    	    c.setIsValid(1);
		    	    c.setSelectedNum(null);
			        mytitanService.saveConsume(c);
			        Point pt = mytitanService.getPointByUserId(c.getUserId());
			        Integer totalPoint = pt.getPoints() + c.getPrice();
			        this.savePointHistory(c.getConsumeId(), "refund", c.getUserId(), c.getPrice(), totalPoint);
			        pt.setPoints(totalPoint);
				    mytitanService.savePoint(pt);
				    UserSessionUtil.setPoint(pt.getPoints());
		    	}
		    }
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * This is for consumption report
	 * @return
	 */
	public String comsumptionCount(){
		try {
			ccList = getCClist();
			json = JSONArray.fromObject(ccList);
			navigation += "->Consumption report</div>";
		} catch (BaseException e) {
			e.printStackTrace(); 
		}
		return SUCCESS;
	}
	
	private List<Consumption> getCClist() throws BaseException{
		User user = UserSessionUtil.getUser();
		Map<String, String> authMap = user.getAuthMap();
		if(null != authMap && authMap.containsKey("BTN_MYORDER")){
			isManager = "Yes";
		}
		else{
			isManager = "No";
		}
		int groupId = user.getGroupId();
		return mytitanService.comsumptionCount(groupId);
	}
	
	public void getChartXml(){
		PrintWriter out = null;
		JSONArray json = new JSONArray();
		try {
			ccList = this.getCClist();
		} catch (BaseException e1) {
			e1.printStackTrace();
		}
		
		json = JSONArray.fromObject(ccList);
		try {
			out = this.getResponse().getWriter();
			out.print(json);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	public void calculatePoint(){
		PrintWriter out = null;
		try {
			ccList = this.getCClist();
			User u = UserSessionUtil.getUser();
			EmailManageAction email = new EmailManageAction();
			String content = this.generateConcent(ccList, rcPoint);
			email.sendEmail("Recognition Points", content, u.getMail().replace("hp.com", "hpe.com"));
		} catch (BaseException e1) {
			e1.printStackTrace();
		}
		
		try {
			out = this.getResponse().getWriter();
			out.print("The result has been sent to your email!");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	private String generateConcent(List<Consumption> ccList, String rcPoint){
		
		StringBuffer s = new StringBuffer();
		s.append("<div id=\"roll\"><h3>Recognition Points:</h3><br>");
		if(ccList.size() > 0){
			int totalPoint = this.getTotalPoint(ccList);
			s.append("<table cellspacing=\"0px\" border=\"1\"><tr align=\"center\"><th width=\"30%\">Name</th><th width=\"30%\">Percentage</th><th width=\"30%\">Actual</th></tr>");
			Iterator<Consumption> iter = ccList.iterator();
			while(iter.hasNext()){
				Consumption cc = iter.next();
				s.append("<tr align=\"center\"><td>");
				s.append(cc.getGoodsName());
				s.append("</td><td>");
				s.append(Math.round(cc.getPrice() * 100 / totalPoint));
				s.append("%</td><td>");
				if(totalPoint <= Integer.valueOf(rcPoint)){
					s.append(cc.getPrice());
				}
				else{
					s.append(Integer.valueOf(rcPoint) * cc.getPrice()/totalPoint);
				}
				s.append("</td></tr>");
			}
			s.append("</table>");
		}
		s.append("</div>");
		return s.toString();
	}
	
	private int getTotalPoint(List<Consumption> ccList){
		int total = 0;
		Iterator<Consumption> iter = ccList.iterator();
		while(iter.hasNext()){
			Consumption cc = iter.next();
			total += cc.getPrice();
		}
		return total;
	}
	
	public String goAllConsumeList(){
		User user = UserSessionUtil.getUser();
		int groupId = user.getGroupId();
		mypoint = UserSessionUtil.getPoint();
		inboxCount = String.valueOf(UserSessionUtil.getInbox());
		try {
			consumeList = mytitanService.getAllConsumptionByGroup(groupId);
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String completeConsume(){
		Consumption bean = new Consumption();
		bean.setConsumeId(consumeId);
		try {
			List<Consumption> cList = mytitanService.getConsumptionByBean(bean);
			Consumption c = cList.get(0);
			c.setStatus(TitanContent.CONSUME_FINISHED);
			mytitanService.saveConsume(c);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	public String goAllWinnerList(){
		try {
			Winner w = new Winner();
			winnerList = mytitanService.getWinnersByBeam(w);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String completeWinner(){
		Winner w = new Winner();
		w.setWinnerId(winnerId);
		try {
			winnerList = mytitanService.getWinnersByBeam(w);
			Winner winner = winnerList.get(0);
			winner.setStatus(TitanContent.CONSUME_FINISHED);
			winner.setCreateDate(DateUtils.convertStringToDate(winner.getStrcreateDate()));
			mytitanService.saveWinner(winner);
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public void addPoint(){
		PrintWriter out = null;
		try {
			User u = UserSessionUtil.getUser();
			Point p = mytitanService.getPointByUserId(u.getUserId());
			Integer currentPoint = p.getPoints() + Integer.valueOf(tensNum);
			p.setPoints(currentPoint);
			p.setLastUpdateDate(DateUtils.getCurrentDate());
			mytitanService.savePoint(p);
			
			InBox inbox = new InBox();
			inbox.setMsgId(messageId);
			List<InBox> inboxList = mytitanService.getInBoxByBean(inbox);
			inbox = new InBox();
			inbox = inboxList.get(0);
			inbox.setIsReaded(TitanContent.INBOX_READED_YES);
			mytitanService.saveInbox(inbox);
			this.savePointHistory(u.getUserId().toString(), "Treasure Box",  u.getUserId(), Integer.valueOf(tensNum), currentPoint);
			UserSessionUtil.setPoint(currentPoint);
			UserSessionUtil.setInbox(UserSessionUtil.getInbox() - 1);
			
		} catch (BaseException e1) {
			e1.printStackTrace();
		}
		
		try {
			out = this.getResponse().getWriter();
			out.print("The result has been sent to your email!");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	
//	private int getTens(int num){
//		return num/10%10; 
//	}
//	
//	private int getDigits(int num ){
//		return num%10;
//	}
	
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

	public String getDefaultProjectId() {
		return defaultProjectId;
	}

	public void setDefaultProjectId(String defaultProjectId) {
		this.defaultProjectId = defaultProjectId;
	}

	public TestcaseService getTestcaseService() {
		return testcaseService;
	}

	public void setTestcaseService(TestcaseService testcaseService) {
		this.testcaseService = testcaseService;
	}

	public List<DefectInfo> getMysubitteddefectList() {
		return mysubitteddefectList;
	}

	public void setMysubitteddefectList(List<DefectInfo> mysubitteddefectList) {
		this.mysubitteddefectList = mysubitteddefectList;
	}

	public List<DefectInfo> getMyowneddefectList() {
		return myowneddefectList;
	}

	public void setMyowneddefectList(List<DefectInfo> myowneddefectList) {
		this.myowneddefectList = myowneddefectList;
	}
	
	public String inboxList(){
		User user = UserSessionUtil.getUser();
		InBox inbox = new InBox();
		inbox.setUserId(user.getUserId());
		mypoint = UserSessionUtil.getPoint();
		inboxCount = String.valueOf(UserSessionUtil.getInbox());
		try {
			inboxList = mytitanService.getInBoxByBean(inbox);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String openTreasureBox(){
		return SUCCESS;
	}

	public String getRallyUrl() {
		return rallyUrl;
	}

	public void setRallyUrl(String rallyUrl) {
		this.rallyUrl = rallyUrl;
	}

	public MyTitanService getMytitanService() {
		return mytitanService;
	}

	public void setMytitanService(MyTitanService mytitanService) {
		this.mytitanService = mytitanService;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getReportContent() {
		return reportContent;
	}

	public void setReportContent(String reportContent) {
		this.reportContent = reportContent;
	}

	public String getJsonObj() {
		return jsonObj;
	}

	public void setJsonObj(String jsonObj) {
		this.jsonObj = jsonObj;
	}

	public String getSendTo() {
		return sendTo;
	}

	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}

	public List<WeeklyReport> getWeeklyreportList() {
		return weeklyreportList;
	}

	public void setWeeklyreportList(List<WeeklyReport> weeklyreportList) {
		this.weeklyreportList = weeklyreportList;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<UserstoryInfo> getUsInfoList() {
		return usInfoList;
	}

	public void setUsInfoList(List<UserstoryInfo> usInfoList) {
		this.usInfoList = usInfoList;
	}

	public String getReportaryName() {
		return reportaryName;
	}

	public void setReportaryName(String reportaryName) {
		this.reportaryName = reportaryName;
	}

	public String getUsNum() {
		return usNum;
	}

	public void setUsNum(String usNum) {
		this.usNum = usNum;
	}

	public List<MyCommitVo> getCommitList() {
		return commitList;
	}

	public void setCommitList(List<MyCommitVo> commitList) {
		this.commitList = commitList;
	}

	public String getReportaryId() {
		return reportaryId;
	}

	public void setReportaryId(String reportaryId) {
		this.reportaryId = reportaryId;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

//	public ProjectService getProjectService() {
//		return projectService;
//	}
//
//	public void setProjectService(ProjectService projectService) {
//		this.projectService = projectService;
//	}

	public String getCommitId() {
		return commitId;
	}

	public void setCommitId(String commitId) {
		this.commitId = commitId;
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

	public StrongFieldReportVo getStrongFieldReportVo() {
		return strongFieldReportVo;
	}

	public void setStrongFieldReportVo(StrongFieldReportVo strongFieldReportVo) {
		this.strongFieldReportVo = strongFieldReportVo;
	}

	public String getSelectedsprintId() {
		return selectedsprintId;
	}

	public void setSelectedsprintId(String selectedsprintId) {
		this.selectedsprintId = selectedsprintId;
	}

	public String getSprintId() {
		return sprintId;
	}

	public void setSprintId(String sprintId) {
		this.sprintId = sprintId;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public List<PointHist> getPhList() {
		return phList;
	}

	public void setPhList(List<PointHist> phList) {
		this.phList = phList;
	}

	public Integer getMypoint() {
		return mypoint;
	}

	public void setMypoint(Integer mypoint) {
		this.mypoint = mypoint;
	}
	
	
	public Integer getMyAwardPoint() {
		return myAwardPoint;
	}

	public void setMyAwardPoint(Integer myAwardPoint) {
		this.myAwardPoint = myAwardPoint;
	}
	
	public List<PointHist> getAphList() {
		return aphList;
	}

	public void setAphList(List<PointHist> aphList) {
		this.aphList = aphList;
	}

	public String getPageTable1_length() {
		return pageTable1_length;
	}

	public void setPageTable1_length(String pageTable1Length) {
		pageTable1_length = pageTable1Length;
	}

	public String getPageTable2_length() {
		return pageTable2_length;
	}

	public void setPageTable2_length(String pageTable2Length) {
		pageTable2_length = pageTable2Length;
	}

	public List<Goods> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<Goods> goodsList) {
		this.goodsList = goodsList;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public List<Consumption> getConsumeList() {
		return consumeList;
	}

	public void setConsumeList(List<Consumption> consumeList) {
		this.consumeList = consumeList;
	}

	public String getConsumeId() {
		return consumeId;
	}

	public void setConsumeId(String consumeId) {
		this.consumeId = consumeId;
	}

	public JSONArray getJson() {
		return json;
	}

	public void setJson(JSONArray json) {
		this.json = json;
	}

	public List<Consumption> getCcList() {
		return ccList;
	}

	public void setCcList(List<Consumption> ccList) {
		this.ccList = ccList;
	}

	public String getRcPoint() {
		return rcPoint;
	}

	public void setRcPoint(String rcPoint) {
		this.rcPoint = rcPoint;
	}

	public String getIsManager() {
		return isManager;
	}

	public void setIsManager(String isManager) {
		this.isManager = isManager;
	}

	public String get___t() {
		return ___t;
	}

	public void set___t(String t) {
		___t = t;
	}

	public String getSelectedNum() {
		return selectedNum;
	}

	public void setSelectedNum(String selectedNum) {
		this.selectedNum = selectedNum;
	}

	public String getSelectedNumbers() {
		return selectedNumbers;
	}

	public void setSelectedNumbers(String selectedNumbers) {
		this.selectedNumbers = selectedNumbers;
	}

	public String getTensNum() {
		return tensNum;
	}

	public void setTensNum(String tensNum) {
		this.tensNum = tensNum;
	}

	public String getDigitsNum() {
		return digitsNum;
	}

	public void setDigitsNum(String digitsNum) {
		this.digitsNum = digitsNum;
	}

	public String getWinnerName() {
		return winnerName;
	}

	public void setWinnerName(String winnerName) {
		this.winnerName = winnerName;
	}

	public List<InBox> getInboxList() {
		return inboxList;
	}

	public void setInboxList(List<InBox> inboxList) {
		this.inboxList = inboxList;
	}

	public List<Winner> getWinnerList() {
		return winnerList;
	}

	public void setWinnerList(List<Winner> winnerList) {
		this.winnerList = winnerList;
	}

	public String getWinnerId() {
		return winnerId;
	}

	public void setWinnerId(String winnerId) {
		this.winnerId = winnerId;
	}

	public String getInboxCount() {
		return inboxCount;
	}

	public void setInboxCount(String inboxCount) {
		this.inboxCount = inboxCount;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
}
