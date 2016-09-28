package com.hp.titan.mytitan.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.hp.app.exception.BaseException;
import com.hp.quix.dao.QuixDao;
import com.hp.titan.auth.model.User;
import com.hp.titan.common.vo.DefectVo;
import com.hp.titan.common.vo.LoginUserVo;
import com.hp.titan.common.vo.TaskVo;
import com.hp.titan.common.vo.UserstoryVo;
import com.hp.titan.mytitan.dao.MytitanDao;
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
import com.hp.titan.rally.dao.RallyDao;
import com.hp.titan.report.vo.StrongFieldReportVo;

public class MyTitanServiceImpl implements MyTitanService{
	
	private RallyDao rallyDao;
	private QuixDao quixDao;
	private MytitanDao mytitanDao;
	
	public List<TaskVo> getTaskInfoByUserEmail(String email, String startDate, String endDate) throws BaseException, IOException{
		return rallyDao.getTaskInfoByUserEmail(email, startDate, endDate);
	}
	
	public List<DefectVo> getRallyDefectInfoByEmail(String email) throws IOException{
		return rallyDao.getDefectInfoByEmail(email);
	}
	
	public List<DefectVo> getRallyDefectInfoByDeveloper(String email) throws IOException{
		return rallyDao.getRallyDefectInfoByDeveloper(email);
	}
	
//	public List<DefectVo> getRallyDefectInfoByProjectModule(String projectName, String module) throws IOException{
//		return rallyDao.getRallyDefectInfoByProjectModule(projectName, module);
//	}
	
//	public List<DefectVo> getQuixDefectInfoByProjectModule(String projectName, String module) throws IOException{
//		quixDao = new QuixDao();
//		return quixDao.getQuixDefectInfoByProjectModule(projectName, module);
//	}
	
	public List<DefectVo> getQuixDefectInfoByEmail(String email) throws IOException{
		quixDao = new QuixDao();
		return quixDao.getQuixDefectInfoByEmail(email);
	}
	
	public List<DefectVo> getQuixDefectInfoByDeveloper(String email) throws IOException{
		quixDao = new QuixDao();
		return quixDao.getQuixDefectInfoByDeveloper(email);
	}
	
	public void saveReport(WeeklyReport wr) throws BaseException, IOException{
		mytitanDao.saveReport(wr);
	}
	
	public void saveReportTask(List<WeeklyReportTask> wrtList) throws BaseException, IOException{
		Iterator<WeeklyReportTask> iter = wrtList.iterator();
		while(iter.hasNext()){
			mytitanDao.saveReportTask(iter.next());
		}
	}
	
	public List<WeeklyReport> getWeeklyReportByUserId(Integer userId) throws BaseException{
		return mytitanDao.getWeeklyReportByUserId(userId);
	}
	
	public StrongFieldReportVo getStrongFieldReportByUserMail(String userMail,String projectId, String startDate, String endDate) throws BaseException{
		List<Object[]> objects = mytitanDao.getStrongFieldReportByUserMail(userMail, projectId, startDate, endDate);
		Object[] object = objects.get(0);
		int java = Integer.valueOf(object[1].toString().replace(".0", ""));
		int xml = Integer.valueOf(object[2].toString().replace(".0", ""));
		int ftl = Integer.valueOf(object[3].toString().replace(".0", ""));
		int sql = Integer.valueOf(object[4].toString().replace(".0", ""));
		int js = Integer.valueOf(object[5].toString().replace(".0", ""));
		int png = Integer.valueOf(object[6].toString().replace(".0", ""));
		int jsp = Integer.valueOf(object[7].toString().replace(".0", ""));
		int css = Integer.valueOf(object[8].toString().replace(".0", ""));			                              
		int[] count = {java,xml,ftl,sql,js,png,jsp,css};
		int max = java;
		for (int k=1;k<count.length;k++) {
			if(count[k]>max){
				max = count[k];
			}
		}
		StringBuffer sb = new StringBuffer();
		if(java==max){
			sb.append("java");
		}
		if(xml==max){
			if(sb.length()!=0){
				sb.append(",");
			}
			sb.append("xml");
		}
		if(sql==max){
			if(sb.length()!=0){
				sb.append(",");
			}
			sb.append("sql");
		}
		if(ftl==max){
			if(sb.length()!=0){
				sb.append(",");
			}
			sb.append("ftl");
		}
		if(css==max){
			if(sb.length()!=0){
				sb.append(",");
			}
			sb.append("css");
		}
		if(jsp==max){
			if(sb.length()!=0){
				sb.append(",");
			}
			sb.append("jsp");
		}
		if(png==max){
			if(sb.length()!=0){
				sb.append(",");
			}
			sb.append("png");
		}
		if(js==max){
			if(sb.length()!=0){
				sb.append(",");
			}
			sb.append("js");
		}			                              
		StrongFieldReportVo strongFieldReportVo = new StrongFieldReportVo();
        strongFieldReportVo.setStrongFieldName(sb.toString());
		strongFieldReportVo.setName(String.valueOf(object[0]));
		strongFieldReportVo.setJavaCommitLineCount(java);
		strongFieldReportVo.setXmlCommitLineCount(xml);
		strongFieldReportVo.setFtlCommitLineCount(ftl);
		strongFieldReportVo.setSqlCommitLineCount(sql);
		strongFieldReportVo.setJsCommitLineCount(js);
		strongFieldReportVo.setPngCommitLineCount(png);
		strongFieldReportVo.setJspCommitLineCount(jsp);
		strongFieldReportVo.setCssCommitLineCount(css);
		return strongFieldReportVo;
	}
	
