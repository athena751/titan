package com.hp.titan.test.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.hp.app.common.util.DateUtils;
import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.test.model.RunJob;
import com.hp.titan.test.model.Testjob;
import com.hp.titan.test.vo.TestjobrunVo;



public class RunJobDao extends DefaultBaseDao<RunJob, String> {

	public void saveOrUpdate(RunJob runjob) throws BaseDaoException {
		this.getHibernateTemplate().saveOrUpdate(runjob);
	}
	
	public List<RunJob> findAllRunJobByJobId(String jobId)throws BaseDaoException {
		String hql = "from RunJob as runJob where isValid=0 and runJob.jobId=? order by startTime desc";
		return this.getHibernateTemplate().find(hql,jobId);
	}
	
	public RunJob findRunJobById(String runJobId) throws BaseException{
		String hql = "from RunJob as runJob where isValid=0 and runJob.runJobId=?";
		List<RunJob> runJobList = this.getHibernateTemplate().find(hql, runJobId);
		return runJobList.size()>0 ? runJobList.get(0) : null;
	}
	

	public TestjobrunVo getRunJobInfoById(String runJobId) throws BaseException{
		StringBuffer s = new StringBuffer();
		s.append("select tj.JOB_NAME, tj.JOB_CODE, rj.RESULT, rj.START_TIME, rj.END_TIME, rj.RESULT, tu.USER_CODE, tj.JOB_ID from test_job tj, run_job rj, titan_user tu where tj.JOB_ID = rj.JOB_ID and rj.IS_VALID = 0 and tj.IS_VALID = 0 and tu.USER_ID = rj.CREATE_USER_ID and rj.RUN_JOB_ID = '");
		s.append(runJobId);
		s.append("'");
		Query query = this.getSession().createSQLQuery(s.toString());
		List<Object[]> objects = query.list();
		if(null != objects && objects.size() != 0){
			TestjobrunVo tjr = new TestjobrunVo();
			tjr.setTestjobName(String.valueOf(objects.get(0)[0]));
			tjr.setTestjobCode(String.valueOf(objects.get(0)[1]));
			tjr.setResult(String.valueOf(objects.get(0)[2]));
			tjr.setStartTime(String.valueOf(objects.get(0)[3]));
			tjr.setEndTime(String.valueOf(objects.get(0)[4]));
			tjr.setResult(String.valueOf(objects.get(0)[5]));
			tjr.setOwner(String.valueOf(objects.get(0)[6]));
			tjr.setTestjobId(String.valueOf(objects.get(0)[7]));
			return tjr;
		}
		else{
			return null;
		}
	}
}
