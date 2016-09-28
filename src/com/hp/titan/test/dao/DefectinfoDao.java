package com.hp.titan.test.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.test.model.CaseDefect;
import com.hp.titan.test.model.DefectInfo;
import com.hp.titan.test.vo.DefectInfoVo;

public class DefectinfoDao extends DefaultBaseDao<DefectInfo, String> {


	public void saveOrUpdate(DefectInfo defectInfo) throws BaseDaoException {
		this.getHibernateTemplate().saveOrUpdate(defectInfo);
	}

	public List<DefectInfo> getDefectInfoByBean(DefectInfo di) throws BaseDaoException {
		Calendar calendar = Calendar.getInstance();  
		Date d1;
		Date d2 = calendar.getTime();
		d2.setHours(0);
		d2.setMinutes(0);
		d2.setSeconds(0);
		String hql = "from DefectInfo as di ";
		if(null != di.getSubmittEmail() && !"".equals(di.getSubmittEmail())){
			if(null == di.getSubmitDate()){
				hql += "where di.submittEmail=?";
				return this.getHibernateTemplate().find(hql, di.getSubmittEmail());
			}
			hql += "where di.submittEmail=? and di.submitDate between ? and ?";
			d1 = di.getSubmitDate();
			d1.setHours(0);
			d1.setMinutes(0);
			d1.setSeconds(0);
			return this.getHibernateTemplate().find(hql, di.getSubmittEmail(), d1, d2);
		}
		else if(null != di.getDeveloperEmail() && !"".equals(di.getDeveloperEmail())){
			if(null == di.getLastupdateDate()){
				hql += "where di.developerEmail=?";
				return this.getHibernateTemplate().find(hql, di.getDeveloperEmail());
			}
			hql += "where (di.state = ? or di.state = ?) and di.developerEmail=? and di.lastupdateDate between ? and ?";
			d1 = di.getLastupdateDate();
			d1.setHours(0);
			d1.setMinutes(0);
			d1.setSeconds(0);
			return this.getHibernateTemplate().find(hql, "Awaiting Release", "Closed", di.getDeveloperEmail(), d1, d2);
		}
		else if(null != di.getProjectName() && !"".equals(di.getProjectName())){
			if(null == di.getComponent() || "".equals(di.getClass())){
				hql += "where di.projectName=?";
				return this.getHibernateTemplate().find(hql, di.getProjectName());
			}
			hql += "where di.component=? and di.projectName=?";
			return this.getHibernateTemplate().find(hql, di.getComponent(), di.getProjectName());
		}
		return null;
	}
	
	public List<CaseDefect> getCaseDefectByDefectNo(String defectNo) throws BaseDaoException{
		String hql = "from CaseDefect as cd where cd.defectNo = ?";
		return this.getHibernateTemplate().find(hql, defectNo);
	}
	
	public List<DefectInfo> getDefectListByState(String state) throws BaseDaoException{
		String hql = "from DefectInfo as di where di.state = ?";
		return this.getHibernateTemplate().find(hql, state);
	}
	
	public List<DefectInfoVo> getDefectInfoBySprintId(String sprintId) throws BaseDaoException{
		StringBuffer s = new StringBuffer();
		List<DefectInfoVo> diVoList = new ArrayList<DefectInfoVo>();
		s.append("SELECT cd.DEFECT_NUMBER, tc.CASE_NAME, tj.JOB_NAME, cd.CREATE_DATE");
		s.append(" FROM test_job as tj, run_job as rj, case_defect as cd, sprint as p, run_case as rc, testcase as tc");
		s.append(" WHERE p.SPRINT_ID = tj.SPRINT_ID AND tj.JOB_ID = rj.JOB_ID AND rj.RUN_JOB_ID = rc.RUN_JOB_ID AND cd.RUN_CASE_ID = rc.RUN_CASE_ID AND rc.CASE_ID = tc.CASE_ID AND p.SPRINT_ID = '");
		s.append(sprintId);
		s.append("'");
		Query query = this.getSession().createSQLQuery(s.toString());		
		List<Object[]> objects = query.list();
		for (Object[] object : objects){
			DefectInfoVo vo = new DefectInfoVo();
			vo.setDefectNum(String.valueOf(object[0]));
			vo.setTestcaseName(String.valueOf(object[1]));
			vo.setTestjobName(String.valueOf(object[2]));
			vo.setInputDate(String.valueOf(object[3]));
			diVoList.add(vo);
		}
		return diVoList;
	}
	
	public List<DefectInfoVo> getDefectInfoByTestJobId(String testjobId) throws BaseDaoException{
		StringBuffer s = new StringBuffer();
		List<DefectInfoVo> diVoList = new ArrayList<DefectInfoVo>();
		s.append("SELECT cd.DEFECT_NUMBER, tc.CASE_NAME, cd.CREATE_DATE");
		s.append(" FROM test_job as tj, run_job as rj, case_defect as cd, run_case as rc, testcase as tc");
		s.append(" WHERE tj.JOB_ID = rj.JOB_ID AND rj.RUN_JOB_ID = rc.RUN_JOB_ID AND cd.RUN_CASE_ID = rc.RUN_CASE_ID AND rc.CASE_ID = tc.CASE_ID AND tj.JOB_ID = '");
		s.append(testjobId);
		s.append("'");
		Query query = this.getSession().createSQLQuery(s.toString());		
		List<Object[]> objects = query.list();
		for (Object[] object : objects){
			DefectInfoVo vo = new DefectInfoVo();
			vo.setDefectNum(String.valueOf(object[0]));
			vo.setTestcaseName(String.valueOf(object[1]));
			vo.setInputDate(String.valueOf(object[2]));
			diVoList.add(vo);
		}
		return diVoList;
	}

}
