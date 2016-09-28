package com.hp.titan.test.service;

import java.util.List;
import java.util.Set;

import com.hp.app.exception.BaseException;
import com.hp.titan.project.model.Project;
import com.hp.titan.test.model.TestPlanCase;
import com.hp.titan.test.model.Testcase;
import com.hp.titan.test.model.Testplan;
import com.hp.titan.test.vo.TestplanVo;

public interface TestplanService {

	public void saveTestplan(Testplan testplan) throws BaseException;
	
	public List<Testplan> getAllTestplan() throws BaseException;
	
	public String newTestplanCodeByProject(String projectId)throws BaseException;
	
	public boolean isExistTestplan(String testplanName) throws BaseException;
	
	public List<TestplanVo> findAllTestplan(TestplanVo testplanVo, Set<Project> s ) throws BaseException;
	
	public Testplan getTestplanById(String testplanId) throws BaseException;
	
	public List<Testplan> getAllTestplanByType(String type, String defaultProjectId) throws BaseException;
	
	public void planManagement(String testPlanId, List<TestPlanCase> testplancaseList) throws BaseException;
	
	public List<TestPlanCase> getPlanCase(String testPlanId) throws BaseException;
	
	public List<Testcase> getTestcaseByPlan(String testplanId) throws BaseException;
	
	public Boolean checkCaseHasBeenUsed(String testPlanId) throws BaseException;
	
}
