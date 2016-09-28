package com.hp.titan.test.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.test.model.Parameter;
import com.hp.titan.test.model.RunCaseParameter;
import com.hp.titan.test.model.Testjob;
import com.hp.titan.test.vo.ParameterVo;

public class RuncaseparameterDao extends DefaultBaseDao<Testjob, String> {

	public void save(RunCaseParameter runCaseParameter) throws BaseDaoException {
		this.getHibernateTemplate().save(runCaseParameter);
	}
	
	public void deleteRunCasePara(String jobId, String planId, String caseId){
		String hql = "Delete FROM RunCaseParameter rcp Where rcp.id.testjobId=? " ;
		if(null != planId && !"".equals(planId)){
			hql += " and rcp.id.testplanId=? ";
		}
		if(null != caseId && !"".equals(caseId)){
			hql += " and rcp.id.testcaseId=? ";
		}
		Query query = this.getSession().createQuery(hql.toString());
		query.setString(0, jobId);
		if(null != planId && !"".equals(planId)){
			query.setString(1, planId);
		}
		if(null != caseId && !"".equals(caseId)){
			query.setString(2, caseId);
		}
		query.executeUpdate() ;
	}
	
	public List<ParameterVo> getParameterInfo(String testjobId, String testPlanId, String testCaseId) throws BaseDaoException{
		StringBuffer s = new StringBuffer();
		s.append("select p.PARA_NAME, p.PARA_VALUE, rcp.PARAMETER_VALUE from parameter p, run_case_parameter rcp where p.PARA_ID = rcp.PARAMETER_ID and rcp.JOB_ID = '");
		s.append(testjobId);
		s.append("' and rcp.CASE_ID = '");
		s.append(testCaseId);
		s.append("' and ");
		if(null != testPlanId && !"".equals(testPlanId)){
			s.append("rcp.PLAN_ID = '");
			s.append(testPlanId);
			s.append("'");
		}
		else{
			s.append("ISNULL(rcp.PLAN_ID)");
		}
		s.append("ORDER BY p.PARA_ORDER");
		Query query = this.getSession().createSQLQuery(s.toString());
		List<Object[]> objects = query.list();
		if(null != objects && objects.size() != 0){
			List<ParameterVo> resList = new ArrayList<ParameterVo>();
			Iterator<Object[]> l = objects.iterator();
			while(l.hasNext()){
				Object[] obj = l.next();
				ParameterVo p = new ParameterVo();
				p.setParaName(obj[0].toString());
				p.setParaValue(obj[1].toString());
				p.setRuncaseparaValue(obj[2].toString());
				resList.add(p);
			}
			return resList;
		}
		return null;
	}
	
}
