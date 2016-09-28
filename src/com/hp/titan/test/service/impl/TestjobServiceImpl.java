package com.hp.titan.test.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.common.util.UnitCodeUtil;
import com.hp.titan.project.model.Project;
import com.hp.titan.server.model.Server;
import com.hp.titan.test.dao.DefectinfoDao;
import com.hp.titan.test.dao.ParameterDao;
import com.hp.titan.test.dao.ParameterdataDao;
import com.hp.titan.test.dao.RunCaseDao;
import com.hp.titan.test.dao.RunJobDao;
import com.hp.titan.test.dao.RuncaseparameterDao;
import com.hp.titan.test.dao.RuncaseserverDao;
import com.hp.titan.test.dao.TestjobDao;
import com.hp.titan.test.dao.TestjobcaseDao;
import com.hp.titan.test.dao.TestjobplanDao;
import com.hp.titan.test.dao.TestjobserverDao;
import com.hp.titan.test.model.DefaultParameter;
import com.hp.titan.test.model.Parameter;
import com.hp.titan.test.model.ParameterData;
import com.hp.titan.test.model.RunCase;
import com.hp.titan.test.model.RunJob;
import com.hp.titan.test.model.TestJobCase;
import com.hp.titan.test.model.TestJobPlan;
import com.hp.titan.test.model.TestJobServer;
import com.hp.titan.test.model.TestJobServerId;
import com.hp.titan.test.model.Testcase;
import com.hp.titan.test.model.Testjob;
import com.hp.titan.test.model.Testplan;
import com.hp.titan.test.model.UserTag;
import com.hp.titan.test.service.TestjobService;
import com.hp.titan.test.vo.DefectInfoVo;
import com.hp.titan.test.vo.ParameterVo;
import com.hp.titan.test.vo.RunCaseVo;
import com.hp.titan.test.vo.TestcaserunVo;
import com.hp.titan.test.vo.TestjobVo;
import com.hp.titan.test.vo.TestjobrunVo;

public class TestjobServiceImpl implements TestjobService {

	private TestjobDao testjobDao;
	private TestjobcaseDao testjobcaseDao;
	private TestjobplanDao testjobplanDao;
	private TestjobserverDao testjobserverDao;
	private RuncaseserverDao runcaseserverDao;
	private RuncaseparameterDao runcaseparameterDao;
	private RunJobDao runJobDao;
	private RunCaseDao runCaseDao;
	private ParameterDao parameterDao;
	private ParameterdataDao parameterdataDao;
	private DefectinfoDao defectinfoDao;
	
	@Override
	public void saveTestjob(Testjob testjob) throws BaseException {
		testjobDao.saveOrUpdate(testjob);
	}
	
	public void saveTestJobServer(TestJobServer testjobServer) throws BaseException{
		testjobserverDao.saveOrUpdate(testjobServer);
	}
	
	public void saveTestJobServerId(TestJobServerId testjobServerId) throws BaseException{
		testjobserverDao.saveOrUpdate(testjobServerId);
	}

	@Override
	public List<Testjob> getAllTestjob() throws BaseException{
	     return	testjobDao.findAllTestjob();
	}
	
	public List<Testjob> findTestjobBySprint(String sprintId) throws BaseException{
	     return	testjobDao.findTestjobBySprint(sprintId);
	}
	
	
	public List<TestjobVo> getAllTestjobByGroup(Integer groupId) throws BaseException{
	     return	testjobDao.findTestjobByGroup(groupId);
	}
	
	public Testjob getTestjobByCode(String testJobCode) throws BaseException{
	     return	testjobDao.getTestjobByCode(testJobCode);
	}
	
	
	public List<RunCase> getRunCaseByRunjobId(String runJobId) throws BaseException{
	     return	runCaseDao.getRunCaseByRunjobId(runJobId);
	}
	
	public List<RunCase> getRunCaseByResult(String runJobId, String result) throws BaseException{
		return	runCaseDao.getRunCaseByResult(runJobId, result);
	}
	
	public RunCase getRunCaseByVo(RunCaseVo rcv) throws BaseException{
		return	runCaseDao.getRunCaseByVo(rcv);
	}
	
	public TestjobrunVo getRunJobInfoById(String runJobId) throws BaseException{
		return runJobDao.getRunJobInfoById(runJobId);
	}
	
