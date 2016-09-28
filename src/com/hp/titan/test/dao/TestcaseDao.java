package com.hp.titan.test.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.project.model.Project;
import com.hp.titan.test.model.CaseDefect;
import com.hp.titan.test.model.TestJobCase;
import com.hp.titan.test.model.TestPlanCase;
import com.hp.titan.test.model.Testcase;
import com.hp.titan.test.vo.RunCaseVo;
import com.hp.titan.test.vo.TestcaseVo;

public class TestcaseDao extends DefaultBaseDao<Testcase, String> {

	public List<Testcase> findAllTestcase() throws BaseDaoException {
		String hql = "from Testcase as testcase";
		return this.getHibernateTemplate().find(hql);
	}
	
	public List<Testcase> findAllTestcaseByType(String type, String defaultProjectId) throws BaseDaoException {
		String hql = "from Testcase as testcase where isValid=0 and testcase.type=? and testcase.projectId=?";
		return this.getHibernateTemplate().find(hql, type, defaultProjectId);
	}
	
	public Testcase getTestcaseByCaseName(String caseName) throws BaseDaoException {
		String hql = "from Testcase tc where tc.caseName = ? and isValid=0";
		List<Testcase> testcaseList = this.getHibernateTemplate().find(hql, caseName);
		return testcaseList.size()>0 ? testcaseList.get(0) : null;
	}
	
	public Testcase getTestcaseByCaseId(String caseId) throws BaseDaoException {
		String hql = "from Testcase tc where tc.caseId = ? and isValid=0";
		List<Testcase> testcaseList = this.getHibernateTemplate().find(hql, caseId);
		return testcaseList.size()>0 ? testcaseList.get(0) : null;
	}
	
	public String findMaxCodeByProject(String projectId) throws BaseDaoException {
		String hql = 
				"select max(tc.caseCode) from Testcase as tc where tc.projectId = ?";		
		List<String> result = this.getHibernateTemplate().find(hql, projectId);
		
		String maxCaseCode = null;
		if(result != null){
			maxCaseCode = (String) result.get(0);
		}
		
		return maxCaseCode;
	}

	public void saveOrUpdate(Testcase testcase) throws BaseDaoException {
		this.getHibernateTemplate().saveOrUpdate(testcase);
	}

	public List<TestcaseVo> findAllTestcase(TestcaseVo testcaseVo, Set<Project> s) throws BaseDaoException {
		
		List<TestcaseVo> rtn = new ArrayList<TestcaseVo>();
		String sql = "select tv.*, pm.MODULE_NAME from ("
				+ "select t.CASE_ID, t.CASE_CODE, t.CASE_NAME, t.TYPE, t.DESCRIPTION, t.OWNER_ID, t.PROJECT_ID, t.MODULE_ID, "
			    + "u.USER_CODE, p.PROJECT_NAME "
			    + "from TESTCASE t, titan_user u, PROJECT p "
			    + "where t.OWNER_ID=u.USER_ID and t.PROJECT_ID=p.PROJECT_ID and t.IS_VALID=0"
			    + ") tv left join PROJECT_MODULE pm on tv.MODULE_ID=pm.MODULE_ID where 1=1";
		if (testcaseVo != null) {
			if (testcaseVo.getCaseCode() != null && !testcaseVo.getCaseCode().equals("")){
				sql += " and tv.CASE_CODE='" + testcaseVo.getCaseCode() + "'";
			}
			if (testcaseVo.getCaseName() != null && !testcaseVo.getCaseName().equals("")){
				sql += " and tv.CASE_NAME like '%" + testcaseVo.getCaseName() + "%'";
			}
			if (testcaseVo.getCaseType() != null && !testcaseVo.getCaseType().equals("")){
				sql += " and tv.TYPE like '%" + testcaseVo.getCaseType() + "%'";
			}
			if (testcaseVo.getOwnerId() != null && !testcaseVo.getOwnerId().equals("")){
				sql += " and tv.OWNER_ID='" + testcaseVo.getOwnerId() + "'";
			}
			if (testcaseVo.getProjectId() != null && !testcaseVo.getProjectId().equals("")){
				sql += " and tv.PROJECT_ID='" + testcaseVo.getProjectId() + "'";
			}
			if (testcaseVo.getModuleId() != null && !testcaseVo.getModuleId().equals("")){
				sql += " and tv.MODULE_ID='" + testcaseVo.getModuleId() + "'";
			}
		}
		if(s!=null && s.size() != 0){
			Iterator<Project> it = s.iterator();
			sql += " and tv.PROJECT_ID in(";
			while(it.hasNext()){
				Project p = it.next();
				sql += "'" + p.getProjectId()+"',";
			}
			sql += ")";
		}

		Query query = this.getSession().createSQLQuery(sql.replace(",)", ")"));		
		List<Object[]> objects = query.list();
		for (Object[] object : objects){
			TestcaseVo vo = new TestcaseVo();
			vo.setCaseID(object[0].toString());
			vo.setCaseCode(object[1].toString());
			vo.setCaseName(object[2].toString());
			vo.setCaseType(object[3].toString());
			vo.setDescription(object[4].toString());
			if (object[5] != null){
				vo.setOwner(object[8].toString());				
			}
			if (object[6] != null){
				vo.setProject(object[9].toString());
			}
			if (object[10] != null && !object[10].equals("")){
				vo.setModule(object[10].toString());
			}
			rtn.add(vo);
		}
		return rtn;
	}	
	
