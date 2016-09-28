package com.hp.titan.test.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

import com.hp.app.exception.BaseException;
import com.hp.titan.common.vo.DefectVo;
import com.hp.titan.common.vo.LoginUserVo;
import com.hp.titan.project.model.Project;
import com.hp.titan.project.vo.ReportVo;
import com.hp.titan.test.model.CaseDefect;
import com.hp.titan.test.model.CaseLog;
import com.hp.titan.test.model.DefectInfo;
import com.hp.titan.test.model.Parameter;
import com.hp.titan.test.model.ParameterData;
import com.hp.titan.test.model.RunCaseServer;
import com.hp.titan.test.model.TestJobCase;
import com.hp.titan.test.model.TestPlanCase;
import com.hp.titan.test.model.Testcase;
import com.hp.titan.test.vo.RunCaseVo;
import com.hp.titan.test.vo.TestcaseVo;

public interface TestcaseService {

	public List<Testcase> getAllTestcase() throws BaseException;
	
	public List<TestcaseVo> findAllTestcase(TestcaseVo testcaseVo, Set<Project> s) throws BaseException;
	
	public Testcase getTestcaseById(String caseId) throws BaseException;
	
	public boolean isExistTestcase(String caseName) throws BaseException;
	
	public String newTestcaseCodeByProject(String projectId) throws BaseException;
	
	public void saveTestcase(Testcase testcase) throws BaseException;
	
	public void saveParameters(List<Parameter> parameterList) throws BaseException;
	
	public List<Parameter> getParameters(String caseId) throws BaseException;
	
	public List<Parameter> getParametersByCaseId(String caseId) throws BaseException;
	
	public void saveCaseLogs(List<CaseLog> caseLogList) throws BaseException;
	
	public List<CaseLog> getCaseLogs(String caseId) throws BaseException ;	
	
	public List<Testcase> getAllTestcaseByType(String type, String defaultProjectId) throws BaseException;
	
	public List<String> getFileListByTestcaseId(String testcaseId) throws BaseException;
	
	public void moveTestcaseFiles(String testcaseId, String newPath) throws BaseException;
	
	public void deleteParametersByTestcaseId(String testcaseId) throws BaseException;

	public void deleteCaseLogsByTestcaseId(String testcaseId) throws BaseException;
	
	public void deleteParameter(Parameter param) throws BaseException;

	public void deleteCaseLog(CaseLog log) throws BaseException;

	public void deleteTestcase(String testcaseId) throws BaseException;
	
	public void deleteTestcaseFile(String testcaseId, String fileName) throws BaseException;
	
	public List<ParameterData> getParameterData(String type)  throws BaseException;
	
	public void saveCaseServer(RunCaseServer runCaseServer) throws BaseException;
	
	public void deleteCaseServer(String caseId) throws BaseException;
	
	public RunCaseServer findCaseServerById(String caseId) throws BaseException;
	
	public List<Parameter> getParaResult(String caseId) throws BaseException;
	
	public List<RunCaseVo> getRunCaseHistory(String caseId, String testjobId) throws BaseException;
	
	public List<CaseDefect> getCaseDefectList(String runcaseId) throws BaseException;
	
	public void saveCaseDefect(CaseDefect cd) throws BaseException;
	
	public int getdefectCount(String testcaseId, String testjobId) throws BaseException;
	
	public void saveDefectInfo(DefectInfo di) throws BaseException;
	
	public List<DefectInfo> getDefectInfoByBean(DefectInfo di) throws BaseException;
	
	public List<DefectInfo> getDefectListByState(String state) throws BaseException;
	
	public DefectVo getDefectInfoByDefectNoFromRally(String defectNum) throws BaseException, IOException;
	
	public DefectVo getDefectInfoByDefectNoFromQuix(String defectNum) throws BaseException;
	
	public LoginUserVo getUserInfoFromRallyByObjectId(String userNameInRally) throws BaseException, IOException;
	
	public List<CaseDefect> getCaseDefectByDefectNo(String defectNo) throws BaseException;
	
	public Boolean checkCaseHasBeenUsed(String caseId) throws BaseException;
	
	public ReportVo getRunCaseReportByPeriod(String projectId, String startDate, String endDate) throws BaseException;
	
	public List<DefectInfo> goDefectDetail(String projectId, String startDate, String endDate) throws BaseException, ParseException;
	
	public Integer getCaseNumByResult(String caseId, String state) throws BaseException;
	
	public Integer getCaseNumByResult(String caseId, String state, String projectId) throws BaseException;
	
	public List<TestPlanCase> getPlanByCaseId(String caseId) throws BaseException;
	
	public List<TestJobCase> getJobByCaseId(String caseId) throws BaseException;
}