	public List<TestcaserunVo> getRunCaseInfoById(String runJobId) throws BaseException{
		return runCaseDao.getRunCaseInfoById(runJobId);
	}
	
	public List<Parameter> getParameterByJobId(String runJobId) throws BaseException{
		return parameterDao.getParameterByJobId(runJobId);
	}
	
	public List<Parameter> getParameterByJobPlanId(String runJobId) throws BaseException{
		return parameterDao.getParameterByJobPlanId(runJobId);
	}
	
	public List<Parameter> getServerParaByJobId(String runJobId) throws BaseException{
		return parameterDao.getServerParaByJobId(runJobId);
	}
	public List<Parameter> getServerParaByJobPlanId(String runJobId) throws BaseException{
		return parameterDao.getServerParaByJobPlanId(runJobId);
	}

	public TestjobDao getTestjobDao() {
		return testjobDao;
	}

	public void setTestjobDao(TestjobDao testjobDao) throws BaseException {
		this.testjobDao = testjobDao;
	}
	@Override
	public String newTestjobCodeByProject()
			throws BaseException {
		
		String maxCode = testjobDao.findMaxCodeByTestjob();		
		String newCode = UnitCodeUtil.generateUnitCode("TJ", maxCode);				
		return newCode;
	}
	public boolean isExistTestjob(String testjobName) throws BaseException {
		boolean isExist = false;
		Testjob testjob = testjobDao.getTestjobByName(testjobName);
		if(testjob != null){
			isExist=true;
		}
		return isExist;
	}
	public List<TestjobVo> findAllTestjob(TestjobVo testjobVo, Set<Project> s) throws BaseException{
		return testjobDao.findAllTestjob(testjobVo, s);		
	}

	public Testjob getTestjobById(String testjobId) throws BaseException{
		return testjobDao.getTestjobById(testjobId);		
	}
	
//	public void deleteJobPlanByJob(String testJobId) throws BaseException{
//		testjobplanDao.deleteJobPlanByJob(testJobId);
//	}
//	
//	public void saveJobPlans(List<TestJobPlan> testjobplanList) throws BaseException{
//		Iterator<TestJobPlan> l = testjobplanList.iterator();
//		while(l.hasNext()){
//			TestJobPlan tjp = l.next();
//			testjobplanDao.save(tjp);
//		}
//	}
//	
//	public void deleteJobCaseByJob(String testJobId) throws BaseException{
//		testjobcaseDao.deleteJobCaseByJob(testJobId);
//	}
//	
//	public void saveJobCases(List<TestJobCase> testjobcaseList) throws BaseException{
//		Iterator<TestJobCase> l = testjobcaseList.iterator();
//		while(l.hasNext()){
//			TestJobCase tjc = l.next();
//			testjobcaseDao.save(tjc);
//		}
//	}
	
	public void jobManagement(String testJobId, String testJobJson) throws BaseException{
		Testjob tj = testjobDao.getTestjobById(testJobId);
		tj.setPlancaseJson(testJobJson);
		testjobDao.saveOrUpdate(tj);
//		this.deleteJobPlanByJob(testJobId);
//		this.saveJobPlans(testjobplanList);
//		this.deleteJobCaseByJob(testJobId);
//		this.saveJobCases(testjobcaseList);
	}
	
	public void jobManagement(String testJobId, List<TestJobPlan> testjobplanList, List<TestJobCase> testjobcaseList) throws BaseException{
		this.deleteJobPlanByJob(testJobId);
		this.saveJobPlans(testjobplanList);
		this.deleteJobCaseByJob(testJobId);
		this.saveJobCases(testjobcaseList);
	}
	
	public void deleteJobPlanByJob(String testJobId) throws BaseException{
		testjobplanDao.deleteJobPlanByJob(testJobId);
	}
	
	public void saveJobPlans(List<TestJobPlan> testjobplanList) throws BaseException{
		Iterator<TestJobPlan> l = testjobplanList.iterator();
		while(l.hasNext()){
			TestJobPlan tjp = l.next();
			testjobplanDao.save(tjp);
		}
	}
	
	public void deleteJobCaseByJob(String testJobId) throws BaseException{
		testjobcaseDao.deleteJobCaseByJob(testJobId);
	}
	
