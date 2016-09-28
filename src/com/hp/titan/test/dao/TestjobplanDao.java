package com.hp.titan.test.dao;

import org.hibernate.Query;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.test.model.TestJobPlan;
import com.hp.titan.test.model.Testjob;

public class TestjobplanDao extends DefaultBaseDao<Testjob, String> {

	public void save(TestJobPlan testjobcase) throws BaseDaoException {
		this.getHibernateTemplate().save(this.getSession().merge(testjobcase));
	}
	
	public void deleteJobPlanByJob(String testJobId){
		String hql = "Delete FROM TestJobPlan tjp Where tjp.id.testjobId=?" ;
		Query query = this.getSession().createQuery(hql.toString());
		query.setString(0, testJobId) ;
		query.executeUpdate() ;
	}
}
