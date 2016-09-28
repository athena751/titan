package com.hp.titan.test.dao;

import java.util.List;

import org.hibernate.Query;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.test.model.RunCaseServer;
import com.hp.titan.test.model.RunCaseServerId;
import com.hp.titan.test.model.Testjob;

public class RuncaseserverDao extends DefaultBaseDao<Testjob, String> {

	public void save(RunCaseServer runCaseServer) throws BaseDaoException {
		this.getHibernateTemplate().save(runCaseServer);
	}
	
	public void deleteByCaseId(String caseId) throws BaseDaoException {
		String hql = "Delete FROM RunCaseServer rcs Where rcs.id.caseId=? " ;
		Query query = this.getSession().createQuery(hql.toString());
		query.setString(0, caseId);
		query.executeUpdate();
	}
	
	public RunCaseServer findCaseServerById(String caseId) throws BaseException{
		String sql = "SELECT rcs.DEDICATED_SERVER_ID, rcs.SERVER_DATA from run_case_server rcs WHERE rcs.CASE_ID = '";
		sql += caseId;
		sql += "'";
		Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		if(null != objects && objects.size() != 0){
			RunCaseServer runCaseServer = new RunCaseServer();
			RunCaseServerId runCaseServerId = new RunCaseServerId();
			if(null != objects.get(0)[0]){
				runCaseServerId.setDedicatedserverId(String.valueOf(objects.get(0)[0]));
			}
			if(null != objects.get(0)[1]){
				runCaseServerId.setServerData(String.valueOf(objects.get(0)[1]));
			}
			runCaseServer.setId(runCaseServerId);
			return runCaseServer;
		}
		return null;
	}
	
//	public void deleteRunCaseServer(RunCaseServer runCaseServer) throws BaseDaoException{
//		String hql = "Delete FROM RunCaseServer rcs Where rcs.id.testjobId=? " ;
//		if(null != runCaseServer.getId().getTestplanId() && !"".equals(runCaseServer.getId().getTestplanId())){
//			hql += " and rcs.id.testplanId=? ";
//		}
//		if(null != runCaseServer.getId().getTestcaseId() && !"".equals(runCaseServer.getId().getTestcaseId())){
//			hql += " and rcs.id.testcaseId=? ";
//		}
//		Query query = this.getSession().createQuery(hql.toString());
//		query.setString(0, runCaseServer.getId().getTestjobId());
//		if(null != runCaseServer.getId().getTestplanId() && !"".equals(runCaseServer.getId().getTestplanId())){
//			query.setString(1, runCaseServer.getId().getTestplanId());
//		}
//		if(null != runCaseServer.getId().getTestcaseId() && !"".equals(runCaseServer.getId().getTestcaseId())){
//			query.setString(2, runCaseServer.getId().getTestcaseId());
//		}
//		query.executeUpdate() ;
//	}
	
//	@SuppressWarnings("unchecked")
//	public Server getServerInfo(String testjobId, String testPlanId, String testCaseId){
//		StringBuffer s = new StringBuffer();
//		s.append("select s.SERVER_IP, s.SERVER_ACCOUNT, s.SERVER_PASSWD from server s, run_case_server rcs where s.SERVER_ID = rcs.SERVER_ID and rcs.CASE_ID = '");
//		s.append(testCaseId);
//		s.append("' and rcs.JOB_ID = '");
//		s.append(testjobId);
//		s.append("' and ");
//		if(null != testPlanId && !"".equals(testPlanId)){
//			s.append("rcs.PLAN_ID = '");
//			s.append(testPlanId);
//			s.append("'");
//		}
//		else{
//			s.append("ISNULL(rcs.PLAN_ID)");
//		}
//		
//		Query query = this.getSession().createSQLQuery(s.toString());
//		List<Object[]> objects = query.list();
//		if(null != objects && objects.size() != 0){
//			Server serverInfo = new Server();
//			serverInfo.setServerIp(objects.get(0)[0].toString());
//			serverInfo.setServerAccount(objects.get(0)[1].toString());
//			serverInfo.setServerPasswd(objects.get(0)[2].toString());
//			return serverInfo;
//		}
//		return null;
//	}
	
}
