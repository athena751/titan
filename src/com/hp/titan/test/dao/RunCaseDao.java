package com.hp.titan.test.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.hp.app.common.util.DateUtils;
import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.common.constants.TitanContent;
import com.hp.titan.test.model.DefectInfo;
import com.hp.titan.test.model.RunCase;
import com.hp.titan.test.vo.RunCaseVo;
import com.hp.titan.test.vo.TestcaserunVo;



public class RunCaseDao extends DefaultBaseDao<RunCase, String> {

	public void saveOrUpdate(RunCase runCase) throws BaseDaoException {
		this.getHibernateTemplate().saveOrUpdate(runCase);
	}
	
	public List<RunCase> getRunCaseByRunjobId(String runJobId) throws BaseDaoException {
		String hql = "from RunCase as runcase where isValid=0 and runcase.runJobId=?";
		return this.getHibernateTemplate().find(hql,runJobId);
	}
	
	public List<RunCase> getRunCaseByResult(String runJobId, String result) throws BaseDaoException {
		String hql = "from RunCase as runcase where isValid=0 and runcase.runJobId=? and runcase.state=?";
		return this.getHibernateTemplate().find(hql,runJobId,result);
	}
	
	
	public RunCase getRunCaseById(String runCaseId) throws BaseDaoException {
		String hql = "from RunCase as runcase where isValid=0 and runcase.runCaseId=?";
		List<RunCase> runcaseList = this.getHibernateTemplate().find(hql,runCaseId);
		return runcaseList.size()>0 ? runcaseList.get(0) : null;
	}
	
	public RunCase getRunCaseByVo(RunCaseVo rcv) throws BaseDaoException {
		String hql = "from RunCase as runcase where isValid=0 and runcase.runJobId=:runJobId and runcase.caseId=:caseId";
		if(null != rcv.getPlanId() && !"".equals(rcv.getPlanId())){
			hql += " and runcase.planId=:planId";
		}
		Query query = this.getSession().createQuery(hql);
		query.setParameter("runJobId", rcv.getRunjobId());
		query.setParameter("caseId", rcv.getCaseId());
		if(null != rcv.getPlanId() && !"".equals(rcv.getPlanId())){
			query.setParameter("planId", rcv.getPlanId());
		}
		List<RunCase> runcaseList =  query.list();
		return runcaseList.size()>0 ? runcaseList.get(0) : null;
	}
	
	public List<TestcaserunVo> getRunCaseInfoById(String runJobId) throws BaseException{
		StringBuffer s = new StringBuffer();
		s.append("SELECT tc.CASE_ID, tc.CASE_NAME, rc.RUN_CASE_ID, rc.STATE, rc.PLAN_ID, rc.STATE, rc.COMMAND FROM testcase tc, run_case rc WHERE tc.IS_VALID = 0 AND rc.IS_VALID = 0 AND tc.CASE_ID = rc.CASE_ID AND rc.RUN_JOB_ID = '");
		s.append(runJobId);
		s.append("'");
		Query query = this.getSession().createSQLQuery(s.toString());
		List<Object[]> objects = query.list();
		if(null != objects && objects.size() != 0){
			List<TestcaserunVo> resList = new ArrayList<TestcaserunVo>();
			Iterator<Object[]> l = objects.iterator();
			while(l.hasNext()){
				TestcaserunVo tcrVo = new TestcaserunVo();
				Object[] obj = l.next();
				tcrVo.setTestcaseId(String.valueOf(obj[0]));
				tcrVo.setTestcaseName(String.valueOf(obj[1]));
				tcrVo.setRuntestcaseId(String.valueOf(obj[2]));
				tcrVo.setTestcaseState(String.valueOf(obj[3]));
				if(null != obj[4]){
					tcrVo.setTestplanId(String.valueOf(obj[4]));
				}
				tcrVo.setState(String.valueOf(obj[5]));
				tcrVo.setCommand(String.valueOf(obj[6]));
				resList.add(tcrVo);
			}
			return resList;
		}
		else{
			return null;
		}
	}
	
	public Integer getRunCaseCountByResult(String projectId, String startDate, String endDate, String res) throws BaseException{
		StringBuffer s = new StringBuffer();
		s.append("SELECT rc.CASE_ID, rc.STATE FROM testcase AS tc, run_case AS rc WHERE rc.CASE_ID = tc.CASE_ID AND tc.PROJECT_ID = '" );
		s.append(projectId);
		s.append("'  AND rc.START_TIME > '");
		s.append(startDate);
		s.append(" ' AND rc.START_TIME < '");
		s.append(endDate);
		s.append("' GROUP BY rc.CASE_ID ");
		s.append("HAVING rc.STATE = '");
		s.append(res);
		s.append("'");
		Query query = this.getSession().createSQLQuery(s.toString());
		List<Object[]> objects = query.list();
		return Integer.valueOf(objects.size());
	}
	
