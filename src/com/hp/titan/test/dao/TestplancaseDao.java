package com.hp.titan.test.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.test.model.TestPlanCase;
import com.hp.titan.test.model.Testcase;
import com.hp.titan.test.model.Testplan;
import com.hp.titan.test.model.Testjob;

public class TestplancaseDao extends DefaultBaseDao<Testjob, String> {

	public void save(TestPlanCase testplancase) throws BaseDaoException {
		this.getHibernateTemplate().save(this.getSession().merge(testplancase));
	}
	
	public void deletePlanCaseByPlan(String testPlanId) throws BaseDaoException{
		String hql = "Delete FROM TestPlanCase tpc Where tpc.id.testplanId=?" ;
		Query query = this.getSession().createQuery(hql.toString());
		query.setString(0, testPlanId) ;
		query.executeUpdate() ;
	}
	
	public List<Testcase> getTestcaseByPlan(String testplanId) throws BaseDaoException{
		String sql = "SELECT TC.CASE_ID, TC.CASE_NAME,  TC.CASE_CODE FROM TESTCASE TC, TEST_PLAN_CASE TPC WHERE TC.CASE_ID = TPC.CASE_ID AND TPC.PLAN_ID ='" + testplanId + "' order by sort";
		Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		List<Testcase> res = new ArrayList<Testcase>();
		for (Object[] obj : objects){
			Testcase s = new Testcase();
			s.setCaseId(obj[0].toString());
			s.setCaseName(obj[1].toString());
			s.setCaseCode(obj[2].toString());
			res.add(s);
		}
		return res;
	}
	
	public List<TestPlanCase> getPlanCase(String testPlanId) throws BaseException{
		String hql = "from TestPlanCase tpc Where tpc.id.testplanId=?";
		return this.getHibernateTemplate().find(hql, testPlanId);
	}

}
