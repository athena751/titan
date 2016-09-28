package com.hp.titan.test.service.impl;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

import com.hp.app.common.file.FileUpload;
import com.hp.app.exception.BaseException;
import com.hp.quix.dao.QuixDao;
import com.hp.titan.common.util.UnitCodeUtil;
import com.hp.titan.common.vo.DefectVo;
import com.hp.titan.common.vo.LoginUserVo;
import com.hp.titan.project.model.Project;
import com.hp.titan.project.vo.ReportVo;
import com.hp.titan.rally.dao.RallyDao;
import com.hp.titan.test.dao.CaseLogDao;
import com.hp.titan.test.dao.DefectinfoDao;
import com.hp.titan.test.dao.ParameterDao;
import com.hp.titan.test.dao.ParameterdataDao;
import com.hp.titan.test.dao.RunCaseDao;
import com.hp.titan.test.dao.RuncaseserverDao;
import com.hp.titan.test.dao.TestcaseDao;
import com.hp.titan.test.model.CaseDefect;
import com.hp.titan.test.model.CaseLog;
import com.hp.titan.test.model.DefectInfo;
import com.hp.titan.test.model.Parameter;
import com.hp.titan.test.model.ParameterData;
import com.hp.titan.test.model.RunCaseServer;
import com.hp.titan.test.model.TestJobCase;
import com.hp.titan.test.model.TestPlanCase;
import com.hp.titan.test.model.Testcase;
import com.hp.titan.test.service.TestcaseService;
import com.hp.titan.test.vo.RunCaseVo;
import com.hp.titan.test.vo.TestcaseVo;

public class TestcaseServiceImpl implements TestcaseService {

	private TestcaseDao testcaseDao;
	private RunCaseDao runCaseDao;
	private TestcaseVo testcaseVo;
	private CaseLogDao caseLogDao;
	private ParameterDao parameterDao;
	private ParameterdataDao parameterdataDao;
	private RuncaseserverDao runcaseserverDao;
	private DefectinfoDao defectinfoDao;
	private RallyDao rallyDao;
	
	@Override
	public List<Testcase> getAllTestcase() throws BaseException{
	     return	testcaseDao.findAllTestcase();
	}
	
	public List<TestcaseVo> findAllTestcase(TestcaseVo testcaseVo, Set<Project> s) throws BaseException{
		return testcaseDao.findAllTestcase(testcaseVo, s);		
	}
	
	public Testcase getTestcaseById(String caseId) throws BaseException{
		return testcaseDao.getTestcaseByCaseId(caseId);
	}

	@Override
	public boolean isExistTestcase(String caseName) throws BaseException {
		boolean isExist = false;
		Testcase testcase = testcaseDao.getTestcaseByCaseName(caseName);
		if(testcase != null){
			isExist=true;
		}
		return isExist;
	}

	@Override
	public String newTestcaseCodeByProject(String projectId)
			throws BaseException {
		
		String maxCode = testcaseDao.findMaxCodeByProject(projectId);		
		String newCode = UnitCodeUtil.generateUnitCode("TC", maxCode);				
		return newCode;
	}
	
	public List<Testcase> getAllTestcaseByType(String type, String defaultProjectId) throws BaseException{
		return	testcaseDao.findAllTestcaseByType(type, defaultProjectId);
	}

	@Override
	public void saveTestcase(Testcase testcase) throws BaseException {
		testcaseDao.saveOrUpdate(testcase);
	}

	@Override
	public void saveParameters(List<Parameter> parameterList)
			throws BaseException {		
		for(Parameter param : parameterList){
			parameterDao.saveOrUpdate(param);
		}
	}

	@Override
	public void saveCaseLogs(List<CaseLog> caseLogList) throws BaseException {
		for(CaseLog log : caseLogList){
			caseLogDao.saveOrUpdate(log);
		}
	}
	
	@Override
	public List<Parameter> getParameters(String caseId) throws BaseException {	
		return parameterDao.findByTestcaseId(caseId);		
	}
	
	@Override
	public List<Parameter> getParametersByCaseId(String caseId) throws BaseException{
		return parameterDao.findParameterListByTestcaseId(caseId);	
	}
	
	@Override
	public List<CaseLog> getCaseLogs(String caseId) throws BaseException {	
		return caseLogDao.findByTestcaseId(caseId);		
	}

	@Override
	public List<String> getFileListByTestcaseId(String testcaseId)
			throws BaseException {
		
		Testcase testcase = getTestcaseById(testcaseId);
		String path = testcase.getLocation();
		
		List<String> fileList = null;
		if(path!=null && !"".equals(path))
			fileList = FileUpload.getFileNames(path);
		
		return fileList;
	}

	@Override
	public void moveTestcaseFiles(String testcaseId, String newPath)
			throws BaseException {
		
		Testcase testcase = getTestcaseById(testcaseId);
		String path = testcase.getLocation();
		
		if(path!=null && !path.equals(newPath))
			FileUpload.move(path, newPath);
	}

