package com.hp.titan.test.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.hp.app.exception.BaseException;
import com.hp.titan.common.util.UnitCodeUtil;
import com.hp.titan.project.model.Project;
import com.hp.titan.test.dao.TestplanDao;
import com.hp.titan.test.dao.TestplancaseDao;
import com.hp.titan.test.model.TestJobPlan;
import com.hp.titan.test.model.TestPlanCase;
import com.hp.titan.test.model.Testcase;
import com.hp.titan.test.model.Testplan;
import com.hp.titan.test.service.TestplanService;
import com.hp.titan.test.vo.TestplanVo;

public class TestplanServiceImpl implements TestplanService {

	private TestplanDao testplanDao;
	private TestplancaseDao testplancaseDao;
	
	@Override
	public void saveTestplan(Testplan testplan) throws BaseException {
		testplanDao.saveOrUpdate(testplan);
	}

	@Override
	public List<Testplan> getAllTestplan() throws BaseException{
	     return	testplanDao.findAllTestplan();
	}

	

	public TestplanDao getTestplanDao() {
		return testplanDao;
	}

	public void setTestplanDao(TestplanDao testplanDao) throws BaseException {
		this.testplanDao = testplanDao;
	}
	@Override
	public String newTestplanCodeByProject(String projectId)
			throws BaseException {
		
		String maxCode = testplanDao.findMaxCodeByProject(projectId);		
		String newCode = UnitCodeUtil.generateUnitCode("TP", maxCode);				
		return newCode;
	}
	public boolean isExistTestplan(String testplanName) throws BaseException {
		boolean isExist = false;
		Testplan testplan = testplanDao.getTestplanByName(testplanName);
		if(testplan != null){
			isExist=true;
		}
		return isExist;
	}
	
	public void planManagement(String testPlanId, List<TestPlanCase> testplancaseList) throws BaseException{
		this.deletePlanCaseByPlan(testPlanId);
		this.savePlanCases(testplancaseList);
	}
	
	public List<TestPlanCase> getPlanCase(String testPlanId) throws BaseException{
		return testplancaseDao.getPlanCase(testPlanId);
	}
	
	public void deletePlanCaseByPlan(String testPlanId) throws BaseException{
		testplancaseDao.deletePlanCaseByPlan(testPlanId);
	}
	
	public void savePlanCases(List<TestPlanCase> testplancaseList) throws BaseException{
		Iterator<TestPlanCase> l = testplancaseList.iterator();
		while(l.hasNext()){
			TestPlanCase tpc = l.next();
			testplancaseDao.save(tpc);
		}
	}
	public Boolean checkCaseHasBeenUsed(String testPlanId) throws BaseException{
		List<TestJobPlan> jpList = testplanDao.getJobPlanByPlanId(testPlanId);
		if(null != jpList && jpList.size() != 0){
			return true;
		}
		return false;
	}
	public List<Testcase> getTestcaseByPlan(String testplanId) throws BaseException{
		return testplancaseDao.getTestcaseByPlan(testplanId);
	}
	
	public List<Testplan> getAllTestplanByType(String type, String defaultProjectId) throws BaseException{
		 return	testplanDao.findAllTestplanByType(type, defaultProjectId);
	}
	public List<TestplanVo> findAllTestplan(TestplanVo testplanVo, Set<Project> s) throws BaseException{
		return testplanDao.findAllTestplan(testplanVo, s);		
	}

	public Testplan getTestplanById(String testplanId) throws BaseException{
		return testplanDao.getTestplanById(testplanId);		
	}

	public TestplancaseDao getTestplancaseDao() {
		return testplancaseDao;
	}

	public void setTestplancaseDao(TestplancaseDao testplancaseDao) {
		this.testplancaseDao = testplancaseDao;
	}
	
}
