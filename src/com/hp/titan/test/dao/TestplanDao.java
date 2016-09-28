package com.hp.titan.test.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.project.model.Project;
import com.hp.titan.test.model.TestJobPlan;
import com.hp.titan.test.model.Testcase;
import com.hp.titan.test.model.Testplan;
import com.hp.titan.test.vo.TestcaseVo;
import com.hp.titan.test.vo.TestplanVo;

public class TestplanDao extends DefaultBaseDao<Testplan, String> {

	public void saveOrUpdate(Testplan testplan) throws BaseDaoException {
		this.getHibernateTemplate().saveOrUpdate(testplan);
	}

	public List<Testplan> findAllTestplan() throws BaseDaoException {
		String hql = "from Testplan as testplan";
		return this.getHibernateTemplate().find(hql);
	}
	public List<Testplan> findAllTestplanByType(String type, String defaultProjectId) throws BaseDaoException {
		String hql = "from Testplan as testplan where isValid=0 and testplan.testplanType=? and testplan.projectId=?";
		return this.getHibernateTemplate().find(hql,type, defaultProjectId);
	}
	public Testplan getTestplanById(String testplanId) throws BaseDaoException {
		String hql = "from Testplan tp where tp.testplanId = ? and isValid=0";
		List<Testplan> testplanList = this.getHibernateTemplate().find(hql, testplanId);
		return testplanList.size()>0 ? testplanList.get(0) : null;
	}
	
	public String findMaxCodeByProject(String projectId) throws BaseDaoException {
		String hql = 
				"select max(tp.testplanCode) from Testplan as tp where tp.projectId = '"
						+ projectId + "'";		
		List<String> result = this.getHibernateTemplate().find(hql);
		
		String maxCaseCode = null;
		if(result != null){
			maxCaseCode = (String) result.get(0);
		}
		
		return maxCaseCode;
	}
	public Testplan getTestplanByName(String testplanName) throws BaseDaoException {
		String hql = "from Testplan tp where tp.testplanName = ? and isValid=0";
		List<Testplan> testplanList = this.getHibernateTemplate().find(hql, testplanName);
		return testplanList.size()>0 ? testplanList.get(0) : null;
	}
	
   public List<TestplanVo> findAllTestplan(TestplanVo testplanVo, Set<Project> s) throws BaseDaoException {
		
		List<TestplanVo> rtn = new ArrayList<TestplanVo>();
		String sql = "select tp.PLAN_ID, tp.PLAN_CODE, tp.PLAN_NAME, tp.PLAN_TYPE, tp.REMARK, tp.OWNER_ID, tp.PROJECT_ID, "
			    + "u.USER_CODE, p.PROJECT_ID, p.PROJECT_NAME "
			    + "from TESTPLAN tp, titan_user u, PROJECT p "
			    + "where tp.OWNER_ID=u.USER_ID and tp.PROJECT_ID=p.PROJECT_ID and tp.IS_VALID=0";
		if (testplanVo != null) {
			if (testplanVo.getTestplanCode() != null && !testplanVo.getTestplanCode().equals("")){
				sql += " and tp.PLAN_CODE='" + testplanVo.getTestplanCode() + "'";
			}
			if (testplanVo.getTestplanName() != null && !testplanVo.getTestplanName().equals("")){
				sql += " and tp.PLAN_NAME like '%" + testplanVo.getTestplanName() + "%'";
			}
			if (testplanVo.getTestplanType() != null && !testplanVo.getTestplanType().equals("")){
				sql += " and tp.PLAN_TYPE like '%" + testplanVo.getTestplanType() + "%'";
			}
			if (testplanVo.getOwnerId() != null && !testplanVo.getOwnerId().equals("")){
				sql += " and tp.OWNER_ID='" + testplanVo.getOwnerId() + "'";
			}
			if (testplanVo.getProjectId() != null && !testplanVo.getProjectId().equals("")){
				sql += " and tp.PROJECT_ID='" + testplanVo.getProjectId() + "'";
			}		
		}
		if(s!=null && s.size() != 0){
			Iterator<Project> it = s.iterator();
			sql += " and tp.PROJECT_ID in(";
			while(it.hasNext()){
				Project p = it.next();
				sql += "'" + p.getProjectId()+"',";
			}
			sql += ")";
		}
		
		Query query = this.getSession().createSQLQuery(sql.replace(",)", ")"));			
		List<Object[]> objects = query.list();
		for (Object[] object : objects){
			TestplanVo vo = new TestplanVo();
			vo.setTestplanID(object[0].toString());
			vo.setTestplanCode(object[1].toString());
			vo.setTestplanName(object[2].toString());
			vo.setTestplanType(object[3].toString());
			vo.setDescription(object[4].toString());
			if (object[5] != null){
				vo.setOwner(object[7].toString());				
			}
			if (object[6] != null){
				vo.setProject(object[9].toString());
			}
			rtn.add(vo);
		}
		return rtn;
	}	
   
   public List<TestJobPlan> getJobPlanByPlanId(String testplanId){
	   String hql = "from TestJobPlan jp where jp.id.testplanId = ?";
	   return this.getHibernateTemplate().find(hql, testplanId);
   }

}
