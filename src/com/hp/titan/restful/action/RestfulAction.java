package com.hp.titan.restful.action;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.neoremind.sshxcute.core.ConnBean;
import net.neoremind.sshxcute.core.IOptionName;
import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hp.app.common.util.DateUtils;
import com.hp.app.common.util.JsonUtil;
import com.hp.app.common.util.SSHExecClient;
import com.hp.app.config.SysConfiger;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.auth.model.Auth;
import com.hp.titan.auth.model.Role;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.service.UserService;
import com.hp.titan.common.constants.TitanContent;
import com.hp.titan.common.util.UserSessionUtil;
import com.hp.titan.notify.EmailManageAction;
import com.hp.titan.project.model.DedicatedServer;
import com.hp.titan.project.service.ProjectService;
import com.hp.titan.project.vo.ReportVo;
import com.hp.titan.server.model.Server;
import com.hp.titan.server.service.ServerService;
import com.hp.titan.test.model.DefaultParameter;
import com.hp.titan.test.model.Parameter;
import com.hp.titan.test.model.ParameterData;
import com.hp.titan.test.model.RunCase;
import com.hp.titan.test.model.RunCaseServer;
import com.hp.titan.test.model.RunJob;
import com.hp.titan.test.model.Testcase;
import com.hp.titan.test.model.Testjob;
import com.hp.titan.test.model.Testplan;
import com.hp.titan.test.service.TestcaseService;
import com.hp.titan.test.service.TestjobService;
import com.hp.titan.test.service.TestplanService;
import com.jcraft.jsch.JSchException;