	public void saveJobCases(List<TestJobCase> testjobcaseList) throws BaseException{
		Iterator<TestJobCase> l = testjobcaseList.iterator();
		while(l.hasNext()){
			TestJobCase tjc = l.next();
			testjobcaseDao.save(tjc);
		}
	}
	
//	@SuppressWarnings("unchecked")
//	public void saveRunCasePorperty(Map runCaseServerMap) throws BaseException{
//		// Save case server
//		RunCaseServer runCaseServer = new RunCaseServer();
//		RunCaseServerId runCaseServerId = new RunCaseServerId();
//		runCaseServerId.setTestjobId(runCaseServerMap.get("jobId").toString());
//		// Only for test cases in test plan
//		if(null != runCaseServerMap.get("planId") && !"".equals(runCaseServerMap.get("planId"))){
//			runCaseServerId.setTestplanId(runCaseServerMap.get("planId").toString());
//		}
//		runCaseServerId.setTestcaseId(runCaseServerMap.get("caseId").toString());
//		runCaseServerId.setServerId(runCaseServerMap.get("serverId").toString());
//		runCaseServer.setId(runCaseServerId);
//		runcaseserverDao.deleteRunCaseServer(runCaseServer);
//		runcaseserverDao.save(runCaseServer);
//		// Save case parameter
//		List runCaseParaList = (List)runCaseServerMap.get("paras");
//		runcaseparameterDao.deleteRunCasePara(runCaseServer.getId().getTestjobId(),runCaseServer.getId().getTestplanId(),runCaseServer.getId().getTestcaseId());
//		Iterator l = runCaseParaList.iterator();
//		while(l.hasNext()){
//			Map m = (Map)l.next();
//			RunCaseParameter runCaseParameter = new RunCaseParameter();
//			RunCaseParameterId runCaseParameterId = new RunCaseParameterId();
//			runCaseParameterId.setTestjobId(runCaseServerMap.get("jobId").toString());
//			if(null != runCaseServerMap.get("planId") && !"".equals(runCaseServerMap.get("planId"))){
//				runCaseParameterId.setTestplanId(runCaseServerMap.get("planId").toString());
//			}
//			runCaseParameterId.setParameterId(m.get("paraId").toString());
//			runCaseParameterId.setTestcaseId(runCaseServerMap.get("caseId").toString());
//			runCaseParameter.setId(runCaseParameterId);
//			runCaseParameter.setParameterValue(m.get("paraValue").toString());
//			runcaseparameterDao.save(runCaseParameter);
//		}
//	
//	}
	
	public void deleteTestJob(String testjobId) throws BaseException{
//		RunCaseServer runCaseServer = new RunCaseServer();
//		RunCaseServerId runCaseServerId = new RunCaseServerId();
//		runCaseServerId.setTestjobId(testjobId);
//		runCaseServer.setId(runCaseServerId);
//		runcaseserverDao.deleteRunCaseServer(runCaseServer);
		runcaseparameterDao.deleteRunCasePara(testjobId,"","");
		testjobDao.delete(testjobId);
	}
	
	public void clearServerFromJob(String testjobId) throws BaseException{
		testjobserverDao.deleteJobServer(testjobId);
	}
	
	
//	public Server getServerInfo(String testjobId, String testPlanId, String testCaseId) throws BaseException {
//		Server serverInfo = runcaseserverDao.getServerInfo(testjobId, testPlanId, testCaseId);
//		return serverInfo;
//	}
	
	public List<ParameterVo> getParameterInfo(String testjobId, String testPlanId, String testCaseId) throws BaseException {
		List<ParameterVo> parameterInfo = runcaseparameterDao.getParameterInfo(testjobId, testPlanId, testCaseId);
		return parameterInfo;
	}
	
	public void saveDefaultePara(List<DefaultParameter> dpl, int userId) throws BaseException{
		parameterdataDao.deleteDefaultParaByUserId(userId, dpl.get(0).getId().getTestjobId());
		for(DefaultParameter dp : dpl){
			parameterdataDao.saveDefaultPara(dp);
		}
	}
	
	public ParameterData  findParameterDataByParaName(String paraName) throws BaseException{
		return parameterdataDao.findParameterDataByParaName(paraName);
	}
	