	public Integer getCaseDefectCountByResult(String projectId, String startDate, String endDate) throws BaseException{
		StringBuffer s = new StringBuffer();
		s.append("SELECT rc.CASE_ID, rc.STATE, count(rc.RUN_CASE_ID) FROM testcase AS tc, run_case AS rc, case_defect AS cd WHERE rc.CASE_ID = tc.CASE_ID  AND rc.RUN_CASE_ID = cd.RUN_CASE_ID AND  rc.STATE = 'FAIL' AND tc.PROJECT_ID = '" );
		s.append(projectId);
		s.append("'  AND rc.START_TIME > '");
		s.append(startDate);
		s.append(" ' AND rc.START_TIME < '");
		s.append(endDate);
		s.append("' GROUP BY rc.CASE_ID HAVING count(rc.RUN_CASE_ID) > 0");
		Query query = this.getSession().createSQLQuery(s.toString());
		List<Object[]> objects = query.list();
		return Integer.valueOf(objects.size());
	}
	
	public Integer getDefectFixedCountByResult(String projectId, String startDate, String endDate, String res) throws BaseException{
		StringBuffer s = new StringBuffer();
		s.append("SELECT count(rc.RUN_CASE_ID) FROM testcase AS tc, run_case AS rc, case_defect AS cd, defect_info AS di WHERE rc.CASE_ID = tc.CASE_ID  AND rc.RUN_CASE_ID = cd.RUN_CASE_ID AND cd.DEFECT_NUMBER = di.DEFECT_NUM AND tc.PROJECT_ID = '" );
		s.append(projectId);
		s.append("'  AND rc.START_TIME > '");
		s.append(startDate);
		s.append(" ' AND rc.START_TIME < '");
		s.append(endDate);
		s.append("'");
		if(null != res){
			s.append(" AND (di.STATE = 'Fixed' OR di.STATE = 'Closed')");
		}
		else{
			s.append(" AND di.STATE<> 'Fixed' AND di.STATE <> 'Closed'");
		}
		Query query = this.getSession().createSQLQuery(s.toString());
		Object object = query.uniqueResult();
		return Integer.valueOf(object.toString());
	}
	
	public List<DefectInfo> getDefectDetail(String projectId, String startDate, String endDate) throws BaseException, ParseException{
		List<DefectInfo> diList = new ArrayList<DefectInfo>();
		StringBuffer s = new StringBuffer();
		s.append("SELECT di.DEFECT_NUM, di.DEFECT_NAME, di.STATE, di.PRIORITY, di.SUBMITTEDBY, di.DEVELOPER, di.LAST_UPDATE_DATE FROM testcase AS tc, run_case AS rc, case_defect AS cd, defect_info AS di WHERE rc.CASE_ID = tc.CASE_ID  AND rc.RUN_CASE_ID = cd.RUN_CASE_ID AND cd.DEFECT_NUMBER = di.DEFECT_NUM AND tc.PROJECT_ID = '" );
		s.append(projectId);
		s.append("'  AND rc.START_TIME > '");
		s.append(startDate);
		s.append(" ' AND rc.START_TIME < '");
		s.append(endDate);
		s.append("'");
		Query query = this.getSession().createSQLQuery(s.toString());		
		List<Object[]> objects = query.list();
		for (Object[] object : objects){
			DefectInfo di = new DefectInfo();
			di.setDefectNum(String.valueOf(object[0]));
			di.setDefectName(String.valueOf(object[1]));
			di.setState(String.valueOf(object[2]));
			di.setPriority(String.valueOf(object[3]));
			di.setSubmittedBy(String.valueOf(object[4]));
			if(null != object[5] && !"".equals(object[5])){
				di.setDeveloper(String.valueOf(object[5]));
			}
			di.setLastupdateDate(DateUtils.convertStringToDate(String.valueOf(object[6])));
			diList.add(di);
		}
		return diList;
	}
	
	public Integer getCaseNumByResult(String caseId, String state, String projectId) throws BaseException{
		StringBuffer s = new StringBuffer();
		s.append("SELECT count(*) FROM run_case AS rc, testcase AS tc WHERE rc.CASE_ID = tc.CASE_ID AND tc.PROJECT_ID= '");
		s.append(projectId);
		s.append("' AND rc.CASE_ID = '");
		s.append(caseId);
		
		if(!state.equals(TitanContent.CASE_REPORT_TOALL)){
		    s.append("'  AND rc.STATE = '");
		    s.append(state);
		}
		s.append("'");		
		Query query = this.getSession().createSQLQuery(s.toString());
		Object object = query.uniqueResult();
		return Integer.valueOf(object.toString());
	}
	
	public Integer getCaseNumByResult(String caseId, String state) throws BaseException{
		StringBuffer s = new StringBuffer();
		s.append("SELECT count(*) FROM run_case WHERE CASE_ID = '" );
		s.append(caseId);
		
		if(!state.equals(TitanContent.CASE_REPORT_TOALL)){
		s.append("'  AND STATE = '");
		s.append(state);
		}
		s.append("'");		
		Query query = this.getSession().createSQLQuery(s.toString());
		Object object = query.uniqueResult();
		return Integer.valueOf(object.toString());
	}
	
}
