package com.hp.titan.test.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.server.model.Server;
import com.hp.titan.test.model.TestJobCase;
import com.hp.titan.test.model.Testjob;
import com.hp.titan.test.model.Testcase;
import com.hp.titan.test.model.Testplan;

public class TestjobcaseDao extends DefaultBaseDao<Testjob, String> {

	public void save(TestJobCase testjobcase) throws BaseDaoException {
		this.getHibernateTemplate().save(this.getSession().merge(testjobcase));
	}
	
	public void deleteJobCaseByJob(String testJobId) throws BaseDaoException{
		String hql = "Delete FROM TestJobCase tjp Where tjp.id.testjobId=?" ;
		Query query = this.getSession().createQuery(hql.toString());
		query.setString(0, testJobId) ;
		query.executeUpdate() ;
	}
	
	public List<Testcase> findCaseByJob(String testJobId) throws BaseDaoException{
		String sql = "SELECT  TC.CASE_ID, TC.CASE_NAME, TJC.SORT,TC.CASE_CODE FROM TEST_JOB TJ, TESTCASE TC, TEST_JOB_CASE TJC WHERE TJ.JOB_ID = TJC.JOB_ID AND TJC.CASE_ID = TC.CASE_ID AND TJ.JOB_ID = '" +  testJobId + "'";
		Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		List<Testcase> res = new ArrayList<Testcase>();
		for (Object[] obj : objects){
			Testcase s = new Testcase();
			s.setCaseId(obj[0].toString());
			s.setCaseName(obj[1].toString());
			s.setSort(obj[2].toString());
			s.setCaseCode(obj[3].toString());
			res.add(s);
		}
		return res;
	}
	
	public List<Testplan> findPlanByJob(String testJobId) throws BaseDaoException{
		
		String sql = "SELECT  TP.PLAN_ID, TP.PLAN_NAME, TJP.SORT,TP.PLAN_CODE FROM TEST_JOB TJ, TESTPLAN TP, TEST_JOB_PLAN TJP WHERE TJ.JOB_ID = TJP.JOB_ID AND TJP.PLAN_ID = TP.PLAN_ID AND TJ.JOB_ID = '" +  testJobId + "'";
		Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		List<Testplan> res = new ArrayList<Testplan>();
		for (Object[] obj : objects){
			Testplan s = new Testplan();
			s.setTestplanId(obj[0].toString());
			s.setTestplanName(obj[1].toString());
			s.setSort(obj[2].toString());
			s.setTestplanCode(obj[3].toString());
			res.add(s);
		}
		return res;
	} 

}
