package com.hp.titan.test.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.server.model.Server;
import com.hp.titan.test.model.Testjob;
import com.hp.titan.test.model.TestJobServer;
import com.hp.titan.test.model.TestJobServerId;


public class TestjobserverDao extends DefaultBaseDao<Testjob, String> {

	
	public List<Server> findServerByJob(String testJobId, int userId) throws BaseDaoException{
		String sql = "SELECT  S.SERVER_ID, S.SERVER_NAME, S.SERVER_IP FROM TEST_JOB TJ, SERVER S, TEST_JOB_SERVER TJS WHERE TJ.JOB_ID = TJS.JOB_ID AND TJS.SERVER_ID = S.SERVER_ID AND TJ.JOB_ID = '" +  testJobId + "'";
		if(0 != userId){
			sql += "AND S.OWNER_ID = '" + userId + "'";
		}
		Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		List<Server> res = new ArrayList<Server>();
		for (Object[] obj : objects){
			Server s = new Server();
			s.setServerId(obj[0].toString());
			s.setServerName(obj[1].toString());
			s.setServerIp(obj[2].toString());
			res.add(s);
		}
		return res;
	}
	public void saveOrUpdate(TestJobServer testJobServer) throws BaseDaoException {
		this.getHibernateTemplate().saveOrUpdate(testJobServer);
	}
	
	public void saveOrUpdate(TestJobServerId testJobServerId) throws BaseDaoException {
		this.getHibernateTemplate().saveOrUpdate(testJobServerId);
	}
	
	public void deleteJobServer(String testJobId) throws BaseDaoException{
		String sql = "DELETE FROM TEST_JOB_SERVER WHERE JOB_ID = '" + testJobId + "'"  ;	
		Query query = this.getSession().createSQLQuery(sql);		
		query.executeUpdate() ;
	}
	

}
