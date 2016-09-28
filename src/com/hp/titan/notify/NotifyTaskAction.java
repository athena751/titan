package com.hp.titan.notify;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.hp.app.common.util.DateUtils;
import java.util.List;
import java.util.TimerTask;

import org.springframework.expression.ParseException;
import org.springframework.web.context.WebApplicationContext;

import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.service.UserService;
import com.hp.titan.common.constants.TitanContent;
import com.hp.titan.common.util.UserSessionUtil;
import com.hp.titan.project.service.ProjectService;
import com.hp.titan.project.vo.ReportVo;
import com.hp.titan.server.model.ReserHist;
import com.hp.titan.server.model.Reservation;
import com.hp.titan.server.model.Server;
import com.hp.titan.server.service.ServerService;
import com.hp.titan.sprint.model.Sprint;
import com.hp.titan.sprint.service.SprintService;
import com.hp.titan.test.model.RunJob;
import com.hp.titan.test.service.TestjobService;
import com.hp.titan.test.vo.TestjobVo;

public class NotifyTaskAction extends TimerTask {
	private SprintService sprintService;
	private TestjobService testjobService;
	private ReportVo reportVo;
	private TestjobVo testjobVo;
	public List<TestjobVo> testjobVoList;
	private List<RunJob> runjobList;
	private UserService userService;
	private ProjectService projectService;
	private ServerService serverService;
	private Server server;
	

	public NotifyTaskAction(WebApplicationContext ctx) {
		sprintService = (SprintService) ctx.getBean("sprintService");
		testjobService = (TestjobService) ctx.getBean("testjobService");
		userService = (UserService) ctx.getBean("userService");
		projectService = (ProjectService) ctx.getBean("projectService");
		serverService = (ServerService) ctx.getBean("serverService");
		
	}

	public void run() {
		try {
			
			// send sprint report by the end day
			List<Sprint> sprintList = sprintService.getAllSprintList();
			EmailManageAction email = new EmailManageAction();
			if(sprintList!=null&&sprintList.size()!=0){
				for(Sprint s : sprintList){
					if(s.getEndDate().toString().substring(0,10).equals(DateUtils.getCurrentDate().toString().substring(0, 10))){						
						reportVo = this.getReportVoFromProject(s.getId().getProjectId(), s.getId().getSprintId());
						String projectName = projectService.getProjectById(s.getId().getProjectId()).getProjectName();
						String notifyto = projectService.getProjectById(s.getId().getProjectId()).getProjectManger().getMail();
						String title = " Test Report: " + s.getSprintName() + " of project " + projectName ;
//						email.sendEmail(title, this.createEmailContext(title,reportVo), userService.getUserById(s.getCreateUser()).getMail());	
						email.sendEmail(title, this.createEmailContext(title,reportVo), notifyto);
					}
				}
			}
			
			//reservation reminder
			List<Reservation> reserveList = serverService.getAllReservation(TitanContent.RESERVATION_TYPE_SERVER);
			if(reserveList!=null&&reserveList.size()!=0){
				for(Reservation re : reserveList){
//					long da = this.getCompareDate(re.getStartDate().toString(), re.getEndDate().toString());
	                long da = this.getCompareDate(DateUtils.getCurrentDate().toString(), re.getEndDate().toString());
					if(da==1||da==7){
							String serverNotify = userService.getUserById(re.getUserId()).getMail();
							String title = "Expire Reminder";
							Server server = serverService.getServerById(re.getServerId());
							email.sendEmail(title, this.createEmailContextForServer(title, re, server, da), serverNotify);							
					}
					if(da==0||da<0){
						this.releaseServer(re);
					};
								
				}
			}
			
		} catch (BaseException e) {
			e.printStackTrace();
		}
	}
	