	@Override
	public void deleteParametersByTestcaseId(String testcaseId)
			throws BaseException {
		List<Parameter> parameterList = parameterDao.findParameterListByTestcaseId(testcaseId);
		parameterDao.deleteAll(parameterList);
	}

	@Override
	public void deleteCaseLogsByTestcaseId(String testcaseId)
			throws BaseException {
		List<CaseLog> caseLogList = caseLogDao.findByTestcaseId(testcaseId);
		caseLogDao.deleteAll(caseLogList);
	}

	@Override
	public void deleteParameter(Parameter param) throws BaseException {
		parameterDao.delete(param);
	}

	@Override
	public void deleteCaseLog(CaseLog log) throws BaseException {
		caseLogDao.delete(log);
	}

	@Override
	public void deleteTestcase(String testcaseId) throws BaseException {
		Testcase testcase = getTestcaseById(testcaseId);
		String path = testcase.getLocation();
		
		FileUpload.delete(path);
		
		this.deleteCaseLogsByTestcaseId(testcaseId);
		this.deleteParametersByTestcaseId(testcaseId);
		this.deleteCaseServer(testcaseId);
		testcaseDao.delete(testcaseId);
	}

	@Override
	public void deleteTestcaseFile(String testcaseId, String fileName)
			throws BaseException {
		Testcase testcase = getTestcaseById(testcaseId);
		String path = testcase.getLocation();
		String delFile = path + fileName;
		FileUpload.delete(delFile);
	}
	
	public List<ParameterData> getParameterData(String type) throws BaseException{
		if(null != type && !"".equals(type)){
			return parameterdataDao.findByType(type);
		}
		return parameterdataDao.findAllParameterData();
	}

	public void saveCaseServer(RunCaseServer runCaseServer) throws BaseException{
		runcaseserverDao.save(runCaseServer);
	}
	
	public void deleteCaseServer(String caseId) throws BaseException{
		runcaseserverDao.deleteByCaseId(caseId);
	}
	
	public RunCaseServer findCaseServerById(String caseId) throws BaseException{
		return runcaseserverDao.findCaseServerById(caseId);
	}
	
	public List<Parameter> getParaResult(String caseId) throws BaseException{
		return parameterDao.getParaResult(caseId);
	}
	
	public List<RunCaseVo> getRunCaseHistory(String caseId, String testjobId) throws BaseException{
		return testcaseDao.getRunCaseHistory(caseId, testjobId);
	}
	
	public void saveCaseDefect(CaseDefect cd) throws BaseException{
		testcaseDao.saveCaseDefect(cd);
	}
	
	public List<CaseDefect> getCaseDefectList(String runcaseId) throws BaseException{
		return testcaseDao.getCaseDefectList(runcaseId);
	}
	
	public int getdefectCount(String testcaseId, String testjobId) throws BaseException{
		return testcaseDao.getdefectCount(testcaseId, testjobId);
	}
	
	public void saveDefectInfo(DefectInfo di) throws BaseException{
		defectinfoDao.saveOrUpdate(di);
	}
	
	/**
	 * Get defect info from rally by defect number
	 * @param defectNum
	 * @return
	 * @throws IOException 
	 * @throws IOException
	 */
	public DefectVo getDefectInfoByDefectNoFromRally(String defectNum) throws BaseException, IOException{
		return rallyDao.getDefectInfoByDefectNo(defectNum);
	}
	
	public DefectVo getDefectInfoByDefectNoFromQuix(String defectNum) throws BaseException{
		QuixDao qd = new QuixDao();
		return qd.getQuixVoByQuixNum(defectNum);
	}
	
	/**
	 * Get user info from rally by user name
	 * @return
	 * @throws IOException 
	 */
	public LoginUserVo getUserInfoFromRallyByObjectId(String ObjectId) throws BaseException, IOException{
		return rallyDao.getUserInfoFromRallyByObjectId(ObjectId);
	}
	
	public List<DefectInfo> getDefectInfoByBean(DefectInfo di) throws BaseException{
		return defectinfoDao.getDefectInfoByBean(di);
	}
	
	public List<DefectInfo> getDefectListByState(String state) throws BaseException{
		return defectinfoDao.getDefectListByState(state);
	}
	
	public List<CaseDefect> getCaseDefectByDefectNo(String defectNo) throws BaseException{
		return defectinfoDao.getCaseDefectByDefectNo(defectNo);
	}
	
	public Boolean checkCaseHasBeenUsed(String caseId) throws BaseException{
		List<TestPlanCase> pcList = testcaseDao.getPlanCaseByCaseId(caseId);
		if(null != pcList && pcList.size() != 0){
			return true;
		}
		List<TestJobCase> jcList = testcaseDao.getJobCaseByCaseId(caseId);
		if(null != jcList && jcList.size() != 0){
			return true;
		}
		return false;
	}
	
	public boolean getPlanNameHasBeenUsed(String caseId) throws BaseException{
		List<TestPlanCase> pcList = testcaseDao.getPlanCaseByCaseId(caseId);
		if(null != pcList && pcList.size() != 0){
			return true;
		}
		List<TestJobCase> jcList = testcaseDao.getJobCaseByCaseId(caseId);
		if(null != jcList && jcList.size() != 0){
			return true;
		}
		return false;
	}
	
