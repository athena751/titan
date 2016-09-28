package com.hp.titan.test.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.test.model.Parameter;
import com.hp.titan.test.model.ParameterData;
import com.hp.titan.test.vo.TestcaserunVo;

public class ParameterDao extends DefaultBaseDao<Parameter, String> {

	public void saveOrUpdate(Parameter parameter) throws BaseDaoException{
		this.getHibernateTemplate().saveOrUpdate(parameter);
	}
	
	/**
	 * Type will be pass fail or input
	 * @param testcaseId
	 * @return
	 * @throws BaseDaoException
	 */
	public List<Parameter> findParameterListByTestcaseId(String testcaseId) throws BaseDaoException{
		String hql = "from Parameter where caseId = ?";
		List<Parameter> parameterList = this.getHibernateTemplate().find(hql, testcaseId);
	    return parameterList;
	}

	/**
	 * Type will be username pwd ip and so on
	 * @param testcaseId
	 * @return
	 * @throws BaseDaoException
	 */
	public List<Parameter> findByTestcaseId(String testcaseId) throws BaseDaoException{
	    StringBuffer s = new StringBuffer();
		s.append("SELECT p.PARA_VALUE, p.PARA_NAME, pd.TYPE, p.PARA_ID  FROM parameter as p, parameter_data as pd WHERE p.PARA_VALUE <> '' AND pd.PARA_DATA_NAME = p.PARA_VALUE AND p.case_id = '");
		s.append(testcaseId);
		s.append("'");
		Query query = this.getSession().createSQLQuery(s.toString());
		List<Object[]> objects = query.list();
		if(null != objects && objects.size() != 0){
			List<Parameter> resList = new ArrayList<Parameter>();
			Iterator<Object[]> l = objects.iterator();
			while(l.hasNext()){
				Parameter p = new Parameter();
				Object[] obj = l.next();
				p.setParaValue(String.valueOf(obj[0]));
				p.setParaName(String.valueOf(obj[1]));
				p.setType(String.valueOf(obj[2]));
				p.setParaId(String.valueOf(obj[3]));
				resList.add(p);
			}
			return resList;
		}
		else{
			return null;
		}
	}
	
	public void deleteAll(List<Parameter> parameters) throws BaseDaoException{
		this.getHibernateTemplate().deleteAll(parameters);
	}
	
	public List<Parameter> getParameterByJobId(String runJobId) throws BaseException{
		StringBuffer s = new StringBuffer();
		s.append("SELECT p.PARA_VALUE, p.PARA_NAME, pd.TYPE  FROM parameter as p, test_job_case as tjc, parameter_data as pd WHERE p.CASE_ID = tjc.CASE_ID AND p.PARA_VALUE <> '' AND pd.PARA_DATA_NAME = p.PARA_VALUE AND tjc.JOB_ID = '");
		s.append(runJobId);
		s.append("'");
		Query query = this.getSession().createSQLQuery(s.toString());
		List<Object[]> objects = query.list();
		if(null != objects && objects.size() != 0){
			List<Parameter> resList = new ArrayList<Parameter>();
			Iterator<Object[]> l = objects.iterator();
			while(l.hasNext()){
				Parameter p = new Parameter();
				Object[] obj = l.next();
				p.setParaValue(String.valueOf(obj[0]));
				p.setType(String.valueOf(obj[2]));
				resList.add(p);
			}
			return resList;
		}
		else{
			return null;
		}
	}
	
	public List<Parameter> getParameterByJobPlanId(String runJobId) throws BaseException{
		StringBuffer s = new StringBuffer();
		s.append("SELECT p.PARA_VALUE, p.PARA_NAME, pd.TYPE  FROM parameter as p, test_job_plan as tjp, test_plan_case as tpc, parameter_data as pd WHERE tjp.PLAN_ID = tpc.PLAN_ID AND p.PARA_VALUE <> '' AND pd.PARA_DATA_NAME = p.PARA_VALUE AND  tpc.CASE_ID = p.CASE_ID AND tjp.JOB_ID = '");
		s.append(runJobId);
		s.append("'");
		Query query = this.getSession().createSQLQuery(s.toString());
		List<Object[]> objects = query.list();
		if(null != objects && objects.size() != 0){
			List<Parameter> resList = new ArrayList<Parameter>();
			Iterator<Object[]> l = objects.iterator();
			while(l.hasNext()){
				Parameter p = new Parameter();
				Object[] obj = l.next();
				p.setParaValue(String.valueOf(obj[0]));
				p.setType(String.valueOf(obj[2]));
				resList.add(p);
			}
			return resList;
		}
		else{
			return null;
		}
	}
	
	public List<Parameter> getServerParaByJobId(String runJobId) throws BaseException{
		StringBuffer s = new StringBuffer();
		s.append("SELECT rcs.SERVER_DATA FROM run_case_server as rcs, test_job_case as tjc WHERE rcs.CASE_ID = tjc.CASE_ID AND tjc.JOB_ID = '");
		s.append(runJobId);
		s.append("' AND rcs.SERVER_DATA IS NOT NULL AND rcs.SERVER_DATA <> ''");
		Query query = this.getSession().createSQLQuery(s.toString());
		List<Object> objects = query.list();
		if(null != objects && objects.size() != 0){
			List<Parameter> resList = new ArrayList<Parameter>();
			Iterator<Object> l = objects.iterator();
			while(l.hasNext()){
				Parameter p = new Parameter();
				Object obj = l.next();
				p.setParaValue(String.valueOf(obj));
				p.setType("host");
				resList.add(p);
			}
			return resList;
		}
		else{
			return null;
		}
	}
	
	public List<Parameter> getServerParaByJobPlanId(String runJobId) throws BaseException{
		StringBuffer s = new StringBuffer();
		s.append("SELECT rcs.SERVER_DATA FROM run_case_server as rcs, test_job_plan as tjp, test_plan_case as tpc WHERE tpc.PLAN_ID = tjp.PLAN_ID AND tpc.CASE_ID = rcs.CASE_ID AND tjp.JOB_ID = '");
		s.append(runJobId);
		s.append("' AND rcs.SERVER_DATA IS NOT NULL AND rcs.SERVER_DATA <> ''");
		Query query = this.getSession().createSQLQuery(s.toString());
		List<Object> objects = query.list();
		if(null != objects && objects.size() != 0){
			List<Parameter> resList = new ArrayList<Parameter>();
			Iterator<Object> l = objects.iterator();
			while(l.hasNext()){
				Parameter p = new Parameter();
				Object obj = l.next();
				p.setParaValue(String.valueOf(obj));
				p.setType("host");
				resList.add(p);
			}
			return resList;
		}
		else{
			return null;
		}
	}
	
	public List<Parameter> getParaResult(String caseId) throws BaseException{
		StringBuffer s = new StringBuffer();
		s.append("SELECT p.PARA_NAME FROM parameter as p WHERE p.TYPE = 'PASS' AND p.CASE_ID ='");
		s.append(caseId);
		s.append("'");
		Query query = this.getSession().createSQLQuery(s.toString());
		List<Object> objects = query.list();
		if(null != objects && objects.size() != 0){
			List<Parameter> resList = new ArrayList<Parameter>();
			Iterator<Object> l = objects.iterator();
			while(l.hasNext()){
				Parameter p = new Parameter();
				Object obj = l.next();
				p.setParaName(String.valueOf(obj));
				resList.add(p);
			}
			return resList;
		}
		else{
			return null;
		}
	}
}
