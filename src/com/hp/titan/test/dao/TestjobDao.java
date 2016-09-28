package com.hp.titan.test.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.project.model.Project;
import com.hp.titan.test.model.Testjob;
import com.hp.titan.test.model.UserTag;
import com.hp.titan.test.vo.TestjobVo;

public class TestjobDao extends DefaultBaseDao<Testjob, String> {

	public void saveOrUpdate(Testjob testjob) throws BaseDaoException {
		this.getHibernateTemplate().saveOrUpdate(testjob);
	}

	public List<Testjob> findAllTestjob() throws BaseDaoException{
		String hql = "from Testjob tj where tj.isValid=0";
		List<Testjob> testjobList = this.getHibernateTemplate().find(hql);
		return testjobList;
	}
	public List<Testjob> findTestjobBySprint(String sprintId) throws BaseDaoException{
		String hql = "from Testjob tj where tj.sprintId = ? and tj.isValid=0";
		List<Testjob> testjobList = this.getHibernateTemplate().find(hql, sprintId);
		return testjobList;
	}
	
	public List<TestjobVo> findTestjobByGroup(Integer groupId) throws BaseDaoException{
		List<TestjobVo> rtn = new ArrayList<TestjobVo>();
		String hql = "select tj.JOB_ID, tj.JOB_CODE, tj.JOB_NAME from TEST_JOB tj, PROJECT p where tj.PROJECT_ID = p.PROJECT_ID and p.GROUP_ID='" + groupId + "'";
		Query query = this.getSession().createSQLQuery(hql);		
		List<Object[]> objects = query.list();
		if(objects!=null && objects.size()!=0){
		for (Object[] object : objects){
			TestjobVo vo = new TestjobVo();
			vo.setTestjobID(object[0].toString());
			vo.setTestjobCode(object[1].toString());
			vo.setTestjobName(object[2].toString());
			rtn.add(vo);
		}
		}
		return rtn;
	}

	public Testjob getTestjobById(String testjobId) throws BaseDaoException {
		String hql = "from Testjob tj where tj.testjobId = ? and isValid=0";
		List<Testjob> testjobList = this.getHibernateTemplate().find(hql, testjobId);
		return testjobList.size()>0 ? testjobList.get(0) : null;
	}
	
	public Testjob getTestjobByCode(String testjobCode) throws BaseDaoException {
		String hql = "from Testjob tj where tj.testjobCode = ? and isValid=0";
		List<Testjob> testjobList = this.getHibernateTemplate().find(hql, testjobCode);
		return testjobList.size()>0 ? testjobList.get(0) : null;
	}
	
	public String findMaxCodeByTestjob() throws BaseDaoException {
		String hql = 
				"select max(tj.testjobCode) from Testjob as tj ";		
		List<String> result = this.getHibernateTemplate().find(hql);
		
		String maxCaseCode = null;
		if(result != null){
			maxCaseCode = (String) result.get(0);
		}
		
		return maxCaseCode;
	}
	public Testjob getTestjobByName(String testjobName) throws BaseDaoException {
		String hql = "from Testjob tj where tj.testjobName = ? and isValid=0";
		List<Testjob> testjobList = this.getHibernateTemplate().find(hql, testjobName);
		return testjobList.size()>0 ? testjobList.get(0) : null;
	}
	
   public List<TestjobVo> findAllTestjob(TestjobVo testjobVo, Set<Project> s) throws BaseDaoException {
		
		List<TestjobVo> rtn = new ArrayList<TestjobVo>();
		String sql = "select tj.JOB_ID, tj.JOB_CODE, tj.JOB_NAME,tj.JOB_TYPE, tj.STATE, tj.UPDATE_DATE, tj.REMARK, tj.OWNER_ID, tj.PROJECT_ID, tj.SPRINT_ID, "
			    + "u.USER_CODE, p.PROJECT_ID, p.PROJECT_NAME, s.SPRINT_NAME, tj.TAG "
			    + "from TEST_JOB tj, titan_user u, PROJECT p, SPRINT s "
			    + "where tj.OWNER_ID=u.USER_ID and tj.PROJECT_ID=p.PROJECT_ID and tj.SPRINT_ID=s.SPRINT_ID and tj.IS_VALID=0 ";
		if (testjobVo != null) {
			if (testjobVo.getTestjobCode() != null && !testjobVo.getTestjobCode().equals("")){
				sql += " and tj.JOB_CODE='" + testjobVo.getTestjobCode() + "'";
			}
			if (testjobVo.getTestjobName() != null && !testjobVo.getTestjobName().equals("")){
				sql += " and tj.JOB_NAME like '%" + testjobVo.getTestjobName() + "%'";
			}
			if (testjobVo.getTestjobType() != null && !testjobVo.getTestjobType().equals("")){
				sql += " and tj.JOB_TYPE like '%" + testjobVo.getTestjobType() + "%'";
			}			
			if (testjobVo.getTestjobState() != null && !testjobVo.getTestjobState().equals("")){
				sql += " and tj.STATE like '%" + testjobVo.getTestjobState() + "%'";
			}
			if (testjobVo.getOwnerId() != null && !testjobVo.getOwnerId().equals("")){
				sql += " and tj.OWNER_ID='" + testjobVo.getOwnerId() + "'";
			}
			if (testjobVo.getProjectId() != null && !testjobVo.getProjectId().equals("")){
				sql += " and tj.PROJECT_ID='" + testjobVo.getProjectId() + "'";
			}
			if (testjobVo.getSprintId() != null && !testjobVo.getSprintId().equals("")){
				sql += " and tj.SPRINT_ID='" + testjobVo.getSprintId() + "'";
			}
			if (testjobVo.getTag() != null && !testjobVo.getTag().equals("")){
				sql += " and tj.TAG='" + testjobVo.getTag() + "'";
			}
		}
		if(s!=null && s.size() != 0){
			Iterator<Project> it = s.iterator();
			sql += " and tj.PROJECT_ID in(";
			while(it.hasNext()){
				Project p = it.next();
				sql += "'" + p.getProjectId()+"',";
			}
			sql += ")";
		}
		sql += "ORDER BY tj.TAG, tj.JOB_CODE";
		
		Query query = this.getSession().createSQLQuery(sql.replace(",)", ")"));		
		List<Object[]> objects = query.list();
		for (Object[] object : objects){
			TestjobVo vo = new TestjobVo();
			vo.setTestjobID(object[0].toString());
			vo.setTestjobCode(object[1].toString());
			vo.setTestjobName(object[2].toString());
			if (object[3] !=null){
				vo.setTestjobType((object[3].toString()));
				}
			if (object[4] !=null){
			vo.setTestjobState(object[4].toString());
			}
			if (object[5] !=null){
				vo.setUpdateDate(object[5].toString());
				}
			vo.setDescription(object[6].toString());
			if (object[10] != null){
				vo.setOwner(object[10].toString());				
			}
			if (object[12] != null){
				vo.setProject(object[12].toString());
			}
			if (object[13] != null && !object[13].equals("")){
				vo.setSprint(object[13].toString());
			}
			if (object[14] != null && !object[14].equals("")){
				vo.setTag(object[14].toString());
			}
			rtn.add(vo);
		}
		return rtn;
	}	
   
   public void saveUserTag(UserTag ut) throws BaseDaoException {
	   this.getHibernateTemplate().saveOrUpdate(ut);
   }
   
   public List<UserTag> getUserTag(Integer userId) throws BaseDaoException {
	   String hql = "from UserTag ut where ut.id.userId = ?";
	   return this.getHibernateTemplate().find(hql, userId);
   }
}