	public List<RunCaseVo> getRunCaseHistory(String caseId, String testjobId) throws BaseDaoException{
		List<RunCaseVo> rcVoList = new ArrayList<RunCaseVo>();
		String sql = "SELECT rc.START_TIME, rc.END_TIME, rc.STATE, tu.USER_CODE, rc.RUN_CASE_ID, rc.RUN_JOB_ID FROM run_case AS rc, titan_user as tu, run_job as rj WHERE rc.CREATE_USER_ID = tu.USER_ID AND rj.RUN_JOB_ID = rc.RUN_JOB_ID  AND rc.CASE_ID = '";
		sql += caseId;
		sql += "' AND rj.JOB_ID = '";
		sql += testjobId;
		sql += "' ORDER BY rc.START_TIME DESC";
		
		Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		for (Object[] object : objects){
			RunCaseVo rcVo = new RunCaseVo();
			rcVo.setStartTime(String.valueOf(object[0]));
			rcVo.setEndTime(String.valueOf(object[1]));
			rcVo.setState(String.valueOf(object[2]));
			rcVo.setRunUnser(String.valueOf(object[3]));
			rcVo.setRuncaseId(String.valueOf(object[4]));
			rcVo.setRunjobId(String.valueOf(object[5]));
			rcVoList.add(rcVo);
		}
		return rcVoList;
	}
	
	public void saveCaseDefect(CaseDefect cd) throws BaseDaoException{
		this.getHibernateTemplate().saveOrUpdate(cd);
	}
	
	public List<CaseDefect> getCaseDefectList(String runcaseId) throws BaseDaoException{
		String hql = "from CaseDefect cd where cd.runcaseId = ?";
		List<CaseDefect> caseDefectList = this.getHibernateTemplate().find(hql, runcaseId);
		return caseDefectList.size()>0 ? caseDefectList : null;
	}
	
	public int getdefectCount(String testcaseId, String testjobId) throws BaseDaoException{
		String sql = "SELECT DISTINCT(cd.DEFECT_NUMBER) FROM testcase as tc, run_case as rc, case_defect as cd, run_job as rj WHERE tc.CASE_ID = rc.CASE_ID AND cd.RUN_CASE_ID = rc.RUN_CASE_ID AND rj.RUN_JOB_ID = rc.RUN_JOB_ID AND tc.CASE_ID = '";
		sql += testcaseId;
		sql += "' AND rj.JOB_ID = '";
		sql += testjobId;
		sql += "'";
		Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		if(null != objects && objects.size() != 0){
			return objects.size();
		}
		return 0;
	}
	
	public List<TestPlanCase> getPlanCaseByCaseId(String caseId){
		String hql = "from TestPlanCase pc where pc.id.testcaseId = ?";
		return this.getHibernateTemplate().find(hql, caseId);
	}
	
	public List<TestJobCase> getJobCaseByCaseId(String caseId){
		String hql = "from TestJobCase jc where jc.id.testcaseId = ?";
		return this.getHibernateTemplate().find(hql, caseId);
	}

}
