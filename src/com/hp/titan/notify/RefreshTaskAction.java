package com.hp.titan.notify;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.context.WebApplicationContext;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.hp.app.common.util.DateUtils;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.service.UserService;
import com.hp.titan.common.constants.TitanContent;
import com.hp.titan.common.util.SVNDiff;
import com.hp.titan.common.vo.DefectVo;
import com.hp.titan.common.vo.LoginUserVo;
import com.hp.titan.common.vo.UserstoryVo;
import com.hp.titan.mytitan.model.CommitPath;
import com.hp.titan.mytitan.model.CommitReport;
import com.hp.titan.mytitan.model.ExpAccount;
import com.hp.titan.mytitan.model.ExpLog;
import com.hp.titan.mytitan.model.Point;
import com.hp.titan.mytitan.model.PointHist;
import com.hp.titan.mytitan.model.ProjectRallyQuix;
import com.hp.titan.mytitan.model.Reportary;
import com.hp.titan.mytitan.model.UserstoryInfo;
import com.hp.titan.mytitan.service.MyTitanService;
import com.hp.titan.project.model.CommitusDefect;
import com.hp.titan.project.model.CommitusDefectId;
import com.hp.titan.project.service.ProjectService;
import com.hp.titan.server.model.Reservation;
import com.hp.titan.server.model.Server;
import com.hp.titan.test.model.DefectInfo;
import com.hp.titan.test.service.TestcaseService;

public class RefreshTaskAction extends TimerTask {
	private ProjectService projectService;
	private TestcaseService testcaseService;
	private MyTitanService mytitanService;
	private UserService userService;

	public RefreshTaskAction(WebApplicationContext ctx) {
		projectService = (ProjectService) ctx.getBean("projectService");
		testcaseService = (TestcaseService) ctx.getBean("testcaseService");
		 mytitanService = (MyTitanService) ctx.getBean("mytitanService");
		 userService = (UserService) ctx.getBean("userService");
	}

	public void run() {
		try {
			
            // Award points
		    
		    Calendar c = Calendar.getInstance();
		    int day = c.get(Calendar.DAY_OF_MONTH);
		    if(day == 1){
		    	Integer exPoint = 20;
				mytitanService.chargeExpint(exPoint);
		    }
		    
		    // Reminder for expired points
		    
		    EmailManageAction email = new EmailManageAction();
		    int reDays = c.getActualMaximum(Calendar.DATE) - day;
		    int month = c.get(Calendar.MONTH)+1;
		    int year = c.get(Calendar.YEAR);
		    if(month == 11){
		    	month = 1;
		    	year += 1;
		    }else{
		    	month += 1;
		    }
		    String dateInfo = year + "-" + month + "-" + "1";
		     
		    
			if(reDays == 7 || reDays == 1){
		    	List<Point> pointList = mytitanService.getAllPoint();
				if(pointList!=null&&pointList.size()!=0){
					for(Point pt : pointList){
						if(pt.getExPoint()>0){
							String notifyto = userService.getUserById(pt.getUserId()).getMail();
							String title = "Your award points are going to be expired";
							String userName = userService.getUserById(pt.getUserId()).getUserCode();
							email.sendEmail(title, this.createEmailContextForPoint(title, userName, pt, dateInfo), notifyto);	
						}
					}
				}
		    }
		
			// SVN
//			List<Reportary> reportaryList = projectService.getAllReportary();
//			Iterator<Reportary> iter = reportaryList.iterator();
//			while(iter.hasNext()){
//				Reportary r = iter.next();
//				if("1".equals(r.getAutoRefresh())){
//					this.syncCommit(r.getReportaryId());
//				}
//			}
			
			// Rallly defects
//			Set<String> projectNameSet = new HashSet<String>();
//			List<ProjectRallyQuix> projectRallyList = projectService.getProjectRallyQuixByType("rally");
//			Iterator<ProjectRallyQuix> iter1 = projectRallyList.iterator();
//			while(iter1.hasNext()){
//				ProjectRallyQuix pr = iter1.next();
//				if("1".equals(pr.getAutoRefresh())){
//					if(!projectNameSet.contains(pr.getNameinRally())){
//						projectNameSet.add(pr.getNameinRally());
//					}
//					this.syncRallyDefect(pr.getNameinRally(), pr.getComponent());
//				}
//			}
			
			// Rally user stories
//			Iterator<String> iter3 = projectNameSet.iterator();
//			while(iter3.hasNext()){
//				String projectName = iter3.next();
//				this.syncRallyUs(projectName);
//			}
			
			// Quix
			List<ProjectRallyQuix> projectQuixList = projectService.getProjectRallyQuixByType("quix");
			Iterator<ProjectRallyQuix> iter2 = projectQuixList.iterator();
			while(iter2.hasNext()){
				ProjectRallyQuix pq = iter2.next();
				if("1".equals(pq.getAutoRefresh())){
					this.syncQuix(pq.getNameinQuix(), pq.getComponent());
				}
			}
			
			// points
		    this.syncPoint();
			
		} catch (BaseException e) {
			e.printStackTrace();
		}
	}
	
