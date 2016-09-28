package com.hp.titan.test.dao;

import java.util.List;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.test.model.CaseLog;

public class CaseLogDao extends DefaultBaseDao<CaseLog, String> {

	public void saveOrUpdate(CaseLog caseLog) throws BaseDaoException{
		this.getHibernateTemplate().saveOrUpdate(caseLog);
	}

	public List<CaseLog> findByTestcaseId(String testcaseId) throws BaseDaoException{
		String hql = "from CaseLog as cl where cl.caseId = ?";
		List<CaseLog> caseLogList = this.getHibernateTemplate().find(hql, testcaseId);
	    return caseLogList;
	}
	
	public void deleteAll(List<CaseLog> caseLogs) throws BaseDaoException{
		this.getHibernateTemplate().deleteAll(caseLogs);
	}
}