	public WeeklyReport getReportById(String reportId) throws BaseException{
		return mytitanDao.getReportById(reportId);
	}
	
	public List<WeeklyReportTask> getReportTask(String reportId, String type) throws BaseException{
		return mytitanDao.getReportTask(reportId, type);
	}
	
	public LoginUserVo getUserVoByEmail(String email) throws BaseException, IOException{
		return rallyDao.getUserInfoByEmail(email);
	}
	
	public List<UserstoryVo> getUserStoriesByUser(String userObjectId)  throws IOException{
		return rallyDao.getUserStoriesByUser(userObjectId);
	}
	
	public List<UserstoryInfo> getUserStoriesFromDB(String email) throws BaseException{
		return mytitanDao.getUserStoriesFromDB(email);
	}
	
	public List<PointHist> getPointHistByUser(int userId) throws BaseException, ParseException{
		return mytitanDao.getPointHistByUser(userId);
	}
	
	public List<PointHist> getAwardPointHistByUser(String userId) throws BaseException , ParseException{
		return mytitanDao.getAwardPointHistByUser(userId);
	}
	
	public Point getPointByUserId(int userId) throws BaseException{
		return mytitanDao.getPointByUserId(userId);
	}
	
	public ExpAccount getExpByUserId(int userId) throws BaseException{
		return mytitanDao.getExpByUserId(userId);
	}
	public List<PointHist> getPointHistByBean(PointHist ph) throws BaseException{
		return mytitanDao.getPointHistByBean(ph);
	}
	public List<UserstoryVo> getUserStoriesList(String state , Calendar calendar, String email) throws BaseException{
		return mytitanDao.getUserStoriesList(state, calendar, email);
	}
	public void saveMyUserStories(List<UserstoryInfo> usList) throws BaseException{
		if(null != usList && usList.size() != 0){
			Iterator<UserstoryInfo> iter = usList.iterator();
			while(iter.hasNext()){
				UserstoryInfo usInfo = iter.next();
				mytitanDao.saveMyUserStory(usInfo);
			}
		}
	}
	
	public void savePointHist(PointHist ph) throws BaseException {
		mytitanDao.saveOrUpdate(ph);
	}
	
	public void saveExpLog(ExpLog el) throws BaseException {
		mytitanDao.saveOrUpdate(el);
	}
	
	public void savePoint(Point pt) throws BaseException {
		mytitanDao.savePoint(pt);
	}
	
	public void saveExp(ExpAccount ea) throws BaseException{
		mytitanDao.saveExp(ea);
	}
	
	public List<MyCommitVo> getCommitList(String email, String reportaryId)throws BaseException{
		return mytitanDao.getCommitList(email, reportaryId);
	}
	
//	public List<CommitReport> getCommitReportList(String email, String reportId)throws BaseException{
//		return mytitanDao.getCommitReportList(email, reportId);
//	}
	
//	public void saveCommitReport(CommitReport cr)throws BaseException{
//		mytitanDao.saveCommit(cr);
//	}
	
//	public String getMaxSvnVersion(String reportaryId)throws BaseException{
//		return mytitanDao.getMaxSvnVersion(reportaryId);
//	}
	
	public List<WeeklyReportTask> getUsTasks(String usNum) throws BaseException{
		return mytitanDao.getUsTasks(usNum);
	}
	