	public void syncCommit(String reportaryId){
		try {
			Reportary reportary = projectService.getReportaryByReportaryId(reportaryId);
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
				cr.setComment(current.getMessage());
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
				String comment = cr.getComment();
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
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (SVNException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void syncRallyDefect(String projectnameinRally, String component){
		try {
			List<DefectVo> dvo = projectService.getRallyDefectInfoByProjectModule(projectnameinRally, component);
			if(null != dvo && dvo.size() != 0){
				this.saveDefectInfo(dvo);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void syncRallyUs(String projectnameinRally){
		try {
			List<UserstoryVo> usvo = projectService.getRallyUSInofoByPorject(projectnameinRally);
			if(null != usvo && usvo.size() != 0){
				this.saveUSInfo(usvo);
			}
		} catch (IOException e) {
			e.printStackTrace();
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
					di.setSubmittEmail(defect.getSubmittedBy().replace("@hp.com", "@hpe.com"));
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
				if(null != defect.getDeveloper()){
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
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void syncQuix(String projectnameinQuix, String component){
		try {
			List<DefectVo> dvo = projectService.getQuixDefectInfoByProjectModule(projectnameinQuix, component);
			this.saveDefectInfo(dvo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void syncPoint(){
		try {
			List<User> userList = userService.getAllUserListForSync();
			Iterator<User> l = userList.iterator();
			while(l.hasNext()){
				User u = l.next();
				this.getTodayPointFare(u);
			}
		} catch (BaseException e) {
			e.printStackTrace();
		}
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
	
	private String createEmailContextForPoint(String title, String userName, Point pt, String dateInfo){
		StringBuffer sb = new StringBuffer();
		sb.append("Dear " + userName + ",");
		sb.append("<br>");
		sb.append("<br>");
		sb.append("you have " + pt.getExPoint() +  " award points will be expired on " + dateInfo + ".");
		sb.append("<br>");
		sb.append("<br>");
		sb.append("You can login " + TitanContent.TITAN_URL + " to use your award points.");
		sb.append("<br>");
		sb.append("<br>");
		sb.append("From Titan team." );
		return sb.toString();
	}
	
	
//	public void syncPoint(){
//		try {
// 			Calendar calendar = Calendar.getInstance();   			
// 			calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)-1);
// 			System.out.println(calendar.get(Calendar.DATE));
//			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");   
// 			List<UserstoryVo> usInfoList = mytitanService.getUserStoriesList("Accepted",calendar);
//			if(null != usInfoList && usInfoList.size()!=0){
//				for(UserstoryVo usinfo:usInfoList){
//					PointHist ph = new PointHist();
//					ph.setPointFare(5);
//					Point pt = mytitanService.getPointByEmail(usinfo.getOwnerEmail());
//					if(null != pt){
//						ph.setPointTotal(5+pt.getPoints());
//						pt.setPoints(5+pt.getPoints());
//					    mytitanService.savePoint(pt);
//					}
//				    ph.setType("User Story");
//				    ph.setObjectId(usinfo.getObjectId());
//				    ph.setOwnerEmail(usinfo.getOwnerEmail());
//				    ph.setDetail(usinfo.getUsName());
//				    
//				    ph.setCreateDate(df.parse(usinfo.getAcceptedDate()));
//				    ph.setIsValid(0);
//				    mytitanService.savePointHist(ph);
//				}
//			}
//			List<DefectInfo> sdefectList = testcaseService.getDefectListByState("xxxx");
//			
//			if(null != sdefectList && sdefectList.size() != 0){
//				for(DefectInfo di: sdefectList){
//					PointHist ph = new PointHist();
////save point info for QA;
//					ph.setPointFare(5);
//					Point pt = mytitanService.getPointByEmail(di.getSubmittEmail());
//					if(null != pt){
//						ph.setPointTotal(5+pt.getPoints());
//						pt.setPoints(5+pt.getPoints());
//					    mytitanService.savePoint(pt);
//						}
//					ph.setType("Submit Defect");
//					ph.setObjectId(di.getObjectId());
//					ph.setOwnerEmail(di.getSubmittEmail());
//					ph.setDetail(di.getDefectName());
//					if(null != di.getSubmitDate())    ph.setCreateDate(di.getSubmitDate());
//					else ph.setCreateDate(di.getLastupdateDate());
//					ph.setIsValid(0);
//					mytitanService.savePointHist(ph);
//					
////save point info for developer
//					PointHist ph1 = new PointHist();
//					ph1.setPointFare(5);
//					Point pt1 = mytitanService.getPointByEmail(di.getDeveloperEmail());
//					if(null != pt1 ){
//						ph.setPointTotal(5+pt1.getPoints());
//						pt1.setPoints(5+pt1.getPoints());
//					    mytitanService.savePoint(pt1);
//					}
//					ph1.setType("Fix Defect");
//					ph1.setObjectId(di.getObjectId());
//					ph1.setOwnerEmail(di.getDeveloperEmail());
//					ph1.setDetail(di.getDefectName());
//					ph1.setCreateDate(di.getLastupdateDate());
//					ph1.setIsValid(0);
//					mytitanService.savePointHist(ph1);
//				}
//			}
//			
//		} catch (BaseException e) {
//			e.printStackTrace();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		} 
//	}
	
}