	public long getCompareDate(String startDate,String endDate) throws ParseException{
		
		long d = -1;
		try{		
	        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
	        Date date1;		
		    date1 = formatter.parse(startDate);		 
	        Date date2=formatter.parse(endDate);
	        long l = date2.getTime() - date1.getTime();
	        d = l/(24*60*60*1000);  	     
		} catch (java.text.ParseException  e)  {
			e.printStackTrace();			
		}
		 return d;
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
	
	private String createEmailContext(String title, ReportVo reportVo){
		StringBuffer sb = new StringBuffer();
		sb.append(title);
		sb.append("<br>");
		sb.append("<br>");
		sb.append("Test Job Report:");
		sb.append("<br>");
		sb.append("<br>");
		sb.append("<table border='1' width='100%'>");
		sb.append("<tr><td width='10%'>Test job Total</td><td width='10%'>" + reportVo.getTestjobTotal() + "</td><td width='10%'> 100% </td></tr>");
		sb.append("<tr><td>Test job Pass</td><td>" + reportVo.getTestjobPass() + "</td><td>" + reportVo.getPassCent() +"%</td></tr>");
		sb.append("<tr><td>Test job Fail</td><td>" + reportVo.getTestjobFail() + "</td><td>" + reportVo.getFailCent() +"%</td></tr>");
		sb.append("<tr><td>Testjob Active</td><td>" + reportVo.getTestjobActive() + "</td><td>" + reportVo.getActiveCent() +"%</td></tr>");
		sb.append("<tr><td>Testjob Pending</td><td>" + reportVo.getTestjobPending() + "</td><td>" + reportVo.getPendCent() +"%</td></tr>");
		sb.append("<tr><td>Testjob Running</td><td>" + reportVo.getTestjobRunning() + "</td><td>" + reportVo.getRunCent() +"%</td></tr>");
		sb.append("<tr><td>Testjob Abort</td><td>" + reportVo.getTestjobAbort() + "</td><td>" + reportVo.getAbortCent() +"%</td></tr>");
		sb.append("</table>");
		sb.append("<br>");
		sb.append("<br>");
		sb.append("Test Case Report:");
		sb.append("<br>");
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
	
	private String createEmailContextForServer(String title, Reservation re, Server server, long dnum){
		StringBuffer sb = new StringBuffer();
		sb.append(title);
		sb.append("<br>");
		sb.append("<br>");
		sb.append("your reservation " + server.getServerName() + " will be expire on " + re.getEndDate() + ", " + dnum + " days later" );
		sb.append("<br>");
		sb.append("<br>");
		sb.append("Check output at " + TitanContent.TITAN_URL + " to view the details.");
		sb.append("<br>");
		sb.append("<br>");
		sb.append("DO NOT REPLY TO THIS EMAIL." );
		return sb.toString();
	}
	
	private String createEmailContextForAutoRelease(String title, Reservation re, Server server){
		StringBuffer sb = new StringBuffer();
		sb.append(title);
		sb.append("<br>");
		sb.append("<br>");
		sb.append("your reservation " + server.getServerName() + " released by system for expire." );
		sb.append("<br>");
		sb.append("<br>");
		sb.append("Check output at " + TitanContent.TITAN_URL + " to view the details.");
		sb.append("<br>");
		sb.append("<br>");
		sb.append("DO NOT REPLY TO THIS EMAIL." );
		return sb.toString();
	}
	
	public void releaseServer(Reservation reservation){
		
		try{
			reservation.setIsValid(1);
		    serverService.doSaveReservation(reservation);
			// update server status to free
			server = serverService.getServerById(reservation.getServerId());
			server.setServerState(TitanContent.SERVER_STATUS_FREE);
			server.setOwnerId(0);
			serverService.doSaveServer(server);
				
			//save reservation history
			this.saveReserHistory(reservation, TitanContent.SERVER_ACTION_RELEASE);
			
			//send email notify
			EmailManageAction email = new EmailManageAction();
			String title = "Server Release Notify";
			User resUser = userService.getUserById(reservation.getUserId());
			email.sendEmail(title, this.createEmailContextForAutoRelease(title,reservation, server), resUser.getMail());
				 
			}catch (BaseDaoException e) {
				e.printStackTrace();
			}
	}
	

private void saveReserHistory(Reservation reservation, String type){
	
	try{
		 Date currentDate = new Date();
		 ReserHist reserHist = new ReserHist();
		 reserHist.setActionType(type);		 
		 reserHist.setServerId(reservation.getServerId());
		 reserHist.setReserveId(reservation.getReserveId());
		 reserHist.setUserId(0);
		 reserHist.setCreateDate(currentDate);
		 reserHist.setIsValid(0);
		 serverService.doSaveReserHist(reserHist);	
		 
	}catch(BaseDaoException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
  }		
}
	

	public ReportVo getReportVo() {
		return reportVo;
	}

	public void setReportVo(ReportVo reportVo) {
		this.reportVo = reportVo;
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

	public List<RunJob> getRunjobList() {
		return runjobList;
	}

	public void setRunjobList(List<RunJob> runjobList) {
		this.runjobList = runjobList;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}
	
	
	
}