	public void chargeExpint(Integer exPoint) throws BaseException{
		mytitanDao.chargeExpint(exPoint);
	}
	
//	public List<Reportary> getAllReportary() throws BaseException{
//		return mytitanDao.getAllReportary();
//	}
	
//	public Reportary getReportaryByReportaryId(String reportaryId) throws BaseException{
//		return mytitanDao.getReportaryByReportaryId(reportaryId);
//	}
	
//	public List<Reportary> getReportaryByProjectId(String projectId) throws BaseException{
//		return mytitanDao.getReportaryByProjectId(projectId);
//	}
	
//	public List<ProjectRallyQuix>getProjectRallyByProjectId(String projectId) throws BaseException{
//		return mytitanDao.getProjectRallyByProjectId(projectId);
//	}
	
//	public List<ProjectRallyQuix>getProjectQuixByProjectId(String projectId) throws BaseException{
//		return mytitanDao.getProjectQuixByProjectId(projectId);
//	}
	
//	public void saveReportary(Reportary r) throws BaseException{
//		mytitanDao.saveReportary(r);
//	}
	
//	public void saveCommitPath(List<CommitPath> cpList)throws BaseException{
//		Iterator<CommitPath> iter = cpList.iterator();
//		while(iter.hasNext()){
//			CommitPath cp = iter.next();
//			mytitanDao.saveCommitPath(cp);
//		}
//	}
	
//	public void clearSyncedRepositoryData(String reportaryId)throws BaseException{
//		List<MyCommitVo> myCommitVoList = mytitanDao.getCommitList("", reportaryId);
//		if(null != myCommitVoList && myCommitVoList.size() != 0){
//			Iterator<MyCommitVo> iter = myCommitVoList.iterator();
//			while(iter.hasNext()){
//				MyCommitVo vo = iter.next();
//				mytitanDao.deleteChangePath(vo.getCommitreportId());
//			}	
//		}
//		mytitanDao.deleteCommitReport(reportaryId);
//	}
	
//	public void doProjectAddRally(ProjectRallyQuix projectRallyQuix)throws BaseException{
//		mytitanDao.doProjectAddRally(projectRallyQuix);
//	}
	
	public List<CommitPath> getCommitChangeList(String commitId)throws BaseException{
		return mytitanDao.getCommitChangeList(commitId);
	}
	
//	public ProjectRallyQuix getProjectRallyQuixById(String rallyQuixId) throws BaseException{
//		return mytitanDao.getProjectRallyQuixById(rallyQuixId);
//	}
		
	public Boolean checkExpLogByDetail(Integer userId) throws BaseException{
		return mytitanDao.checkDupReportByDate(userId);
	}
	
	public List<Goods> getAllGoods() throws BaseException{
		return mytitanDao.getAllGoods();
	}
	
	public List<Point> getAllPoint() throws BaseException{
		return mytitanDao.getAllPoint();
	}

	public void saveConsume(Consumption c) throws BaseException{
		mytitanDao.saveConsume(c);
	}
	
	public Goods getGoodsByName(String goodName)  throws BaseException{
		return mytitanDao.getGoodsByName(goodName);
	}
	
	public List<Consumption> getConsumptionByBean(Consumption c) throws BaseException{
		return mytitanDao.getConsumptionByBean(c);
	}
	
	public PointHist getPointHistByObjectId(String objectId) throws BaseException{
		return mytitanDao.getPointHistByObjectId(objectId);
	}
	
	public List<Consumption> comsumptionCount(int groupId) throws BaseException{
		return mytitanDao.comsumptionCount(groupId);
	}
	
	public List<Consumption> getAllConsumptionByGroup(int groupId) throws BaseException, ParseException{
		return mytitanDao.getAllConsumptionByGroup(groupId);
	}
	
	public List<Consumption> hasGamble(int userId) throws BaseException{
		return mytitanDao.getGambleConsumption(userId);
	}
	
	public Consumption getConsumptionById(String consumeId) throws BaseException{
		return mytitanDao.getConsumptionById(consumeId);
	}
	
	public List<Integer> getSelectNumberByGroup(int groupId) throws BaseException{
		return mytitanDao.getSelectNumberByGroup(groupId);
	}
	
	public User getGambleWinner(int selectedNum) throws BaseException{
		return mytitanDao.getUserByNumber(selectedNum);
	}
	
	public void saveWinner(Winner winner) throws BaseException{
		mytitanDao.saveWinner(winner);
	}
	
	public List<Winner> getWinnersByBeam(Winner w) throws BaseException{
		return mytitanDao.getWinnersByBeam(w);
	}
	
	public Integer getInBoxCount(InBox inBox) throws BaseException{
		List<InBox> ibList = mytitanDao.getInBoxByBean(inBox);
		if(null == ibList){
			return 0;
		}
		return ibList.size();
	}
	
	public List<InBox> getInBoxByBean(InBox inBox) throws BaseException{
		return mytitanDao.getInBoxByBean(inBox);
	}
	
	public void saveInbox(InBox inBox) throws BaseException{
		mytitanDao.saveInbox(inBox);
	}
	
	public RallyDao getRallyDao() {
		return rallyDao;
	}

	public void setRallyDao(RallyDao rallyDao) {
		this.rallyDao = rallyDao;
	}

	public MytitanDao getMytitanDao() {
		return mytitanDao;
	}

	public void setMytitanDao(MytitanDao mytitanDao) {
		this.mytitanDao = mytitanDao;
	}
	
	

}
