package com.hp.titan.test.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.project.model.Project;
import com.hp.titan.server.model.Server;
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
import com.hp.titan.test.vo.DefectInfoVo;
import com.hp.titan.test.vo.ParameterVo;
import com.hp.titan.test.vo.RunCaseVo;
import com.hp.titan.test.vo.TestcaserunVo;
import com.hp.titan.test.vo.TestjobVo;
import com.hp.titan.test.vo.TestjobrunVo;

public interface TestjobService {

	public void saveTestjob(Testjob testjob) throws BaseException;
	
	public void saveTestJobServer(TestJobServer testjobServer) throws BaseException;
	
	public void saveTestJobServerId(TestJobServerId testjobServerId) throws BaseException;
	
	public List<Testjob> getAllTestjob() throws BaseException;
	
	public List<Testjob> findTestjobBySprint(String sprintId) throws BaseException;
	
	public List<TestjobVo> getAllTestjobByGroup(Integer groupId) throws BaseException;
	
	public String newTestjobCodeByProject()throws BaseException;
	
	public boolean isExistTestjob(String testjobName) throws BaseException;
	
	public List<TestjobVo> findAllTestjob(TestjobVo testjobVo, Set<Project> s) throws BaseException;
	
	public Testjob getTestjobById(String testjobId) throws BaseException;
	
	public Testjob getTestjobByCode(String testJobCode) throws BaseException;
	
	public void jobManagement(String testJobId, String testJobJson) throws BaseException;
	
	public void jobManagement(String testJobId, List<TestJobPlan> testjobplanList, List<TestJobCase> testjobcaseList) throws BaseException;
	
	public List<Server> getJobServer(String testjobId, int userId) throws BaseException;
	
//	@SuppressWarnings("unchecked")
//	public void saveRunCasePorperty(Map runCaseServerMap) throws BaseException;
	
//	public Server getServerInfo(String testjobId, String testPlanId, String testCaseId) throws BaseException;
	
	public List<ParameterVo> getParameterInfo(String testjobId, String testPlanId, String testCaseId) throws BaseException;
	
	public List<Testcase> getTestcaseByJobId(String testjobId) throws BaseException;
	
	public List<Testplan> getTestplanByJobId(String testjobId) throws BaseException;
	
	public void deleteTestJob(String testjobId) throws BaseException;
	
	public void clearServerFromJob(String testjobId) throws BaseException;
	
	public List<RunJob> getAllRunJob(String jobId) throws BaseException;
	
	public void saveRunJob(RunJob runJob) throws BaseException;
	
	public RunJob getRunJobById(String runJobId) throws BaseException;
	
	public void saveRunCase(RunCase runCase) throws BaseException;
	
	public RunCase getRunCaseById(String runCaseId) throws BaseException;
	
	public List<RunCase> getRunCaseByRunjobId(String runJobId) throws BaseException;
	
	public List<RunCase> getRunCaseByResult(String runJobId, String result) throws BaseException;
	
	public RunCase getRunCaseByVo(RunCaseVo rcv) throws BaseException;
	
	public TestjobrunVo getRunJobInfoById(String runJobId) throws BaseException;
	
	public List<TestcaserunVo> getRunCaseInfoById(String runJobId) throws BaseException;
	
	public List<Parameter> getParameterByJobId(String runJobId) throws BaseException;
	
	public List<Parameter> getParameterByJobPlanId(String runJobId) throws BaseException;
	
	public List<Parameter> getServerParaByJobId(String runJobId) throws BaseException;
	
	public List<Parameter> getServerParaByJobPlanId(String runJobId) throws BaseException;
	
	public void saveDefaultePara(List<DefaultParameter> dpl, int userId) throws BaseException;
	
	public Map<String, String> getDefaultParaMap(int userId, String projectId) throws BaseException;
	
	public List<DefaultParameter> getDefaultParaList(int userId, String testjobId) throws BaseException;
	
	public void saveUserTag(UserTag ut) throws BaseException;
	
	public List<UserTag> getUserTag(Integer userId) throws BaseException;
	
	public List<DefectInfoVo> getDefectInfoByTestJobId(String testjobId) throws BaseDaoException;
	
	public ParameterData findParameterDataByParaName(String paraName) throws BaseException;
	
}
