package com.hp.titan.mytitan.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import com.hp.app.exception.BaseException;
import com.hp.titan.auth.model.User;
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
import com.hp.titan.mytitan.vo.MyCommitVo;
import com.hp.titan.report.vo.StrongFieldReportVo;

public interface MyTitanService {
	public List<TaskVo> getTaskInfoByUserEmail(String email, String startDate, String endDate) throws BaseException, IOException;
	public void saveReport(WeeklyReport wr) throws BaseException, IOException;
	public void saveReportTask(List<WeeklyReportTask> wrtList) throws BaseException, IOException;
	public List<WeeklyReport> getWeeklyReportByUserId(Integer userId) throws BaseException;
	public StrongFieldReportVo getStrongFieldReportByUserMail(String userMail,String projectId,String startDate,String endDate) throws BaseException;
	public WeeklyReport getReportById(String reportId) throws BaseException;
	public List<DefectVo> getRallyDefectInfoByEmail(String email) throws IOException;
	public List<DefectVo> getRallyDefectInfoByDeveloper(String email) throws IOException;
//	public List<DefectVo> getRallyDefectInfoByProjectModule(String projectName, String module) throws IOException;
//	public List<DefectVo> getQuixDefectInfoByProjectModule(String projectName, String module) throws IOException;
	public List<DefectVo> getQuixDefectInfoByEmail(String email) throws IOException;
	public List<DefectVo> getQuixDefectInfoByDeveloper(String email) throws IOException;
	public List<WeeklyReportTask> getReportTask(String reportId, String type) throws BaseException;
	public LoginUserVo getUserVoByEmail(String email) throws BaseException, IOException;
	public List<UserstoryVo> getUserStoriesByUser(String userObjectId)  throws IOException;
	public List<UserstoryInfo> getUserStoriesFromDB(String email) throws BaseException;
	public List<PointHist> getPointHistByUser(int userId) throws BaseException, ParseException;
	public List<PointHist> getAwardPointHistByUser(String userId) throws BaseException, ParseException;
	public List<PointHist> getPointHistByBean(PointHist ph) throws BaseException;
	public Point getPointByUserId(int userId) throws BaseException;
	public ExpAccount getExpByUserId(int userId) throws BaseException;
	public List<UserstoryVo> getUserStoriesList(String state , Calendar calendar, String email) throws BaseException;
	public void saveMyUserStories(List<UserstoryInfo> usList) throws BaseException;
	public void savePointHist(PointHist ph) throws BaseException;
	public void saveExpLog(ExpLog el) throws BaseException;
	public void savePoint(Point pt) throws BaseException;
	public void saveExp(ExpAccount ea) throws BaseException;
	public List<WeeklyReportTask> getUsTasks(String usNum) throws BaseException;
	public void chargeExpint(Integer exPoint) throws BaseException;
//	public List<Reportary> getAllReportary() throws BaseException;
//	public Reportary getReportaryByReportaryId(String reportaryId) throws BaseException;
//	public List<Reportary> getReportaryByProjectId(String projectId) throws BaseException;
//	public void saveReportary(Reportary r) throws BaseException;
	public List<MyCommitVo> getCommitList(String email, String reportaryId)throws BaseException;
//	public List<CommitReport> getCommitReportList(String email, String reportId)throws BaseException;
//	public void saveCommitReport(CommitReport cr)throws BaseException;
//	public void saveCommitPath(List<CommitPath> cpList)throws BaseException;
//	public String getMaxSvnVersion(String reportaryId)throws BaseException;
	public List<CommitPath> getCommitChangeList(String commitId)throws BaseException;
//	public void clearSyncedRepositoryData(String reportaryId)throws BaseException;
	public Boolean checkExpLogByDetail(Integer userId) throws BaseException;
	public List<Goods> getAllGoods() throws BaseException;
	public List<Point> getAllPoint() throws BaseException;
	public void saveConsume(Consumption c) throws BaseException;
	public Goods getGoodsByName(String goodName)  throws BaseException;
	public List<Consumption> getConsumptionByBean(Consumption c) throws BaseException;
	public PointHist getPointHistByObjectId(String objectId) throws BaseException;
	public List<Consumption> comsumptionCount(int groupId) throws BaseException;
	public List<Consumption> getAllConsumptionByGroup(int groupId) throws BaseException, ParseException;
	public List<Consumption> hasGamble(int userId) throws BaseException;
	public Consumption getConsumptionById(String consumeId) throws BaseException;
	public List<Integer> getSelectNumberByGroup(int groupId) throws BaseException;
	public User getGambleWinner(int selectedNum) throws BaseException;
	public Integer getInBoxCount(InBox inBox) throws BaseException;
	public List<InBox> getInBoxByBean(InBox inBox) throws BaseException;
	public void saveInbox(InBox inBox) throws BaseException;
	public List<Winner> getWinnersByBeam(Winner w) throws BaseException;
	public void saveWinner(Winner winner) throws BaseException;
}