	public List<TestPlanCase> getPlanByCaseId(String caseId) throws BaseException{
		return testcaseDao.getPlanCaseByCaseId(caseId);
		
	}
	
	public List<TestJobCase> getJobByCaseId(String caseId) throws BaseException{
		return testcaseDao.getJobCaseByCaseId(caseId);
	}
	
	public ReportVo getRunCaseReportByPeriod(String projectId, String startDate, String endDate) throws BaseException{
		Integer passCount = runCaseDao.getRunCaseCountByResult(projectId, startDate, endDate, "PASS");
		Integer failCount = runCaseDao.getRunCaseCountByResult(projectId, startDate, endDate, "FAIL");
		Integer hasDefectCount = runCaseDao.getCaseDefectCountByResult(projectId, startDate, endDate);
		Integer defectFixedCount = runCaseDao.getDefectFixedCountByResult(projectId, startDate, endDate, "Fixed");
		Integer defectNotFixedCount = runCaseDao.getDefectFixedCountByResult(projectId, startDate, endDate, null);
		
		NumberFormat numberFormat1 = NumberFormat.getInstance();
	   	numberFormat1.setMaximumFractionDigits(1);
	   	String passCent = "0", failCent = "0", hasdefectCent = "0", nodefectCent = "0", defectfixedCent = "0", defectnotfixedCent = "0";
	   	if(passCount+failCount != 0){
	   		passCent = numberFormat1.format((float)passCount/(float)(passCount+failCount)*100);
	   		failCent = numberFormat1.format((float)failCount/((float)passCount+(float)failCount)*100);
	   	}
	   	if(failCount != 0){
	   		hasdefectCent = numberFormat1.format((float)hasDefectCount/(float)failCount*100);
	   		nodefectCent = numberFormat1.format(((float)failCount-(float)hasDefectCount)/(float)failCount*100);
	   	}
	   	if(defectFixedCount + defectNotFixedCount != 0){
		   	defectfixedCent = numberFormat1.format((float)defectFixedCount/((float)defectFixedCount+(float)defectNotFixedCount)*100);
		   	defectnotfixedCent = numberFormat1.format((float)defectNotFixedCount/((float)defectFixedCount+(float)defectNotFixedCount)*100);
	   	}
		
		ReportVo rVo = new ReportVo();
		rVo.setTestcasePass(passCount);
		rVo.setTestcaseFail(failCount);
		rVo.setHasDefect(hasDefectCount);
		rVo.setDefectFixed(defectFixedCount);
		rVo.setNoDefect(failCount - hasDefectCount);
		rVo.setDefectNotfixed(defectNotFixedCount);
		rVo.setPassCent(passCent);
		rVo.setFailCent(failCent);
		rVo.setHasdefectCent(hasdefectCent);
		rVo.setNodefectCent(nodefectCent);
		rVo.setDefectfixedCent(defectfixedCent);
		rVo.setDefectnotfixedCent(defectnotfixedCent);
		return rVo;
	}
	
	public List<DefectInfo> goDefectDetail(String projectId, String startDate, String endDate) throws BaseException, ParseException{
		List<DefectInfo> res = runCaseDao.getDefectDetail(projectId, startDate, endDate);
		return res;
	}
	
	public Integer getCaseNumByResult(String caseId, String state) throws BaseException{
		return runCaseDao.getCaseNumByResult(caseId, state);
	}
	public Integer getCaseNumByResult(String caseId, String state, String projectId) throws BaseException{
		return runCaseDao.getCaseNumByResult(caseId, state, projectId);
	}

	public TestcaseDao getTestcaseDao() {
		return testcaseDao;
	}
	
	public void setTestcaseDao(TestcaseDao testcaseDao) throws BaseException {
		this.testcaseDao = testcaseDao;
	}

	public TestcaseVo getTestcaseVo() {
		return testcaseVo;
	}

	public void setTestcaseVo(TestcaseVo testcaseVo) {
		this.testcaseVo = testcaseVo;
	}

	public CaseLogDao getCaseLogDao() {
		return caseLogDao;
	}

	public void setCaseLogDao(CaseLogDao caseLogDao) {
		this.caseLogDao = caseLogDao;
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

	public RuncaseserverDao getRuncaseserverDao() {
		return runcaseserverDao;
	}

	public void setRuncaseserverDao(RuncaseserverDao runcaseserverDao) {
		this.runcaseserverDao = runcaseserverDao;
	}

	public DefectinfoDao getDefectinfoDao() {
		return defectinfoDao;
	}

	public void setDefectinfoDao(DefectinfoDao defectinfoDao) {
		this.defectinfoDao = defectinfoDao;
	}

	public RallyDao getRallyDao() {
		return rallyDao;
	}

	public void setRallyDao(RallyDao rallyDao) {
		this.rallyDao = rallyDao;
	}

	public RunCaseDao getRunCaseDao() {
		return runCaseDao;
	}

	public void setRunCaseDao(RunCaseDao runCaseDao) {
		this.runCaseDao = runCaseDao;
	}
}
