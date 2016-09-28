package com.hp.titan.report.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;

import com.hp.app.action.DefaultBaseAction;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.model.UserGroup;
import com.hp.titan.auth.service.GroupService;
import com.hp.titan.auth.service.UserService;
import com.hp.titan.auth.vo.UserVo;
import com.hp.titan.common.util.UserSessionUtil;
import com.hp.titan.mytitan.model.ProjectRallyQuix;
import com.hp.titan.mytitan.model.Reportary;
import com.hp.titan.mytitan.vo.CodeReportVo;
import com.hp.titan.mytitan.vo.MyCommitVo;
import com.hp.titan.project.model.Project;
import com.hp.titan.project.service.ProjectService;
import com.hp.titan.project.vo.ReportVo;
import com.hp.titan.report.service.ReportService;
import com.hp.titan.report.vo.ContributionReportVo;
import com.hp.titan.report.vo.DefectReportVo;
import com.hp.titan.report.vo.StrongFieldReportVo;
import com.hp.titan.sprint.model.Sprint;
import com.hp.titan.sprint.model.SprintId;
import com.hp.titan.sprint.service.SprintService;
import com.hp.titan.test.model.DefectInfo;
import com.hp.titan.test.service.TestcaseService;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

public class ReportAction  extends DefaultBaseAction {

	private List<Project> projectList;
	private List<CodeReportVo> codeReportList;
	private List<DefectReportVo> defectReportList;
	private List<Reportary> reportaryList;
	private List<DefectInfo> mysubitteddefectList;
	private List<ContributionReportVo> contributionReportList;
	private List<StrongFieldReportVo> strongFieldReportList;
	private ProjectService projectService;
	private UserService userService;
	private GroupService groupService;
	private TestcaseService testcaseService;
	private SprintService sprintService;
	private ReportService reportService;

	private DefectReportVo defectreportVo;
	private ReportVo reportVo;
	private ContributionReportVo contributionReportVo;
	private StrongFieldReportVo strongFieldReportVo;
	
	private String reportId;
	private String reportaryId;
	private String startDate;
	private String endDate;
	private String projectId;
	private String showType;
	private String selectedreportId;
	private String selectedShowtype;
	private String rallyquixId;
	private String selectedrallyquixId;
	private String sprintId;
	private String selectedsprintId;

	private String navigation = " <div class=\"port_bar\"><a href=\"../project/projectList.do\" class=\"marfin_lef10\">" + UserSessionUtil.getDefaultProjectName() + "</a>";

	/**
	 * Commit report
	 * @return
	 */
	public String goCodeReport(){
		try{
			User currentUser = UserSessionUtil.getUser();
			projectList = projectService.getProjectByGroup(currentUser.getGroupId());
			if ((this.reportId != null) && !this.reportId.equals("")) {
				reportaryId = this.reportId;
				selectedreportId = reportId;
				selectedShowtype = showType;
				//			    projectId = this.projectId;
				//			    this.createCodeReportList(reportaryId);
				this.createCodeReportList(reportaryId,startDate,endDate);
			}

		}catch (BaseDaoException e) {
			e.printStackTrace();
		}
		navigation += "->Code report</div>";
		return SUCCESS;

	}

	public void createCodeReportList(String reportaryId){
		codeReportList = new ArrayList<CodeReportVo>(); 
		UserVo userVo = new UserVo();
		try {
			User currentUser = UserSessionUtil.getUser();
			List<User> userList = userService.getAllUserByUserVo(userVo);
			if(userList!=null && userList.size()!=0){
				for(User user : userList){
					UserGroup group = groupService.findGroupUserById(user.getUserId());
					if((group!=null)&&(group.getGroup().getGroupId()==currentUser.getGroupId())){
						int commitNum = 0;
						CodeReportVo vo = new CodeReportVo();
						List<MyCommitVo> crList = projectService.getCommitVo(user.getMail(), reportaryId);
						if(crList!=null && crList.size()!=0){
							for(MyCommitVo cr : crList){
								commitNum = commitNum + Integer.valueOf(cr.getCodeChange());
							}
						}
						vo.setUserCode(user.getUserCode());
						vo.setCodeNum(commitNum);
						codeReportList.add(vo);
					}
				}
			}

		}catch (BaseDaoException e) {
			e.printStackTrace();
		}catch (BaseException e){
			e.printStackTrace();
		}

	}