@Controller
@RequestMapping("/restful")
@Component
public class RestfulAction{
	private UserService userService;
	private TestjobService testjobService; 
	private TestplanService testplanService;
	private TestcaseService testcaseService;
	private ProjectService projectService;
	private ServerService serverService;
	private ReportVo reportVo;
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request ,HttpServletResponse response){
		request.setAttribute("message", "Hello,This is a example of Spring3 RESTful!");
		return null;
	}
	
	/**
	 * restful/jenkins/create/{code}
	 * @param job_code
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/jenkins/create/{job_code}",method=RequestMethod.GET)
	public String createRunTestJob(@PathVariable String job_code,HttpServletRequest request ,HttpServletResponse response) throws IOException{
		User user = null;
		try {
			user = userService.getUserByCode("jenkins");
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, String> authMap = new HashMap<String, String>();
		Iterator roleIter = user.getUserRoles().iterator();
		while (roleIter.hasNext()) {
			Role role = (Role) roleIter.next();
			Iterator authIter = role.getAuths().iterator();
			while (authIter.hasNext()) {
				Auth auth = (Auth) authIter.next();
				authMap.put(auth.getAuthName(), auth.getAuthName());
			}

		}
		user.setAuthMap(authMap);
		user.setLoginTime(new Date());
		UserSessionUtil.setUser(user);

		String runJobId = "";
		try {
			Testjob tj = testjobService.getTestjobByCode(job_code);
			if(null != tj){
				String testjobId = tj.getTestjobId();
				if(checkIsTestJobRunning(testjobId)){
					response.getWriter().write("Test job is running! ");
					return null;
				}
				runJobId = this.doCreateRunJob(testjobId);
				this.createAllRunCases(testjobId, runJobId);
//				String paraJson = this.getJenkinsPara();
//				this.doCaseInBack(testjobId, runJobId, paraJson);
			}
			else{
				response.getWriter().write("Test job code error! ");
				return null;
			}
		} catch (BaseException e) {
			e.printStackTrace();
		}
		
		response.getWriter().write(runJobId);
		//return "index.jsp";
		return null;
	}
	
	/**
	 * restful/jenkins/run/{runjobId}
	 * @param runJobId
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/jenkins/run/{runJobId}",method=RequestMethod.GET)
	public String runTestJob(@PathVariable String runJobId,HttpServletRequest request ,HttpServletResponse response) throws IOException{
		String testjobId = this.getRunJobInfo(runJobId);
		if(null == testjobId){
			response.getWriter().write("No runJob created!");
			return null;
		}
		String paraJson = this.getJenkinsPara(testjobId);
		if(null == paraJson || "[{}]".equals(paraJson)){
			response.getWriter().write("You didn't set all the parameters!");
			return null;
		}
		
		List<String> serverUsedList = this.getUsedServerList(paraJson);
		Iterator<String> iter = serverUsedList.iterator();
		while(iter.hasNext()){
			String serverId = iter.next();
			Server s;
			try {
				s = serverService.getServerById(serverId);
				s.setStatus("used");
				serverService.doSaveServer(s);
			} catch (BaseDaoException e) {
				e.printStackTrace();
			}
		}
		
		this.doCaseInBack(testjobId, runJobId, paraJson);
		response.getWriter().write(runJobId);
		return null;
	}
	
	/**
	 * restful/jenkins/result/{runjobId}
	 * @param testjobId
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/jenkins/result/{runJobId}",method=RequestMethod.GET)
	public String runTestJobResult(@PathVariable String runJobId,HttpServletRequest request ,HttpServletResponse response) throws IOException{
		String testjobId = this.getRunJobInfo(runJobId);
		if(null == testjobId){
			response.getWriter().write("No runJob created");
			return null;
		}
		Testjob tj = null;
		try {
			tj = testjobService.getTestjobById(testjobId);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		if(null != tj){
			response.getWriter().write(tj.getState());
		}
		return null;
	}
	
	/**
	 * Get test job id by runJobId
	 * @param runJobId
	 * @return
	 */
	public String getRunJobInfo(String runJobId){
		String testjobId = null;
		try {
			RunJob rj = testjobService.getRunJobById(runJobId);
			if(null != rj){
				testjobId = rj.getJobId();
			}
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return testjobId;
	}
	
	/**
	 * Check if test job is running
	 * @param testjobId
	 * @return
	 */
	public Boolean checkIsTestJobRunning(String testjobId){
		Boolean isRun = false;
		try {
			Testjob tj = testjobService.getTestjobById(testjobId);
			if(TitanContent.TEST_JOB_STATUS_RUNNING.equals(tj.getState()) || TitanContent.TEST_JOB_STATUS_PENDING.equals(tj.getState())){
				isRun = true;
			}else{
				isRun = false;
			}
		} catch (BaseException e) {
			e.printStackTrace();
		} 
		return isRun;
	}
	
	/**
	 * create run test job
	 * @param testjobId
	 * @return run job id
	 */
	public String doCreateRunJob(String testjobId){
		String runJobId = "";
		try {
			RunJob rj = new RunJob();
			rj.setJobId(testjobId);
			rj.setStartTime(DateUtils.getCurrentDate());
			rj.setCreateDate(DateUtils.getCurrentDate());
			rj.setCreateUserId(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
			rj.setIsValid(0);
			testjobService.saveRunJob(rj);
			
			Testjob tj = testjobService.getTestjobById(testjobId);
			tj.setState(TitanContent.TEST_JOB_STATUS_RUNNING);
			testjobService.saveTestjob(tj);
			runJobId = rj.getRunJobId();
		} catch (BaseException e) {
			e.printStackTrace();
		} 
		return runJobId;
	}
	
	/**
	 * Create all the test cases
	 * @param testjobId
	 * @param runJobId
	 */
	public void createAllRunCases(String testjobId, String runJobId){
		try {
			RunCase rc = null;
			List<Testplan> testplanList = testjobService.getTestplanByJobId(testjobId);
			List<Testcase>testcaseList = testjobService.getTestcaseByJobId(testjobId);
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
	
	/**
	 * Get para data in json
	 * @return para json data
	 */
	public String getJenkinsPara(String testjobId){
		StringBuffer s = new StringBuffer();
		s.append("[{");
		List<DefaultParameter> defaultParaList = new ArrayList<DefaultParameter>();
		try {
			defaultParaList = testjobService.getDefaultParaList(8, testjobId);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		Iterator<DefaultParameter> it = defaultParaList.iterator();
		while(it.hasNext()){
			DefaultParameter dp = it.next();
			if(null != dp.getDefaultValue() && !"".equals(dp.getDefaultValue())){
				s.append("\"");
				s.append(dp.getId().getParadataName().replace("$", ""));
				s.append("\":\"");
				s.append(dp.getDefaultValue());
				s.append("\",");
			}
		}
		s.append("}]");
		return s.toString();
	}
	
	
	/**
	 * Run test cases
	 * @param runJobId
	 */

	public void doCaseInBack(String testjobId, String runJobId, String paraJson){
		User user = null;
		try {
			user = userService.getUserByCode("jenkins");
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, String> authMap = new HashMap<String, String>();
		Iterator roleIter = user.getUserRoles().iterator();
		while (roleIter.hasNext()) {
			Role role = (Role) roleIter.next();
			Iterator authIter = role.getAuths().iterator();
			while (authIter.hasNext()) {
				Auth auth = (Auth) authIter.next();
				authMap.put(auth.getAuthName(), auth.getAuthName());
			}

		}
		user.setAuthMap(authMap);
		user.setLoginTime(new Date());
		UserSessionUtil.setUser(user);
		String rjRes = TitanContent.RUN_JOB_STATUS_SUCCESS;
		String tjState = TitanContent.TEST_JOB_STATUS_SUCCESS;
		RunJob rj;
		try {
			rj = testjobService.getRunJobById(runJobId);
			Testjob tj = testjobService.getTestjobById(testjobId);
			List<RunCase> rcList = testjobService.getRunCaseByRunjobId(runJobId);
			if(null != rcList && rcList.size() != 0){
				Iterator<RunCase> it = rcList.iterator();
				int i = 1;
				while(it.hasNext()){
					System.out.println(i + "of" + rcList.size());
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
					String cRes = this.doCaseRun(rc, paraJson);
					if(TitanContent.RUN_JOB_STATUS_SUCCESS.equals(rjRes)){
						if("".equals(cRes) || TitanContent.RUN_CASE_STATUS_FAIL.equals(cRes)){
							rjRes = TitanContent.RUN_JOB_STATUS_FAIL;
							tjState = TitanContent.TEST_JOB_STATUS_FAIL;
							System.out.println("finish 1");
						}
					}
				}
				i++;
			}
			rj.setResult(rjRes);
			rj.setEndTime(DateUtils.getCurrentDate());
			rj.setUpdateDate(DateUtils.getCurrentDate());
			rj.setUpdateUserId(Integer.valueOf(user.getUserId()));
			testjobService.saveRunJob(rj);
			tj.setState(tjState);
			tj.setUpdateDate(DateUtils.getCurrentDate());
			tj.setUpdateUserId(Integer.valueOf(user.getUserId()));
			testjobService.saveTestjob(tj);
			
			List<String> serverUsedList = this.getUsedServerList(paraJson);
			Iterator<String> iter = serverUsedList.iterator();
			while(iter.hasNext()){
				String serverId = iter.next();
				Server s;
				try {
					s = serverService.getServerById(serverId);
					s.setStatus("Not used");
					serverService.doSaveServer(s);
				} catch (BaseDaoException e) {
					e.printStackTrace();
				}
			}
			
			//notification
			reportVo = this.getReportVoFromJob(runJobId);
			EmailManageAction email = new EmailManageAction();
			String title = tj.getTestjobName() + " - Test job #" + tj.getTestjobCode() + "-" +  tj.getState();
			if(tjState.equals(TitanContent.TEST_JOB_STATUS_FAIL)||tjState.equals(TitanContent.TEST_JOB_STATUS_SUCCESS)||tjState.equals(TitanContent.TEST_JOB_STATUS_ABORT)){
			   email.sendEmail(title, this.createEmailContext(title,reportVo), user.getMail());
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
	
	public String doCaseRun(RunCase runCase, String paraJson){
		SSHExecClient ssh = null;
		String caseRes = "";
		// para info
		List paraMapList = null;
		if(null != paraJson && !"[{}]".equals(paraJson)){
			paraMapList = (ArrayList)JsonUtil.JsonToList(JsonUtil.getJsonArrayFromString(paraJson.replace(",}", "}")));
		}
		Map paraMap = new HashMap();
        if(null != paraMapList){
        	paraMap = (Map)paraMapList.get(0);
        }
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
            // ssh run
            runCase.setStartTime(DateUtils.getCurrentDate());
            runCase.setState(TitanContent.RUN_CASE_STATUS_RUNNING);
            testjobService.saveRunCase(runCase);
            
            ConnBean cb = new ConnBean(serverInfo.getServerIp(), serverInfo.getServerAccount(), serverInfo.getServerPasswd());
			CustomTask sampleTask = new ExecCommand(command );
			Result res = new Result();
			try {
				SSHExecClient.setOption(IOptionName.TIMEOUT, 10*120*1000l);
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
			runCase.setState(caseRes);
			runCase.setCommand(command);
			runCase.setEndTime(DateUtils.getCurrentDate());
            testjobService.saveRunCase(runCase);
            // save log
            if(null != res.sysout && !"".equals(res.sysout)){
            	this.saveRunLog(SysConfiger.getProperty("file.log.path") + runCase.getRunJobId(), runCase.getCaseId(), res.sysout);
            }
            else if(null != res.sysout && !"".equals(res.error_msg)){
            	this.saveRunLog(SysConfiger.getProperty("file.log.path") + runCase.getRunJobId(), runCase.getCaseId(), res.error_msg);
            }
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return caseRes;
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

	public TestjobService getTestjobService() {
		return testjobService;
	}

	@Resource(name="testjobService")
	public void setTestjobService(TestjobService testjobService) {
		this.testjobService = testjobService;
	}

	public UserService getUserService() {
		return userService;
	}

	@Resource(name="userService")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public TestplanService getTestplanService() {
		return testplanService;
	}

	@Resource(name="testplanService")
	public void setTestplanService(TestplanService testplanService) {
		this.testplanService = testplanService;
	}

	public TestcaseService getTestcaseService() {
		return testcaseService;
	}

	@Resource(name="testcaseService")
	public void setTestcaseService(TestcaseService testcaseService) {
		this.testcaseService = testcaseService;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name="projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public ServerService getServerService() {
		return serverService;
	}

	@Resource(name="serverService")
	public void setServerService(ServerService serverService) {
		this.serverService = serverService;
	}

	public ReportVo getReportVo() {
		return reportVo;
	}

	public void setReportVo(ReportVo reportVo) {
		this.reportVo = reportVo;
	}
	
}