	public Map<String, String> getDefaultParaMap(int userId, String projectId) throws BaseException{
		List<DefaultParameter> dpl = parameterdataDao.getDefaultPara(userId, projectId);
		Map<String, String> paraMap = new HashMap<String, String>();
		if(null != dpl){
			paraMap = new HashMap<String, String>();
			Iterator<DefaultParameter> iter = dpl.iterator();
			while(iter.hasNext()){
				DefaultParameter dp = iter.next();
				paraMap.put(dp.getId().getParadataName(), dp.getDefaultValue());
			}
		}
		return paraMap;
	}
	
	public List<DefaultParameter> getDefaultParaList(int userId, String testjobId) throws BaseException{
		return parameterdataDao.getDefaultPara(userId, testjobId);
	}
	
	public List<RunJob> getAllRunJob(String jobId) throws BaseException {
		return runJobDao.findAllRunJobByJobId(jobId);
	}
	
	public void saveUserTag(UserTag ut) throws BaseException{
		testjobDao.saveUserTag(ut);
	}
	
	public void saveRunJob(RunJob runjob) throws BaseException{
		runJobDao.saveOrUpdate(runjob);
	}
	
	public RunJob getRunJobById(String runJobId) throws BaseException{
		return runJobDao.findRunJobById(runJobId);
	}
	
	public void saveRunCase(RunCase runCase) throws BaseException{
		runCaseDao.saveOrUpdate(runCase);
	}

	public RunCase getRunCaseById(String runCaseId) throws BaseException{
		return runCaseDao.getRunCaseById(runCaseId);
	}
	
	public List<Server> getJobServer(String testjobId, int userId) throws BaseException{
		return testjobserverDao.findServerByJob(testjobId, userId);
	}
	
	public List<Testcase> getTestcaseByJobId(String testjobId) throws BaseException{
		return testjobcaseDao.findCaseByJob(testjobId);
	}
	public List<Testplan> getTestplanByJobId(String testjobId) throws BaseException{
		return testjobcaseDao.findPlanByJob(testjobId);
	}
	
	public List<UserTag> getUserTag(Integer userId) throws BaseException{
		return testjobDao.getUserTag(userId);
	}
	
	public List<DefectInfoVo> getDefectInfoByTestJobId(String testjobId) throws BaseDaoException{
		return defectinfoDao.getDefectInfoByTestJobId(testjobId);
	}

	public TestjobcaseDao getTestjobcaseDao() {
		return testjobcaseDao;
	}

	public void setTestjobcaseDao(TestjobcaseDao testjobcaseDao) {
		this.testjobcaseDao = testjobcaseDao;
	}

	public TestjobplanDao getTestjobplanDao() {
		return testjobplanDao;
	}

	public void setTestjobplanDao(TestjobplanDao testjobplanDao) {
		this.testjobplanDao = testjobplanDao;
	}

	public TestjobserverDao getTestjobserverDao() {
		return testjobserverDao;
	}

	public void setTestjobserverDao(TestjobserverDao testjobserverDao) {
		this.testjobserverDao = testjobserverDao;
	}

	public RuncaseserverDao getRuncaseserverDao() {
		return runcaseserverDao;
	}

	public void setRuncaseserverDao(RuncaseserverDao runcaseserverDao) {
		this.runcaseserverDao = runcaseserverDao;
	}

	public RuncaseparameterDao getRuncaseparameterDao() {
		return runcaseparameterDao;
	}

	public void setRuncaseparameterDao(RuncaseparameterDao runcaseparameterDao) {
		this.runcaseparameterDao = runcaseparameterDao;
	}

	public RunJobDao getRunJobDao() {
		return runJobDao;
	}

	public void setRunJobDao(RunJobDao runJobDao) {
		this.runJobDao = runJobDao;
	}

	public RunCaseDao getRunCaseDao() {
		return runCaseDao;
	}

	public void setRunCaseDao(RunCaseDao runCaseDao) {
		this.runCaseDao = runCaseDao;
	}

	public ParameterDao getParameterDao() {
		return parameterDao;
	}

	public void setParameterDao(ParameterDao parameterDao) {
		this.parameterDao = parameterDao;
	}

	public ParameterdataDao getParameterdataDao() {
		return parameterdataDao;
	}

	public void setParameterdataDao(ParameterdataDao parameterdataDao) {
		this.parameterdataDao = parameterdataDao;
	}

	public DefectinfoDao getDefectinfoDao() {
		return defectinfoDao;
	}

	public void setDefectinfoDao(DefectinfoDao defectinfoDao) {
		this.defectinfoDao = defectinfoDao;
	}

	
}