	public void createCodeReportList(String reportaryId, String startDate, String endDate){
		codeReportList = new ArrayList<CodeReportVo>(); 
		UserVo userVo = new UserVo();
		try {
			User currentUser = UserSessionUtil.getUser();
			List<User> userList = userService.getAllUserByUserVo(userVo);
			if(userList!=null && userList.size()!=0){
				for(User user : userList){
					UserGroup group = groupService.findGroupUserById(user.getUserId());
					if((group!=null)&&(group.getGroup().getGroupId()==currentUser.getGroupId())){
						int codeNum = 0;
						int commitNum = 0;
						CodeReportVo vo = new CodeReportVo();
						List<MyCommitVo> crList = projectService.getCommitVo(user.getMail(), reportaryId);
						if(crList!=null && crList.size()!=0){
							for(MyCommitVo cr : crList){
								long da1 = 0, da2 = 0;
								if(null != startDate && !"".equals(startDate)){
									da1 = this.getCompareDate(startDate, cr.getCommitTime().toString());
								}
								if(null != endDate && !"".equals(endDate)){
									da2 = this.getCompareDate(cr.getCommitTime().toString(), endDate);
								}
								if(da1>=0 && da2>=0){
									codeNum = codeNum + Integer.valueOf(cr.getCodeChange());
									commitNum++;
								}

							}
						}
						vo.setUserCode(user.getUserCode());
						vo.setCodeNum(codeNum);
						vo.setCommitNum(commitNum);
						NumberFormat numberFormat = NumberFormat.getInstance();
						numberFormat.setMinimumFractionDigits(0);
						String average = "0";
						if(commitNum != 0){
							average = numberFormat.format(Math.round((float)codeNum/(float)commitNum));
							int n=average.indexOf(",");
							if(n!=-1){
								average = average.replace(",", "");
							}
						}
						vo.setAverage(average);
						codeReportList.add(vo);
					}
				}
			}

		}catch (BaseDaoException e) {
			e.printStackTrace();
		}catch (BaseException e){
			e.printStackTrace();
		}catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public long getCompareDate(String startDate,String endDate){

		long d = -1;
		try{		
			SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = formatter.parse(startDate);		 
			Date date2=formatter.parse(endDate);
			long l = date2.getTime() - date1.getTime();
			d = l/(24*60*60*1000);  	     
		} catch (ParseException  e)  {
			e.printStackTrace();			
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return d;
	} 

	public void getReposByPorject(){
		PrintWriter out = null;
		if (this.projectId != null){
			try{	
				reportaryList = projectService.getReportaryByProjectId(projectId);
				out = this.getResponse().getWriter();
				this.getResponse().setHeader("Cache-Control", "no-cache");
				this.getResponse().setContentType("text/json;charset=UTF-8");
				this.getResponse().setCharacterEncoding("utf-8");

				JSONArray json = JSONArray.fromObject(reportaryList);
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

	public void getCodeCommitChartXml(){
		PrintWriter out = null;
		List<CodeReportVo> list=new ArrayList<CodeReportVo>();
		if ((this.reportId != null) && !this.reportId.equals("")) {
			try {
				this.createCodeReportList(reportId,startDate,endDate);
				list = this.getChartXmlForCodeReport(showType);
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
	}

	//	public String getChartXmlForCodeReport(String showType){
	//		Document document = DocumentHelper.createDocument();
	//		// root element
	//		Element rootElement=document.addElement("chart");
	//		if(!"3".equals(showType)){
	//			rootElement.addAttribute("showLegend", "1");
	//			rootElement.addAttribute("legendPosition", "RIGHT");
	//			rootElement.addAttribute("showValues", "0");
	//			if(codeReportList!=null &&codeReportList.size()!=0){
	//				for(CodeReportVo codeRep : codeReportList){
	//					Element passElement = rootElement.addElement("set");
	//					passElement.addAttribute("label", codeRep.getUserCode());
	//					if("0".equals(showType)){
	//						passElement.addAttribute("value", String.valueOf(codeRep.getCommitNum()));
	//						rootElement.addAttribute("caption", "Code Committed By User");
	//					}else if("1".equals(showType)){
	//						passElement.addAttribute("value", String.valueOf(codeRep.getAverage()));
	//						rootElement.addAttribute("caption", "Average");
	//					}else if("2".equals(showType)){
	//						passElement.addAttribute("value", String.valueOf(codeRep.getCodeNum()));
	//						rootElement.addAttribute("caption", "Code changed By User");
	//					}	
	//				}
	//			}
	//		}
	//		else if("3".equals(showType)){
	//			List<String> yearList = this.getYearList(reportId);
	//			if(null != yearList && yearList.size() != 0){
	//				rootElement.addAttribute("bgColor", "E9E9E9");
	//				rootElement.addAttribute("outCnvBaseFontColor", "666666");
	//				rootElement.addAttribute("caption", "Commit annual report");
	//				rootElement.addAttribute("xAxisName", "Month");
	//				rootElement.addAttribute("yAxisName", "Count");
	//				rootElement.addAttribute("plotFillAlpha", "70");
	//				rootElement.addAttribute("numVDivLines", "10");
	//				rootElement.addAttribute("showAlternateVGridColor", "1");
	//				rootElement.addAttribute("AlternateVGridColor", "e1f5ff");
	//				rootElement.addAttribute("divLineColor", "e1f5ff");
	//				rootElement.addAttribute("vdivLineColor", "e1f5ff");
	//				rootElement.addAttribute("baseFontColor", "666666");
	//				rootElement.addAttribute("canvasBorderThickness", "1");
	//				rootElement.addAttribute("showPlotBorder", "0");
	//				rootElement.addAttribute("plotBorderThickness", "0");
	//				rootElement.addAttribute("startAngX", "7");
	//				rootElement.addAttribute("endAngX", "7");
	//				rootElement.addAttribute("startAngY", "-18");
	//				rootElement.addAttribute("endAngY", "-18");
	//				rootElement.addAttribute("zgapplot", "20");
	//				rootElement.addAttribute("zdepth", "60");
	//				rootElement.addAttribute("exeTime", "2");
	//
	//				Element categoriesElement = rootElement.addElement("categories");
	//				Element categoryJanElement = categoriesElement.addElement("category");
	//				categoryJanElement.addAttribute("label", "Jan");
	//				Element categoryFebElement = categoriesElement.addElement("category");
	//				categoryFebElement.addAttribute("label", "Feb");
	//				Element categoryMarElement = categoriesElement.addElement("category");
	//				categoryMarElement.addAttribute("label", "Mar");
	//				Element categoryAprElement = categoriesElement.addElement("category");
	//				categoryAprElement.addAttribute("label", "Apr");
	//				Element categoryMayElement = categoriesElement.addElement("category");
	//				categoryMayElement.addAttribute("label", "May");
	//				Element categoryJunElement = categoriesElement.addElement("category");
	//				categoryJunElement.addAttribute("label", "Jun");
	//				Element categoryJulElement = categoriesElement.addElement("category");
	//				categoryJulElement.addAttribute("label", "Jul");
	//				Element categoryAugElement = categoriesElement.addElement("category");
	//				categoryAugElement.addAttribute("label", "Aug");
	//				Element categorySepElement = categoriesElement.addElement("category");
	//				categorySepElement.addAttribute("label", "Sep");
	//				Element categoryOctElement = categoriesElement.addElement("category");
	//				categoryOctElement.addAttribute("label", "Oct");
	//				Element categoryNovElement = categoriesElement.addElement("category");
	//				categoryNovElement.addAttribute("label", "Nov");
	//				Element categoryDecElement = categoriesElement.addElement("category");
	//				categoryDecElement.addAttribute("label", "Dec");
	//
	//				List<CodeReportVo> annualReport = this.getAnnualReport(yearList);
	//				Iterator<CodeReportVo> iter = annualReport.iterator();
	//				while(iter.hasNext()){
	//					CodeReportVo crVo = iter.next();
	//					Element datasetElement = rootElement.addElement("dataset");
	//					datasetElement.addAttribute("seriesName", crVo.getYear());
	//					datasetElement.addAttribute("renderAs", "Area");
	//
	//					Element setJanElement = datasetElement.addElement("set");
	//					setJanElement.addAttribute("value", crVo.getJanNum().toString());
	//					Element setFebElement = datasetElement.addElement("set");
	//					setFebElement.addAttribute("value", crVo.getFebNum().toString());
	//					Element setMatElement = datasetElement.addElement("set");
	//					setMatElement.addAttribute("value", crVo.getMatNum().toString());
	//					Element setAprElement = datasetElement.addElement("set");
	//					setAprElement.addAttribute("value", crVo.getAprNum().toString());
	//					Element setMayElement = datasetElement.addElement("set");
	//					setMayElement.addAttribute("value", crVo.getMayNum().toString());
	//					Element setJunElement = datasetElement.addElement("set");
	//					setJunElement.addAttribute("value", crVo.getJunNum().toString());
	//					Element setJulElement = datasetElement.addElement("set");
	//					setJulElement.addAttribute("value", crVo.getJulNum().toString());
	//					Element setAugElement = datasetElement.addElement("set");
	//					setAugElement.addAttribute("value", crVo.getAugNum().toString());
	//					Element setSepElement = datasetElement.addElement("set");
	//					setSepElement.addAttribute("value", crVo.getSepNum().toString());
	//					Element setOctElement = datasetElement.addElement("set");
	//					setOctElement.addAttribute("value", crVo.getOctNum().toString());
	//					Element setNovElement = datasetElement.addElement("set");
	//					setNovElement.addAttribute("value", crVo.getNovNum().toString());
	//					Element setDecElement = datasetElement.addElement("set");
	//					setDecElement.addAttribute("value", crVo.getDecNum().toString());
	//				}
	//			}
	//		}	
	//		return document.asXML();
	//	}

	public List<CodeReportVo> getChartXmlForCodeReport(String showType){
		List<CodeReportVo> list=new ArrayList<CodeReportVo>();
		//		Document document = DocumentHelper.createDocument();
		// root element
		//		Element rootElement=document.addElement("chart");
		if(!"3".equals(showType)){
			list=codeReportList;
		}
		else if("3".equals(showType)){
			List<String> yearList = this.getYearList(reportId);
			if(null != yearList && yearList.size() != 0){	
				List<CodeReportVo> annualReport = this.getAnnualReport(yearList);
				Iterator<CodeReportVo> iter = annualReport.iterator();
				while(iter.hasNext()){
					CodeReportVo crVo = iter.next();
					list.add(crVo);
				}
			}
			for(int i=0;i<list.size();i++){
				System.out.println(list.get(i).getAugNum());
			}

		}	
		return list;
	}

	private List<String> getYearList(String reportId){
		List<String> yearList = null; 
		try {
			yearList = projectService.getCommitYearList(reportId);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return yearList;
	}

	private List<CodeReportVo>getAnnualReport(List<String> yearList){
		List<CodeReportVo> annualReport = new ArrayList<CodeReportVo>();
		try {
			annualReport = projectService.getAnnualReportList(yearList, reportId);
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return annualReport;
	}

	public void getDefaultPeriod(){
		PrintWriter out = null;
		try {
			String res = projectService.getDefaultPeriod(reportId);
			out = this.getResponse().getWriter();
			out.print(res);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	/**
	 * Defect report
	 * @return
	 */
	public String goDefectReport(){
		try{
			User currentUser = UserSessionUtil.getUser();
			projectList = projectService.getProjectByGroup(currentUser.getGroupId());
			if ((this.rallyquixId != null) && !this.rallyquixId.equals("")) {
				selectedrallyquixId = rallyquixId;
				selectedShowtype = showType;
				this.createDefectReportList(rallyquixId,startDate,endDate);
			}

		}catch (BaseDaoException e) {
			e.printStackTrace();
		}
		navigation += "->Defect report</div>";
		return SUCCESS;
	}

	/**
	 * Commit rule report
	 * @return
	 */
	public String goContribution(){
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
				contributionReportList = reportService.getContributionReportVoAll(projectId,startDate, endDate);
				contributionReportVo = new ContributionReportVo();
				int maxTotalNumberTemp = 0;
				int maxFixBugsNumberTemp = 0;
				int maxunknownNumberTemp = 0;
				int maxUSNumberTemp = 0;
				String maxCodesName = "";
				String maxFixBugsName = "";
				String maxkunknowName = "";	
				String maxUSNumberName = "";	
				String maxName = "";
				for(int i=0;i<contributionReportList.size();i++){
					ContributionReportVo crVo = contributionReportList.get(i);
					maxName = crVo.getDevName();
					if(crVo.getTotalCommit() > maxTotalNumberTemp){
						maxTotalNumberTemp = crVo.getTotalCommit();
						maxCodesName = crVo.getDevName();
					}else if(crVo.getTotalCommit() == maxTotalNumberTemp){
						maxCodesName = maxCodesName + maxName+", ";
					}
					if(crVo.getDefectcodeCount() + crVo.getUscodeCount() > maxFixBugsNumberTemp){
						maxFixBugsNumberTemp = crVo.getDefectcodeCount() + crVo.getUscodeCount();
						maxFixBugsName = crVo.getDevName();
					}else if(crVo.getDefectcodeCount() + crVo.getUscodeCount() == maxFixBugsNumberTemp){
						maxFixBugsName = maxFixBugsName + maxName+", ";
					}
					if(crVo.getUnknowcodeCount() > maxunknownNumberTemp){
						maxunknownNumberTemp = crVo.getUnknowcodeCount();
						maxkunknowName = crVo.getDevName();
					}else if(crVo.getUnknowcodeCount() == maxunknownNumberTemp){
						maxkunknowName = maxkunknowName + maxName+", ";
					}
					if(crVo.getUscodeCount() > maxUSNumberTemp){
						maxUSNumberTemp = crVo.getUscodeCount();
						maxUSNumberName = crVo.getDevName();
					}else if(crVo.getUscodeCount() == maxUSNumberTemp){
						maxUSNumberName = maxUSNumberName + maxName+", ";
					}
				}
				if(maxCodesName.endsWith(", ")){
					maxCodesName = maxCodesName.substring(0, maxCodesName.length()-2);
				}
				if(maxFixBugsName.endsWith(", ")){
					maxFixBugsName = maxFixBugsName.substring(0, maxFixBugsName.length()-2);
				}
				if(maxkunknowName.endsWith(", ")){
					maxkunknowName = maxkunknowName.substring(0, maxkunknowName.length()-2);
				}
				if(maxUSNumberName.endsWith(", ")){
					maxUSNumberName = maxUSNumberName.substring(0, maxUSNumberName.length()-2);
				}
				contributionReportVo.setMaxtotalCount(maxTotalNumberTemp);
				contributionReportVo.setMaxtotalName(maxCodesName);
				contributionReportVo.setMaxbugfixCount(maxFixBugsNumberTemp);
				contributionReportVo.setMaxbugfixName(maxFixBugsName);
				contributionReportVo.setMaxunknowCount(maxunknownNumberTemp);
				contributionReportVo.setMaxunknownName(maxkunknowName);
				contributionReportVo.setMaxuscodeCount(maxUSNumberTemp);
				contributionReportVo.setMaxuscodeName(maxUSNumberName);
			}
		}catch (BaseDaoException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * Strong field report
	 * @return
	 */
	public String showStrongField(){
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
				strongFieldReportList = reportService.getStrongFieldReportVoAll(projectId, startDate, endDate);
			}
		}catch (BaseDaoException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public void getRallyComponentByPorject(){
		PrintWriter out = null;
		if (this.projectId != null){
			try{	
				List<ProjectRallyQuix> rallyComponentList = projectService.getProjectRallyByProjectId(projectId);
				List<ProjectRallyQuix> quixComponentList = projectService.getProjectQuixByProjectId(projectId);
				List<ProjectRallyQuix> componentList = new ArrayList<ProjectRallyQuix>();
				componentList.addAll(rallyComponentList);
				componentList.addAll(quixComponentList);
				out = this.getResponse().getWriter();
				this.getResponse().setHeader("Cache-Control", "no-cache");
				this.getResponse().setContentType("text/json;charset=UTF-8");
				this.getResponse().setCharacterEncoding("utf-8");

				JSONArray json = JSONArray.fromObject(componentList);
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

	//	public void getDefectChartXml(){
	//		PrintWriter out = null;
	//		if ((this.rallyquixId != null) && !this.rallyquixId.equals("")) {
	//			this.createDefectReportList(rallyquixId, startDate, endDate);
	//		}
	//		String strXml = "";
	//		strXml = this.getChartXmlForDefectReport(showType);
	//		try {
	//			out = this.getResponse().getWriter();
	//			out.print(strXml);
	//			out.flush();
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		} finally {
	//			out.close();
	//		}
	//	}

	public void getDefectChartXml(){
		List<DefectReportVo> list=new ArrayList<DefectReportVo>();
		PrintWriter out = null;
		if ((this.rallyquixId != null) && !this.rallyquixId.equals("")) {
			try {
				this.createDefectReportList(rallyquixId, startDate, endDate);
				list=this.getChartXmlForDefectReport(showType);
				//				for(int i=0;i<list.size();i++){
				//					System.out.println(list.get(i).getUserCode()+"***"+list.get(i).getUsersubmitNum());
				//				}
				JSONArray json = JSONArray.fromObject(list);
				out = this.getResponse().getWriter();
				out.print(json);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				out.close();
			}
		}
	}

	public List<DefectReportVo> getChartXmlForDefectReport(String showType){
		List<DefectReportVo> list=new ArrayList<DefectReportVo>();
		if("2".equals(showType)){
			Iterator<DefectReportVo> iter = defectReportList.iterator();
			while(iter.hasNext()){
				DefectReportVo drvo = iter.next();
				list.add(drvo);
			}
		}
		else if("1".equals(showType)){
			Iterator<DefectReportVo> iter = defectReportList.iterator();
			while(iter.hasNext()){
				DefectReportVo drvo = iter.next();
				list.add(drvo);
			}
		}
		else if("3".equals(showType)){
			DefectReportVo drvo=new DefectReportVo();
			drvo.setSubmittedCount(defectreportVo.getSubmittedCount());
			drvo.setOpenCount(defectreportVo.getOpenCount());
			drvo.setFixedCount(defectreportVo.getFixedCount());
			drvo.setClosedCount(defectreportVo.getClosedCount());
			list.add(drvo);
		}
		else if("4".equals(showType)){
			DefectReportVo drvo=new DefectReportVo();
			drvo.setResolveImmCount(defectreportVo.getResolveImmCount());
			drvo.setHighCount(defectreportVo.getHighCount());
			drvo.setNormalCount(defectreportVo.getNormalCount());
			drvo.setLowCount(defectreportVo.getLowCount());
			list.add(drvo);
		}
		return list;
	}

	//	public String getChartXmlForDefectReport(String showType){
	//		Document document = DocumentHelper.createDocument();
	//		// root element
	//		Element rootElement=document.addElement("chart");
	//		rootElement.addAttribute("showLegend", "1");
	//		rootElement.addAttribute("legendPosition", "RIGHT");
	//		rootElement.addAttribute("showValues", "0");
	//		if("2".equals(showType)){
	//			rootElement.addAttribute("caption", "User developer");
	//			Iterator<DefectReportVo> iter = defectReportList.iterator();
	//			while(iter.hasNext()){
	//				DefectReportVo drvo = iter.next();
	//				Element submittedElement = rootElement.addElement("set");
	//				submittedElement.addAttribute("label", drvo.getUserCode());
	//				submittedElement.addAttribute("value", String.valueOf(drvo.getUserdeveloperNum()));
	//			}
	//
	//		}
	//		else if("1".equals(showType)){
	//			rootElement.addAttribute("caption", "User submitted");
	//			Iterator<DefectReportVo> iter = defectReportList.iterator();
	//			while(iter.hasNext()){
	//				DefectReportVo drvo = iter.next();
	//				Element submittedElement = rootElement.addElement("set");
	//				submittedElement.addAttribute("label", drvo.getUserCode());
	//				submittedElement.addAttribute("value", String.valueOf(drvo.getUsersubmitNum()));
	//			}
	//
	//		}
	//		else if("3".equals(showType)){
	//			rootElement.addAttribute("caption", "State");
	//			Element submittedElement = rootElement.addElement("set");
	//			submittedElement.addAttribute("label", "Submitted");
	//			submittedElement.addAttribute("value", String.valueOf(defectreportVo.getSubmittedCount()));
	//
	//			Element openElement = rootElement.addElement("set");
	//			openElement.addAttribute("label", "Open");
	//			openElement.addAttribute("value", String.valueOf(defectreportVo.getOpenCount()));
	//
	//			Element fixedElement = rootElement.addElement("set");
	//			fixedElement.addAttribute("label", "Fixed");
	//			fixedElement.addAttribute("value", String.valueOf(defectreportVo.getFixedCount()));
	//
	//			Element closedElement = rootElement.addElement("set");
	//			closedElement.addAttribute("label", "Closed");
	//			closedElement.addAttribute("value", String.valueOf(defectreportVo.getClosedCount()));
	//		}
	//		else if("4".equals(showType)){
	//			rootElement.addAttribute("caption", "Priority");
	//			Element resolveImmElement = rootElement.addElement("set");
	//			resolveImmElement.addAttribute("label", "Resolve Immediately");
	//			resolveImmElement.addAttribute("value", String.valueOf(defectreportVo.getResolveImmCount()));
	//
	//			Element highElement = rootElement.addElement("set");
	//			highElement.addAttribute("label", "High Attention");
	//			highElement.addAttribute("value", String.valueOf(defectreportVo.getHighCount()));
	//
	//			Element normalElement = rootElement.addElement("set");
	//			normalElement.addAttribute("label", "Normal");
	//			normalElement.addAttribute("value", String.valueOf(defectreportVo.getNormalCount()));
	//
	//			Element lowElement = rootElement.addElement("set");
	//			lowElement.addAttribute("label", "Low");
	//			lowElement.addAttribute("value", String.valueOf(defectreportVo.getLowCount()));
	//		}
	//		return document.asXML();
	//	}





	public void createDefectReportList(String rallyQuixId, String startDate, String endDate){
		try {
			ProjectRallyQuix projectRallyQuix = projectService.getProjectRallyQuixById(rallyQuixId);
			DefectInfo di = new DefectInfo();
			di.setComponent(projectRallyQuix.getComponent());
			if(null != projectRallyQuix.getNameinRally() && !"".equals(projectRallyQuix.getNameinRally())){
				di.setProjectName(projectRallyQuix.getNameinRally());
			}
			else if(null != projectRallyQuix.getNameinQuix() && !"".equals(projectRallyQuix.getNameinQuix())){
				di.setProjectName(projectRallyQuix.getNameinQuix());
			}
			List<DefectInfo> diList = testcaseService.getDefectInfoByBean(di);
			// Submit, owner, developer
			this.getDefectUserReport(diList, startDate, endDate);
			// State report
			this.getDefectStateReport(diList, startDate, endDate);
			// Priority report
			this.getDefectPriorityReport(diList, startDate, endDate);


		} catch (BaseException e) {
			e.printStackTrace();
		} 
	}

	private void getDefectUserReport(List<DefectInfo> diList, String startDate, String endDate){
		UserVo userVo = new UserVo();
		User currentUser = UserSessionUtil.getUser();
		List<User> userList;
		try {
			userList = userService.getAllUserByUserVo(userVo);
			if(userList!=null && userList.size()!=0){
				defectReportList = new ArrayList<DefectReportVo>();
				for(User user : userList){
					int submitNum = 0, developerNum = 0;
					UserGroup group = groupService.findGroupUserById(user.getUserId());
					if((group!=null)&&(group.getGroup().getGroupId()==currentUser.getGroupId())){
						Iterator<DefectInfo> iter = diList.iterator();
						DefectReportVo dpvo = new DefectReportVo();
						while(iter.hasNext()){
							DefectInfo di = iter.next();
							long da1 = 0, da2 = 0;
							if(null != startDate && !"".equals(startDate)){
								da1 = this.getCompareDate(startDate, di.getLastupdateDate().toString());
							}
							if(null != endDate && !"".equals(endDate)){
								da2 = this.getCompareDate(di.getLastupdateDate().toString(), endDate);
							}
							if(da1>=0&&da2>=0){
								if(user.getMail().equals(di.getSubmittEmail())){
									submitNum++;
								}
								if(user.getMail().equals(di.getDeveloperEmail())){
									developerNum++;
								}
							}
						}
						dpvo.setUserCode(user.getUserCode());
						dpvo.setUsersubmitNum(submitNum);
						dpvo.setUserdeveloperNum(developerNum);
						defectReportList.add(dpvo);
					}
				}
			}
		} catch (BaseDaoException e) {
			e.printStackTrace();
		}
	}

	private void getDefectStateReport(List<DefectInfo> diList, String startDate, String endDate){
		Iterator<DefectInfo> iter = diList.iterator();
		int submittedNum = 0, openNum = 0, fixedNum = 0, closedNum = 0;
		//"Submitted", "Open", "Fixed", "Closed", Awaiting Release, Closed-No Change, Lab Review, Duplicate, Completed
		while(iter.hasNext()){
			DefectInfo di = iter.next();
			long da1 = 0, da2 = 0;
			if(null != startDate && !"".equals(startDate)){
				da1 = this.getCompareDate(startDate, di.getLastupdateDate().toString());
			}
			if(null != endDate && !"".equals(endDate)){
				da2 = this.getCompareDate(di.getLastupdateDate().toString(), endDate);
			}
			if(da1>=0&&da2>=0){
				if("Submitted".equals(di.getState()) || "Lab Review".equals(di.getState())){
					submittedNum ++;
				}
				if("Open".equals(di.getState())){
					openNum ++;
				}
				if("Fixed".equals(di.getState()) || "Closed-No Change".equals(di.getState()) || "Awaiting Release".equals(di.getState())){
					fixedNum ++;
				}
				if("Closed".equals(di.getState()) || "Completed".equals(di.getState())){
					closedNum ++;
				}
			}
		}
		defectreportVo = new DefectReportVo();
		defectreportVo.setSubmittedCount(submittedNum);
		defectreportVo.setOpenCount(openNum);
		defectreportVo.setFixedCount(fixedNum);
		defectreportVo.setClosedCount(closedNum);
	}

	private void getDefectPriorityReport(List<DefectInfo> diList, String startDate, String endDate){
		Iterator<DefectInfo> iter = diList.iterator();
		int lowNum = 0, normalNum = 0, highNum = 0, resImmNum = 0;
		//"Resolve Immediately", "High Attention", "Normal", "Low", Medium, High, Low
		while(iter.hasNext()){
			DefectInfo di = iter.next();
			long da1 = 0, da2 = 0;
			if(null != startDate && !"".equals(startDate)){
				da1 = this.getCompareDate(startDate, di.getLastupdateDate().toString());
			}
			if(null != endDate && !"".equals(endDate)){
				da2 = this.getCompareDate(di.getLastupdateDate().toString(), endDate);
			}
			if(da1>=0&&da2>=0){
				if("Low".equals(di.getPriority())){
					lowNum ++;
				}
				if("Normal".equals(di.getPriority())|| "Medium".equals(di.getPriority())){
					normalNum ++;
				}
				if("High Attention".equals(di.getPriority()) || "High".equals(di.getPriority())){
					highNum ++;
				}
				if("Resolve Immediately".equals(di.getPriority())){
					resImmNum ++;
				}
			}
		}
		defectreportVo.setLowCount(lowNum);
		defectreportVo.setNormalCount(normalNum);
		defectreportVo.setHighCount(highNum);
		defectreportVo.setResolveImmCount(resImmNum);
	}


	/**
	 * For Curtis Begin
	 * @return
	 */
	public String goScopeReport(){
		try{
			User currentUser = UserSessionUtil.getUser();
			projectList = projectService.getProjectByGroup(currentUser.getGroupId());
			if(null != startDate && !"".equals(startDate) && null != endDate && !"".equals(endDate)){
				selectedsprintId = sprintId;
				this.createScopeReportList(projectId, startDate, endDate);
			}
			//			if ((this.reportId != null) && !this.reportId.equals("")) {
			//				reportaryId = this.reportId;
			//				selectedreportId = reportId;
			//				selectedShowtype = showType;
			//			    this.createCodeReportList(reportaryId,startDate,endDate);
			//			}

		}catch (BaseDaoException e) {
			e.printStackTrace();
		}
		navigation += "->Scope report</div>";
		return SUCCESS;

	}

	public void getSprintPeriod(){
		PrintWriter out = null;
		try {
			SprintId sId = new SprintId();
			sId.setProjectId(projectId);
			sId.setSprintId(sprintId);
			Sprint s = sprintService.getSprintById(sId);
			String res = s.getStartDate().toString().split(" ")[0] + ";" + s.getEndDate().toString().split(" ")[0];
			out = this.getResponse().getWriter();
			out.print(res);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	private void createScopeReportList(String projectId, String startDate, String endDate){
		try {
			reportVo = testcaseService.getRunCaseReportByPeriod(projectId, startDate, endDate);
		} catch (BaseException e) {
			e.printStackTrace();
		}
	}

	//	public void getScopeChartXml(){
	//		PrintWriter out = null;
	//		if ((this.projectId != null) && !this.projectId.equals("")) {
	//		    this.createScopeReportList(projectId,startDate,endDate);
	//		}
	//		StringBuffer strXml = new StringBuffer();
	//		strXml.append(this.getDefectChartXmlForScopeReport("0")).
	//		append("###").append(this.getDefectChartXmlForScopeReport("1")).
	//		append("###").append(this.getDefectChartXmlForScopeReport("2"));
	//		try {
	//			out = this.getResponse().getWriter();
	//			out.print(strXml.toString());
	//			out.flush();
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		} finally {
	//			out.close();
	//		}
	//	}

	public void getScopeChartXml(){
		
		PrintWriter out = null;
		if ((this.projectId != null) && !this.projectId.equals("")) {
			try {

				this.createScopeReportList(projectId,startDate,endDate);
				List<ReportVo> list = new ArrayList<ReportVo>();
				ReportVo re=new ReportVo();
				re.setTestcasePass(reportVo.getTestcasePass());
				re.setTestcaseFail(reportVo.getTestcaseFail());
				re.setHasDefect(reportVo.getHasDefect());
				re.setNoDefect(reportVo.getNoDefect());
				re.setDefectFixed(reportVo.getDefectFixed());
				re.setDefectNotfixed(reportVo.getDefectNotfixed());
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

	} 





	//	public String getDefectChartXmlForScopeReport(String showType){
	//		Document document = DocumentHelper.createDocument();
	//		// root element
	//		Element rootElement=document.addElement("chart");
	//
	//		if("0".equals(showType)){
	//			rootElement.addAttribute("caption", "Test result");
	//		}
	//		else if("1".equals(showType)){
	//			rootElement.addAttribute("caption", "Test effective");
	//		}
	//		else{
	//			rootElement.addAttribute("caption", "Defect state");
	//		}
	//		rootElement.addAttribute("pieRadius", "110");
	//		rootElement.addAttribute("showPercentValues", "1");
	//
	//		// set element
	//		if("0".equals(showType)){
	//			Element passElement = rootElement.addElement("set");
	//			passElement.addAttribute("label", "Passed");
	//			passElement.addAttribute("value", String.valueOf(reportVo.getTestcasePass()));
	//			passElement.addAttribute("color", "7FFF00");
	//			passElement.addAttribute("toolText", "Cases never failed");
	//
	//			Element failElement = rootElement.addElement("set");
	//			failElement.addAttribute("label","Failed");
	//			failElement.addAttribute("value",String.valueOf(reportVo.getTestcaseFail()));
	//			failElement.addAttribute("color", "FF4500");
	//			failElement.addAttribute("toolText", "Cases used failed");
	//		}
	//		else if("1".equals(showType)){
	//			Element hasDefectElement = rootElement.addElement("set");
	//			hasDefectElement.addAttribute("label", "Defect");
	//			hasDefectElement.addAttribute("value", String.valueOf(reportVo.getHasDefect()));
	//			hasDefectElement.addAttribute("color", "DA70D6");
	//			hasDefectElement.addAttribute("toolText", "Failed case linked to defect");
	//
	//			Element noDefectElement = rootElement.addElement("set");
	//			noDefectElement.addAttribute("label","Vacuous");
	//			noDefectElement.addAttribute("value",String.valueOf(reportVo.getNoDefect()));
	//			noDefectElement.addAttribute("color", "FFFF00");
	//			noDefectElement.addAttribute("toolText", "Failed case not linked to defect");
	//		}
	//		else{
	//			Element fixedElement = rootElement.addElement("set");
	//			fixedElement.addAttribute("label", "Fixed");
	//			fixedElement.addAttribute("value", String.valueOf(reportVo.getDefectFixed()));
	//			fixedElement.addAttribute("color", "54FF9F");
	//			fixedElement.addAttribute("toolText", "Defect fixed");
	//
	//			Element notfixedElement = rootElement.addElement("set");
	//			notfixedElement.addAttribute("label","Not fixed");
	//			notfixedElement.addAttribute("value",String.valueOf(reportVo.getDefectNotfixed()));
	//			notfixedElement.addAttribute("color", "969696");
	//			notfixedElement.addAttribute("toolText", "Defect not fixed");
	//		}
	//
	//		return document.asXML();
	//	}

	public String goDefectDetail(){
		try {
			mysubitteddefectList = testcaseService.goDefectDetail(projectId, startDate, endDate);
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * For Curtis End
	 * @return
	 */

	public List<Project> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}

	public List<CodeReportVo> getCodeReportList() {
		return codeReportList;
	}

	public void setCodeReportList(List<CodeReportVo> codeReportList) {
		this.codeReportList = codeReportList;
	}

	public List<Reportary> getReportaryList() {
		return reportaryList;
	}

	public void setReportaryList(List<Reportary> reportaryList) {
		this.reportaryList = reportaryList;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public GroupService getGroupService() {
		return groupService;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getReportaryId() {
		return reportaryId;
	}

	public void setReportaryId(String reportaryId) {
		this.reportaryId = reportaryId;
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

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getSelectedreportId() {
		return selectedreportId;
	}

	public void setSelectedreportId(String selectedreportId) {
		this.selectedreportId = selectedreportId;
	}

	public String getSelectedShowtype() {
		return selectedShowtype;
	}

	public void setSelectedShowtype(String selectedShowtype) {
		this.selectedShowtype = selectedShowtype;
	}

	public String getNavigation() {
		return navigation;
	}

	public void setNavigation(String navigation) {
		this.navigation = navigation;
	}

	public String getRallyquixId() {
		return rallyquixId;
	}

	public void setRallyquixId(String rallyquixId) {
		this.rallyquixId = rallyquixId;
	}

	public TestcaseService getTestcaseService() {
		return testcaseService;
	}

	public void setTestcaseService(TestcaseService testcaseService) {
		this.testcaseService = testcaseService;
	}

	public String getSelectedrallyquixId() {
		return selectedrallyquixId;
	}

	public void setSelectedrallyquixId(String selectedrallyquixId) {
		this.selectedrallyquixId = selectedrallyquixId;
	}

	public List<DefectReportVo> getDefectReportList() {
		return defectReportList;
	}

	public void setDefectReportList(List<DefectReportVo> defectReportList) {
		this.defectReportList = defectReportList;
	}

	public DefectReportVo getDefectreportVo() {
		return defectreportVo;
	}

	public void setDefectreportVo(DefectReportVo defectreportVo) {
		this.defectreportVo = defectreportVo;
	}

	public SprintService getSprintService() {
		return sprintService;
	}

	public void setSprintService(SprintService sprintService) {
		this.sprintService = sprintService;
	}

	public String getSprintId() {
		return sprintId;
	}

	public void setSprintId(String sprintId) {
		this.sprintId = sprintId;
	}

	public ReportVo getReportVo() {
		return reportVo;
	}

	public void setReportVo(ReportVo reportVo) {
		this.reportVo = reportVo;
	}

	public String getSelectedsprintId() {
		return selectedsprintId;
	}

	public void setSelectedsprintId(String selectedsprintId) {
		this.selectedsprintId = selectedsprintId;
	}

	public List<DefectInfo> getMysubitteddefectList() {
		return mysubitteddefectList;
	}

	public void setMysubitteddefectList(List<DefectInfo> mysubitteddefectList) {
		this.mysubitteddefectList = mysubitteddefectList;
	}

	public List<ContributionReportVo> getContributionReportList() {
		return contributionReportList;
	}

	public void setContributionReportList(
			List<ContributionReportVo> contributionReportList) {
		this.contributionReportList = contributionReportList;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public ContributionReportVo getContributionReportVo() {
		return contributionReportVo;
	}

	public void setContributionReportVo(ContributionReportVo contributionReportVo) {
		this.contributionReportVo = contributionReportVo;
	}

	public List<StrongFieldReportVo> getStrongFieldReportList() {
		return strongFieldReportList;
	}

	public void setStrongFieldReportList(
			List<StrongFieldReportVo> strongFieldReportList) {
		this.strongFieldReportList = strongFieldReportList;
	}

	public StrongFieldReportVo getStrongFieldReportVo() {
		return strongFieldReportVo;
	}

	public void setStrongFieldReportVo(StrongFieldReportVo strongFieldReportVo) {
		this.strongFieldReportVo = strongFieldReportVo;
	}

}
